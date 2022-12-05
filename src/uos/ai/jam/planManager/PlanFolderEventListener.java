package uos.ai.jam.planManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import uos.ai.jam.Interpreter;
import uos.ai.jam.parser.JAMParser;

public class PlanFolderEventListener extends Thread{
	private WatchService					_watchService;
	private Interpreter						_interpreter;
	private Path							_planFolder;
	public PlanFolderEventListener(Path planFolder, WatchService ws, Interpreter i) {
		_watchService = ws;
		_interpreter = i;
		_planFolder = planFolder;
	}
	@Override
	public void run() {
		while(true){
			WatchKey key = null;
			try {
				key = _watchService.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(WatchEvent<?> event : key.pollEvents()){
				
				if(!isValidEvent(event)){
					continue;
				}
				File eventFile = getEventFile(event);
				try {
					System.out.println(eventFile.getCanonicalPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(eventFile.isFile()){
					
					JAMParser.parseFile(_interpreter, eventFile);
				}
			}
			if(!key.reset()){
				break;
			}
		}
	}
	private File getEventFile(WatchEvent<?> event) {
		WatchEvent<Path> ev = (WatchEvent<Path>) event;
		Path file = ev.context();
		Path filename = _planFolder.resolve(file);
		return filename.toFile();
	}
	private boolean isValidEvent(WatchEvent<?> event) {
		WatchEvent.Kind<?> kind = event.kind();
//		return !(kind == StandardWatchEventKinds.OVERFLOW);
		return kind == StandardWatchEventKinds.ENTRY_CREATE;
	}
}

package uos.ai.jam.planManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import uos.ai.jam.Interpreter;


public class PlanManager {
	private WatchService				ws;
	private Path 						planFolder;
	
	
	public PlanManager(String s, Interpreter i) {
		
		planFolder = new File(s).toPath();
		try {
			ws = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		registPlanFolder(planFolder);
		new PlanFolderEventListener(planFolder, ws, i).start();
	}
	private void registPlanFolder(Path planFolder) {
		try {
			planFolder.register(ws, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void registPlanFolder(String s){
		registPlanFolder(new File(s).toPath());
	}
}

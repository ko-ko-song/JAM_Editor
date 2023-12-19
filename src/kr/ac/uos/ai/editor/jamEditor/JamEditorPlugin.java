package kr.ac.uos.ai.editor.jamEditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.print.attribute.standard.Severity;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.undo.DeleteMarkersOperation;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import kr.ac.uos.ai.editor.jam.model.EditorModel;
import kr.ac.uos.ai.editor.jamEditor.util.DocumentAssistor;
import kr.ac.uos.ai.editor.jamEditor.util.JamColorProvider;
import kr.ac.uos.ai.editor.jamEditor.util.Util;
import uos.ai.jam.Interpreter;
import uos.ai.jam.expression.Relation;
import uos.ai.jam.parser.JAMParser;
import uos.ai.jam.plan.Plan;
import uos.ai.jam.plan.constructor.PlanConstruct;
import uos.ai.jam.plan.constructor.PlanSimpleConstruct;
/**
 * 
 * @author JiHun
 *
 */
public class JamEditorPlugin extends AbstractUIPlugin {

//	private static JavaEditorExamplePlugin fgInstance;

	private static JamEditorPlugin plugin;
	private JamColorProvider provider;
	private EditorModel jamEditorModel;
	
	public JamEditorPlugin() {
		super();
		plugin = this;
		jamEditorModel = EditorModel.getInstance();
		initMarker();
		initFileEventListener();
		System.out.println("-------- Jam editor Activate");
	}

	private void initMarker() {
		Job markerJob = null;
		
		IResource adapter = ResourcesPlugin.getWorkspace().getRoot().getAdapter(IResource.class);
				
		markerJob = Job.create("remove Markers", (ICoreRunnable) monitor -> {
			List<IMarker> problemMarkers = Arrays.asList(adapter.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE));
			List<IMarker> bookmarkMarkers = Arrays.asList(adapter.findMarkers(IMarker.BOOKMARK, true, IResource.DEPTH_INFINITE));
			List<IMarker> taskMarkMarkers = Arrays.asList(adapter.findMarkers(IMarker.TASK, true, IResource.DEPTH_INFINITE));
			
			List<IMarker> markers = new ArrayList<IMarker>();
			if(problemMarkers != null)
				markers.addAll(problemMarkers);
			if(bookmarkMarkers != null)
				markers.addAll(bookmarkMarkers);
			if(taskMarkMarkers != null)
				markers.addAll(taskMarkMarkers);
			
			for (IMarker iMarker : markers) {
				if(iMarker != null)
					iMarker.delete();
			}
		});
		markerJob.setUser(false);
		markerJob.setPriority(Job.DECORATE);
		markerJob.schedule(10);
		
	}

	public static JamEditorPlugin getDefault() {
		return plugin;
	}
	
	public static JamEditorPlugin getInstance() {
		return plugin;
	}
	
	public void initEditorModel(String projectName) {
		jamEditorModel.init(projectName);
		List<String> AllJamFiles = this.getAllJamFiles(jamEditorModel.getPlanPath());
		if(AllJamFiles == null) 
			return;
		
		
		for (String jamFilePath : AllJamFiles) {
		
			 Interpreter interpreter = JAMParser.parseFile(null, jamFilePath);
			 if(interpreter != null) {
				 jamEditorModel.addFileContentToModel(interpreter);
			 }
			 
			 try {
					this.updateErrorMarker(null, jamFilePath);
				}catch (CoreException e) {
					e.printStackTrace();
				}
		}
		
		try {
			updateRelationWarningMarker();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jamEditorModel.printEditorModel();
	}
	
	public EditorModel getEditorModel() {
		return this.jamEditorModel; 
	}
	
	private void initFileEventListener() {
		
		//file change, remove, add listener
		JamFileChangeListener lc = new JamFileChangeListener();
		IWorkspace workspace = ResourcesPlugin.getWorkspace(); 
		workspace.addResourceChangeListener(lc);
		
		
		//editor part open, active, deactivate listener
		JamFileStateListener ls = new JamFileStateListener();
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		activeWorkbenchWindow.getPartService().addPartListener(ls);
	}

	public void delelteFileEvent(String fileFullpath) {
		jamEditorModel.deleteFileContentFromModel(fileFullpath);
	}
	
	public void changeFileEvent(String fileFullpath) {
		jamEditorModel.deleteFileContentFromModel(fileFullpath);
		try {
			Interpreter interpreter = JAMParser.parseFile(null, fileFullpath);
			if(interpreter != null) {
				jamEditorModel.addFileContentToModel(interpreter);
			}			
		}catch (Exception e){
			
		}

		
		try {
			this.updateMarkers(fileFullpath);
		} catch (CoreException e) {
//			e.printStackTrace(); 
		}
//		jamEditorModel.printEditorModel();
	}
 
	public void addFileEvent(String fileFullpath) {
		Interpreter interpreter = JAMParser.parseFile(null, fileFullpath);
		jamEditorModel.addFileContentToModel(interpreter);
	}

	public JamColorProvider getColorProvider() {
		if(provider == null)
			provider = new JamColorProvider();
		return provider;
	}

	private List<String> getAllJamFiles(String fileFullpath){
		List<String> files = new LinkedList<String>();
		
		IPath path = new Path(fileFullpath);
	  	IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
	  	
	  	if(!folder.exists())
	  		return null;
	  	
	  	if(folder.getType() != IResource.FOLDER) {
		  	System.out.println(fileFullpath + " is not folder " );
		  	return files;
	  	}
		
		IResource[] resources = null;
		try {
				resources = folder.members();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		
		if(resources == null)
			return files;
				
		for (IResource resource : resources) {
			if(resource.getType() == IResource.FOLDER){
				files.addAll(this.getAllJamFiles(fileFullpath + "/" + resource.getName()));
			}
			else
				files.add(fileFullpath + "/" + resource.getName());
		}
		
		return files;
	}
	
	public void updateMarkers(String filePath) throws CoreException {
		updateErrorMarker(null, filePath);
		updateRelationWarningMarker();
	}
	
	private void updateRelationWarningMarker() throws CoreException{
		List<Relation> relations = this.getNotExistRelations(jamEditorModel.getPlanPath());
		if(relations == null)
			return;
		
		Job markerJob = null;
		
		IPath path = new Path(jamEditorModel.getPlanPath());
		
		IFolder planFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path);  
		
		if(!planFolder.exists())
			return;
		
		IResource adapter = planFolder.getAdapter(IResource.class);
		
		if(!adapter.exists())
			return;
		
		markerJob = Job.create("update Warning Marker", (ICoreRunnable) monitor -> {
			
			List<IMarker> markers = Arrays.asList(adapter.findMarkers(IMarker.PROBLEM, true, 0));
			for (IMarker iMarker : markers) {
				if(iMarker != null && iMarker.getAttribute(IMarker.SEVERITY, -999)== IMarker.SEVERITY_WARNING)
					iMarker.delete();
			}
			for (Relation relation : relations) {
				String fileName = relation.get_fileName();
				int line = relation.get_line();
				
				IPath filePath = new Path(fileName);
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
				
				IMarker marker = file.createMarker(IMarker.PROBLEM);
				marker.setAttribute(IMarker.LINE_NUMBER, line);
				marker.setAttribute(IMarker.MESSAGE, "maybe this relation does not exist");
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
				
				DocumentAssistor da = new DocumentAssistor();
				da.readInputStream(file.getContents());
				
				int start = da.getLineOffset(line - 1);
				int end = da.getLineOffset(line) - 1;
				
				while(da.charAt(start) == ' ' || da.charAt(start) == '\t'){                                   
				    start++;
				}
				
				marker.setAttribute(IMarker.CHAR_START, start);
				marker.setAttribute(IMarker.CHAR_END, end);
				
			}
		});
		markerJob.setUser(false);
		markerJob.setPriority(Job.DECORATE);
		markerJob.schedule(200);
	}
	
	public void updateErrorMarker(DocumentEvent event, String filePath) throws CoreException {
		updatePlanIdErrorMarker(event, filePath);
		updateSyntaxErrorMarker(event, filePath);
	}
	
	public void updatePlanIdErrorMarker(DocumentEvent event, String filePath) throws CoreException {
		IPath path = new Path(filePath);
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		if(!file.exists()) {
			System.out.println("no file");
			return;
		}
		Job updateErrorMarkerJob = Job.create("update plan id Error Marker", (ICoreRunnable) monitor -> {
			deletePlanIdErrorMarks(file);
			addPlanIDErrorMark(event, file);
		});
		updateErrorMarkerJob.setUser(false);
		updateErrorMarkerJob.setPriority(Job.DECORATE);
		updateErrorMarkerJob.schedule(100);
	}
	
	
	private void deletePlanIdErrorMarks(IFile file) throws CoreException {
		List<IMarker> markers = Arrays.asList(file.findMarkers(IMarker.PROBLEM, true, 0));
		for (IMarker iMarker : markers) {
			if(iMarker!= null) {
				if(iMarker.getAttribute("isPlanIdError", false))
					iMarker.delete();
			}
		}
	}

	public void updateSyntaxErrorMarker(DocumentEvent event, String filePath) throws CoreException {
		IPath path = new Path(filePath);
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		if(!file.exists()) {
			System.out.println("file not exist");
			return;
		}
			
		Job updateErrorMarkerJob = Job.create("update syntax Error Marker", (ICoreRunnable) monitor -> {
			deleteSyntaxErrorMarks(file);
			addSyntaxErrorMark(event, file);
		});
			
		if(updateErrorMarkerJob.cancel()) {
			return;
		}
		
		updateErrorMarkerJob.setUser(false);
		updateErrorMarkerJob.setPriority(Job.DECORATE);
		updateErrorMarkerJob.schedule(500);
	}
	
	private void deleteSyntaxErrorMarks(IFile file) throws CoreException {
		List<IMarker> markers = Arrays.asList(file.findMarkers(IMarker.PROBLEM, true, 0));
		if(markers == null)
			return;
		
		for (IMarker iMarker : markers) {
			if(iMarker!= null) {
				if(iMarker.getAttribute("isSyntaxError", false)) {
					System.out.println("a :" + iMarker);
					iMarker.delete();
					System.out.println("b :" + iMarker);
				}
			}
		}
	}
	
	private boolean addSyntaxErrorMark(DocumentEvent event, IFile file) throws CoreException {
		
		String fileContent;
		
		if(event == null)
			fileContent = this.changeInputStreamToString(file.getContents());
		else
			fileContent = event.getDocument().get();

		ErrorInformation information = JAMParser.parseStringForErrorDetection(null, fileContent);
		
		if(information==null)
			return false;
		
		List<String> expectedTokens = information.getExpectedTokenImages();
		
		String errorMessage = "exptected tokens : \n";
		for (String expectedToken : expectedTokens) {
			errorMessage += expectedToken +"\n";
		}
		int line = information.getCurrentToken().beginLine;
		
		IMarker marker = file.createMarker(IMarker.PROBLEM);
		
		marker.setAttribute(IMarker.LINE_NUMBER, line);
		marker.setAttribute(IMarker.MESSAGE, errorMessage);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		marker.setAttribute("isSyntaxError", true);
		
		DocumentAssistor da = new DocumentAssistor();
		da.readString(fileContent);
		
		int start = da.getLineOffset(line - 1);
		int end = da.getLineOffset(line) - 1;
		
		while(da.charAt(start) == ' ' || da.charAt(start) == '\t'){                                   
		    start++;
		}
		
		marker.setAttribute(IMarker.CHAR_START, start);
		marker.setAttribute(IMarker.CHAR_END, end);
		return true;
	}
	
	private void addPlanIDErrorMark(DocumentEvent event, IFile file) throws CoreException {
		
		Interpreter interpreter;
		DocumentAssistor da= new DocumentAssistor();;
		
		if(event != null)
			return;
		
//		if(event == null) {
//			interpreter = JAMParser.parseFile(null, file);
//			da.readInputStream(file.getContents());
//		}
//		else {
//			interpreter = JAMParser.parseDocument(null, event.getDocument().get());
//			da.readString(event.getDocument().get());
//		}
//			
		interpreter = JAMParser.parseFile(null, file);
		da.readInputStream(file.getContents());
		
		if(interpreter == null)
			return;
		
		List<Plan> plans = new LinkedList<Plan>();
		
		plans.addAll(interpreter.getPlanLibrary().getGoalSpecPlans());
		plans.addAll(interpreter.getPlanLibrary().getConcludePlans());
		
		List<Plan> undefinedIdPlans = new LinkedList<Plan>();
		List<Plan> duplicatedIdPlans = new LinkedList<Plan>();
		for (Plan plan : plans) {
			if(plan.getId() == null) {
				undefinedIdPlans.add(plan);
			}
			else if(isDuplicatedId(plan)){
				duplicatedIdPlans.add(plan);
			}
		}
		
		for (Plan plan : undefinedIdPlans) {
			IMarker marker = file.createMarker(IMarker.PROBLEM);
			if(marker.exists()){
				int line = plan.get_line();
				
				marker.setAttribute(IMarker.LINE_NUMBER, line);
				marker.setAttribute(IMarker.MESSAGE, "plan id undefined");
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
				marker.setAttribute("isPlanIdError", true);

				
				int start = da.getLineOffset(line - 1);
				int end = da.getLineOffset(line) - 1;
				
				while(da.charAt(start) == ' ' || da.charAt(start) == '\t'){                                   
				    start++;
				}
				
//				System.out.println("------------------undefined : " + plan.getGoalSpecification());
				
				marker.setAttribute(IMarker.CHAR_START, start);
				marker.setAttribute(IMarker.CHAR_END, end);
			}
		}
		
		for (Plan plan : duplicatedIdPlans) {
			IMarker marker = file.createMarker(IMarker.PROBLEM);
			if(marker.exists()){
				int line = plan.get_line();
				
				marker.setAttribute(IMarker.LINE_NUMBER, line);
				marker.setAttribute(IMarker.MESSAGE, "plan id duplicated");
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
				marker.setAttribute("isPlanIdError", true);

				
//				System.out.println("------------------duplicated : " + plan.getGoalSpecification());
				
				int start = da.getLineOffset(line - 1) + 2;
				int end = da.getLineOffset(line) - 1;
				
				while(da.charAt(start) == ' ' || da.charAt(start) == '\t'){                                   
				    start++;
				}
				
				marker.setAttribute(IMarker.CHAR_START, start);
				marker.setAttribute(IMarker.CHAR_END, end);
			}
		}
	}
	
	private boolean isDuplicatedId(Plan plan) {
		if(plan == null)
			return false;
		
		for (Plan p: jamEditorModel.getPlanManager().getAllPlans()) {
			
			if(plan.getId() != null && p.getId().equals(plan.getId())) {
				if(plan.get_fileName().contentEquals(p.get_fileName()) && plan.get_line() == p.get_line()) {
					continue;
				}
					return true;
			}
		}
		return false;
	}
	
	
	private String changeInputStreamToString(InputStream inputStream) {

		    StringBuilder textBuilder = new StringBuilder();
		    try (Reader reader = new BufferedReader(new InputStreamReader
		      (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
		        int c = 0;
		        while ((c = reader.read()) != -1) {
		            textBuilder.append((char) c);
		        }
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return textBuilder.toString();
	}

	private List<Relation>getNotExistRelations(String planPath) {
		
		List<String> allJamfiles = this.getAllJamFiles(planPath);
		
		List<Relation> notExistRelations = new LinkedList<Relation>();
		for (String jamFilePath : allJamfiles) {
			List<Relation> notExistRelations1 = jamEditorModel.checkRelationExist(jamFilePath);
			
			if(notExistRelations1 != null) {
				notExistRelations.addAll(notExistRelations1);
			}
		}
		return notExistRelations;
	}

}

package kr.ac.uos.ai.editor.jamEditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import kr.ac.uos.ai.editor.jam.model.EditorModel;
import kr.ac.uos.ai.editor.jam.model.Plan;
import kr.ac.uos.ai.editor.jam.model.PlanTable;
import kr.ac.uos.ai.editor.jam.model.Prefix;
import kr.ac.uos.ai.editor.jam.model.PrefixManager;
import kr.ac.uos.ai.editor.jam.model.Relation;
import kr.ac.uos.ai.editor.jam.model.RelationTable;
import kr.ac.uos.ai.editor.jam.parser.JAMParser;
import kr.ac.uos.ai.editor.jamEditor.util.JamColorProvider;

public class JamEditorPlugin extends AbstractUIPlugin {

//	private static JavaEditorExamplePlugin fgInstance;

	private static JamEditorPlugin plugin;
	private JamColorProvider provider;
	private EditorModel jamEditorModel;
	
	public JamEditorPlugin() {
		super();
		plugin = this;
		jamEditorModel = EditorModel.getInstance();
		System.out.println("-------- Jam editor Activate");
		initFileEventListener();
	}

	public static JamEditorPlugin getDefault() {
		return plugin;
	}
	
	public static JamEditorPlugin getInstance() {
		return plugin;
	}
	
	public void initEditorModel(String projectName) {
		jamEditorModel.init(projectName);
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
//		System.out.println("delete file event : " + fileFullpath);
		PlanTable planManager = jamEditorModel.getPlanManager();
		List<Plan> plans = planManager.getPlansFromFileName(fileFullpath);
		
		if(plans != null) {
			List<Plan> plansCopy = new ArrayList<>(plans);
			for (Plan plan : plansCopy) {
				planManager.removePlan(plan);
			}
		}
		
		
		RelationTable relationManager = jamEditorModel.getRelationManager();
		List<Relation> relations = relationManager.getRelationsFromFile(fileFullpath);

		if(relations != null) {
			List<Relation> relationsCopy = new ArrayList<>(relations);
			for (Relation relation : relationsCopy) {
				relationManager.removeRelation(relation);
			}
		}

		
		PrefixManager prefixManager = jamEditorModel.getPrefixManager();
		List<Prefix> prefixes = prefixManager.getAllPrefixes();
		
		if(prefixes != null) {
			List<Prefix> prefixesCopy = new ArrayList<>(prefixes);
			for (Prefix prefix : prefixesCopy) {
				prefix.get_fileName().equals(fileFullpath);
				prefixManager.remove(prefix);
			}	
		}
	}
	
	public void changeFileEvent(String fileFullpath) {
		delelteFileEvent(fileFullpath);
		addFileEvent(fileFullpath);
		jamEditorModel.printEditorModel();
	}

	public void addFileEvent(String fileFullpath) {
//		System.out.println("add file event : " + fileFullpath);
		JAMParser.parseFile(fileFullpath);
	}

	public JamColorProvider getColorProvider() {
		if(provider == null)
			provider = new JamColorProvider();
		return provider;
	}

}

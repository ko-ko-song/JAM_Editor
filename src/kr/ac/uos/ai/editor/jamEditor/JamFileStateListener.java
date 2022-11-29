package kr.ac.uos.ai.editor.jamEditor;

import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

import kr.ac.uos.ai.editor.jam.model.EditorModel;
import kr.ac.uos.ai.editor.jamEditor.util.Util;

public class JamFileStateListener implements IPartListener{

	private JamEditorPlugin jamEditorPlugin= JamEditorPlugin.getDefault();
	
	@Override
	public void partActivated(IWorkbenchPart part) {
		
		
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		IEditorPart activeEditor = Util.getActiveEditor();
		if(activeEditor == null)
			return;
		
		String projectName = activeEditor.getEditorInput().getAdapter(IResource.class).getProject().getName();
		EditorModel jamEditorModel = jamEditorPlugin.getEditorModel();
		if(projectName.equals(jamEditorModel.getProjectName())){
			return;
		}
		else {
			jamEditorPlugin.initEditorModel(projectName);
		}
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		IEditorPart activeEditor = Util.getActiveEditor();
		if(activeEditor == null)
			return;
		
		String projectName = activeEditor.getEditorInput().getAdapter(IResource.class).getProject().getName();
		EditorModel jamEditorModel = jamEditorPlugin.getEditorModel();
		if(projectName.equals(jamEditorModel.getProjectName())){
			return;
		}
		else {
			jamEditorPlugin.initEditorModel(projectName);
		}
		
	}

}

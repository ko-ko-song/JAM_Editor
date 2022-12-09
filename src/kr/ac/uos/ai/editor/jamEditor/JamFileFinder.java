package kr.ac.uos.ai.editor.jamEditor;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

import kr.ac.uos.ai.editor.jamEditor.util.Util;

public class JamFileFinder{
	
	public JamFileFinder() {
	}
	
	public void findFiles() {
		IEditorPart activeEditor = Util.getActiveEditor();
		if(activeEditor != null) {
			IEditorInput editorInput = activeEditor.getEditorInput();
			IResource adapter = editorInput.getAdapter(IResource.class);
			
			IResource[] members = null;
			try {
				members = adapter.getParent().members();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (IResource member : members) {
				System.out.println(member.getName());
			}
			
		}
	}

}

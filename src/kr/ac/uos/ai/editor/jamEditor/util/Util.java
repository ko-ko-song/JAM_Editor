package kr.ac.uos.ai.editor.jamEditor.util;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class Util {
	
	private Util() {
	       // only helper
    }

	public static IEditorPart getActiveEditor() {
		
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
        if (null == activeWorkbenchWindow) {
            activeWorkbenchWindow = workbench.getWorkbenchWindows()[0];
        }
        IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
        if (activePage == null) {
            return null;
        }
        
        
        
        return activePage.getActiveEditor();
    }
	
	
//	public void getDocument() {
//		IPath path = Path.fromOSString(file);
//		IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
//
//		ITextFileBufferManager iTextFileBufferManager = FileBuffers.getTextFileBufferManager();
//		ITextFileBuffer iTextFileBuffer = null;
//		IDocument iDoc = null;
//		try    {
//		    iTextFileBufferManager.connect(iFile.getFullPath(), LocationKind.IFILE, new NullProgressMonitor());
//		    iTextFileBuffer = iTextFileBufferManager.getTextFileBuffer(iFile.getFullPath(), LocationKind.IFILE);
//		    iDoc = iTextFileBuffer.getDocument();
//
//		    iTextFileBufferManager.disconnect(iFile.getFullPath(), LocationKind.IFILE, new NullProgressMonitor());
//		} catch (Exception e) {
//		    e.printStackTrace();
//		}
//
//		int start = iDoc.getLineOffset(iline - 1);
//		int end = iDoc.getLineLength(iline - 1);
//
//		//this next section was done to remove the leading white spaces                  
//		while(iDoc.getChar(start) == ' ' || iDoc.getChar(start) == '\t'){                                   
//		    start++;
//		    end--;
//		}
//
//		 final int charStart = start;
//		 final int charEnd = start + end;
//	}
	
	
}

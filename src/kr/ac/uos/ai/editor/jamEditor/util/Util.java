package kr.ac.uos.ai.editor.jamEditor.util;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import kr.ac.uos.ai.editor.jamEditor.JamEditorPlugin;
import uos.ai.jam.Prefix;

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
	
	
	public static String getPrefixedStringValue(String value, String filePath) {
		String prefixStringValue = null;
		for (Prefix prefix : JamEditorPlugin.getInstance().getEditorModel().getPrefixManager().getAllPrefixes()) {
			if(!prefix.get_fileName().contentEquals(filePath)) 
				continue;
			
			String prefixValue = prefix.getValue();
			
			if(value.contains(prefixValue)) {
				prefixStringValue = value.replace(prefixValue, prefix.getPrefix()+":");
				prefixStringValue.substring(1, prefixStringValue.length()-1);
				System.out.println("prefixed value : " + prefixStringValue);
				return prefixStringValue;
			}
		}

		return prefixStringValue;
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

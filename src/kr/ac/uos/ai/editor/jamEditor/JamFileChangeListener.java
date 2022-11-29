package kr.ac.uos.ai.editor.jamEditor;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.EditorPart;

import kr.ac.uos.ai.editor.jam.model.EditorModel;
import kr.ac.uos.ai.editor.jamEditor.util.Util;

public class JamFileChangeListener implements IResourceChangeListener {
	
//    private TableViewer table; //assume this gets initialized somewhere

    public void resourceChanged(IResourceChangeEvent event) {
       //we are only interested in POST_CHANGE events
    	
       if (event.getType() != IResourceChangeEvent.POST_CHANGE)
          return;
       
       IResourceDelta rootDelta = event.getDelta();
       
       IResourceDelta planDocDelta = null;
//       for (IProject iProject : projects) {
//    	   String projectName = iProject.getName();
//    	   IPath planPath = new Path(projectName + "/plan");
//    	   planDocDelta = rootDelta.findMember(planPath);
//    	   
//    	   if(planDocDelta != null)
//    		   break;
//       }
       
       IResourceDelta projectDelta = rootDelta.getAffectedChildren()[0];
       
       if(projectDelta == null)
    	   return;
       
       planDocDelta = projectDelta.findMember(new Path("plan"));
       
       if(planDocDelta == null)
    	   planDocDelta = projectDelta.findMember(new Path("plans"));   

       
//       IResourceDelta docDelta = rootDelta.findMember(docPath);
       if (planDocDelta == null)
          return;

       System.out.println(planDocDelta.toString());
       
       final ArrayList changed = new ArrayList();
//       IFile a = new File(DOC_PATH, Workspace.get);
       
//       System.out.println(planDocDelta.toString());
       
       IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {
          public boolean visit(IResourceDelta delta) {
        	  
        	  IResource resource = delta.getResource();
        	  //only jam file extension
        	  if (resource.getType() != IResource.FILE) {
        		  return true;
        	  }
        	  
        	  if(resource.getFileExtension() == null) {
        		  return true;
        	  }
        	  
        	  if(!resource.getFileExtension().equals("jam")) {
        		  return true;
        	  }
        	  
        	  String path = resource.getFullPath().toString();
//    		  String fileName = resource.getName();
        	  
              //file removed 
        	  if(delta.getKind() == IResourceDelta.REMOVED) {
        		  JamEditorPlugin.getInstance().delelteFileEvent(path);
//        		  JamEditorPlugin.getInstance().getEditorModel().printEditorModel();
        		  System.out.println("removed");
        		  return true;
        	  }      
        	  
        	  //file added
        	  if(delta.getKind() == IResourceDelta.ADDED) {
        		  JamEditorPlugin.getInstance().addFileEvent(path);
//        		  JamEditorPlugin.getInstance().getEditorModel().printEditorModel();        		  
        		  System.out.println("added");
        		  return true;
        	  }
        	  
        	  //only interested in content changes
        	  if (delta.getFlags() == IResourceDelta.CONTENT) {
        		  JamEditorPlugin.getInstance().changeFileEvent(path);
//        		  JamEditorPlugin.getInstance().getEditorModel().printEditorModel();
        		  System.out.println("changed");
        		  return true;
        	  }
                
             //only interested in files with the "txt" extension
//             if (resource.getType() == IResource.FILE && 
//				"txt".equalsIgnoreCase(resource.getFileExtension())) {
//                changed.add(resource);
//             }
             return true;
          }
       };
       try {
          planDocDelta.accept(visitor);
       } catch (CoreException e) {
          //open error dialog with syncExec or print to plugin log file
       }
       //nothing more to do if there were no changed text files
       if (changed.size() == 0)
          return;
       //post this update to the table
//       Display display = table.getControl().getDisplay();
//       if (!display.isDisposed()) {
//          display.asyncExec(new Runnable() {
//             public void run() {
//                //make sure the table still exists
//                if (table.getControl().isDisposed())
//                   return;
//                table.update(changed.toArray(), null);
//             }
//          });
//       }
    }
 }
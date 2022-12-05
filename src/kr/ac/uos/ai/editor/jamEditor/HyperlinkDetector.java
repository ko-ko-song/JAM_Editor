package kr.ac.uos.ai.editor.jamEditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import kr.ac.uos.ai.editor.jamEditor.util.Util;
import uos.ai.jam.plan.Plan;

public class HyperlinkDetector extends AbstractHyperlinkDetector{
	private static final String DEPENDENT_PROPERTY = "Dependent:";
	private Region targetRegion = null;
	
    @Override
    public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
//        IDocument document = textViewer.getDocument();
//
//        IEditorPart activeEditor = Util.getActiveEditor();
//        if (activeEditor != null) {
//            IEditorInput editorInput = activeEditor.getEditorInput();
//            IResource adapter = editorInput.getAdapter(IResource.class);
//            IContainer parent = adapter.getParent();
//            try {
//                int offset = region.getOffset();
//                IRegion lineInformationOfOffset = document.getLineInformationOfOffset(offset);
//                String lineContent = document.get(lineInformationOfOffset.getOffset(),
//                        lineInformationOfOffset.getLength());
//                
//  
//                String[] lineContentSplit = lineContent.split(" ");
//                String relationName = "";
//                int relationNameStart = 0;
//                targetRegion = null;
//                if(lineContentSplit!= null) {
//                	if(lineContentSplit.length == 2) {
//                		relationName = lineContentSplit[1];
//                		relationNameStart = lineContent.lastIndexOf(relationName);
//                		targetRegion = new Region(lineInformationOfOffset.getOffset() + relationNameStart, relationName.length());
//
//                	}
//                }
//                
//                if(targetRegion == null) {
//                	return new IHyperlink[0];
//                }
//                
//                List<Plan> plans = JamEditorPlugin.getInstance().getEditorModel().getPlanManager().getPlans(relationName);
//                List<IResource> resources = new LinkedList<IResource>();
//                
//                if(plans == null) {
//                	return new IHyperlink[0];
//                }
//                
//                for (Plan plan : plans) {
//                	IPath path = new Path(plan.get_fileName());
//                    IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
//                    resources.add(iFile);
//				}
//                
//                return resources.stream().filter(res -> res instanceof IFile)
//                        .map(res -> new ResourceHyperlink(targetRegion, res.getName(), (IFile) res))
//                        .toArray(IHyperlink[]::new);
//                
//                
////                    IResource[] members = parent.members();                    
//                // current resource itself
////                    return Arrays.asList(members).stream()
////                            .filter(res -> res instanceof IFile)
////                            .map(res -> new ResourceHyperlink(targetRegion, res.getName(), (IFile) res))
////                            .toArray(IHyperlink[]::new);
////                }
//            } catch (BadLocationException e) {
//                e.printStackTrace();
//            }
//        }
//        // do not return new IHyperlink[0] because the array may only be null or not
//        // empty
        return null;
    }
    
    
    private boolean isContainPlan(IResource planFile, String planName) {
    	try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(((IFile)planFile).getContents()));
			String lineText = "";
			
			while((lineText = reader.readLine()) != null) {
				if((lineText.contains("PLAN") || lineText.contains("plan")) && lineText.contains(planName)) 
					return true;
			}
			
		} catch (CoreException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return false;
    }
    
}

package kr.ac.uos.ai.editor.jamEditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
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

public class HyperlinkDetector extends AbstractHyperlinkDetector{
	private static final String DEPENDENT_PROPERTY = "Dependent:";

    @Override
    public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
        IDocument document = textViewer.getDocument();

        IEditorPart activeEditor = Util.getActiveEditor();
        if (activeEditor != null) {
            IEditorInput editorInput = activeEditor.getEditorInput();
            IResource adapter = editorInput.getAdapter(IResource.class);
            IContainer parent = adapter.getParent();
            try {
                int offset = region.getOffset();
                IRegion lineInformationOfOffset = document.getLineInformationOfOffset(offset);
                String lineContent = document.get(lineInformationOfOffset.getOffset(),
                        lineInformationOfOffset.getLength());

                // Content assist should only be used in the dependent line
                if (lineContent.startsWith(DEPENDENT_PROPERTY)) {
                    String planNameBeingDetect = lineContent.substring(DEPENDENT_PROPERTY.length()).trim();

                    Region targetRegion = new Region(lineInformationOfOffset.getOffset() + DEPENDENT_PROPERTY.length(),
                            lineInformationOfOffset.getLength() - DEPENDENT_PROPERTY.length());

                    IResource[] members = parent.members();

                    // Only take resources, which have the "todo" file extension and skip the
                    // current resource itself
                    return Arrays.asList(members).stream()
                            .filter(res -> res instanceof IFile && isContainPlan(res, planNameBeingDetect))
                            .map(res -> new ResourceHyperlink(targetRegion, res.getName(), (IFile) res))
                            .toArray(IHyperlink[]::new);
                }
            } catch (CoreException | BadLocationException e) {
                e.printStackTrace();
            }
        }
        // do not return new IHyperlink[0] because the array may only be null or not
        // empty
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

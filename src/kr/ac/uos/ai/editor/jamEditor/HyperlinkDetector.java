package kr.ac.uos.ai.editor.jamEditor;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;

import kr.ac.uos.ai.editor.jamEditor.util.Util;
import uos.ai.jam.parser.JAMParser;
import uos.ai.jam.plan.Plan;
import uos.ai.jam.plan.action.GoalAction;

/**
 * 
 * @author JiHun
 *
 */
public class HyperlinkDetector extends AbstractHyperlinkDetector{
	private Region targetRegion = null;
	
    @Override
    public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
        IDocument document = textViewer.getDocument();

        IEditorPart activeEditor = Util.getActiveEditor();
        if (activeEditor != null) {
            try {
                int offset = region.getOffset();
                IRegion lineInformationOfOffset = document.getLineInformationOfOffset(offset);
                String lineContent = document.get(lineInformationOfOffset.getOffset(),
                        lineInformationOfOffset.getLength());
                
                GoalAction goalAction = JAMParser.parseAction(lineContent);
                
                if(goalAction == null)
                	return null;
                
                String relationName = goalAction.getRelation().getName(); 
                int arity = goalAction.getRelation().getArity();
                
                int relationNameStart = lineContent.indexOf(relationName);
                int relationArgEnd = lineContent.indexOf(")");
                
                targetRegion = new Region(lineInformationOfOffset.getOffset() + relationNameStart, relationArgEnd - relationNameStart);
                if(targetRegion == null) 
                	return null;

                System.out.println("rel name : " + relationName);
                
                List<Plan> plans = JamEditorPlugin.getInstance().getEditorModel().getPlanManager().getPlans(relationName);
                if(plans == null) 
                	return null;
                
                List<IResource> resources = new LinkedList<IResource>();
                for (Plan plan : plans) {
                	int planRelationArty = 0;
                	if(plan.getGoalSpecification() != null) {
                		planRelationArty = plan.getGoalSpecification().getRelation().getArity();
                	}
                	
                	if(plan.getConcludeSpecification() != null) {
                		planRelationArty = plan.getConcludeSpecification().getArity();
                	}
                	
                	if(planRelationArty != arity)
                		continue;
                	
                	IPath path = new Path(plan.get_fileName());
                    IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
                    resources.add(iFile);
				}
                
                List<IHyperlink> hyperlinks = new LinkedList<IHyperlink>();
                for (IResource resource : resources) {
					if(!(resource instanceof IFile))
						continue;
					
					hyperlinks.add(new ResourceHyperlink(targetRegion, resource.getName(), (IFile)resource));
				}
                
                if(hyperlinks.size() == 0)
                	return null;
                
                return hyperlinks.toArray(new IHyperlink[0]);
                
                
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
}

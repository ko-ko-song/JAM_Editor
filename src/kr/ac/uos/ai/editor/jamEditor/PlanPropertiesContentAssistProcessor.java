package kr.ac.uos.ai.editor.jamEditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import kr.ac.uos.ai.editor.jam.model.Plan;
import kr.ac.uos.ai.editor.jamEditor.util.Util;

public class PlanPropertiesContentAssistProcessor implements IContentAssistProcessor{
	// public as used later by other code
//    public static final List<String> PALNINNERPROPOSALS = Arrays.asList("ID:", "NAME:", "PRECONDITION:", "CONTEXT:", "BODY:", "UTILITY:");
    public static List<String> GoalNameProposals;
    
    public PlanPropertiesContentAssistProcessor() {
		GoalNameProposals = new LinkedList<String>();
	
	}
    
//    public void saveEditor() {
//    	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//		IEditorPart editor = page.getActiveEditor();
//		page.saveEditor(editor, true /* confirm */);
//    }
    
    private List<String> getGoalNameList(IResource planFile) {
    	List<String> goalNames = null;
    	try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(((IFile)planFile).getContents()));
			goalNames = new LinkedList<String>();
			String lineText = "";
			try {
				while((lineText = reader.readLine()) != null) {
					if((lineText.contains("PLAN") || lineText.contains("plan")) && 
							(lineText.contains("ACHIEVE") || lineText.contains("achieve") 
							||lineText.contains("PERFORM") || lineText.contains("perform"))) {
							
						String[] lineTextSplit = lineText.split(" ");
						String goalName = lineTextSplit[2];
						for(int i=3; i<lineTextSplit.length; i++) {
							goalName = goalName + lineTextSplit[i];
						}
						
						goalNames.add(goalName.replace("{", "").trim());	
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
			
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return goalNames;
	}

	@Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		
        IDocument document = viewer.getDocument();
        
        //임시
//		List<IResource> jamFiles = findJamFiles();
		
//		if(jamFiles != null) {
//			for (IResource jamFile : jamFiles) {
//				List<String> jamFileList = getGoalNameList(jamFile);
//				if(jamFile != null) {
//					for (String goalName : jamFileList) {
//						if(!GoalNameProposals.contains(goalName))
//							GoalNameProposals.add(goalName);
//					}
//				}
//			}
//		}
		
//		for (String goalName: GoalNameProposals) {
//			System.out.println(goalName);
//		}
		
        
        
        
        try {
        	
        	String documentText = document.get();
        	
        	int scopeStartOffset = -1;
        	int scopeEndOffset = -1;
        	boolean isInScope = false;
        	
        	for (int i = 0; i < documentText.length(); i++) {
        		
				if(documentText.charAt(i) == '{') 
					scopeStartOffset = i;
				
				if(documentText.charAt(i) == '}') 
					scopeEndOffset = i;
				
				if(scopeStartOffset != -1 && scopeEndOffset != -1) {
					if(scopeStartOffset < offset && scopeEndOffset > offset) {
						isInScope = true;
						break;
					}
						
					else {
						scopeStartOffset = -1;
						scopeEndOffset = -1;
					}
				}
			}
        	
        	int lineOfOffset = document.getLineOfOffset(offset);
            int lineOffset = document.getLineOffset(lineOfOffset);

            int lineTextLength = offset - lineOffset;
            String lineStartToOffsetValue = document.get(lineOffset, lineTextLength).toLowerCase().trim();
            
            
            
            int replacementLength = lineStartToOffsetValue.length(); 
            
            String scopeText = document.get(scopeStartOffset, scopeEndOffset - scopeStartOffset +1);
            
            
            
            boolean isBodyScope = false;
            
            
            int i=0;
            int bodyScopeLine= lineOfOffset;
            String lineProperty = document.get(lineOffset, document.getLineOffset(lineOfOffset+1) - lineOffset);
            while(true) {
            	if(lineProperty.contains(":")) {
            		if(lineProperty.contains("BODY") || lineProperty.contains("body")) {
            			isBodyScope = true;
            		}
            		else {
            			isBodyScope = false;
            		}
            	}
            	else {
            		 bodyScopeLine = bodyScopeLine -1;
            		 int lineOffset2 = document.getLineOffset(bodyScopeLine);
            		 int downlineOffset = document.getLineOffset(bodyScopeLine+1);
            		 lineProperty = document.get(lineOffset2, downlineOffset - lineOffset2);
            	}
            	
            	i++;
            	if(i>100)
            		break;
            }
           
            if(isBodyScope) {
            	
            	
            	String[] lineStartToOffsetValueSplit = lineStartToOffsetValue.split(" ");
            	
//            	System.out.println(lineStartToOffsetValue);
//            	System.out.println(lineStartToOffsetValueSplit.length);
            	
            	List<String> planGoalsString = new LinkedList<String>();
            	
            	for (Plan plan  : JamEditorPlugin.getDefault().getEditorModel().getPlanManager().getAllPlans()) {
            		
            		String argString = "(";
            		
            		String[] args = plan.getGoalAction().getRelation().getArgs(); 
            		
            		if(args.length != 0) {
            			for (String arg : args) {
    						argString += arg + ", ";
    					}
            		}
            		argString = argString.substring(0, argString.length() - 2);
            		String planGoalString = plan.getGoalAction().getRelation().getName() + argString + ")";
            		planGoalsString.add(planGoalString);
				}
            	
            	if(lineStartToOffsetValueSplit.length < 2)
            		return new ICompletionProposal[0];
            	else {
            		int replacementLength2 = lineStartToOffsetValueSplit[1].length();
            		int replacementStartOffset = offset - replacementLength2;
            		return planGoalsString.stream().filter(proposal -> proposal.toLowerCase().startsWith(lineStartToOffsetValueSplit[1]))
                			.map(proposal -> new CompletionProposal(proposal, replacementStartOffset, replacementLength2, proposal.length()))
                			.toArray(ICompletionProposal[]::new);
            	}
            	
            	
            }
            
            
//            else if(isInScope) {
//                return PALNINNERPROPOSALS.stream().filter(proposal -> !scopeText.contains(proposal)
//                		&& proposal.toLowerCase().startsWith(lineStartToOffsetValue))
//                        .map(proposal -> new CompletionProposal(proposal, offset - replacementLength, replacementLength, proposal.length()))
//                        .toArray(ICompletionProposal[]::new);            	
//            }
            else {
                return new ICompletionProposal[0];
            }
            
        } catch (BadLocationException e) {
            // ignore here and just continue
        }
        return new ICompletionProposal[0];

    }

   
    public List<IResource> findJamFiles() {
		IEditorPart activeEditor = Util.getActiveEditor();
		if(activeEditor != null) {
			IEditorInput editorInput = activeEditor.getEditorInput();
			IResource adapter = editorInput.getAdapter(IResource.class);

			//root 까지 탐색 후 
			int i=0;
			while(adapter.getType() != 8) {
				adapter = adapter.getParent();
				if(i>100)
					break;
			}
			
			List<IResource> jamFiles = getMembers(((IContainer) adapter));
			return jamFiles;
		}
		
		System.out.println("active editor null");
		return null;
	}
    
    public List<IResource> getMembers(IContainer foler) {
    	IResource[] members = null;
    	List<IResource> jamFiles = new LinkedList<IResource>();
		try {
			members = foler.members();
	    	for (IResource member : members) {
				if(member.getType() == 2 ||member.getType() == 4) {
					List<IResource> childJamfiles = this.getMembers((IContainer)member);
					if(childJamfiles != null) {
						for (IResource iResource : childJamfiles) {
							jamFiles.add(iResource);
						}
						
					}
				}
				else if(member.getType() == 1){
					if(member.getFileExtension().equals("jam")) {
						jamFiles.add(member);
					}
				}
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jamFiles;
    }
    
    @Override
    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
        return null;
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        return null;
    }

    @Override
    public char[] getContextInformationAutoActivationCharacters() {
        return null;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }

    @Override
    public IContextInformationValidator getContextInformationValidator() {
        return null;
    }
}

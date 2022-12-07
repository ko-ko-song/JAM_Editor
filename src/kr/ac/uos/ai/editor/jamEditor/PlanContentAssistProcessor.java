package kr.ac.uos.ai.editor.jamEditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.ui.IEditorPart;

import kr.ac.uos.ai.editor.jamEditor.util.DocumentAssistor;
import kr.ac.uos.ai.editor.jamEditor.util.Util;
import uos.ai.jam.Prefix;
import uos.ai.jam.expression.Relation;
import uos.ai.jam.parser.JAMParser;
import uos.ai.jam.plan.Plan;

public class PlanContentAssistProcessor implements IContentAssistProcessor{
    public static List<String> goalActionProposals = Arrays.asList("perform", "achieve", "maintain", "conclude");
    public static List<String> worldModelActionProposals = Arrays.asList("fact", "retrieve", "retract", "update", "assert");
    public static List<String> etcProposals = Arrays.asList("System.out.println()");
    
    private String replacementText = "";
    private int replacementLength = 0;
    
    public PlanContentAssistProcessor() {
	
	}
    
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		
		IDocument document = viewer.getDocument();
		List<String> proposalList = new LinkedList<String>();
		

		
		
//		String documentText = document.get();
		
		int lineOfOffset;
		int lineOffset = 0;
		try {
			lineOfOffset = document.getLineOfOffset(offset);
			lineOffset = document.getLineOffset(lineOfOffset);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int lineTextLength = offset - lineOffset;
		
		String lineStartToOffsetValue = null;
		try {
			lineStartToOffsetValue = document.get(lineOffset, lineTextLength).toLowerCase().trim();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(lineStartToOffsetValue == null)
			return new ICompletionProposal[0];
		
		String[] lineText = lineStartToOffsetValue.split(" ");
	
		
		if(lineText == null) 
			return new ICompletionProposal[0];
	
		if(lineText.length < 2) {
			replacementText = lineText[0];
			replacementLength = replacementText.length(); 

			proposalList.addAll(goalActionProposals);
			proposalList.addAll(worldModelActionProposals);
			proposalList.addAll(etcProposals);
			//action ÃßÃµ
			
			for (String goalActionProposal : goalActionProposals) {
				if(goalActionProposal.equalsIgnoreCase(replacementText)) {
					proposalList.clear();
					proposalList.addAll(getGoalProposalList());
					replacementText = "";
					replacementLength = 0;
					break;
				}
			}
			
			for (String worldModelActionProposal : worldModelActionProposals) {
				if(worldModelActionProposal.equalsIgnoreCase(replacementText)) {
					proposalList.clear();
					proposalList.addAll(getRelationProposalList());
					replacementText = "";
					replacementLength = 0;
					break;
				}
			}
		}
		
		else if(lineText.length == 2) {
			replacementText = lineText[1];
			replacementLength =replacementText.length();
			for (String goalActionProposal : goalActionProposals) {
				if(goalActionProposal.equalsIgnoreCase(lineText[0])) {
					proposalList.addAll(getGoalProposalList());
					break;
				}
			}
			
			for (String worldModelActionProposal : worldModelActionProposals) {
				if(worldModelActionProposal.equalsIgnoreCase(lineText[0])) {
					proposalList.addAll(getRelationProposalList());
					break;
				}
			}
			
		}
		else {
			return new ICompletionProposal[0];
		}
		
		
		
		
		List<String> prefixedProposals = null;
		String content = document.get();
		int index = content.indexOf("PREFIXES");
		if(index != -1) {
			try {
				String c = document.get(index, document.getLength() - index);
				System.out.println(c);
				Map<String, String> prefixMap = JAMParser.parsePrefixes(c);
				
				System.out.println("null");
				if(prefixMap != null) {
					System.out.println("not null");
					prefixedProposals = new LinkedList<String>();
					for (String proposal : proposalList) {
						boolean prefixed = false;
						
						for (Entry<String, String> entry : prefixMap.entrySet()) {
							System.out.println("entry set : " + entry.getKey() + "  " + entry.getValue());
							System.out.println(proposal);
							if(proposal.contains(entry.getValue())) {
								System.out.println("ex : " + proposal);
								proposal = proposal.replace(entry.getValue(), entry.getKey()+":");
								proposal = proposal.replace("<", "");
								proposal = proposal.replace(">", "");
								prefixedProposals.add(proposal);
								prefixed = true;
								System.out.println("af : " + proposal);
								break;
							}
						}
						if(!prefixed) 
							prefixedProposals.add(proposal);
						
						else 
							prefixed = false;
					}
				}
				
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(prefixedProposals != null)
			proposalList = prefixedProposals;
		
		
   		int replacementStartOffset = offset - replacementLength;
   		return proposalList.stream().filter(proposal -> proposal.toLowerCase().startsWith(replacementText))
       			.map(proposal -> new CompletionProposal(proposal, replacementStartOffset, replacementLength, proposal.length()))
       			.toArray(ICompletionProposal[]::new);
		
	}
    
    
    private List<String> getGoalProposalList() {
    	List<Plan> plans = JamEditorPlugin.getDefault().getEditorModel().getPlanManager().getAllPlans();
		List<String> proposals = new LinkedList<String>();
		for (Plan plan : plans) {
			Relation r =  plan.getGoalSpecification().getRelation();
			List<String> argString = new LinkedList<String>();
			for (int i = 0; i < r.getArity(); i++) {
				argString.add(r.getArg(i).toString());
			}
			
			proposals.add(r.getName() + "(" + String.join(", ", argString)  +")");
			
			//prefix
			List<Prefix> prefixes = new LinkedList<Prefix>();
			
			
		}
		return proposals;
    }
    
    private List<String> getRelationProposalList(){
    	List<Relation> relations = JamEditorPlugin.getDefault().getEditorModel().getRelationManager().getAllRelations();
		List<String> proposals = new LinkedList<String>();

		for (Relation relation : relations) {
			List<String> argString = new LinkedList<String>();
			for (int i = 0; i < relation.getArity(); i++) {
				argString.add(relation.getArg(i).toString());
			}
			
			proposals.add(relation.getName() + "(" + String.join(", ", argString)  +")");
		}
		return proposals;
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

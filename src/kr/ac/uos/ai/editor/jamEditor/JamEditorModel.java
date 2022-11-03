package kr.ac.uos.ai.editor.jamEditor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JamEditorModel {

	private static JamEditorModel instance = new JamEditorModel();
	
	private List<String> PLANPROPOSAL;
	private List<String> PLANSCHEMAPROPOSAL;
	
	
	private JamEditorModel() {
		PLANPROPOSAL = new LinkedList<>();
		PLANSCHEMAPROPOSAL = Arrays.asList("ID:", "NAME:", "PRECONDITION:", "CONTEXT:", "BODY:", "UTILITY:");
	}


	public static JamEditorModel getInstance() {
		return instance;
	}


	public static void setInstance(JamEditorModel instance) {
		JamEditorModel.instance = instance;
	}


	public List<String> getPLANPROPOSAL() {
		return PLANPROPOSAL;
	}


	public void setPLANPROPOSAL(List<String> pLANPROPOSAL) {
		PLANPROPOSAL = pLANPROPOSAL;
	}


	public List<String> getPLANSCHEMAPROPOSAL() {
		return PLANSCHEMAPROPOSAL;
	}


	public void setPLANSCHEMAPROPOSAL(List<String> pLANSCHEMAPROPOSAL) {
		PLANSCHEMAPROPOSAL = pLANSCHEMAPROPOSAL;
	}
	
	public void addPLANPROPOSAL(String e) {
		this.PLANPROPOSAL.add(e);
	}
	
	public void removePLANPROPOSAL(String e) {
		for(String p : PLANPROPOSAL){
			if(p.equals(e))
				PLANPROPOSAL.remove(p);
		}
	}
	
}


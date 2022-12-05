package kr.ac.uos.ai.editor.jam.action;

import kr.ac.uos.ai.editor.jam.model.Relation;

public class RetrieveAction extends WorldmodelAction{
	
	public RetrieveAction(String name, Relation relation) {
		super(name, relation);
		actType = ACT_RETRIEVE;
	}
}

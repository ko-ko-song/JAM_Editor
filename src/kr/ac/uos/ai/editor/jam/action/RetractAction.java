package kr.ac.uos.ai.editor.jam.action;

import kr.ac.uos.ai.editor.jam.model.Relation;

public class RetractAction extends WorldmodelAction{
	
	public RetractAction(String name, Relation relation) {
		super(name, relation);
		actType = ACT_RETRACT;
	}
}

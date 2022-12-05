package kr.ac.uos.ai.editor.jam.action;

import kr.ac.uos.ai.editor.jam.model.Relation;

public class UpdateAction extends WorldmodelAction{
	
	Relation newRelation;
	
	public UpdateAction(String name, Relation oldRelation, Relation newRelation) {
		super(name, oldRelation);
		this.newRelation = newRelation;
		actType = ACT_UPDATE;
	}
}

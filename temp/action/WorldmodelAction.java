package kr.ac.uos.ai.editor.jam.action;

import kr.ac.uos.ai.editor.jam.model.Relation;

public class WorldmodelAction extends Action{
	
	protected Relation relation;
	
	public WorldmodelAction(String name, Relation relation) {
		this.name = name;
		this.relation = relation;
	}
	
	public Relation getRelation() {
		return this.relation;
	}
	
}

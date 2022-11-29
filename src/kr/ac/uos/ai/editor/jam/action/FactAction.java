package kr.ac.uos.ai.editor.jam.action;

import kr.ac.uos.ai.editor.jam.model.Relation;

public class FactAction extends WorldmodelAction{

	public FactAction(String name, Relation relation) {
		super(name, relation);
		actType = ACT_FACT;
	}
}

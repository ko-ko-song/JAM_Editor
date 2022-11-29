package kr.ac.uos.ai.editor.jam.action;

import kr.ac.uos.ai.editor.jam.model.Relation;

public class AssertAction extends WorldmodelAction{
	
	public AssertAction(String name, Relation relation) {
		super(name, relation);
		actType = ACT_ASSERT;
	}
	
}

package kr.ac.uos.ai.editor.jam.action;

import kr.ac.uos.ai.editor.jam.model.Relation;

public class MaintainGoalAction extends GoalAction{
	
	public MaintainGoalAction(String name, Relation goal) {
		super(name, goal);
		actType = ACT_MAINTAIN;
	}
	
}

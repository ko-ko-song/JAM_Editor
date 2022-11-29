package kr.ac.uos.ai.editor.jam.action;

import kr.ac.uos.ai.editor.jam.model.Relation;

public class ConcludeGoalAction extends GoalAction{
	
	public ConcludeGoalAction(String name, Relation goal) {
		super(name, goal);
		actType = ACT_CONCLUDE;
	}
	
}

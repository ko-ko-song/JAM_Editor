package kr.ac.uos.ai.editor.jam.action;

import kr.ac.uos.ai.editor.jam.model.Relation;

public class PerformGoalAction extends GoalAction{
	
	public PerformGoalAction(String name, Relation goal) {
		super(name, goal);
		actType = ACT_PERFORM;
	}
	
}

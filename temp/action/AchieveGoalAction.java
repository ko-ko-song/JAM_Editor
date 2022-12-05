package kr.ac.uos.ai.editor.jam.action;

import kr.ac.uos.ai.editor.jam.model.Relation;

public class AchieveGoalAction extends GoalAction{
	
	public AchieveGoalAction(String name, Relation goal) {
		super(name, goal);
		actType = ACT_ACHIEVE;
	}
	
}

package kr.ac.uos.ai.editor.jam.action;

import kr.ac.uos.ai.editor.jam.model.Relation;

public class GoalAction extends Action{
	
	protected Relation goal;
	
	public GoalAction(String name, Relation goal) {
		this.name = name;
		this.goal = goal;
	}

	public Relation getGoal() {
		return goal;
	}

	public Relation getRelation() {
		return goal;
	}
	
}

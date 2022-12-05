//-*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//
//$Id: IntentionStructure.java,v 1.2 2006/07/25 00:06:13 semix2 Exp $
//$Source: /home/CVS/semix2/jam-060722/uos/ai/jam/IntentionStructure.java,v $
//
//File              : IntentionStructure.java
//Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//: Marcus J. Huber <marcush@irs.home.com>
//Created On        : Tue Sep 30 14:21:55 1997
//Last Modified By  : Jaeho Lee <jaeho@david>
//Last Modified On  : Mon Sep  6 17:54:01 2004
//Update Count      : 170
//
//Copyright (C) 1997-2004 Jaeho Lee and Marcus J. Huber.
//
//////////////////////////////////////////////////////////////////////////////
//
//JAM agent architecture
//
//Copyright (C) 1997-2004 Jaeho Lee and Marcus J. Huber.
//
//Permission is granted to copy and redistribute this software so long
//as no fee is charged, and so long as the copyright notice above, this
//grant of permission, and the disclaimer below appear in all copies
//made.
//
//This software is provided as is, without representation as to its
//fitness for any purpose, and without warranty of any kind, either
//express or implied, including without limitation the implied
//warranties of merchantability and fitness for a particular purpose.
//Jaeho Lee and Marcus J. Huber shall not be liable for any damages,
//including special, indirect, incidental, or consequential damages,
//with respect to any claim arising out of or in connection with the
//use of the software, even if they have been or are hereafter advised
//of the possibility of such damages.
//
//////////////////////////////////////////////////////////////////////////////

package uos.ai.jam;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Relation;
import uos.ai.jam.plan.APLElement;
import uos.ai.jam.plan.Plan;
import uos.ai.jam.plan.action.Action;
import uos.ai.jam.plan.action.GoalAction;
import uos.ai.jam.plan.constructor.PlanRuntimeState;

/**
 * 
 * Represents the agent's intentions
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class IntentionStructure implements Serializable {
	private static final long serialVersionUID = 9147781953145590963L;

	//
	// Members
	//
	public final static int IS_UNTRIED 			= -1;
	public final static int IS_FAILURE 			= 0;
	public final static int IS_SUCCESS 			= 1;
	public final static int IS_ACTIVE 			= 2;
	public final static int IS_BLOCKED 			= 3;
	public final static int IS_ABANDONED 		= 4;


	private final Interpreter 								_interpreter; 		// The Interpreter overwatching this intention structure
	private final List<Goal>								_stacks;			// The list of (Goal, Intention)s being pursued.
	private final List<IntentionStructureChangeListener>	_listeners;
	private Goal 											_currentGoal;		// Store the currently executing goal for each of access from metalevel actions.

	//
	// Constructors
	//

	/**
	 * Default constructor w/ parent interpreter
	 * 
	 */
	public IntentionStructure(Interpreter interpreter) {
		_stacks 		= new LinkedList<Goal>();
		_interpreter 	= interpreter;
		_listeners		= new CopyOnWriteArrayList<IntentionStructureChangeListener>();
	}

	//
	// Member functions
	//
	
	public void addChangeListener(IntentionStructureChangeListener listener) {
		_listeners.add(listener);
	}
	
	public void fireIntendEvent(APLElement goal) {
		for (IntentionStructureChangeListener listener : _listeners) {
			listener.intended(goal);
		}
	}

	
	public void removeChangeListener(IntentionStructureChangeListener listener) {
		_listeners.remove(listener);
	}
	
	public void fireGoalAddedEvent(Goal goal) {
		for (IntentionStructureChangeListener listener : _listeners) {
			listener.goalAdded(goal);
		}
	}
	
	public void fireGoalRemovedEvent(Goal goal) {
		for (IntentionStructureChangeListener listener : _listeners) {
			listener.goalRemoved(goal);
		}
	}

	public List<Goal> getStacks() {
		return _stacks;
	}

	public List<Goal> getToplevelGoals() {
		return _stacks;
	}

	public Goal getCurrentGoal() {
		return _currentGoal;
	}

	public Interpreter getInterpreter() {
		return _interpreter;
	}

	/**
	 * �־��� relation�� ��ġ�Ǵ� goal�� �����ϴ°�?
	 * @param relation
	 * @param binding
	 * @return
	 */
	public boolean matchRelation(Relation relation, Binding binding) {
		for (Goal goal : _stacks) {
			while (goal != null) {
				if (goal.matchRelation(relation, binding)) {
					return true;
				}
				goal = goal.getSubgoal();
			}
		}
		return false;
	}
	

	/**
	 * Arrange the intention stacks according to their evaluated utilities.
	 * 
	 */
	private void sortStacksByUtility() {
		/*
		 * System.out.println("IntentionStructure before sorting:");
		 * this.print(System.out);
		 */

		// Make sure all variable bindings are up to date
		refreshPriorities();

		Collections.sort(_stacks, new Comparator<Goal>() {
			public int compare(Goal goal1, Goal goal2) {
				double utility1 = 0;
				if (goal1.getIntention() != null) {
					utility1 = goal1.getIntention().evalUtility();
				} else {
					utility1 = goal1.evalUtility();
				}
				double utility2 = 0;
				if (goal2.getIntention() != null) {
					utility2 = goal2.getIntention().evalUtility();
				} else {
					utility2 = goal2.evalUtility();
				}
				return (int)(utility2 - utility1);
			}
		});
		

		/*
		 * System.out.println("IntentionStructure after sorting:");
		 * this.print(System.out);
		 */
	}

	/**
	 * Add an intention to the agent's list of intentions
	 * 
	 */
	public APLElement intend(APLElement intention) {
		if (intention.getFromGoal() != null && intention.getFromGoal().getIntention() == null) {
			if (JAM.getShowAPL()) {
				System.out.println("\n\nJAM::IntentionStructure: Intending intention: ");
				intention.print(System.out);
				System.out.println(" to goal: " + intention.getFromGoal());
				intention.getFromGoal().print(System.out);
			}
		
			intention.getFromGoal().setIntention(intention);
			intention.getFromGoal().setStatus(IS_ACTIVE);
			
			intention.getFromGoal().setRuntimeState(intention.getPlan().getBody().newRuntimeState());
			
			fireIntendEvent(intention);
			
			
		}

		if (JAM.getShowAPL()) {
			System.out.println("JAM::IntentionStructure:Intention Structure now:\n");
			print(System.out);
		}

		return intention;
	}

	/**
	 * Execute the highest-utility intention
	 * 
	 */
	public int run() {
		int returnValue = PlanRuntimeState.PLAN_CONSTRUCT_COMPLETE;

		// Sort stacks according to utility
		sortStacksByUtility();

		// Go through stacks in sorted order and try to run something.
		boolean isFound = false;
		Iterator<Goal> iter = _stacks.iterator();
		Goal currentGoal = null;
		while (iter.hasNext()) {
			currentGoal = iter.next();

			if (currentGoal.getStatus() == IS_ABANDONED) {
				// System.out.println("JAM: Plan abandoned! Removing intention!\n");
				currentGoal.removeIntention(true);
				iter.remove();
				fireGoalRemovedEvent(currentGoal);
				
				// renewLeafGoals();
				// return IS_FAILURE;
			}

			if (currentGoal.getRuntimeState() == null) {
				if (JAM.getShowIntentionStructure() || JAM.getShowGoalList()) {
					System.out.println("JAM: Goal \"" + currentGoal.getName() + "\" has no runtime state, skipping.\n");
				}
				continue;
			}
		
			// If this is an achievement goal (currently ACHIEVE or MAINTAIN)
			// then check to see if the goal's already been achieved.
			if (currentGoal.getGoalAction() != null
					&& (currentGoal.getGoalAction().getType() == Action.ACT_ACHIEVE 
							|| currentGoal.getGoalAction().getType() == Action.ACT_MAINTAIN)) {

				Relation rel = currentGoal.getRelation();

				// Binding argument is null in the match() function below
				// because this is a top-level goal and therefore has no variable bindings
				if (_interpreter.getWorldModel().match(rel, null)) {

					if (JAM.getShowIntentionStructure() || JAM.getShowGoalList()) {
						System.out.println("JAM: Goal \"" + currentGoal.getName() + " already achieved!.\n");
					}

					currentGoal.setStatus(IS_SUCCESS);
					currentGoal.setRuntimeState(null);

					// MAINTAIN goals should stick around on the Intention Structure
					// (until, at least, it is explicitly removed by the programmer using an UNPOST)
					if (currentGoal.getGoalAction() != null
							&& currentGoal.getGoalAction().getType() != Action.ACT_MAINTAIN) {
						iter.remove();
						fireGoalRemovedEvent(currentGoal);
					}

					renewLeafGoals();
					return IS_SUCCESS;
				}
			}

			// Check to see if the plan's context is still valid
			if (currentGoal.confirmContext()) {
				if (JAM.getShowIntentionStructure() || JAM.getShowGoalList()) {
					System.out.println("\nJAM: Plan context failed!  Removing intention!\n");
				}
				currentGoal.removeIntention(true);
				currentGoal.setStatus(IS_FAILURE);
				currentGoal.setSubgoal(null);
				renewLeafGoals();
				return IS_FAILURE;
			}
			
			isFound = true;
			break;
		}
		
		if (!isFound) return IS_ACTIVE;
		
		// As soon as something executes (successfully or not) then return.
		if (JAM.getShowIntentionStructure() || JAM.getShowGoalList()) {
			System.out.println("JAM: Executing top-level goal \"" + currentGoal.getName() + "\".");
		}

		_currentGoal = currentGoal;
		try {
			returnValue = currentGoal.execute();
		} catch (AgentRuntimeException e) {
			_interpreter.getExceptionManager().throwException(e);
			returnValue = PlanRuntimeState.PLAN_CONSTRUCT_ABANDON;
		}

		switch (returnValue) {

		case PlanRuntimeState.PLAN_CONSTRUCT_FAILED:

			if (JAM.getShowIntentionStructure() || JAM.getShowGoalList()) {
				System.out.println("JAM: Top-level goal \"" + currentGoal.getName() + "\" failed!");
			}

			if (currentGoal.getIntention().getPlan().getFailure() != null) {
				currentGoal.executeFailure();
			}

			currentGoal.removeIntention(false);
			currentGoal.setStatus(IS_FAILURE);
			currentGoal.setSubgoal(null);
			renewLeafGoals();
			return IS_FAILURE;
			
		case PlanRuntimeState.PLAN_CONSTRUCT_ABANDON:

			if (JAM.getShowIntentionStructure() || JAM.getShowGoalList()) {
				System.out.println("JAM: Top-level goal \"" + currentGoal.getName() + "\" abandon!");
			}

			if (currentGoal.getIntention().getPlan().getFailure() != null) {
				currentGoal.executeFailure();
			}

			currentGoal.removeIntention(true);
			currentGoal.setStatus(IS_ABANDONED);
			currentGoal.setSubgoal(null);
			renewLeafGoals();
			return IS_FAILURE;

		case PlanRuntimeState.PLAN_CONSTRUCT_COMPLETE:

			if (JAM.getShowGoalList()) {
				System.out.println("JAM: Just completed top-level goal " + currentGoal.getName());
			}

			currentGoal.setRuntimeState(null);

			// Execute the plan's "postcondition" (i.e., after-effects)
			if (currentGoal.getIntention().getPlan().getEffects() != null) {
				currentGoal.executeEffects();
			}

			currentGoal.setStatus(IS_SUCCESS);

			// If a MAINTAIN goal, then leave on the intention structure to
			// continue monitoring for the required state but assert that
			// the goal has been achieved. Otherwise, remove the goal from 
			// the intention structure and remove it's particular intention.
			if (currentGoal.getGoalAction() == null
					|| (currentGoal.getGoalAction() != null
							&& currentGoal.getGoalAction().getType() != Action.ACT_MAINTAIN)) {
				_stacks.remove(currentGoal);
				fireGoalRemovedEvent(currentGoal);
			}

			if (currentGoal.getGoalAction() != null
					&& (currentGoal.getGoalAction().getType() == Action.ACT_ACHIEVE
							|| currentGoal.getGoalAction().getType() == Action.ACT_MAINTAIN)) {
				// Assert achieved goal state onto World Model
				Relation rel = (currentGoal.getGoalAction().getRelation());
				_interpreter.getWorldModel().assertFact(rel, null);
				currentGoal.setIntention(null);
			}

			renewLeafGoals();
			return IS_SUCCESS;

		case PlanRuntimeState.PLAN_CONSTRUCT_INCOMP:
			return IS_ACTIVE;

		default:
			System.out.println("JAM: Execution returned invalid value: " + returnValue);
		}
		return IS_ACTIVE;
	}

	/**
	 * Perform an agent's plan
	 */
	public int executeObserver(Plan observer) {
		if (observer == null) return IS_SUCCESS;

		int returnVal = PlanRuntimeState.PLAN_CONSTRUCT_COMPLETE;
		Binding b = new Binding();
		PlanRuntimeState toplevelState = observer.getBody().newRuntimeState();

		while (toplevelState != null) {
			try {
				returnVal = toplevelState.execute(b, null, null);
			} catch (AgentRuntimeException e) {
				_interpreter.getExceptionManager().throwException(e);
			}

			switch (returnVal) {
			case PlanRuntimeState.PLAN_CONSTRUCT_FAILED:
				System.out.println("JAM::IntentionStructure:executePlan - Plan failed!\n");
				return IS_FAILURE;

			case PlanRuntimeState.PLAN_CONSTRUCT_COMPLETE:
				return IS_SUCCESS;

			case PlanRuntimeState.PLAN_CONSTRUCT_INCOMP:
				break;

			default:
				System.out.print("JAM::IntentionStructure:executePlan - Plan returned ");
				System.out.println("unknown state: " + returnVal + "!\n");
				break;
			}
		}

		return IS_SUCCESS;
	}

	/**
	 * Go through all of the stacks and recompute their utility values
	 * 
	 */
	public void refreshPriorities() {
		Iterator<Goal> iter = _stacks.iterator();
		Goal goal = null;
		APLElement intent = null;

		while(iter.hasNext()) {
			goal = iter.next();
			while(goal != null) {
				intent = goal.getIntention();
				if (intent != null) {
					intent.getPlan().confirmContext(goal.getIntentionBinding());
				}
				goal = goal.getSubgoal();
			}
		}
	}

	/**
	 * Go through all of the stacks and mark all inactive goals as being "new"
	 * in order to trigger APL generation.
	 * 
	 */
	public void renewLeafGoals() {
		if (JAM.getShowGoalList()) {
			System.out.println("JAM: renewing leaf goals.");
		}

		Iterator<Goal> iter = _stacks.iterator();
		Goal goal = null;
		while(iter.hasNext()) {
			goal = iter.next();
			while(goal != null) {
				if (goal.getSubgoal() == null && goal.getIntention() == null) {
					goal.setNew();
				}
				goal = goal.getSubgoal();
			}
		}
	}

	/**
	 * Old GoalList functionality
	 * 
	 */
	public boolean allGoalsDone() {
		return (_stacks.size() == 0);
	}

	/**
	 * Find and remove a goal from the Intention Structure
	 * 
	 */
	public void drop(GoalAction goalAction, Binding b) {
		// Find the goal and remove it
		ListIterator<Goal> iter = _stacks.listIterator();
		Goal goal = null;

		// Go through all the stacks
		while(iter.hasNext()) {
			goal = iter.next();

			// Go through all the subgoals
			while (goal != null && !goal.matchGoal(goalAction, b)) {
				goal = goal.getSubgoal();
			}

			// See if we found a match
			if (goal != null && goal.matchGoal(goalAction, b)) {

				// If we've found a matching goal, check to see if this is on
				// the intention structure (i.e. currently executing)
				if (goal.getIntentionStructure() != null) {
					goal.setStatus(IntentionStructure.IS_ABANDONED);
				} else {
					// Goal not being executed, so take it off the intention structure
					iter.remove();
					
				}
				return;
			}
		}
	}

	/**
	 * Add the specified goal to the intention structure only if it doesn't
	 * already exist.
	 */
	public Goal addUnique(GoalAction goalAction, Goal prevGoal, Binding b) throws AgentRuntimeException {
		_interpreter.setNumGoalsStat(_interpreter.getNumGoalsStat() + 1);

		// Go through all the stacks and check all goals for matches
		Iterator<Goal> iter = _stacks.iterator();
		Goal goal = null;
		while(iter.hasNext()) {
			goal = iter.next();
			while(goal != null) {
				if (goal.matchGoal(goalAction, b)) {
					return goal;
				}
				goal = goal.getSubgoal();
			}
		}

		// Convert the argument list into constants if it is a top-level goal
		if (prevGoal == null && goalAction != null) {
			/*
			 * �Ʒ� �ڵ�� goalAction�� ���θ� ground ��Ų��.
			 * GoalAction�� plan���� ���� ���� �����ؾ� �Ѵ�. --> ground�Ǹ� �ȵȴ�.
			 * 
			 */
			// goalAction.getRelation().evalArgs(b);
			
			/*
			 * �ϴ� �Ʒ��� ���� ���ĳ��� ���������� �����Ͽ� �����ϵ���.
			 */
			goalAction = goalAction.eval(b);
		}

		// Not a duplicate goal, so add at top-level if no previous goal,
		// otherwise
		// its already been added to current intention stack (through the Goal
		// constructor).
		Goal newGoal = new Goal(goalAction, prevGoal, this);
		if (prevGoal == null) {
			if (JAM.getShowGoalList()) {
				System.out.println("JAM::IntentionStructure:addUnique(): Adding new top-level goal " + newGoal.getName());
			}
			_stacks.add(newGoal);
			fireGoalAddedEvent(newGoal);
		} else {
			if (JAM.getShowGoalList()) {
				System.out.println("JAM::IntentionStructure:addUnique(): Adding subgoal " + newGoal.getName() + " to goal " + prevGoal.getName());
			}
		}
		return newGoal;
	}

	/**
	 * Remove the indicated goal by searching through each intention stack and
	 * going through each from top to bottom.
	 * 
	 */
	public void removeGoal(Goal goal) {
		ListIterator<Goal> iter = _stacks.listIterator();
		Goal stackGoal = null;

		while(iter.hasNext()) {
			stackGoal = iter.next();
			
			// Go through stack from top to bottom
			while(stackGoal != null) {

				// If we found the goal then either castrate the subgoal chain
				// at the parent or, if a top-level goal, remove the entire
				// intention stack.
				if (stackGoal == goal) {
					if (stackGoal.getPrevGoal() != null) {
						stackGoal.getPrevGoal().setSubgoal(null);
					} else {
						iter.remove();
						fireGoalRemovedEvent(stackGoal);
					}
					return;
				}
				stackGoal = stackGoal.getSubgoal();
			}
		}
	}

	/**
	 * Output information about the Intention Structure in a readable format.
	 * 
	 */
	public void print(PrintStream s) {
		s.println("IntentionStructure:");
		s.println("  Stacks: " + _stacks.size());
		int stackNum = 0;

		Iterator<Goal> iter = _stacks.iterator();
		Goal goal = null;
		while(iter.hasNext()) {
			goal = iter.next();
			s.println("\nStack#" + stackNum);
			while(goal != null) {
				goal.print(s);
				if (goal.getIntention() != null) {
					goal.getIntention().print(s);
				}
				goal = goal.getSubgoal();
			}
			stackNum++;
		}
		s.println();
	}
}

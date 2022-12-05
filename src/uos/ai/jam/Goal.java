//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Goal.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/Goal.java,v $
//  
//  File              : Goal.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:58 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:01 2004
//  Update Count      : 59
//  
//  Copyright (C) 1997-2004 Jaeho Lee and Marcus J. Huber.
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1997-2004 Jaeho Lee and Marcus J. Huber.
//  
//  Permission is granted to copy and redistribute this software so long
//  as no fee is charged, and so long as the copyright notice above, this
//  grant of permission, and the disclaimer below appear in all copies
//  made.
//  
//  This software is provided as is, without representation as to its
//  fitness for any purpose, and without warranty of any kind, either
//  express or implied, including without limitation the implied
//  warranties of merchantability and fitness for a particular purpose.
//  Jaeho Lee and Marcus J. Huber shall not be liable for any damages,
//  including special, indirect, incidental, or consequential damages,
//  with respect to any claim arising out of or in connection with the
//  use of the software, even if they have been or are hereafter advised
//  of the possibility of such damages.
// 
//////////////////////////////////////////////////////////////////////////////

package uos.ai.jam;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Relation;
import uos.ai.jam.plan.APLElement;
import uos.ai.jam.plan.action.GoalAction;
import uos.ai.jam.plan.constructor.PlanAtomicConstruct;
import uos.ai.jam.plan.constructor.PlanRuntimeState;

/**
 * 
 * Represents an agent's goals
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class Goal implements Serializable {
	private static final long serialVersionUID = 5453835266146142911L;

	//
	// Members
	//
	protected GoalAction 				_goalAction; 			// The expression for the goal to be completed - points to action from parent Plan body
	protected Goal 						_subgoal;
	protected Goal 						_prevGoal; 				// Goal for which this intention was created
	protected boolean					_newGoal; 				// Flag of whether goal is new or not.
	protected int 						_status; 				// Run state of the intention (ACTIVE, SUSPENDED, SUCCESS, FAILURE, etc.)
	protected APLElement 				_intention; 			// Intended (instantiated with bindings) Plan
	protected PlanRuntimeState 			_runtimeState; 			// Plan information related to execution
	protected IntentionStructure 		_intentionStructure; 	// The IS from whence this came

	private final String							_id;
	private final List<GoalStatusChangeListener>	_listeners;

	//
	// Constructors
	//

	/**
	 * Basic constructor with the goal specification and parent goal as
	 * parameters
	 * 
	 */
	public Goal(GoalAction ga, Goal prev, IntentionStructure is) {
		_goalAction 			= ga;
		_subgoal 				= null;
		_prevGoal 				= prev;
		_newGoal 				= true;
		_status 				= IntentionStructure.IS_UNTRIED;
		_intention 				= null;
		_runtimeState 			= null;
		_intentionStructure 	= is;

		_id			= "INTENTION_" + System.nanoTime();
		_listeners 	= new CopyOnWriteArrayList<GoalStatusChangeListener>();

		if (prev != null) {
			prev.setSubgoal(this);
		}
	}

	//
	// Member functions
	//

	public String getId() {
		return _id;
	}
	
	public void addStatusChangeListener(GoalStatusChangeListener listener) {
		_listeners.add(listener);
	}

	public void removeStatusChangeListener(GoalStatusChangeListener listener) {
		_listeners.remove(listener);
	}

	private void fireGoalStatusChangeEvent(int oldStatus, int newStatus) {
		for (GoalStatusChangeListener listener : _listeners) {
			listener.statusChanged(this, oldStatus, newStatus);
		}
	}

	public boolean isNew() {
		return _newGoal;
	}

	public boolean isToplevelGoal() {
		return _prevGoal == null;
	}

	public boolean isLeafGoal() {
		return _subgoal == null;
	}

	public GoalAction getGoalAction() {
		return _goalAction;
	}

	public boolean setNew() {
		return _newGoal = true;
	}

	public boolean clearNew() {
		return _newGoal = false;
	}

	public Goal getLeafGoal() {
		return (_subgoal != null) ? _subgoal.getLeafGoal() : this;
	}

	public Goal getSubgoal() {
		return _subgoal;
	}

	public Goal setSubgoal(Goal g) {
		Goal oldSubgoal = _subgoal;
		_subgoal = g;
		if (oldSubgoal != null) {
			_intentionStructure.fireGoalRemovedEvent(oldSubgoal);
		}
		if (g != null) {
			_intentionStructure.fireGoalAddedEvent(g);
		}
		return g;
	}

	public Goal getPrevGoal() {
		return _prevGoal;
	}

	public Goal setPrevGoal(Goal g) {
		return _prevGoal = g;
	}

	public APLElement getIntention() {
		return _intention;
	}

	public APLElement setIntention(APLElement se) {
		return _intention = se;
	}

	public int getStatus() {
		return _status;
	}

	public int setStatus(int st) {
		int oldStatus = _status;
		_status = st;
		if (oldStatus != st) {
			fireGoalStatusChangeEvent(oldStatus, st);
		}
		return st;
	}

	public PlanRuntimeState getRuntimeState() {
		return _runtimeState;
	}

	public PlanRuntimeState setRuntimeState(PlanRuntimeState r) {
		return _runtimeState = r;
	}

	/**
	 * Verify that the plan's context is valid.
	 * 
	 */
	public boolean confirmContext() {
		return (_intention != null && !_intention.getPlan().confirmContext(getIntentionBinding()));
	}

	/**
	 * Execute the procedure associated with a plan's BODY specification.
	 * 
	 */
	public int execute() throws AgentRuntimeException {
		return getRuntimeState().execute(getIntentionBinding(), this, getPrevGoal());
	}

	/**
	 * Execute the procedure associated with a plan's FAILURE specification.
	 * 
	 */
	public int executeFailure() {
		try {
			return getIntention().getPlan().getFailure().newRuntimeState().execute(getIntentionBinding(), this, getPrevGoal());
		} catch (AgentRuntimeException e) {
			return PlanRuntimeState.PLAN_CONSTRUCT_COMPLETE;
		}
	}

	/**
	 * Execute the procedure associated with a plan's EFFECTS specification.
	 * 
	 */
	public int executeEffects() {
		try {
			return getIntention().getPlan().getEffects().newRuntimeState().execute(getIntentionBinding(), this, getPrevGoal());
		} catch (AgentRuntimeException e) {
			return PlanRuntimeState.PLAN_CONSTRUCT_COMPLETE;
		}
	}

	/**
	 * Return the goal's relation label
	 * 
	 */
	public String getName() {
		return (_goalAction != null) ? _goalAction.getName() : null;
	}

	/**
	 * Return the goal specification
	 * 
	 */
	public Relation getRelation() {
		return (_goalAction != null) ? _goalAction.getRelation() : null;
	}

	/**
	 * Check whether the goal should have an Applicable Plan List created for
	 * it.
	 * 
	 */
	public boolean generateAPL(boolean isWorldModelAnyNew) {
		/*
		 * System.out.println("generateAPL: _status = " + _status);
		 * System.out.println("generateAPL: _subgoal = " + _subgoal);
		 * System.out.println("generateAPL: _newGoal = " + _newGoal);
		 * System.out.println("generateAPL: _intention = " + _intention);
		 * System.out.println("generateAPL: WM.anyNew() = " +
		 * _intentionStructure.getInterpreter().getWorldModel().anyNew());
		 */

		// If the goal hasn't been completed yet and hasn't been UNPOSTed and the goal is a "leaf" goal.
		if (isValid() && isLeafGoal()) {

			// If the goal already has an intended plan, then definitely do
			// not generate an APL
			if (_intention != null) {
				return false;
			}

			// If it is "new" (meaning that it has just been POSTed, subgoaled,
			// or has been refreshed by the intention structure) then definitely
			// generate an APL
			if (_newGoal) {
				return true;
			}

			// If anything in the world model has changed, then we need to
			// consider the goal for APL generation
			if (isWorldModelAnyNew) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the binding of the goal arguments based upon the parent goal (if it
	 * exists).
	 * 
	 */
	public Binding getGoalBinding() {
		return (_prevGoal != null) ? _prevGoal.getIntentionBinding() : null;
	}

	/**
	 * Get the binding of the goal arguments based upon the plan's goal (if it
	 * exists).
	 * 
	 */
	public Binding getIntentionBinding() {
		return (_intention != null) ? _intention.getBinding() : null;
	}

	/**
	 * Return whether the goal is still worth considering.
	 * 
	 */
	public boolean isValid() {
		return ((_status != IntentionStructure.IS_SUCCESS) && (_status != IntentionStructure.IS_ABANDONED));
	}

	/**
	 * Check to see if the stack in which this goal is part is blocked.
	 * 
	 */
	public boolean isStackBlocked() {
		// Check this goal
		if (_status == IntentionStructure.IS_BLOCKED) {
			return true;
		}

		// First move up to the top of the stack for this goal (if any),
		// checking
		// on the way up.
		Goal gl = _prevGoal;
		while (gl != null) {
			if (gl.getStatus() == IntentionStructure.IS_BLOCKED) {
				return true;
			}
			gl = gl.getPrevGoal();
		}

		// Then check from the goal down
		gl = _subgoal;
		while (gl != null) {
			if (gl.getStatus() == IntentionStructure.IS_BLOCKED) {
				return true;
			}
			gl = gl.getSubgoal();
		}
		return false;
	}

	/**
	 * This function should be defined in this goal class because it must use
	 * the binding of the subgoaling plan, not the candidate plans for this
	 * goal.
	 * 
	 */
	public double evalUtility() {
		Expression goalUtility = ((_goalAction != null) ? _goalAction.getUtility() : null);
		try {
			return (goalUtility != null) ? goalUtility.eval(getGoalBinding()).getReal() : 0.0;
		} catch (AgentRuntimeException e) {
			return 0.0;
		}
	}

	/**
	 * Find matches between bound and unbound variables
	 * 
	 */
	public boolean matchRelation(Relation pattRelation, Binding pattBinding) {
		try {
			return pattRelation.unify(pattRelation, pattBinding, getRelation(), getGoalBinding());
		} catch (AgentRuntimeException e) {
			return false;
		}
	}

	/**
	 * Check whether the goal's specification compares to the parameters
	 * 
	 */
	public boolean matchGoal(GoalAction goalAction, Binding goalActionBinding) {
		if (goalAction != null && matchRelation(goalAction.getRelation(), goalActionBinding) == true) {
			//			if (goalAction.getUtility() != null || evalUtility() == goalAction.evalUtility(goalActionBinding)) {
			//				return true;
			//			}
			return true;
		}
		return false;
	}

	/**
	 * Return the agent's intention structure
	 * 
	 */
	public IntentionStructure getIntentionStructure() {
		return _intentionStructure;
	}

	/**
	 * Remove the goal's intention and all subgoal intentions
	 * 
	 */
	public void removeIntention(boolean failed) {
		PlanAtomicConstruct failureSection;

		// Move down to bottom of subgoal string
		Goal goal = getLeafGoal();

		// Now backtrack, cleaning things up on the way
		while (goal != null && goal != this) {
			if (failed == true && (goal.getIntention() != null)) {

				// Execute FAILURE section
				if ((failureSection = goal.getIntention().getPlan().getFailure()) != null) {
					goal.setRuntimeState(failureSection.newRuntimeState());
					try {
						goal.getRuntimeState().execute(goal.getIntentionBinding(), goal, goal.getPrevGoal());
					} catch (AgentRuntimeException e) {}
				}
			}

			// clear up memory
			goal.setIntention(null);

			// Remove the goal from the Intention Structure
			goal = goal.getPrevGoal();
			if (goal != null) {
				goal.setSubgoal(null);
			}
		}

		// Execute FAILURE section for this plan too
		if (failed == true) {
			if (_intention != null && (failureSection = _intention.getPlan().getFailure()) != null) {
				_runtimeState = failureSection.newRuntimeState();
				try {
					_runtimeState.execute(getIntentionBinding(), this, getPrevGoal());
				} catch (AgentRuntimeException e) {}
			}
		}

		setIntention(null);
		setSubgoal(null);
		setRuntimeState(null);
	}

	/**
	 * Format output to the given stream without considering having the output
	 * in-line with other output.
	 * 
	 */
	public void print(PrintStream s) {
		s.print("  Goal   \t: ");
		if (_goalAction != null)
			_goalAction.print(s, getGoalBinding(), "", "");
		else
			s.println("null (data-driven)");
		if (_intention != null)
			s.println("  Utility\t: " + _intention.evalUtility());
		else
			s.println("  Utility\t: " + evalUtility());
		s.println("  New ?    \t: " + (_newGoal ? "True" : "False"));
		s.print("  Status   \t: ");
		switch (_status) {
		case IntentionStructure.IS_UNTRIED:
			s.println("IS_UNTRIED");
			break;
		case IntentionStructure.IS_FAILURE:
			s.println("IS_FAILURE");
			break;
		case IntentionStructure.IS_SUCCESS:
			s.println("IS_SUCCESS");
			break;
		case IntentionStructure.IS_ACTIVE:
			s.println("IS_ACTIVE");
			break;
		case IntentionStructure.IS_BLOCKED:
			s.println("IS_BLOCKED");
			break;
		case IntentionStructure.IS_ABANDONED:
			s.println("IS_ABANDONED");
			break;
		}

		s.print("  Subgoal  \t: " + _subgoal);
		s.println(" => " + ((_subgoal != null) ? _subgoal.getName() : "NONE"));
		s.print("  Prev_Goal\t: " + _prevGoal);
		s
		.println(" => "
				+ ((_prevGoal != null) ? _prevGoal.getName() : "NONE"));

		s.println("  Intention\t: " + _intention);
		s.println("  RuntimeState\t: " + _runtimeState);
	}

	/**
	 * Format output to the given stream so that it can be in-line with other
	 * output.
	 * 
	 */
	public void format(PrintStream s) {
		s.print("Goal: ");
		if (_goalAction != null)
			_goalAction.formatArgs(s, getGoalBinding(), "", "");
		else
			s.println("null");
		if (_intention != null)
			s.print(", Utility: " + _intention.evalUtility());
		else
			s.println(", Utility: " + evalUtility());
		s.print(", New?: " + (_newGoal ? "True" : "False"));
		s.print(", Status: ");
		switch (_status) {
		case IntentionStructure.IS_UNTRIED:
			s.print("IS_UNTRIED");
			break;
		case IntentionStructure.IS_FAILURE:
			s.print("IS_FAILURE");
			break;
		case IntentionStructure.IS_SUCCESS:
			s.print("IS_SUCCESS");
			break;
		case IntentionStructure.IS_ACTIVE:
			s.print("IS_ACTIVE");
			break;
		case IntentionStructure.IS_BLOCKED:
			s.print("IS_BLOCKED");
			break;
		case IntentionStructure.IS_ABANDONED:
			s.print("IS_ABANDONED");
			break;
		}

		s.print(", Subgoal: " + _subgoal);
		s.print(" => " + ((_subgoal != null) ? _subgoal.getName() : "NONE"));
		s.print(", Prev_Goal: " + _prevGoal);
		s.print(" => " + ((_prevGoal != null) ? _prevGoal.getName() : "NONE"));

		s.println(", Intention: " + _intention);
		s.println(", RuntimeState: " + _runtimeState);
	}

}

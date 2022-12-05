//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: GoalAction.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/GoalAction.java,v $
//  
//  File              : GoalAction.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:56 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:01 2004
//  Update Count      : 36
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

package uos.ai.jam.plan.action;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Relation;
import uos.ai.jam.expression.Value;
import uos.ai.jam.plan.Plan;

/**
 * 
 * A subgoal action within a plan.
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public abstract class GoalAction extends Action implements Serializable {
	private static final long serialVersionUID = -2556940749276730573L;

	//
	// Members
	//
	protected Relation 				_goal;
	protected Expression 			_utility;
	protected List<Expression>		_by;
	protected List<Expression>		_notBy;
	protected Interpreter 			_interpreter;

	//
	// Constructors
	//

	/**
	 * Full constructor
	 * 
	 */
	public GoalAction(String name, Relation goal, Expression utility, List<Expression> by, List<Expression> not_by, Interpreter interpreter) {
		super(name);

		_goal 			= goal;
		_utility 		= utility;
		_by 			= by;
		_notBy 			= not_by;
		_interpreter 	= interpreter;
	}

	/**
	 * Partial constructor
	 * 
	 */
	public GoalAction(String name, Relation goal, Expression utility, Interpreter interpreter) {
		this(name, goal, utility, null, null, interpreter);
	}

	public abstract GoalAction eval(Binding b) throws AgentRuntimeException;
	
	//
	// Member functions
	//
	public Relation getGoal() {
		return _goal;
	}

	public Relation getRelation() {
		return _goal;
	}

	public Relation setRelation(Relation r) {
		return _goal = r;
	}

	public Expression getUtility() {
		return _utility;
	}

	public Expression setUtility(Expression utility) {
		return _utility = utility;
	}

	public List<Expression> setBy(List<Expression> by) {
		return _by = by;
	}

	public List<Expression> getBy() {
		return _by;
	}

	public List<Expression> setNotBy(List<Expression> notBy) {
		return _notBy = notBy;
	}

	public List<Expression> getNotBy() {
		return _notBy;
	}

	public boolean isExecutableAction() {
		return false;
	}

	public int execute(Binding b, Goal currentGoal) throws AgentRuntimeException {
		return ACT_CANNOT_EXECUTE;
	}

	//

	/**
	 * Check to see if the goal is applicable to the specified plan
	 * 
	 */
	public boolean isEligible(Plan plan, Binding binding) {
		String planName = plan.getName();

		// Check goal specifications
		// Currently, PERFORMS can use any plans, but ACHIEVE and MAINTAIN
		// can only use plans with an ACHIEVE GOAL: specification.
		if (((getType() == ACT_ACHIEVE) || (getType() == ACT_MAINTAIN)) && (plan.getGoalSpecification().getType() != ACT_ACHIEVE)) {
			return false;
		} else if (getType() == ACT_PERFORM) {
			// Accept everything
		}
		// else if (getType() == ACT_QUERY) {
		// 
		// }

		// Check ":BY" list
		if (_by != null) {
			Iterator<Expression> iter = _by.iterator();
			Value str = null;
			while(iter.hasNext()) {
				try {
					str = iter.next().eval(binding);
				} catch (AgentRuntimeException e) {
					return false;
				}

				if (str.isDefined() && str.getString().compareTo(planName) == 0) {
					return true;
				}
			}
			return false;
		}

		// Check ":NOT-BY" list
		if (_notBy != null) {
			Iterator<Expression> iter = _notBy.iterator();
			Value str = null;
			while(iter.hasNext()) {
				try {
					str = iter.next().eval(binding);
				} catch (AgentRuntimeException e) {
					return true;
				}
				
				if (str.isDefined() && str.getString().compareTo(planName) == 0) {
					return false;
				}
			}
			return true;
		}

		return true;
	}

	/**
	 * Format the output and don't worry about being being printed out in-line
	 * with other information.
	 * 
	 */
	public void print(PrintStream s, Binding b, String head, String tail) {
		s.print(head);
		_goal.print(s, b);
		s.print(tail);
	}

	/**
	 * Format the output so that it's conducive to being printed out in-line
	 * with other information.
	 * 
	 */
	public void formatArgs(PrintStream s, Binding b, String head, String tail) {
		s.print(head);
		_goal.format(s, b);
		s.print(tail);
	}

	public double evalUtility(Binding binding) {
		try {
			return (_utility != null) ? _utility.eval(binding).getReal() : 0.0;
		} catch (AgentRuntimeException e) {
			return 0.0;
		}
	}

	public abstract void format(PrintStream s, Binding b);

}

//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: AchieveGoalAction.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/AchieveGoalAction.java,v $
//  
//  File              : AchieveGoalAction.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:23:08 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:02 2004
//  Update Count      : 23
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
import java.util.List;

import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Relation;

/**
 * 
 * A built-in JAM primitive action for achievement subgoaling.
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class AchieveGoalAction extends GoalAction implements Serializable {
	private static final long serialVersionUID = -8233428922474627372L;

	//
	// Members
	//

	//
	// Constructors
	//

	/**
	 * Primary constructor
	 * 
	 */
	public AchieveGoalAction(String name, Relation goal, Expression utility, List<Expression> by, List<Expression> notBy, Interpreter interpreter) {
		super(name, goal, utility, by, notBy, interpreter);
		_actType = ACT_ACHIEVE;
	}

	//
	// Member functions
	//
	
	public AchieveGoalAction eval(Binding b) throws AgentRuntimeException {
		return new AchieveGoalAction(_name, new Relation(_goal, b), _utility != null ? _utility.eval(b) : null, _by, _notBy, _interpreter);
	}
	
	public boolean isExecutableAction() {
		return false;
	}

	/**
	 * Display information about the goal
	 * 
	 */
	public void format(PrintStream s, Binding b) {
		formatArgs(s, b, "ACHIEVE ", ";");
	}

}

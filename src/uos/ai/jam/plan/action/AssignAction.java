//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: AssignAction.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/AssignAction.java,v $
//  
//  File              : AssignAction.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:22:41 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:02 2004
//  Update Count      : 20
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

import java.io.*;

import uos.ai.jam.Goal;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Value;

/**
 * 
 * A built-in JAM primitive action for binding values to local plan variables
 * within plans.
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class AssignAction extends Action implements Serializable {
	private static final long serialVersionUID = 5197270774749142761L;

	//
	// Members
	//
	protected Expression _var;

	protected Expression _exp;

	//
	// Constructors
	//

	/**
	 * 
	 * 
	 */
	public AssignAction(Expression v, Expression e) {
		super(v.getName());

		_var = v;
		_exp = e;
		_actType = ACT_ASSIGN;
	}

	//
	// Member functions
	//
	
	public Expression getVariable() {
		return _var;
	}
	
	public Expression getValue() {
		return _exp;
	}
	
	public boolean isExecutableAction() {
		return true;
	}

	// public int getType() { return ACT_ASSIGN; }

	/**
	 * 
	 * 
	 */
	public int execute(Binding b, Goal currentGoal) throws AgentRuntimeException {
		Value v = _exp.eval(b);
		b.setValue(_var, v);
		return ACT_SUCCEEDED;
	}

	/**
	 * 
	 * 
	 */
	public void format(PrintStream s, Binding b) {
		s.print("ASSIGN: variable: ");
		_var.format(s, b);
		s.print("expression: ");
		_exp.format(s, b);
	}

}

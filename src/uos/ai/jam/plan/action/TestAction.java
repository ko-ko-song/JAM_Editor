//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: TestAction.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/TestAction.java,v $
//  
//  File              : TestAction.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:19:16 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:53:58 2004
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

/**
 * 
 * A built-in JAM primitive action for evaluating boolean expressions.
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class TestAction extends Action implements Serializable {
	private static final long serialVersionUID = -2919087857023033725L;

	//
	// Members
	//
	protected Expression _expression;

	//
	// Constructors
	//
	public TestAction(Expression e) {
		super(e.getName());
		_expression = e;
		_actType = ACT_TEST;
	}

	//
	// Member functions
	//
	public boolean isExecutableAction() {
		return true;
	}

	// public int getType() { return ACT_TEST; }
	public Expression getExp() {
		return _expression;
	}

	/**
	 * 
	 * 
	 */
	public int execute(Binding b, Goal currentGoal) throws AgentRuntimeException {
		return _expression.eval(b).isTrue() ? ACT_SUCCEEDED : ACT_FAILED;
	}

	/**
	 * 
	 * 
	 */
	public void format(PrintStream s, Binding b) {
		s.print("TEST:");
		_expression.format(s, b);
	}

}

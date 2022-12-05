//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: SimpleAction.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/SimpleAction.java,v $
//  
//  File              : SimpleAction.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:19:32 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:53:58 2004
//  Update Count      : 35
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

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Functions;
import uos.ai.jam.expression.Value;

/**
 * 
 * A simple (non-decomposable) action within a plan
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class SimpleAction extends Action implements Serializable {
	private static final long serialVersionUID = 1483885137677305348L;

	//
	// Members
	//
	private final Interpreter 			_interpreter;
	private final Expression[]	 		_args;

	//
	// Constructors
	//

	/**
	 * Constructor with name and argument list
	 * 
	 */
	
	private static final Expression[] EMPTY_EXPRESSION = new Expression[0];
	public SimpleAction(Interpreter interpreter, String name, Expression... args) {
		super(name);
		_interpreter	= interpreter;
		_args			= (args != null) ? args.clone() : EMPTY_EXPRESSION;
		_actType		= ACT_PRIMITIVE;
	}
	
	public SimpleAction(Interpreter interpreter, String name, List<Expression> args) {
		super(name);
		_interpreter 	= interpreter;
		_args 			= (args != null) ? args.toArray(EMPTY_EXPRESSION) : EMPTY_EXPRESSION;
		_actType 		= ACT_PRIMITIVE;
	}

	//
	// Member functions
	//
	public boolean isExecutableAction() {
		return true;
	}

	public int getArity() {
		return _args.length;
	}

	public Expression getArgs(int index) {
		return _args[index];
	}

	/**
	 * Execute a non-decomposable action
	 * 
	 */
	public int execute(Binding b, Goal currentGoal) throws AgentRuntimeException {
		Value returnValue = Functions.execute(_name, _interpreter, currentGoal, b, _args); 
		if (returnValue.isDefined()) {
			return (returnValue.eval(b).isTrue()) ? ACT_SUCCEEDED : ACT_FAILED;
		} else {
			/*
			 * System.out.println("SimpleAction: Action \"" + _name + "\" not
			 * found in user-defined functions in UserFunctions.java!\n");
			 */
			return ACT_FAILED;
		}
	}

	/**
	 * Print out the action information in-line with other information.
	 * 
	 */
	public void format(PrintStream s, Binding b) {
		s.print("PRIMITIVE: " +_name + " ");
		for (int i=0, n=_args.length; i<n; i++) {
			_args[i].format(s, b);
			s.print(" ");
		}
	}
}

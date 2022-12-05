//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: FunctionCall.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/FunctionCall.java,v $
//  
//  File              : FunctionCall.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:22:02 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:01 2004
//  Update Count      : 31
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

package kr.ac.uos.ai.editor.jam.expression;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.List;

import kr.ac.uos.ai.editor.jam.exception.AgentRuntimeException;


/**
 * 
 * Represents a function call
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class FunctionCall extends Expression implements Serializable {
	private static final long serialVersionUID = 7694135869147846221L;

	//
	// Members
	//
	private final Object 			_interpreter;
	private final String 				_name;
	private final Expression[]			_args;

	//
	// Constructors
	//

	/**
	 * Constructor with name and argument list
	 * 
	 */
	
	private static final Expression[] EMPTY_EXPRESSION = new Expression[0];
	public FunctionCall(Object interpreter, String name, Expression... args) {
		_interpreter	= interpreter;
		_name			= name;
		_args			= (args != null) ? args.clone() : EMPTY_EXPRESSION;
	}
	
	public FunctionCall(Object interpreter, String name, List<Expression> args) {
		_interpreter 	= interpreter;
		_name 			= name;
		_args 			= (args != null) ? args.toArray(EMPTY_EXPRESSION) : EMPTY_EXPRESSION;
	}

	//
	// Member functions
	//

	public String getName() {
		return _name;
	}

	public Expression getArg(int index) {
		return _args[index];
	}

	public int getArity() {
		return _args.length;
	}

	public ExpressionType getType() {
		return ExpressionType.FUNCALL;
	}

	/**
	 * Perform the function
	 * 
	 */
	public Value eval(Binding binding) throws AgentRuntimeException {
		Value returnValue = Functions.execute(_name, _interpreter, null, binding, _args);
		if (!returnValue.isDefined()) {
			System.out.println("FunctionCall: Action \"" + _name + "\" not found in user-defined functions!\n");
		}
		return returnValue;
	}
	
	/**
	 * Display information without considering it being in-line with other
	 * information
	 * 
	 */
	public void print(PrintStream s, Binding b) {
		try {
			eval(b).print(s, b);
		} catch (AgentRuntimeException e) {}
	}

	/**
	 * Display information considering that it will be in-line with other
	 * information
	 * 
	 */
	public void format(PrintStream s, Binding b) {
		try {
			eval(b).format(s, b);
		} catch (AgentRuntimeException e) {}
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(_name);
		for (Expression arg : _args) {
			builder.append(" ").append(arg);
		}
		builder.append(")");
		return builder.toString();
	}
}

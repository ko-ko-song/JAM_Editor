//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Expression.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/Expression.java,v $
//  
//  File              : Expression.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:22:14 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:01 2004
//  Update Count      : 17
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

import kr.ac.uos.ai.editor.jam.exception.AgentRuntimeException;

/**
 * 
 * Represents the basic data-types within JAM agents
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public abstract class Expression implements Serializable {
	private static final long serialVersionUID = -5741402794075208307L;
	
	public static final Expression[]	NULL_ARRAY	= new Expression[0];

	//
	// Members
	//

	//
	// Constructors
	//

	//
	// Abstract member functions
	// 
	public abstract String getName();
	public abstract ExpressionType getType();
	public abstract void print(PrintStream s, Binding b);
	public abstract void format(PrintStream s, Binding b);

	/**
	 * Evaluates the expression to a single resultant
	 * @throws AgentRuntimeException TODO
	 */
	public abstract Value eval(Binding b) throws AgentRuntimeException;

	//
	// Member functions
	//

	public boolean isVariable() {
		return false;
	}

	public Variable getVariable() {
		return null;
	}

	/**
	 * Evaluates the expression to a single resultant (whether the two values are equivalent)
	 */
	public boolean equals(Expression e, Binding b) throws AgentRuntimeException {
		Value one = eval(b);
		Value two = e.eval(b);
		if (one.isDefined()) {
			return two.isDefined() ? one.eq(two) : false;
		} else {
			return two.isDefined() ? false : true;
		}
	}

	/**
	 * Evaluates the expression to a single resultant (whether the first value is "less than" the second value)
	 */
	public boolean lessthan(Expression e, Binding b) throws AgentRuntimeException {
		Value one = eval(b);
		Value two = e.eval(b);
		if (one.isDefined()) {
			return two.isDefined() ? one.lt(two) : false;
		} else {
			return two.isDefined() ? false : true;
		}
	}

}

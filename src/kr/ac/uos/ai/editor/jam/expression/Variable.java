//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Variable.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/Variable.java,v $
//  
//  File              : Variable.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:18:59 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:53:58 2004
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
 * Represents plan variables
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class Variable extends Expression implements Serializable {
	private static final long serialVersionUID = -551246411050205910L;

	//
	// Members
	//
	
	private final Symbol			_symbol;

	//
	// Constructors
	//

	public Variable(String name) {
		_symbol = new Symbol(name);
	}
	
	
	/**
	 * 
	 * 
	 */
	public Variable(Symbol symbol) {
		_symbol = symbol;
	}

	//
	// Member functions
	//

	public boolean isVariable() {
		return true;
	}

	public Variable getVariable() {
		return this;
	}

	public Symbol getSymbol() {
		return _symbol;
	}
	
	public String getName() {
		return _symbol.getName();
	}
	
	public ExpressionType getType() {
		return ExpressionType.VARIABLE;
	}

	public Value eval(Binding b) throws AgentRuntimeException {
		return (b == null) ? Value.UNDEFINED : b.getValue(this);
	}

	public void print(PrintStream s, Binding b) {
		try {
			eval(b).print(s, b);
		} catch (AgentRuntimeException e) {}
	}

	public void format(PrintStream s, Binding b) {
		try {
			eval(b).format(s, b);
		} catch (AgentRuntimeException e) {}
	}

	public String toString() {
		return getName();
	}
}

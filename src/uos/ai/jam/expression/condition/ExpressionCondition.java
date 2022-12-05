//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ExpressionCondition.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/ExpressionCondition.java,v $
//  
//  File              : ExpressionCondition.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:22:13 1997
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

package uos.ai.jam.expression.condition;

import java.io.Serializable;
import java.util.List;
import java.util.ListIterator;

import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;

//import java.util.*;

/**
 * 
 * A boolean-evaluable expression condition
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class ExpressionCondition extends Condition implements Serializable {
	private static final long serialVersionUID = 8555764408883656451L;

	//
	// Members
	//
	protected Expression 					_expression;

	//
	// Constructors
	//

	public ExpressionCondition(Expression e) {
		_expression = e;
	}

	//
	// Member functions
	//

	public String getName() {
		return _expression.getName();
	}

	public int getType() {
		return COND_EXP;
	}

	public Expression getExpression() {
		return _expression;
	}
	
	/**
	 * Remove from the given binding list the ones not satisfying the
	 * expression.
	 * 
	 */
	public boolean check(List<Binding> bindingList) {
		ListIterator<Binding> iter = bindingList.listIterator();
		while(iter.hasNext()) {
			Binding binding = iter.next();
			try {
				if (!_expression.eval(binding).isTrue()) {
					iter.remove();
				}
			} catch (AgentRuntimeException e) {
				return false;
			}
		}
		return (bindingList.size() > 0);
	}

	/**
	 * Confirm whether the binding is still valid against the current World
	 * Model.
	 * 
	 */
	public boolean confirm(Binding b) {
		try {
			return _expression.eval(b).isTrue();
		} catch (AgentRuntimeException e) {
			return false;
		}
	}

	public String toString() {
		return _expression.toString();
	}
}

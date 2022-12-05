//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanContext.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PlanContext.java,v $
//  
//  File              : PlanContext.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:37 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:00 2004
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

package uos.ai.jam.plan;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.condition.Condition;

/**
 * 
 * Represents the conditions under which a plan is applicable
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class PlanContext implements Serializable {
	private static final long serialVersionUID = -7869291562774804659L;
	
	//
	// Members
	//
	
	private final List<Condition>		_conditions;

	//
	// Constructors
	//

	/**
	 * 
	 * 
	 */
	public PlanContext() {
		_conditions = new LinkedList<Condition>();
	}

	/**
	 * 
	 * 
	 */
	public PlanContext(List<Condition> cList) {
		_conditions = cList;
	}

	//
	// Member functions
	//

	public List<Condition> addConditions(List<Condition> cList) {
		for (Condition condition : cList) {
			_conditions.add(condition);
		}
		return _conditions;
	}
	
	private static final Condition[] EMPTY_CONDITION = new Condition[0];
	public Condition[] getConditions() {
		return (_conditions != null) ? _conditions.toArray(EMPTY_CONDITION) : EMPTY_CONDITION;
	}

	/**
	 * Establish contexts (generate the binding list)
	 * 
	 */
	public boolean check(List<Binding> bindingList) {
		if (_conditions == null || _conditions.size() == 0) {
			return true;
		}
		
		for (Condition condition : _conditions) {
			if (!condition.check(bindingList)) {	// check() removes elements from bindingList
				break;
			}
		}

		return (bindingList.size() > 0);
	}

	/**
	 * Confirm the validity of the current context with the current binding
	 * 
	 */
	public boolean confirm(Binding b) {
		if (_conditions == null || _conditions.size() == 0) {
			return true;
		}
		
		for (Condition condition : _conditions) {
			if (!condition.confirm(b)) {
				return false;
			}
		}
		
		return true;
	}
	
	public String toString(){
		String result = "";
		for(int i = 0 ;i < _conditions.size();i++){
			result += _conditions.get(i) + "\n";
		}
		
		return result;
	}
	
}

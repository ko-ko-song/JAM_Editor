//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: RelationCondition.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/RelationCondition.java,v $
//  
//  File              : RelationCondition.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:19:40 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:53:59 2004
//  Update Count      : 18
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

package kr.ac.uos.ai.editor.jam.condition;

import java.io.Serializable;
import java.util.List;

import kr.ac.uos.ai.editor.jam.expression.Binding;
import kr.ac.uos.ai.editor.jam.model.Relation;


/**
 * 
 * A boolean-evaluable relation
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public abstract class RelationCondition extends Condition implements Serializable {
	private static final long serialVersionUID = 6089925217818595903L;

	//
	// Members
	//
	protected Relation 				_relation;
	protected Object 			_interpreter;

	//
	// Constructors
	//

	/**
	 * Constructor w/ World Model relation and interpreter (to simplify access
	 * to the agent's World Model) as arguments.
	 * 
	 */
	public RelationCondition(Relation r, Object interpreter) {
		_relation 		= r;
		_interpreter 	= interpreter;
	}

	//
	// Member functions
	//
	public String getName() {
		return _relation.getName();
	}

	public Relation getRelation() {
		return _relation;
	}

	public Object getInterpreter() {
		return _interpreter;
	}

	/**
	 * Return the particular type of the object
	 * 
	 */
	public abstract int getType();

	/**
	 * Remove from the given binding list the ones not satisfying the fact.
	 * 
	 */
	public abstract boolean check(List<Binding> bindingList);

	/**
	 * Confirm whether the binding is still valid against the current WM
	 * 
	 */
	public abstract boolean confirm(Binding b);

}

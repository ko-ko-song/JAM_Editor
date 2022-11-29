//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: FactCondition.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/FactCondition.java,v $
//  
//  File              : FactCondition.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:22:07 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:01 2004
//  Update Count      : 19
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
import java.util.ListIterator;

import kr.ac.uos.ai.editor.jam.expression.Binding;
import kr.ac.uos.ai.editor.jam.model.Relation;


//import java.util.*;

/**
 * 
 * A boolean-evaluable World Model fact
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class FactCondition extends RelationCondition implements Serializable {
	private static final long serialVersionUID = 2351879954817011587L;

	//
	// Members
	//

	//
	// Constructors
	//

	/**
	 * Constructor w/ World Model relation to check and interpreter as
	 * arguments.
	 * 
	 */
	public FactCondition(Relation r, Object interpreter) {
		super(r, interpreter);
	}

	//
	// Member functions
	//
	public int getType() {
		return Condition.COND_FACT;
	}

	/**
	 * Compare the relation against the world model and add and/or delete
	 * bindings as appropriate
	 * 
	 */
	public boolean check(List<Binding> bindingList) {
		ListIterator<Binding> iter = bindingList.listIterator();
		while(iter.hasNext()) {
			
			Binding binding = iter.next();
			iter.remove();
//			for (Binding checkedBinding : getInterpreter().getWorldModel().check(getRelation(), binding)) {
//				iter.add(checkedBinding);
//			}
		}
		return (bindingList.size() > 0);
	}

	/**
	 * Confirm whether the binding is still valid against the current World
	 * Model
	 * 
	 */
	public boolean confirm(Binding b) {
//		return getInterpreter().getWorldModel().match(_relation, b);
		return true;
	}

	public String toString() {
		return "FACT " + _relation.toString();
	}
}

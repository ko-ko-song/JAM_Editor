//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PredicateRetrieve.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PredicateRetrieve.java,v $
//  
//  File              : PredicateRetrieve.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:19:46 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:53:59 2004
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

package uos.ai.jam.expression;

import java.io.Serializable;

import uos.ai.jam.WorldModelTable;
import uos.ai.jam.exception.AgentRuntimeException;

/**
 * 
 * Retrieve Predicate (a World Model Retrieve expression evaluable to
 * true/false)
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class PredicateRetrieve extends Predicate implements Serializable {
	private static final long serialVersionUID = 6083579771898642875L;

	//
	// Members
	//

	private final WorldModelTable		_worldModel;
	
	//
	// Constructors
	//

	/**
	 * Primary constructor
	 * 
	 */
	public PredicateRetrieve(String name, Relation relation, WorldModelTable worldModel) {
		super(name, relation);
		_worldModel = worldModel;
	}

	//
	// Member functions
	//

	/**
	 * Calculate the truth value of the World Model relation
	 * 
	 */
	public Value eval(Binding binding) throws AgentRuntimeException {
		// unbind variables before matching
		binding.unbindVariables(_relation);

		// now try match to get new values
		return _worldModel.match(_relation, binding) ? Value.TRUE : Value.FALSE;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(RETRIEVE ");
		builder.append(_relation);
		builder.append(")");
		return builder.toString();
	}
}

//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanWaitConstruct.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PlanWaitConstruct.java,v $
//  
//  File              : PlanWaitConstruct.java
//  Original author(s): Marcus J. Huber <marcush@home.com>
//  Created On        : Tue Sep 30 14:18:56 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:52:33 2004
//  Update Count      : 28
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

package uos.ai.jam.plan.constructor;

import java.io.*;

import uos.ai.jam.expression.Relation;
import uos.ai.jam.plan.action.Action;

/**
 * 
 * A built-in JAM construct for conditionally delayed execution.
 * 
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class PlanWaitConstruct extends PlanConstruct implements Serializable {
	private static final long serialVersionUID = -497003632898395766L;

	//
	// Members
	//
	protected Action _action;
	protected Relation _rel;

	//
	// Constructors
	//

	/**
	 * Wait on successful completion of an action
	 * 
	 */
	public PlanWaitConstruct(Action a) {
		_action = a;
		_rel = null;
		_constructType = PLAN_WAIT;
	}

	/**
	 * Wait for a goal relation to be achieved
	 * 
	 */
	public PlanWaitConstruct(Relation r) {
		_action = null;
		_rel = r;
		_constructType = PLAN_WAIT;
	}

	//
	// Member functions
	//
	public Action getAction() {
		return _action;
	}

	public Relation getRelation() {
		return _rel;
	}

	/**
	 * Construct an appropriate RuntimeState
	 * 
	 */
	public PlanRuntimeState newRuntimeState() {
		return new PlanRuntimeWaitState(this);
	}

}

//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeState.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PlanRuntimeState.java,v $
//  
//  File              : PlanRuntimeState.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:09 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:53:59 2004
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

package uos.ai.jam.plan.constructor;

import java.io.Serializable;

import uos.ai.jam.Goal;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.plan.APLElement;

/**
 * 
 * Represents the runtime state of plan constructs
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public abstract class PlanRuntimeState implements Serializable {
	private static final long serialVersionUID = -1882981878572134953L;

	//
	// Members
	//
	public final static int PLAN_CONSTRUCT_FAILED 		= -1;
	public final static int PLAN_CONSTRUCT_INCOMP 		= 0;
	public final static int PLAN_CONSTRUCT_COMPLETE 	= 1;
	
	// 일단 임시로 스테이트를 하나 더 늘림
	public final static int PLAN_CONSTRUCT_ABANDON 	= 2;

	protected PlanConstruct 			_thisConstruct;
	protected PlanRuntimeState 			_substate;

	//
	// Constructors
	//

	//
	// Member functions
	//
	public PlanRuntimeState getSubstate() {
		return _substate;
	}

	public void setSubstate(PlanRuntimeState f) {
		_substate = f;
	}

	public PlanConstruct getThisConstruct() {
		return _thisConstruct;
	}

	public void setThisConstruct(PlanConstruct se) {
		_thisConstruct = se;
	}

	/**
	 * 
	 * 
	 */
	public void intend(APLElement s) {
		if (_substate != null)
			_substate.intend(s);
	}

	/**
	 * @throws AgentRuntimeException TODO
	 * 
	 * 
	 */
	public abstract int execute(Binding b, Goal thisGoal, Goal prevGoal) throws AgentRuntimeException;

}

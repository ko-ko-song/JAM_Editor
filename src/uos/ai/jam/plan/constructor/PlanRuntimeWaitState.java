//-*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//
//$Id: PlanRuntimeWaitState.java,v 1.2 2006/07/25 00:06:13 semix2 Exp $
//$Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PlanRuntimeWaitState.java,v $
//
//File              : PlanRuntimeWaitState.java
//Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//: Marcus J. Huber <marcush@irs.home.com>
//Created On        : Tue Sep 30 14:21:01 1997
//Last Modified By  : Jaeho Lee <jaeho@david>
//Last Modified On  : Mon Sep  6 17:53:59 2004
//Update Count      : 30
//
//Copyright (C) 1997-2004 Jaeho Lee and Marcus J. Huber.
//
//////////////////////////////////////////////////////////////////////////////
//
//JAM agent architecture
//
//Copyright (C) 1997-2004 Jaeho Lee and Marcus J. Huber.
//
//Permission is granted to copy and redistribute this software so long
//as no fee is charged, and so long as the copyright notice above, this
//grant of permission, and the disclaimer below appear in all copies
//made.
//
//This software is provided as is, without representation as to its
//fitness for any purpose, and without warranty of any kind, either
//express or implied, including without limitation the implied
//warranties of merchantability and fitness for a particular purpose.
//Jaeho Lee and Marcus J. Huber shall not be liable for any damages,
//including special, indirect, incidental, or consequential damages,
//with respect to any claim arising out of or in connection with the
//use of the software, even if they have been or are hereafter advised
//of the possibility of such damages.
//
//////////////////////////////////////////////////////////////////////////////

package uos.ai.jam.plan.constructor;

import java.io.*;

import uos.ai.jam.Goal;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.plan.action.Action;

/**
 * 
 * Represents the runtime state of plan constructs
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class PlanRuntimeWaitState extends PlanRuntimeState implements
		Serializable {
	private static final long serialVersionUID = -2414970038502150054L;

	//
	// Members
	//

	//
	// Constructors
	//
	public PlanRuntimeWaitState(PlanWaitConstruct be) {
		_thisConstruct = be;
		_substate = null;
	}

	//
	// Member functions
	//

	/**
	 * Check to see whether the action returns successfully or the goal has been
	 * accomplished.
	 * 
	 */
	public int execute(Binding b, Goal thisGoal, Goal prevGoal) throws AgentRuntimeException {
		// -->> semix2
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		// <<-- semix2

		// Check to see if the agent is waiting on action success
		if (((PlanWaitConstruct) _thisConstruct).getAction() != null) {

			int returnVal;
			returnVal = ((PlanWaitConstruct) _thisConstruct).getAction()
					.execute(b, thisGoal);
			if (returnVal == Action.ACT_SUCCEEDED)
				return PLAN_CONSTRUCT_COMPLETE;
			else
				return PLAN_CONSTRUCT_INCOMP;
		}

		// Agent must be waiting on a goal
		else {
			// Check for match of goal relation on world model
			return (thisGoal.getIntentionStructure().getInterpreter()
					.getWorldModel().match(((PlanWaitConstruct) _thisConstruct)
					.getRelation(), b)) ? PLAN_CONSTRUCT_COMPLETE
					: PLAN_CONSTRUCT_INCOMP;
		}
	}

}

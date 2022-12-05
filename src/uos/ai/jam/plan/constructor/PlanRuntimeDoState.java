//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeDoState.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PlanRuntimeDoState.java,v $
//  
//  File              : PlanRuntimeDoState.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:19 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:00 2004
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

public class PlanRuntimeDoState extends PlanRuntimeState implements
		Serializable {
	private static final long serialVersionUID = 3095174119089678761L;

	//
	// Members
	//

	//
	// Constructors
	//

	public PlanRuntimeDoState(PlanDoConstruct be) {
		_thisConstruct = be;
		_substate = be.getSequence().newRuntimeState();
	}

	//
	// Member functions
	//

	/**
	 * 
	 * 
	 */
	public int execute(Binding b, Goal thisGoal, Goal prevGoal) throws AgentRuntimeException {
		int testReturnVal;
		int returnVal;

		// Execute the current PLAN constructs in the loop. If execution
		// reaches the end of the iteration body then check the test condition.
		returnVal = _substate.execute(b, thisGoal, prevGoal);

		if (returnVal == PLAN_CONSTRUCT_FAILED) {
			return PLAN_CONSTRUCT_FAILED;
		} else if (returnVal == PLAN_CONSTRUCT_COMPLETE) {

			testReturnVal = ((PlanDoConstruct) _thisConstruct).getTest().execute(b, thisGoal);
			if (testReturnVal != Action.ACT_SUCCEEDED) {
				return PLAN_CONSTRUCT_COMPLETE;
			} else {
				_substate = ((PlanDoConstruct) _thisConstruct).getSequence()
						.newRuntimeState();
				return PLAN_CONSTRUCT_INCOMP;
			}
		} else { // return_val == PLAN_CONSTRUCT_INCOMP) {
			return PLAN_CONSTRUCT_INCOMP;
		}
	}

}

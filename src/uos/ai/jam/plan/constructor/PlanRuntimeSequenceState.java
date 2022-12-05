//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeSequenceState.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PlanRuntimeSequenceState.java,v $
//  
//  File              : PlanRuntimeSequenceState.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:13 1997
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

package uos.ai.jam.plan.constructor;

import java.io.*;

import uos.ai.jam.Goal;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;

/**
 * 
 * Represents the runtime state of sequence constructs
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class PlanRuntimeSequenceState extends PlanRuntimeState implements
		Serializable {
	private static final long serialVersionUID = -7533528323256613033L;

	//
	// Members
	//
	int _currentConstructNum;

	//
	// Constructors
	//

	/**
	 * 
	 * 
	 */
	public PlanRuntimeSequenceState(PlanSequenceConstruct be) {
		_thisConstruct = be;

		_substate = be.getConstruct(0).newRuntimeState();
		_currentConstructNum = 0;
	}

	//
	// Member functions
	//

	/**
	 * 
	 * 
	 */
	public int execute(Binding b, Goal thisGoal, Goal prevGoal) throws AgentRuntimeException {
		int returnVal;

		if (_substate == null) {
			_substate = ((PlanSequenceConstruct) _thisConstruct)
					.newRuntimeState();
		}

		returnVal = _substate.execute(b, thisGoal, prevGoal);

		if (returnVal == PLAN_CONSTRUCT_FAILED) {

			// The substate failed, so get rid of it
			_substate = null;
			return PLAN_CONSTRUCT_FAILED;
		}

		else if (returnVal == PLAN_CONSTRUCT_COMPLETE) {

			// Check to see if the sequence is finished
			if (_currentConstructNum == ((PlanSequenceConstruct) _thisConstruct)
					.getNumConstructs() - 1) {

				// The substate's done, so first get rid of it
				_substate = null;
				return PLAN_CONSTRUCT_COMPLETE;
			} else { // PLAN_CONSTRUCT_INCOMP

				// Not done yet, so go on to the next action
				_substate = ((PlanSequenceConstruct) _thisConstruct)
						.getConstruct(++_currentConstructNum).newRuntimeState();
			}

			return PLAN_CONSTRUCT_INCOMP;
		} else { // return_val == PLAN_CONSTRUCT_INCOMP
			return PLAN_CONSTRUCT_INCOMP;
		}
	}

}

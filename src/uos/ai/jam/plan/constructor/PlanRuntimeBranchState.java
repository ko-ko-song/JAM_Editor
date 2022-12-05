//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeBranchState.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PlanRuntimeBranchState.java,v $
//  
//  File              : PlanRuntimeBranchState.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:25 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:00 2004
//  Update Count      : 17
//  
//  Copyright (C) 1997 Jaeho Lee and Marcus J. Huber.
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1997 Jaeho Lee and Marcus J. Huber.
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
 * Represents the runtime state of branch constructs
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class PlanRuntimeBranchState extends PlanRuntimeState implements
		Serializable {
	private static final long serialVersionUID = 2942036651383581746L;

	//
	// Members
	//
	protected int _activeBranchNum; // index (0 based) to current branch

	//
	// Constructors
	//

	/**
	 * 
	 * 
	 */
	public PlanRuntimeBranchState(PlanBranchConstruct be) {
		_thisConstruct = be;
		_substate = be.getBranch(0).newRuntimeState();
		_activeBranchNum = 0;
	}

	//
	// Member functions
	//
	public int getActiveBranchNum() {
		return _activeBranchNum;
	}

	/**
	 * Execute something in one of the branches
	 * 
	 */
	public int execute(Binding b, Goal thisGoal, Goal prevGoal) throws AgentRuntimeException {
		PlanConstruct currentConstruct;
		int returnVal;
		int branchType;

		branchType = ((PlanBranchConstruct) _thisConstruct).getBranchType();
		currentConstruct = ((PlanBranchConstruct) _thisConstruct)
				.getBranch(_activeBranchNum);

		if (_substate == null)
			_substate = currentConstruct.newRuntimeState();

		returnVal = _substate.execute(b, thisGoal, prevGoal);
		
		//
		// FAILED!
		//
		// Something in the branch failed so this construct may fail
		if (returnVal == PLAN_CONSTRUCT_FAILED) {

			// If this is an AND branch, or if we've gone through all branches
			// of an OR,
			// then the construct has failed.
			if (branchType == PlanBranchConstruct.PLAN_AND_BRANCH
					|| _activeBranchNum == ((PlanBranchConstruct) _thisConstruct)
							.getNumBranches() - 1) {
				return PLAN_CONSTRUCT_FAILED;
			} else {
				_substate = ((PlanBranchConstruct) _thisConstruct).getBranch(
						++_activeBranchNum).newRuntimeState();
				return PLAN_CONSTRUCT_INCOMP;
			}
		}

		//
		// INCOMPLETE
		//
		// Nothing's been determined at this point
		else if (returnVal == PLAN_CONSTRUCT_INCOMP) {
			return PLAN_CONSTRUCT_INCOMP;
		}

		//
		// COMPLETE!
		//
		else {

			// If this is an OR branch, or if we've gone through all branches of
			// an AND,
			// then the construct has succeeded.
			if (branchType == PlanBranchConstruct.PLAN_OR_BRANCH
					|| (_activeBranchNum == ((PlanBranchConstruct) _thisConstruct)
							.getNumBranches() - 1)) {
				return PLAN_CONSTRUCT_COMPLETE;
			} else { // More branches, so need to keep executing
				_substate = ((PlanBranchConstruct) _thisConstruct).getBranch(
						++_activeBranchNum).newRuntimeState();
				return PLAN_CONSTRUCT_INCOMP;
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public void setActiveBranchNum(int n) {
		if (n >= 0
				&& n < ((PlanBranchConstruct) _thisConstruct).getNumBranches())
			_activeBranchNum = n;
		else
			_activeBranchNum = -1;
	}

}

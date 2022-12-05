//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeDoAnyState.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PlanRuntimeDoAnyState.java,v $
//  
//  File              : PlanRuntimeDoAnyState.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:21 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:00 2004
//  Update Count      : 22
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
import java.util.Random;
import java.util.Vector;

import uos.ai.jam.Goal;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;

/**
 * 
 * Represents the runtime state of DoAny constructs
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class PlanRuntimeDoAnyState extends PlanRuntimeState implements
		Serializable {
	private static final long serialVersionUID = -4191182191590907273L;

	//
	// Members
	//
	protected int 						_activeBranchNum; // index (0 based) to current branch
	protected Vector<Integer>	 		_branchesLeft; // Vector holding indices of branches that

	// have not yet completed (succeeded OR failed)

	//
	// Constructors
	//

	/**
	 * 
	 * 
	 */
	public PlanRuntimeDoAnyState(PlanDoAnyConstruct be) {
		_thisConstruct = be;

		_branchesLeft = new Vector<Integer>(be.getNumBranches());
		for (int i = 0; i < be.getNumBranches(); i++) {
			_branchesLeft.addElement(new Integer(i));
		}

		setActiveBranchNum(selectRandomBranch());
		_substate = be.getBranch(getActiveBranchNum()).newRuntimeState();
	}

	//
	// Member functions
	//
	public int getActiveBranchNum() {
		return _activeBranchNum;
	}

	/**
	 * Determine if any branches are incomplete (i.e., not successful or failed)
	 * 
	 */
	@SuppressWarnings("unused")
	private boolean allBranchesTried() {
		return (_branchesLeft.size() == 0);
	}

	/**
	 * Select a branch randomly from those still not completed or failed.
	 * 
	 */
	private int selectRandomBranch() {
		Random ran = new Random();
		return (((Integer) _branchesLeft.elementAt(Math.abs(ran.nextInt()
				% _branchesLeft.size()))).intValue());
	}

	/**
	 * Execute something in one of the branches
	 * 
	 */
	public int execute(Binding b, Goal thisGoal, Goal prevGoal) throws AgentRuntimeException {
		PlanConstruct currentConstruct;
		int returnVal;

		currentConstruct = ((PlanDoAnyConstruct) _thisConstruct)
				.getBranch(getActiveBranchNum());
		if (_substate == null)
			_substate = currentConstruct.newRuntimeState();

		returnVal = _substate.execute(b, thisGoal, prevGoal);

		//
		// FAILED!
		//
		// Something in the branch failed so this construct may fail
		if (returnVal == PLAN_CONSTRUCT_FAILED) {

			// If we've gone through all branches then the construct has failed.
			if (_branchesLeft.size() == 1) {
				return PLAN_CONSTRUCT_FAILED;
			}

			else {

				// Find and remove the current branch from further consideration
				for (int i = 0; i < _branchesLeft.size(); i++) {
					if (((Integer) _branchesLeft.elementAt(i)).intValue() == getActiveBranchNum()) {
						_branchesLeft.removeElementAt(i);
					}
				}

				setActiveBranchNum(selectRandomBranch());
				_substate = ((PlanDoAnyConstruct) _thisConstruct).getBranch(
						getActiveBranchNum()).newRuntimeState();
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

			return PLAN_CONSTRUCT_COMPLETE;
		}
	}

	/**
	 * 
	 * 
	 */
	public void setActiveBranchNum(int n) {
		if (n >= 0
				&& n < ((PlanDoAnyConstruct) _thisConstruct).getNumBranches())
			_activeBranchNum = n;
		else
			_activeBranchNum = -1;
	}

}

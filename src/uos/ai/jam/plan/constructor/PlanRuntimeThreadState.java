//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeThreadState.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PlanRuntimeThreadState.java,v $
//  
//  File              : PlanRuntimeThreadState.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:02 1997
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

//import java.lang.*;

/**
 * 
 * Represents the runtime state of a threaded sequence of constructs This class
 * re-implements the PlanRuntimeState members simply because it's easier.
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class PlanRuntimeThreadState extends Thread implements Serializable {
	private static final long serialVersionUID = 5367810157562816669L;

	//
	// Members
	//
	protected PlanSequenceConstruct _thisConstruct;

	protected PlanRuntimeSequenceState _substate;

	protected Binding _binding;

	protected Goal _goal;

	protected int _threadNumber;

	protected int _threadState[];

	//
	// Constructors
	//

	/**
	 * 
	 * 
	 */
	public PlanRuntimeThreadState(PlanSequenceConstruct be, Binding b,
			Goal thisGoal, int threadNumber, int threadState[]) {
		_thisConstruct = be;
		_substate = (PlanRuntimeSequenceState) be.newRuntimeState(); // Execute
																		// this
																		// construct
																		// directly
		// (it's the only one)
		_binding = b; // To hold for subconstructs
		_goal = thisGoal; // To hold for subconstructs
		_threadNumber = threadNumber; // Index into state array
		_threadState = threadState; // reference to array held by
		// PlanRuntimeSimultaneousState

		/*
		 * System.out.println("New thread created: threadNumber = " +
		 * threadNumber + " with threadState of " + threadState);
		 */

	}

	//
	// Member functions
	//

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void run() {
		/*
		 * System.out.println("Starting to run thread " + _threadNumber);
		 */

		int returnVal;

		if (_substate == null) {
			_substate = (PlanRuntimeSequenceState) _thisConstruct
					.newRuntimeState();
		}

		while (true) { // Loop forever until sequence completes or fails

			// System.out.println("Running thread " + _threadNumber);

			try {
				returnVal = _substate.execute(_binding, _goal, _goal.getPrevGoal());
			} catch (AgentRuntimeException e) {
				returnVal = PlanRuntimeState.PLAN_CONSTRUCT_FAILED;
			}

			if (returnVal == PlanRuntimeState.PLAN_CONSTRUCT_FAILED) {

				// System.out.println("Thread#" + _threadNumber + "::10:
				// Subconstruct returned FAILED! (" +
				// returnVal + ")");

				_threadState[_threadNumber] = PlanRuntimeState.PLAN_CONSTRUCT_FAILED;
				return;
			}

			else if (returnVal == PlanRuntimeState.PLAN_CONSTRUCT_COMPLETE) {

				// System.out.println("Thread#" + _threadNumber + "::30:
				// Subconstruct returned complete! (" +
				// returnVal + ")");

				// Check to see if the sequence is finished

				// System.out.println("Subconstruct complete.");

				_threadState[_threadNumber] = PlanRuntimeState.PLAN_CONSTRUCT_COMPLETE;
				return;
			}

			else { // return_val == PLAN_CONSTRUCT_INCOMP

				// System.out.println("Thread#" + _threadNumber +
				// "::50:Subconstruct execution incomplete.");

				_threadState[_threadNumber] = PlanRuntimeState.PLAN_CONSTRUCT_INCOMP;

				// System.out.println("Suspending thread " + _threadNumber);

				suspend();

				// System.out.println("Thread " + _threadNumber + " out of
				// suspend()");

			}
		}
	}

}

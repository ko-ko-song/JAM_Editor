//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeParallelState.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PlanRuntimeParallelState.java,v $
//  
//  File              : PlanRuntimeParallelState.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:15 1997
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

//import java.util.*;
//import java.lang.*;

/**
 * 
 * Represents the runtime state of parallel execution of sequences within plans.
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class PlanRuntimeParallelState extends PlanRuntimeState implements
		Serializable {
	private static final long serialVersionUID = -8428946263155427987L;

	//
	// Members
	//
	protected boolean _threadsStarted;

	protected PlanRuntimeThreadState _threads[];

	public int _threadState[];

	//
	// Constructors
	//

	/**
	 * 
	 * 
	 */
	public PlanRuntimeParallelState(PlanParallelConstruct be) {
		/*
		 * System.out.println("Creating PlanRuntimeParallelState");
		 */

		_thisConstruct = be;
		_substate = be.getConstruct(0).newRuntimeState();

		// Initialize the array to all constructs incomplete.
		_threadsStarted = false;
		int threadNum;
		_threadState = new int[be.getNumConstructs()];
		_threads = new PlanRuntimeThreadState[be.getNumConstructs()];
		for (threadNum = 0; threadNum < be.getNumConstructs(); threadNum++) {
			_threadState[threadNum] = PLAN_CONSTRUCT_INCOMP;
		}
	}

	//
	// Member functions
	//

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	public int execute(Binding b, Goal thisGoal, Goal prevGoal) throws AgentRuntimeException {
		/*
		 * System.out.println("Starting execution of PlanRuntimeParallelState");
		 */

		// Spawn off each thread and then, every execution cycle, wake them up
		// so
		// they'll execute the next action/construct.
		int threadNum;
		boolean anyFailed;
		boolean allComplete;
		int numConstructs = ((PlanParallelConstruct) _thisConstruct)
				.getNumConstructs();

		// If threads have not been started then created them and start them.
		if (_threadsStarted == false) {
			for (threadNum = 0; threadNum < numConstructs; threadNum++) {

				/*
				 * System.out.println("Creating new thread: threadNumber = " +
				 * threadNum + " with threadState of " + _threadState);
				 */

				_threads[threadNum] = new PlanRuntimeThreadState(
						((PlanSequenceConstruct) ((PlanParallelConstruct) _thisConstruct)
								.getConstruct(threadNum)), b, thisGoal,
						threadNum, _threadState);
				_threads[threadNum].start();
			}
			_threadsStarted = true;
		}

		// Do I need to yield to the threads????
		try {
			synchronized (this) {
				Thread.sleep(0, 1);
			}
		} catch (InterruptedException e) {

			/*
			 * System.out.println("Interrupted while in wait(): " + e + ".");
			 */

			System.exit(1);
		}

		/*
		 * System.out.println("Checking _threadState");
		 */

		// Go through the threads and check if any have failed or if all
		// have completed.
		anyFailed = false;
		allComplete = true;
		for (threadNum = 0; threadNum < numConstructs; threadNum++) {

			if (_threadState[threadNum] == PLAN_CONSTRUCT_FAILED) {
				anyFailed = true;
				allComplete = false;
			}

			if (_threadState[threadNum] == PLAN_CONSTRUCT_INCOMP)
				allComplete = false;
		}

		// Make sure all the threads are done before we signal that
		// the construct completed.
		if (allComplete == true) {

			/*
			 * System.out.println("PRPS:All threads completed! Waiting for them
			 * to join().");
			 */

			for (threadNum = 0; threadNum < numConstructs; threadNum++) {

				try {
					_threads[threadNum].join();
				} catch (InterruptedException e) {

					/*
					 * System.out.println("Interrupted while in waiting for
					 * join()s " + e + ".");
					 */

					System.exit(1);
				}

			}
			return PLAN_CONSTRUCT_COMPLETE;
		}

		// At least one thread failed, so stop the threads and wait for them
		// to die
		if (anyFailed == true) {

			/*
			 * System.out.println("PRPS:A thread failed! Stopping all threads
			 * and waiting for them to join().");
			 */

			for (threadNum = 0; threadNum < numConstructs; threadNum++) {
				_threads[threadNum].stop();
			}
			for (threadNum = 0; threadNum < numConstructs; threadNum++) {
				try {
					_threads[threadNum].join();
				} catch (InterruptedException e) {

					/*
					 * System.out.println("Interrupted while in waiting for
					 * join()s " + e + ".");
					 */

					System.exit(1);
				}
			}

			return PLAN_CONSTRUCT_FAILED;
		}

		// Go through the threads and resume those that haven't completed or
		// failed.

		/*
		 * System.out.println("PRPS: No threads failed and some still running!
		 * Resuming their execution.");
		 */

		for (threadNum = 0; threadNum < numConstructs; threadNum++) {

			if (_threadState[threadNum] == PLAN_CONSTRUCT_INCOMP) {

				/*
				 * System.out.println("PRPS: Resuming thread #" + threadNum);
				 */

				_threads[threadNum].resume();
			}
		}

		return PLAN_CONSTRUCT_INCOMP;
	}

}

//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanDoConstruct.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PlanDoConstruct.java,v $
//  
//  File              : PlanDoConstruct.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:30 1997
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

import java.io.Serializable;
import java.util.Vector;

import uos.ai.jam.plan.action.Action;

/**
 * 
 * Represents one case of an iterative construct within plans
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class PlanDoConstruct extends PlanConstruct implements Serializable {
	private static final long serialVersionUID = 1204600072952829032L;

	//
	// Members
	//
	protected Action _test; // The predicate for the loop

	protected PlanSequenceConstruct _constructs; // The constructs in the
													// WHILE loop

	//
	// Constructors
	//

	/**
	 * 
	 * 
	 */
	public PlanDoConstruct(Action a, PlanConstruct be) {
		_test = a;

		_constructs = new PlanSequenceConstruct(be);
		_constructType = PLAN_DO;
	}

	//
	// Member functions
	//
	public Action getTest() {
		return _test;
	}

	public Action setTest(Action a) {
		return _test = a;
	}

	public PlanSequenceConstruct getSequence() {
		return _constructs;
	}

	/**
	 * 
	 * 
	 */
	public PlanRuntimeState newRuntimeState() {
		return new PlanRuntimeDoState(this);
	}

	/**
	 * 
	 * 
	 */
	public int getNumConstructs() {
		return _constructs.getNumConstructs();
	}

	/**
	 * 
	 * 
	 */
	public Vector<PlanConstruct> getConstructs() {
		return _constructs.getConstructs();
	}

	/**
	 * 
	 * 
	 */
	public PlanConstruct getConstruct(int n) {
		return _constructs.getConstruct(n);
	}

	/**
	 * 
	 * 
	 */
	public void insertConstruct(PlanConstruct be) {
		_constructs.insertConstruct(be);
	}

}

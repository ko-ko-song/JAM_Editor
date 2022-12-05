//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanWhenConstruct.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PlanWhenConstruct.java,v $
//  
//  File              : PlanWhenConstruct.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:20:53 1997
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
import java.util.Vector;

import uos.ai.jam.plan.action.Action;

/**
 * 
 * Represents a special-case of branching plan (single branch) components
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class PlanWhenConstruct extends PlanConstruct implements Serializable {
	private static final long serialVersionUID = -2926022149451520167L;

	//
	// Members
	//
	protected Action _test; // The predicate

	protected PlanSequenceConstruct _constructs; // The When body
	protected PlanSequenceConstruct _constructs2; // else

	//
	// Constructors
	//
	
	public PlanWhenConstruct(Action a, PlanConstruct be) {
		this(a, be, null);
	}
	
	public PlanWhenConstruct(Action a, PlanConstruct be, PlanConstruct be2) {
		_test = a;

		_constructs 	= new PlanSequenceConstruct(be);
		_constructs2 	= (be2 != null) ? new PlanSequenceConstruct(be2) : null;
		_constructType 	= PLAN_WHEN;
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

	public PlanSequenceConstruct getSequence2() {
		return _constructs2;
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
	
	public Vector<PlanConstruct> getConstruct2() {
		return _constructs2.getConstructs();
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
	public PlanRuntimeState newRuntimeState() {
		return new PlanRuntimeWhenState(this);
	}

	/**
	 * 
	 * 
	 */
	public void insertConstruct(PlanConstruct be) {
		_constructs.insertConstruct(be);
	}

}

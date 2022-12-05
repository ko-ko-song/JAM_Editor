//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanSimpleConstruct.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PlanSimpleConstruct.java,v $
//  
//  File              : PlanSimpleConstruct.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:20:56 1997
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

import java.io.*;

import uos.ai.jam.plan.action.Action;

/**
 * Represents non-construct plan components
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class PlanSimpleConstruct extends PlanConstruct implements Serializable {
	private static final long serialVersionUID = 6620283504224606665L;

	//
	// Members
	//
	protected Action _action;

	//
	// Constructors
	//
	public PlanSimpleConstruct(Action a) {
		_action = a;
		_constructType = PLAN_SIMPLE;
	}

	//
	// Member functions
	//
	public Action getAction() {
		return _action;
	}

	public Action setAction(Action a) {
		return _action = a;
	}

	public PlanRuntimeState newRuntimeState() {
		if (_action.isExecutableAction())
			return new PlanRuntimeSimpleState(this);
		else
			return new PlanRuntimeGoalState(this);
	}

}

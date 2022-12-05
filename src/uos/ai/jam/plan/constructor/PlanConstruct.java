//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanConstruct.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PlanConstruct.java,v $
//  
//  File              : PlanConstruct.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:38 1997
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

import java.io.Serializable;


/**
 * Represents the basic procedural components within JAM agents
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public abstract class PlanConstruct implements Serializable {
	private static final long serialVersionUID = 1622511813936818401L;

	//
	// Members
	//
	public final static int PLAN_UNDEFINED 		= 0;
	public final static int PLAN_SEQUENCE 		= 1;
	public final static int PLAN_SIMPLE 		= 2;
	public final static int PLAN_BRANCH 		= 3;
	public final static int PLAN_WHEN 			= 4;
	public final static int PLAN_WHILE 			= 5;
	public final static int PLAN_DO 			= 6;
	public final static int PLAN_ATOMIC 		= 7;
	public final static int PLAN_PARALLEL 		= 8;
	public final static int PLAN_DOANY 			= 9;
	public final static int PLAN_DOALL 			= 10;
	public final static int PLAN_WAIT 			= 11;

	protected int _constructType;

	//
	// Constructors
	//

	//
	// Member functions
	//

	// Global exception handler 쪽에서 해당 메소드를 사용하기 때문에 public으로 바꿈
	public abstract PlanRuntimeState newRuntimeState();

	public int getType() {
		return _constructType;
	}

}

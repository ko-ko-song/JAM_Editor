//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PrimitiveAction.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PrimitiveAction.java,v $
//  
//  File              : PrimitiveAction.java
//  Author(s)         : marcush <marcush@irs.home.com>
//  
//  Description       : 
//  
//  Original author(s): marcush <marcush@irs.home.com>
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Fri Oct  2 08:49:11 1998
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:52:33 2004
//  Update Count      : 8
//  
//  Copyright (C) 1998 Marcus J. Huber and Intelligent Reasoning Systems
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1998 Marcus J. Huber and Intelligent Reasoning Systems
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
//  Marcus J. Huber and Intelligent Reasoning Systems shall not be liable
//  for any damages, including special, indirect, incidental, or
//  consequential damages,  with respect to any claim arising out of or
//  in connection with the use of the software, even if they have been or
//  are hereafter advised of the possibility of such damages.
// 
//////////////////////////////////////////////////////////////////////////////

package uos.ai.jam.plan.action;

import java.util.List;

import uos.ai.jam.Goal;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Value;

/**
 * 
 * The interface to which all primitive actions must define
 * 
 * @author Marc Huber
 * @version 1.1
 * 
 */

public interface PrimitiveAction {
	public Value execute(String name, int arity, List<Expression> args, Binding binding, Goal currentGoal) throws AgentRuntimeException;
}

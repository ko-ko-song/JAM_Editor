//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Fail.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/primitives/Fail.java,v $
//  
//  File              : Fail.java
//  Author(s)         : marcush <marcush@irs.home.com>
//  
//  Description       : Provide JAM users some built-in functionality
//  
//  Original author(s): marcush <marcush@irs.home.com>
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Thu Oct  1 20:47:59 1998
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:52:31 2004
//  Update Count      : 21
//  
//  Copyright (C) 1998 Intelligent Reasoning Systems.
//  
//////////////////////////////////////////////////////////////////////////////

package uos.ai.jam.plan.action.primitives;

import java.util.List;

import uos.ai.jam.Goal;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Value;
import uos.ai.jam.plan.action.PrimitiveAction;

public class Fail implements PrimitiveAction {

	// An action that always fails
	//
	public Value execute(String name, int arity, List<Expression> args, Binding binding, Goal currentGoal) throws AgentRuntimeException {
		System.out.println("In fail.");
		return Value.FALSE;
	}

}

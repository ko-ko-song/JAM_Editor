//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Exec.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/primitives/Exec.java,v $
//  
//  File              : Exec.java
//  Author(s)         : marcush <marcush@irs.home.com>
//  
//  Description       : 
//  
//  Original author(s): marcush <marcush@irs.home.com>
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Fri Oct  2 13:56:52 1998
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:52:31 2004
//  Update Count      : 4
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

public class Exec implements PrimitiveAction {
	//
	// Execute the program and args passed in as a string
	//
	// In:String execString - The program and arguments
	//
	public Value execute(String name, int arity, List<Expression> args, Binding binding, Goal currentGoal) throws AgentRuntimeException {
		if (arity != 1) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + name + "\"\n");
			return Value.FALSE;
		}

		String execString = args.get(0).eval(binding).getString();
		Runtime r = Runtime.getRuntime();
		try {
			r.exec(execString);
		} catch (java.io.IOException ie) {
			System.out.println(ie);
			return Value.FALSE;
		}

		return Value.TRUE;
	}

}

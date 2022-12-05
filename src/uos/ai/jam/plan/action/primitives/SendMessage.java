//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: SendMessage.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/primitives/SendMessage.java,v $
//  
//  File              : SendMessage.java
//  Author(s)         : marcush <marcush@irs.home.com>
//  
//  Description       : 
//  
//  Original author(s): marcush <marcush@irs.home.com>
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Fri Oct  2 14:41:19 1998
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:52:31 2004
//  Update Count      : 7
//  
//  Copyright (C) 1998 Intelligent Reasoning Systems.
//  
//////////////////////////////////////////////////////////////////////////////

package uos.ai.jam.plan.action.primitives;

import java.io.PrintStream;
import java.util.List;

import uos.ai.jam.Goal;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Value;
import uos.ai.jam.plan.action.PrimitiveAction;

public class SendMessage implements PrimitiveAction {

	//
	// Send a message to another agent using a low-level socket
	// interface.
	//
	public Value execute(String name, int arity, List<Expression> args, Binding binding, Goal currentGoal) throws AgentRuntimeException {
		if (arity != 2) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + name + "\"\n");
			return Value.FALSE;
		}

		Expression exp = args.get(0);
		// PrintWriter sOut = (PrintWriter) exp.eval(binding).getObject();
		PrintStream sOut = (PrintStream) exp.eval(binding).getObject();
		exp = args.get(1);
		String message = exp.eval(binding).getString();

		sOut.println(message);

		return Value.TRUE;
	}
}

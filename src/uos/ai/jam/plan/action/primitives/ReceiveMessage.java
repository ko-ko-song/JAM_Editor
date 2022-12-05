//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ReceiveMessage.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/primitives/ReceiveMessage.java,v $
//  
//  File              : ReceiveMessage.java
//  Author(s)         : marcush <marcush@irs.home.com>
//  
//  Description       : 
//  
//  Original author(s): marcush <marcush@irs.home.com>
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Fri Oct  2 14:44:13 1998
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:52:31 2004
//  Update Count      : 8
//  
//  Copyright (C) 1998 Intelligent Reasoning Systems.
//  
//////////////////////////////////////////////////////////////////////////////

package uos.ai.jam.plan.action.primitives;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import uos.ai.jam.Goal;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Value;
import uos.ai.jam.plan.action.PrimitiveAction;

public class ReceiveMessage implements PrimitiveAction {

	//
	// Receive a message from another agent using a low-level socket
	// interface.
	//
	@SuppressWarnings("deprecation")
	public Value execute(String name, int arity, List<Expression> args, Binding binding, Goal currentGoal) throws AgentRuntimeException {
		if (arity != 2) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + name + "\"\n");
			return Value.FALSE;
		}

		Expression exp = args.get(0);
		DataInputStream sIn = (DataInputStream) exp.eval(binding).getObject();
		String message;

		try {
			exp = args.get(1);
			message = sIn.readLine();
			binding.setValue(exp, new Value(message));

			return Value.TRUE;
		} catch (IOException e) {
			System.out
					.println("JAM::ConnectToAgentAsServer:IOException : " + e);
			return Value.FALSE;
		}
	}
}

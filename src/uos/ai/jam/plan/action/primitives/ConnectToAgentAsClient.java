//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ConnectToAgentAsClient.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/primitives/ConnectToAgentAsClient.java,v $
//  
//  File              : ConnectToAgentAsClient.java
//  Author(s)         : marcush <marcush@irs.home.com>
//  
//  Description       : 
//  
//  Original author(s): marcush <marcush@irs.home.com>
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Fri Oct  2 14:20:10 1998
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:52:31 2004
//  Update Count      : 15
//  
//  Copyright (C) 1998 Intelligent Reasoning Systems.
//  
//////////////////////////////////////////////////////////////////////////////

package uos.ai.jam.plan.action.primitives;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

import uos.ai.jam.Goal;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Value;
import uos.ai.jam.plan.action.PrimitiveAction;

public class ConnectToAgentAsClient implements PrimitiveAction {

	//
	// Contact another agent as a client to that agent using a
	// low-level socket interface.
	//
	public Value execute(String name, int arity, List<Expression> args, Binding binding, Goal currentGoal) throws AgentRuntimeException {
		if (arity != 4) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + name + "\"\n");
			return Value.FALSE;
		}

		Expression exp = args.get(0);
		int port = (int) exp.eval(binding).getLong();
		exp = args.get(1);
		String host = exp.eval(binding).getString();

		DataInputStream in;
		// PrintWriter out;
		PrintStream out;
		Socket socket;

		try {
			socket = new Socket(host, port);

			in = new DataInputStream(socket.getInputStream());
			// out = new PrintWriter(socket.getOutputStream());
			out = new PrintStream(socket.getOutputStream());

			exp = args.get(2);
			binding.setValue(exp, new Value(in));
			exp = args.get(3);
			binding.setValue(exp, new Value(out));
			return Value.TRUE;
		} catch (IOException e) {
			System.out.println("JAM::ConnectToAgentAsClient:IOException : " + e);
			return Value.FALSE;
		}
	}
}

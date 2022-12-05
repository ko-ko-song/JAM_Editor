//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ConnectToAgentAsServer.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/primitives/ConnectToAgentAsServer.java,v $
//  
//  File              : ConnectToAgentAsServer.java
//  Author(s)         : marcush <marcush@irs.home.com>
//  
//  Description       : 
//  
//  Original author(s): marcush <marcush@irs.home.com>
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Fri Oct  2 14:34:47 1998
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:52:31 2004
//  Update Count      : 5
//  
//  Copyright (C) 1998 Intelligent Reasoning Systems.
//  
//////////////////////////////////////////////////////////////////////////////

package uos.ai.jam.plan.action.primitives;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import uos.ai.jam.Goal;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Value;
import uos.ai.jam.plan.action.PrimitiveAction;

public class ConnectToAgentAsServer implements PrimitiveAction {

	//
	// Contact another agent as a server to that agent using a
	// low-level socket interface.
	//
	public Value execute(String name, int arity, List<Expression> args, Binding binding, Goal currentGoal) throws AgentRuntimeException {
		if (arity != 3) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + name + "\"\n");
			return Value.FALSE;
		}

		Expression exp = args.get(0);
		int port = (int) exp.eval(binding).getLong();

		DataInputStream in;
		// PrintWriter out;
		PrintStream out;
		Socket socket;
		ServerSocket server;

		try {
			server = new ServerSocket(port);

			socket = server.accept();
			in = new DataInputStream(socket.getInputStream());
			// out = new PrintWriter(socket.getOutputStream());
			out = new PrintStream(socket.getOutputStream());

			exp = args.get(1);
			binding.setValue(exp, new Value(in));
			exp = args.get(2);
			binding.setValue(exp, new Value(out));

			return Value.TRUE;
		} catch (IOException e) {
			System.out.println("JAM::ConnectToAgentAsServer:IOException : " + e);
			return Value.FALSE;
		}
	}
}

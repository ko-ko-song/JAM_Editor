//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: GetHostname.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/primitives/GetHostname.java,v $
//  
//  File              : GetHostname.java
//  Author(s)         : marcush <marcush@irs.home.com>
//  
//  Description       : 
//  
//  Original author(s): marcush <marcush@irs.home.com>
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Fri Oct  2 09:35:24 1998
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:52:31 2004
//  Update Count      : 6
//  
//  Copyright (C) 1998 Intelligent Reasoning Systems.
//  
//////////////////////////////////////////////////////////////////////////////

package uos.ai.jam.plan.action.primitives;

import java.net.InetAddress;
import java.util.List;

import uos.ai.jam.Goal;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Value;
import uos.ai.jam.plan.action.PrimitiveAction;

public class GetHostname implements PrimitiveAction {

	public Value execute(String name, int arity, List<Expression> args, Binding binding, Goal currentGoal) throws AgentRuntimeException {

		if (arity != 1) {
			System.out.println("Invalid number of arguments: " + arity
					+ " to function \"" + name + "\"\n");
			return Value.FALSE;
		}

		Expression hostname = args.get(0);

		String localhost;

		try {
			localhost = InetAddress.getLocalHost().getHostName();
		} catch (java.net.UnknownHostException he) {
			localhost = "localhost";
		}
		binding.setValue(hostname, new Value(localhost));

		return Value.TRUE;
	}
}

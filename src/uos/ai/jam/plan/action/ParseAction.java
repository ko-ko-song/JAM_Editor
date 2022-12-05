//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ParseAction.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/ParseAction.java,v $
//  
//  File              : ParseAction.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:46 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:01 2004
//  Update Count      : 20
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

package uos.ai.jam.plan.action;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import uos.ai.jam.Goal;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;

/**
 * 
 * A built-in JAM primitive action for parsing strings with the JAM parser.
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class ParseAction extends Action implements Serializable {
	private static final long serialVersionUID = -7108723601473825154L;

	//
	// Members
	//
	private final List<Expression> 			_args;

	//
	// Constructors
	//

	public ParseAction(List<Expression> el) {
		super("PARSE");
		_args 		= el;
		_actType 	= ACT_PARSE;
	}

	//
	// Member functions
	//
	public boolean isExecutableAction() {
		return true;
	}

	// public int getType() { return ACT_PARSE; }

	// !!!!!NEED TO IMPLEMENT THIS!!!!!
	/**
	 * 
	 * 
	 */
	public int execute(Binding b, Goal currentGoal) throws AgentRuntimeException {
		return ACT_SUCCEEDED;
	}

	/**
	 * 
	 * 
	 */
	public void format(PrintStream s, Binding b) {
		s.println("PARSE: ");
		Iterator<Expression> iter = _args.iterator();
		Expression exp = null;
		while(iter.hasNext()) {
			exp = iter.next();
			
			try {
				exp.eval(b).format(s, b);
				if (iter.hasNext()) {
					s.print(" ");
				}
			} catch (AgentRuntimeException e) {}
		}
	}

}

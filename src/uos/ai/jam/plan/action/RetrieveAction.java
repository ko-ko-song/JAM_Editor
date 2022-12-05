//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: RetrieveAction.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/RetrieveAction.java,v $
//  
//  File              : RetrieveAction.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:19:36 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:53:58 2004
//  Update Count      : 26
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

import java.io.*;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Relation;

/**
 * 
 * A built-in JAM primitive action for binding plan variables with world model
 * entries. This is similar to a FactAction, but passed-in variables bindings
 * are NOT used for matching. Rather, the variables are overwritten with data
 * from the world model whenever any world model entry exists with the given
 * name.
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class RetrieveAction extends WorldModelAction implements Serializable {
	private static final long serialVersionUID = 391625512023757596L;
	
	//
	// Constructors
	//

	/**
	 * Constructor w/ relation to retrieve from the World Model as an argument
	 * in addition to the interpreter.
	 * 
	 */
	public RetrieveAction(Relation r, Interpreter interpreter) {
		super(r, interpreter);
		_actType = ACT_RETRIEVE;
	}

	//
	// Member functions
	//
	public boolean isExecutableAction() {
		return true;
	}

	/**
	 * Retrieve the relation from the World Model.
	 * 
	 */
	public int execute(Binding b, Goal currentGoal) throws AgentRuntimeException {
		// unbound variables before matching
		if (b != null) {
			b.unbindVariables(_relation);
		}

		// now try match to get new values
		return getInterpreter().getWorldModel().match(_relation, b) ? ACT_SUCCEEDED
				: ACT_FAILED;
	}

	/**
	 * Output information to the stream in an in-line manner.
	 * 
	 */
	public void format(PrintStream s, Binding b) {
		s.print("RETRIEVE ");
		_relation.format(s, b);
		s.print(";");
	}

}

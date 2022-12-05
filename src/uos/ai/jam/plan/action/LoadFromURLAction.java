//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: LoadAction.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/LoadAction.java,v $
//  
//  File              : LoadAction.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:50 1997
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.JAM;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.parser.JAMParser;

/**
 * 
 * A built-in JAM primitive action for loading files for parsing by the JAM
 * parser.
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class LoadFromURLAction extends Action implements Serializable {
	private static final long serialVersionUID = -37091340495439090L;

	//
	// Members
	//
	private final List<Expression> 		_args;
	private Interpreter					_interpreter;
	//
	// Constructors
	//

	public LoadFromURLAction(List<Expression> el) {
		super("LOAD");
		_args 		= el;
		_actType 	= ACT_LOAD;
	}
	
	public LoadFromURLAction(List<Expression> el, Interpreter i){
		this(el);
		_interpreter = i;
	}

	//
	// Member functions
	//
	public boolean isExecutableAction() {
		return true;
	}

	// public int getType() { return ACT_LOAD; }
	public int execute(Binding b, Goal currentGoal) throws AgentRuntimeException {
		String path = _args.get(0).eval(b).getString();
		URLConnection connection = null;
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(path);
			System.out.println(url.toString());
			connection = url.openConnection();
			BufferedReader input = null;
			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while((line = input.readLine())!=null){
				sb.append(line);
			}
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JAM.parseString(_interpreter, sb.toString());
		return ACT_SUCCEEDED;
	}
	

	public void format(PrintStream s, Binding b) {
		s.println("LOAD: ");
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

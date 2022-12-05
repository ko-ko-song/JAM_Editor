//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Symbol.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/Symbol.java,v $
//  
//  File              : Symbol.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:19:30 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:53:58 2004
//  Update Count      : 23
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

package uos.ai.jam.expression;

import java.io.Serializable;

import uos.ai.jam.NameSpace;


/**
 * 
 * Represents a symbolic concept within a plan
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class Symbol implements Serializable {
	private static final long serialVersionUID = -4531958911412308359L;

	//
	// Members
	//

	private final String		_name;
	private final NameSpace		_name_space;
	
	//
	// Constructors
	//

	/**
	 * Constructor based on a label and identifier
	 * 
	 */
	public Symbol(String name) {
		_name = name;
		_name_space = null;
	}
	public Symbol(String name, NameSpace ns){
		_name = name;
		_name_space = ns;
	}

	//
	// Member functions
	//

	public String getName() {
		if(_name_space==null){
			return _name;
		}else {
			return _name_space.wrap(_name);
		}
	}
	
	public String toString() {
		return _name;
	}
}

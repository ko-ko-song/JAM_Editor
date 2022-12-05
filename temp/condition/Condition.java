//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Condition.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/Condition.java,v $
//  
//  File              : Condition.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:22:32 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:02 2004
//  Update Count      : 17
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

package kr.ac.uos.ai.editor.jam.condition;

import java.io.Serializable;
import java.util.List;

import kr.ac.uos.ai.editor.jam.expression.Binding;



/**
 * 
 * A boolean-evaluable object
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public abstract class Condition implements Serializable {
	private static final long serialVersionUID = -5205907773634886388L;

	public final static int COND_GOAL 			= 1;
	public final static int COND_EXP 			= 2;
	public final static int COND_FACT 			= 3;
	public final static int COND_RETRIEVE 		= 4;

	//
	// Members
	//
	protected int 				_activeValue;

	//
	// Constructors
	//

	/**
	 * 
	 * 
	 */
	public Condition() {
		_activeValue = 1;
	}

	//
	// Member functions
	//

	/**
	 * 
	 * 
	 */
	public Condition setPositive() {
		_activeValue = 1;
		return this;
	}

	/**
	 * 
	 * 
	 */
	public Condition setNegative() {
		_activeValue = 0;
		return this;
	}

	public abstract String getName();
	public abstract int getType();
	public abstract boolean check(List<Binding> bl);
	public abstract boolean confirm(Binding b);
}

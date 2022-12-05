//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: BindingValue.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/BindingValue.java,v $
//  
//  File              : BindingValue.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:22:34 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:02 2004
//  Update Count      : 25
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

package kr.ac.uos.ai.editor.jam.expression;

import java.io.*;


/**
 * 
 * Represents a particular variable binding
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class BindingValue implements Serializable {
	private static final long serialVersionUID = 8798629945438078305L;
	
	//
	// Members
	//
	
	private Value 					_value;				// Binding value can be either a constant value
	private Variable 				_externalVariable;	// or a variable defined in other external binding
	private Binding 				_externalBinding;

	//
	// Constructors
	//

	/**
	 * 
	 * 
	 */
	public BindingValue() {
		_value				= new Value();
		_externalVariable 	= null;
		_externalBinding 	= null;
	}

	/**
	 * 
	 * 
	 */
	public BindingValue(Value val) {
		_value 				= val;
		_externalVariable 	= null;
		_externalBinding 	= null;
	}

	/**
	 * Copy constructor
	 * 
	 */
	public BindingValue(BindingValue bval) {
		_value 				= new Value(bval.getValue());
		_externalVariable 	= bval.getExternalVariable();
		_externalBinding 	= bval.getExternalBinding();
	}

	//
	// Member functions
	//

	// public boolean isNewWMBinding() { return true; }
	public Value getValue() {
		return _value;
	}

	public Value setValue(Value v) {
		return _value = v;
	}

	public Variable getExternalVariable() {
		return _externalVariable;
	}

	public Variable setExternalVariable(Variable v) {
		return _externalVariable = v;
	}

	public Binding getExternalBinding() {
		return _externalBinding;
	}

	public Binding setExternalBinding(Binding b) {
		return _externalBinding = b;
	}
	
	public boolean isLocalBinding() {
		return (_externalVariable == null);
	}

}

//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ValueObject.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/ValueObject.java,v $
//  
//  File              : ValueObject.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:19:03 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:52:32 2004
//  Update Count      : 12
//  
//  Copyright (C) 1998 Marcus J. Huber.
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1998 Marcus J. Huber.
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
//  Marcus J. Huber shall not be liable for any damages,
//  including special, indirect, incidental, or consequential damages,
//  with respect to any claim arising out of or in connection with the
//  use of the software, even if they have been or are hereafter advised
//  of the possibility of such damages.
// 
//////////////////////////////////////////////////////////////////////////////

package kr.ac.uos.ai.editor.jam.expression;

import java.io.PrintStream;
import java.io.Serializable;


/**
 * 
 * Represents a built-in JAM Java Object data-type
 * 
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class ValueObject extends Value implements Serializable {
	private static final long serialVersionUID = 1489337166734199769L;

	//
	// Members
	//
	protected final Object 					_value;

	//
	// Constructors
	//
	public ValueObject(ValueObject v) {
		_value = v._value;
	}

	public ValueObject(Object o) {
		_value = o;
	}

	//
	// Member functions
	//

	public String getName() {
		return "ValueObject";
	}

	public ValueType type() {
		return ValueType.OBJECT;
	}

	public boolean isTrue() {
		return _value != null;
	}

	public long getLong() {
		return (long) 0;
	}

	public double getReal() {
		return (double) 0.0;
	}

	public String getString() {
		return (_value.toString());
	}

	public Object getObject() {
		return _value;
	}

	public String toString() {
		return getString();
	}

	//

	public Value neg() {
		return new Value(_value);
	}

	public Value add(Value v) {
		return v.strAdd(new ValueString(_value.toString()));
	}

	/*
	public Value add(Value v) {
		return v;
	}
	*/

	public Value sub(Value v) {
		return v;
	}

	public Value mul(Value v) {
		return v;
	}

	public Value div(Value v) {
		return v;
	}

	public Value mod(Value v) {
		return v;
	}

	public boolean not() {
		return !isTrue();
	}

	public boolean lt(Value v) {
		return false;
	}

	public boolean gt(Value v) {
		return false;
	}

	public boolean le(Value v) {
		return false;
	}

	public boolean ge(Value v) {
		return false;
	}

	public boolean eq(Value v) {
		return v.objEq(this);
	}

	public boolean ne(Value v) {
		return v.objNe(this);
	}

	//

	public void print(PrintStream s, Binding b) {
		s.print(_value);
	}

	public void format(PrintStream s, Binding b) {
		print(s, b);
	}

	//
	// Protected Member functions
	// 

	protected Value strAdd(Value v) {
		return new Value(((ValueString) v)._value + _value);
	}

	protected boolean objEq(Value v) {
		return ((ValueObject) v)._value == _value;
	}

	protected boolean objNe(Value v) {
		return ((ValueObject) v)._value != _value;
	}

}

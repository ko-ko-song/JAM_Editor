//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ValueString.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/ValueString.java,v $
//  
//  File              : ValueString.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:19:01 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:53:58 2004
//  Update Count      : 24
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

import java.io.PrintStream;
import java.io.Serializable;


/**
 * 
 * Represents a built-in JAM String data-type
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class ValueString extends Value implements Serializable {
	private static final long serialVersionUID = -6165086282806336042L;

	//
	// Members
	//
	protected final String 						_value;

	//
	// Constructors
	//
	public ValueString(ValueString v) {
		_value = v._value;
	}

	public ValueString(String s) {
		_value = s;
	}

	//
	// Member functions
	//

	public String getName() {
		return "ValueString";
	}

	public ValueType type() {
		return ValueType.STRING;
	}

	public boolean isTrue() {
		return _value.length() > 0;
	}

	public long getLong() {
		return 0;
	}

	public double getReal() {
		return 0.0;
	}

	public String getString() {
		return _value;
	}

	public Object getObject() {
		return _value;
	}

	public String toString() {
		return "\"" + _value + "\"";
	}

	//

	// +++++ need to define ++++
	public Value neg() {
		return new Value(_value);
	}

	public Value add(Value v) {
		return v.strAdd(this);
	}

	public Value sub(Value v) {
		return v.strSub(this);
	}

	public Value mul(Value v) {
		return v.strMul(this);
	}

	public Value div(Value v) {
		return v.strDiv(this);
	}

	public Value mod(Value v) {
		return v.strMod(this);
	}

	public boolean not() {
		return !isTrue();
	}

	public boolean lt(Value v) {
		return v.strLt(this);
	}

	public boolean gt(Value v) {
		return v.strGt(this);
	}

	public boolean le(Value v) {
		return v.strLe(this);
	}

	public boolean ge(Value v) {
		return v.strGe(this);
	}

	public boolean eq(Value v) {
		return v.strEq(this);
	}

	public boolean ne(Value v) {
		return v.strNe(this);
	}

	//

	public void print(PrintStream s, Binding b) {
		// s.print("\"" + _value + "\"");
		s.print(_value);
	}

	public void format(PrintStream s, Binding b) {
		print(s, b);
	}

	//
	// Protected Member functions
	// 
	protected Value longAdd(Value v) {
		return new Value(((ValueLong) v).toString() + _value);
	}

	protected Value realAdd(Value v) {
		return new Value(((ValueReal) v).toString() + _value);
	}

	protected Value strAdd(Value v) {
		return new Value(((ValueString) v)._value + _value);
	}

	protected boolean longEq(Value v) {
		return false;
	}

	protected boolean longNe(Value v) {
		return true;
	}

	protected boolean realEq(Value v) {
		return false;
	}

	protected boolean realNe(Value v) {
		return true;
	}

	protected boolean strEq(Value v) {
		return _value.equals(((ValueString) v)._value);
	}

	protected boolean strNe(Value v) {
		return !_value.equals(((ValueString) v)._value);
	}

	protected boolean strLt(Value v) {
		return ((ValueString) v)._value.compareTo(_value) < 0;
	}

	protected boolean strLe(Value v) {
		return ((ValueString) v)._value.compareTo(_value) <= 0;
	}

	protected boolean strGt(Value v) {
		return ((ValueString) v)._value.compareTo(_value) > 0;
	}

	protected boolean strGe(Value v) {
		return ((ValueString) v)._value.compareTo(_value) >= 0;
	}
}

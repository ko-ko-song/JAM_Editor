//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ValueReal.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/ValueReal.java,v $
//  
//  File              : ValueReal.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:19:03 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:53:58 2004
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

package kr.ac.uos.ai.editor.jam.expression;

import java.io.PrintStream;
import java.io.Serializable;


/**
 * 
 * Represents a built-in JAM Real data-type
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class ValueReal extends Value implements Serializable {
	private static final long serialVersionUID = 2371485378317209523L;

	//
	// Members
	//
	protected final double 					_value;

	//
	// Constructors
	//
	public ValueReal(ValueReal v) {
		_value = v._value;
	}

	public ValueReal(double d) {
		_value = d;
	}

	public ValueReal(float f) {
		_value = f;
	}

	//
	// Member functions
	//

	public String getName() {
		return "ValueReal";
	}

	public ValueType type() {
		return ValueType.REAL;
	}

	public boolean isTrue() {
		return _value != 0.0;
	}

	public long getLong() {
		return (long) _value;
	}

	public double getReal() {
		return _value;
	}

	public String getString() {
		return (new Double(_value)).toString();
	}

	public Object getObject() {
		return new Double(_value);
	}

	public String toString() {
		return getString();
	}

	//

	public Value neg() {
		return new Value(-_value);
	}

	public Value add(Value v) {
		return v.realAdd(this);
	}

	public Value sub(Value v) {
		return v.realSub(this);
	}

	public Value mul(Value v) {
		return v.realMul(this);
	}

	public Value div(Value v) {
		return v.realDiv(this);
	}

	public Value mod(Value v) {
		return v.realMod(this);
	}

	public boolean not() {
		return !isTrue();
	}

	public boolean lt(Value v) {
		return v.realLt(this);
	}

	public boolean gt(Value v) {
		return v.realGt(this);
	}

	public boolean le(Value v) {
		return v.realLe(this);
	}

	public boolean ge(Value v) {
		return v.realGe(this);
	}

	public boolean eq(Value v) {
		return v.realEq(this);
	}

	public boolean ne(Value v) {
		return v.realNe(this);
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
	protected Value longAdd(Value v) {
		return new Value(((ValueLong) v)._value + _value);
	}

	protected Value longSub(Value v) {
		return new Value(((ValueLong) v)._value - _value);
	}

	protected Value longMul(Value v) {
		return new Value(((ValueLong) v)._value * _value);
	}

	protected Value longDiv(Value v) {
		return new Value(((ValueLong) v)._value / _value);
	}

	protected Value longMod(Value v) {
		return new Value(((ValueLong) v)._value % _value);
	}

	protected Value realAdd(Value v) {
		return new Value(((ValueReal) v)._value + _value);
	}

	protected Value realSub(Value v) {
		return new Value(((ValueReal) v)._value - _value);
	}

	protected Value realMul(Value v) {
		return new Value(((ValueReal) v)._value * _value);
	}

	protected Value realDiv(Value v) {
		return new Value(((ValueReal) v)._value / _value);
	}

	protected Value strAdd(Value v) {
		return new Value(((ValueString) v)._value + _value);
	}

	protected boolean longEq(Value v) {
		return ((ValueLong) v)._value == _value;
	}

	protected boolean longNe(Value v) {
		return ((ValueLong) v)._value != _value;
	}

	protected boolean longLt(Value v) {
		return ((ValueLong) v)._value < _value;
	}

	protected boolean longLe(Value v) {
		return ((ValueLong) v)._value <= _value;
	}

	protected boolean longGt(Value v) {
		return ((ValueLong) v)._value > _value;
	}

	protected boolean longGe(Value v) {
		return ((ValueLong) v)._value >= _value;
	}

	protected boolean realEq(Value v) {
		return ((ValueReal) v)._value == _value;
	}

	protected boolean realNe(Value v) {
		return ((ValueReal) v)._value != _value;
	}

	protected boolean realLt(Value v) {
		return ((ValueReal) v)._value < _value;
	}

	protected boolean realLe(Value v) {
		return ((ValueReal) v)._value <= _value;
	}

	protected boolean realGt(Value v) {
		return ((ValueReal) v)._value > _value;
	}

	protected boolean realGe(Value v) {
		return ((ValueReal) v)._value >= _value;
	}

	protected boolean strEq(Value v) {
		return false;
	}

	protected boolean strNe(Value v) {
		return true;
	}

}

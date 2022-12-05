//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ValueLong.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/ValueLong.java,v $
//  
//  File              : ValueLong.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:19:06 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:53:58 2004
//  Update Count      : 21
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
 * Represents a built-in JAM Long data-type
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class ValueLong extends Value implements Serializable {
	private static final long serialVersionUID = -2865154352503133563L;

	//
	// Members
	//
	protected final long 			_value;

	//
	// Constructors
	//
	public ValueLong(ValueLong v) {
		_value = v._value;
	}

	public ValueLong(long i) {
		_value = i;
	}

	//
	// Member functions
	//

	public String getName() {
		return "ValueLong";
	}

	public ValueType type() {
		return ValueType.LONG;
	}

	public boolean isTrue() {
		return _value != 0;
	}

	public long getLong() {
		return _value;
	}

	public double getReal() {
		return (double) _value;
	}

	public String getString() {
		return (new Long(_value)).toString();
	}

	public Object getObject() {
		return new Long(_value);
	}

	public String toString() {
		return getString();
	}

	//

	public Value neg() {
		return new Value(-_value);
	}

	public Value add(Value v) {
		return v.longAdd(this);
	}

	public Value sub(Value v) {
		return v.longSub(this);
	}

	public Value mul(Value v) {
		return v.longMul(this);
	}

	public Value div(Value v) {
		return v.longDiv(this);
	}

	public Value mod(Value v) {
		return v.longMod(this);
	}

	public boolean not() {
		return !isTrue();
	}

	public boolean lt(Value v) {
		return v.longLt(this);
	}

	public boolean gt(Value v) {
		return v.longGt(this);
	}

	public boolean le(Value v) {
		return v.longLe(this);
	}

	public boolean ge(Value v) {
		return v.longGe(this);
	}

	public boolean eq(Value v) {
		return v.longEq(this);
	}

	public boolean ne(Value v) {
		return v.longNe(this);
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

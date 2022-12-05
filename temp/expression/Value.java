//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Value.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/Value.java,v $
//  
//  File              : Value.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:19:08 1997
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

package kr.ac.uos.ai.editor.jam.expression;

import java.io.PrintStream;
import java.io.Serializable;

import kr.ac.uos.ai.editor.jam.exception.AgentRuntimeException;


/**
 * 
 * Represents a built-in JAM data-type
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class Value extends Expression implements Serializable {
	private static final long serialVersionUID = 3243092257121122593L;

	//
	// Members
	//

	public final static Value UNDEFINED 			= new Value();
	public final static Value TRUE 					= new Value(1);
	public final static Value FALSE 				= new Value(0);
	
	private final Value 			_rep;

	//
	// Constructors
	//
	public Value() {
		_rep = null;
	}

	public Value(long i) {
		_rep = new ValueLong(i);
	}

	public Value(int l) {
		_rep = new ValueLong((long) l);
	}

	public Value(double d) {
		_rep = new ValueReal(d);
	}

	public Value(String s) {
		_rep = new ValueString(s);
	}

	public Value(Object o) {
		_rep = new ValueObject(o);
	}

	public Value(Value v) {
		_rep = v._rep;
	}

	//
	// Required Member functions for Expression
	//
	public String getName() {
		return "Value";
	}

	public ExpressionType getType() {
		return ExpressionType.VALUE;
	}

	public Value eval(Binding b) throws AgentRuntimeException {
		return this;
	}

	//
	// Member functions
	//
	public ValueType type() {
		return (_rep == null) ? ValueType.VOID : _rep.type();
	}

	public boolean isTrue() {
		return (_rep == null) ? false : _rep.isTrue();
	}

	public boolean isVariable() {
		return false;
	}

	public boolean isDefined() {
		return _rep != null;
	}

	public long getLong() {
		return _rep.getLong();
	}

	public double getReal() {
		return _rep.getReal();
	}

	public String getString() {
		return _rep.getString();
	}

	public Object getObject() {
		return _rep.getObject();
	}

	public String toString() {
		return (_rep == null) ? "UNDEFINED" : _rep.toString();
	}

	//

	public Value neg() {
		return _rep.neg();
	}

	public Value add(Value v) {
		return _rep.add(v);
	}

	public Value sub(Value v) {
		return _rep.sub(v);
	}

	public Value mul(Value v) {
		return _rep.mul(v);
	}

	public Value div(Value v) {
		return _rep.div(v);
	}

	public Value mod(Value v) {
		return _rep.mod(v);
	}

	public boolean not() {
		return _rep.not();
	}

	public boolean lt(Value v) {
		return _rep.lt(v);
	}

	public boolean gt(Value v) {
		return _rep.gt(v);
	}

	public boolean le(Value v) {
		return _rep.le(v);
	}

	public boolean ge(Value v) {
		return _rep.ge(v);
	}

	public boolean eq(Value v) {
		return _rep.eq(v);
	}

	public boolean ne(Value v) {
		return _rep.ne(v);
	}

	//

	public void print(PrintStream s, Binding b) {
		if (_rep == null) {
			s.print("*Undefined Value*");
		} else {
			_rep.print(s, b);
		}
	}

	public void format(PrintStream s, Binding b) {
		if (_rep == null) {
			s.print("null");
		} else {
			_rep.format(s, b);
		}
	}

	//
	// Protected Member functions
	//
	protected Value longAdd(Value v) {
		return _rep.longAdd(v);
	}

	protected Value longSub(Value v) {
		return _rep.longSub(v);
	}

	protected Value longMul(Value v) {
		return _rep.longMul(v);
	}

	protected Value longDiv(Value v) {
		return _rep.longDiv(v);
	}

	protected Value longMod(Value v) {
		return _rep.longMod(v);
	}

	protected Value realAdd(Value v) {
		return _rep.realAdd(v);
	}

	protected Value realSub(Value v) {
		return _rep.realSub(v);
	}

	protected Value realMul(Value v) {
		return _rep.realMul(v);
	}

	protected Value realDiv(Value v) {
		return _rep.realDiv(v);
	}

	protected Value realMod(Value v) {
		return _rep.realMod(v);
	}

	protected Value strAdd(Value v) {
		return _rep.strAdd(v);
	}

	protected Value strSub(Value v) {
		return _rep.strSub(v);
	}

	protected Value strMul(Value v) {
		return _rep.strMul(v);
	}

	protected Value strDiv(Value v) {
		return _rep.strDiv(v);
	}

	protected Value strMod(Value v) {
		return _rep.strMod(v);
	}

	protected boolean longEq(Value v) {
		return _rep.longEq(v);
	}

	protected boolean longNe(Value v) {
		return _rep.longNe(v);
	}

	protected boolean longLt(Value v) {
		return _rep.longLt(v);
	}

	protected boolean longLe(Value v) {
		return _rep.longLe(v);
	}

	protected boolean longGt(Value v) {
		return _rep.longGt(v);
	}

	protected boolean longGe(Value v) {
		return _rep.longGe(v);
	}

	protected boolean realEq(Value v) {
		return _rep.realEq(v);
	}

	protected boolean realNe(Value v) {
		return _rep.realNe(v);
	}

	protected boolean realLt(Value v) {
		return _rep.realLt(v);
	}

	protected boolean realLe(Value v) {
		return _rep.realLe(v);
	}

	protected boolean realGt(Value v) {
		return _rep.realGt(v);
	}

	protected boolean realGe(Value v) {
		return _rep.realGe(v);
	}

	protected boolean strLt(Value v) {
		return _rep.strLt(v);
	}

	protected boolean strLe(Value v) {
		return _rep.strLe(v);
	}

	protected boolean strGt(Value v) {
		return _rep.strGt(v);
	}

	protected boolean strGe(Value v) {
		return _rep.strGe(v);
	}

	protected boolean strEq(Value v) {
		return _rep.strEq(v);
	}

	protected boolean strNe(Value v) {
		return _rep.strNe(v);
	}

	protected boolean objEq(Value v) {
		return _rep.objEq(v);
	}

	protected boolean objNe(Value v) {
		return _rep.objNe(v);
	}
}

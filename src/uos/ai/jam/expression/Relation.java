//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Relation.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/Relation.java,v $
//  
//  File              : Relation.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:19:42 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:53:59 2004
//  Update Count      : 88
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
import java.util.List;

import uos.ai.jam.NameSpace;
import uos.ai.jam.exception.AgentRuntimeException;

/**
 * 
 * Represents an <attribute> <value_list> pair
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class Relation implements Serializable {
	private static final long serialVersionUID = -6294558332820391781L;

	//
	// Members
	//
	
	private final Symbol				_symbol;
	private final Expression[]			_args;

	//
	// Constructors
	//

	
	private String						_fileName;
	private int							_line;
	private int							_beginColumn;


	public Relation(Symbol symbol, Expression... expressions) {
		_symbol 	= symbol;
		_args		= (expressions != null) ? expressions.clone() : EMPTY_EXPRESSION;
	}
	

	private static final Expression[] EMPTY_EXPRESSION = new Expression[0];
	public Relation(Symbol symbol, List<Expression> expList) {
		_symbol		= symbol;
		_args		= (expList == null) ? EMPTY_EXPRESSION : expList.toArray(EMPTY_EXPRESSION);
	}

	public Relation(Relation relation, Binding binding) throws AgentRuntimeException {
		Expression[] expressions = relation._args;
		int arity = expressions.length;
		Expression[] newExpressions = new Expression[arity];
		for (int i=0; i<arity; i++) {
			newExpressions[i] = expressions[i].eval(binding);
		}
		_symbol		= relation._symbol;
		_args		= newExpressions;
	}

	
	//
	// Member functions
	//
	
	public Symbol getSymbol() {
		return _symbol;
	}
	
	public Expression getArg(int index) {
		return _args[index];
	}
	
	public int getArity() {
		return _args.length;
	}

	/**
	 * Return the relation's string label
	 * 
	 */
	public String getName() {
		return _symbol.getName();
	}

	/**
	 * Format the output and don't worry about being printed out in-line with
	 * other information.
	 * 
	 */
	public void print(PrintStream s, Binding b) {
		s.print(getName() + " ");
		try {
			for (Expression exp : _args) {
				s.print(exp.eval(b));
				s.print(" ");
			}
		} catch (AgentRuntimeException e) {}
		s.println();
	}
	
	public String getPrintData() {
		String data = getName();
		try {
			for (Expression exp : _args) {
				data += " " + exp.eval(null);
			}
		} catch (AgentRuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	/**
	 * Format the output so that it's conducive to being printed out in-line
	 * with other information.
	 * 
	 */
	public void format(PrintStream s, Binding b) {
		s.print(getName() + " ");
		for (int i=0, n=getArity(); i<n; i++) {
			_args[i].format(s, b);
			if (i+1 < n) {
				s.print(" ");
			}
		}

		if (b != null) {
			b.format(s);
		}
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(_symbol);
		for (Expression arg : _args) {
			builder.append(" ").append(arg);
		}
		return builder.toString();
	}
	
	/**
	 * If the source & the destination relations do not match, return false.
	 * Otherwise change the destination binding with linked variables to the
	 * source relation binding and return true.
	 */
	public boolean unify(Relation dstRelation, Binding dstBinding,
			Relation srcRelation, Binding srcBinding) throws AgentRuntimeException {
		if (dstRelation == null || srcRelation == null || srcRelation.getSymbol() != dstRelation.getSymbol()) {
			return false;
		}

		if (srcRelation.getArity() <= 0 && dstRelation.getArity() <= 0) {
			return true;
		}

		Expression[] srcArgs = srcRelation._args;
		Expression[] dstArgs = dstRelation._args;
		Expression srcArg;
		Expression dstArg;

		Binding dstBindingCopy = new Binding(dstBinding);

		for (int i=0, n=Math.min(srcRelation.getArity(), dstRelation.getArity()); i<n; i++) {
			srcArg = srcArgs[i];
			dstArg = dstArgs[i];

			Value srcVal = srcArg.eval(srcBinding);
			Value dstVal = dstArg.eval(dstBinding);

			if (srcVal.isDefined() && dstVal.isDefined())
				if (srcVal.eq(dstVal)) {
					continue;
				} else {
					return false;
				}

			if (dstArg.isVariable()) {
				if (srcArg.isVariable()) {
					dstBindingCopy.linkVariables(dstArg, srcArg, srcBinding);
				} else {
					dstBindingCopy.setValue(dstArg, srcVal);
				}
			} else {
				if (!srcVal.eq(dstVal)) {
					return false;
				}
			}
		}
		
		if (dstBinding != null) {
			dstBinding.copy(dstBindingCopy);
		}

		return true;
	}

	//
	// Won't need these once the symbol table's been moved to
	// the Interpreter
	//

	/**
	 * Save the object to an output stream
	 * 
	 */
	/*
	 * private void writeObject(ObjectOutputStream out) throws IOException,
	 * ClassNotFoundException { out.defaultWriteObject(); SymbolTable st =
	 * _interpreter.getRelationTable(); out.writeObject(st); }
	 */
	/**
	 * Read the object from an input stream
	 * 
	 */
	/*
	 * private void readObject(ObjectInputStream in) throws IOException,
	 * ClassNotFoundException { in.defaultReadObject(); SymbolTable st =
	 * (SymbolTable) in.readObject(); _relationTable = st; }
	 */
	
	
	public String get_fileName() {
		return _fileName;
	}

	public void set_fileName(String _fileName) {
		this._fileName = _fileName;
	}

	public int get_line() {
		return _line;
	}

	public void set_line(int _line) {
		this._line = _line;
	}

	public int get_beginColumn() {
		return _beginColumn;
	}

	public void set_beginColumn(int _beginColumn) {
		this._beginColumn = _beginColumn;
	}
	
}

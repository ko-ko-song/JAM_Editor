//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Binding.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/Binding.java,v $
//  
//  File              : Binding.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:22:38 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:02 2004
//  Update Count      : 32
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import kr.ac.uos.ai.editor.jam.model.Relation;


//import java.util.*;

/**
 * 
 * Represents a plan's variable bindings
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class Binding implements Serializable {
	private static final long serialVersionUID = 4682257098794881532L;

	//
	// Members
	//
	
	private Map<Symbol, BindingValue> 		_map;
	private boolean 						_newWMBinding;		// true if the binding is done with at least one new WM entry
	
	//
	// Constructors
	//

	/**
	 * Constructor based on an externally-supplied symbol table
	 * 
	 */
	public Binding() {
		_map			= new HashMap<Symbol, BindingValue>();
		_newWMBinding 	= false;
	}

	/**
	 * Copy constructor
	 * 
	 */
	public Binding(Binding b) {
		_map			= new HashMap<Symbol, BindingValue>();
		_newWMBinding	= false;
		if (b != null) {
			_newWMBinding = b._newWMBinding;
			for (Map.Entry<Symbol, BindingValue> entry : b._map.entrySet()) {
				_map.put(entry.getKey(), new BindingValue(entry.getValue()));
			}
		} 
	}

	//
	// Member functions
	//

	/**
	 * Assignment operator
	 * 
	 */
	public void copy(Binding b) {
		_map			= b._map;
		_newWMBinding 	= b._newWMBinding;
	}

	/**
	 * Restore variables in the expression to an undefined state
	 * 
	 */
	public void unbindVariable(Expression expression) {
		if (expression.isVariable()) {
			setValue(expression, Value.UNDEFINED);
		}
	}
	
	public void unbindVariables(Relation relation) {
//		for (int i=0, n=relation.getArity(); i<n; i++) {
//			unbindVariable(relation.getArg(i));
//		}
	}

	/**
	 * Tie this binding with an external variable binding
	 * 
	 */
	public void linkVariables(Expression var, Expression extVariable, Binding extBinding) {
		Symbol symbol = var.getVariable().getSymbol();
		BindingValue bval = _map.get(symbol);
		if (bval == null) {
			bval = new BindingValue();
			_map.put(symbol, bval);
		}
		bval.setExternalVariable((Variable) extVariable.getVariable());
		bval.setExternalBinding(extBinding);
	}

	/**
	 * See if binding is based solely on local values
	 * 
	 */
	public boolean isLocalBinding(Expression var) {
		return (!var.isVariable() || isLocalBinding(var.getVariable().getSymbol()));
	}

	/**
	 * See if binding is based solely on local values
	 * 
	 */
	private boolean isLocalBinding(Symbol symbol) {
		BindingValue bval = _map.get(symbol);
		return (bval != null) ? bval.isLocalBinding() : true;
	}

	/**
	 * Set the internal value of the variable
	 * 
	 */
	public void setValue(Expression var, Value val) {
		if (!var.isVariable()) {
			System.out.println("\nArgument should be variable!");
			return;
		}
		setValue(var.getVariable().getSymbol(), val);
	}

	private void setValue(Symbol symbol, Value val) {
		BindingValue bval = _map.get(symbol);
		if (bval == null) {
			bval = new BindingValue();
			_map.put(symbol, bval);
		}
		if (bval.isLocalBinding()) {
			bval.setValue(val);
		} else {
			bval.getExternalBinding().setValue(bval.getExternalVariable(), val);
		}
	}
	
	/**
	 * Get the variable's value by looking up the internal ID
	 * 
	 */
	public Value getValue(Expression var) {
		return getValue(var.getVariable().getSymbol());
	}

	private Value getValue(Symbol symbol) {
		BindingValue bval = _map.get(symbol);
		if (bval == null) {
			return Value.UNDEFINED;
		}
		return bval.isLocalBinding() ? bval.getValue() : bval.getExternalBinding().getValue(bval.getExternalVariable());
	}

	public Variable[] getVariables() {
		Set<Symbol> symbols = _map.keySet();
		int size = symbols.size();
		int index = 0;
		Variable[] variables = new Variable[size];
		for (Symbol symbol : symbols) {
			variables[index] = new Variable(symbol);
			index++;
		}
		return variables;
	}
	
	/**
	 * See if there are any references based upon a newly changed world model
	 * entry
	 * 
	 */
	public boolean isNewWMBinding() {
		return _newWMBinding;
	}

	/**
	 * Check, and possibly alter the flag indicating a reference to a newly
	 * changed world model entry
	 * 
	 */
	public boolean checkNewWMBinding(boolean newWM) {
		return _newWMBinding = _newWMBinding || newWM;
	}

	/**
	 * Indicate that there are no references to newly changed world model
	 * entries
	 * 
	 */
	public void clearNewWMBinding() {
		_newWMBinding = false;
	}

	/**
	 * Print out without worrying about being in-line with other output
	 * 
	 */
	public void print(PrintStream s) {
		/*
		 * 원래 JAM에서는 Binding을 생성할 때 SymbolTable의 크기 만큼 공간을 할당한다. --> size
		 * 변경된 코드에서는 필요한 경우에만 entry를 생성하므로 size가 기존 JAM과 다르게 나올 것이다.
		 * index 역시 보장하지 않는다. 
		 */
		
		s.println("  Binding:");
		s.println("    Size:        \t" + _map.size());	
		s.println("    NewWMBinding:\t" + _newWMBinding);

		int index = 0;
		for (Symbol symbol : _map.keySet()) {
			s.print("      [" + index + "] " + symbol.getName()	+ " = ");
			Value value = getValue(symbol);
			if (value.isDefined()) {
				s.println(value);
			} else {
				s.println("NOT BOUND");
			}
			index++;
		}
		s.println();
	}

	/**
	 * Print out so that it can be in-line with other output
	 * 
	 */
	public void format(PrintStream s) {
		s.print("  Binding: size=" + _map.size() + ", new=" + _newWMBinding + ", ");
		
		int index = 0;
		for (Symbol symbol : _map.keySet()) {
			s.print("[" + index + "] " + symbol.getName()	+ " = ");
			Value value = getValue(symbol);
			if (value.isDefined()) {
				s.print(value + ", ");
			} else {
				s.print("NOT BOUND, ");
			}
			index++;
		}
	}
}

//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Predicate.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/Predicate.java,v $
//  
//  File              : Predicate.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:20:48 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:53:59 2004
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

package uos.ai.jam.expression;

import java.io.PrintStream;
import java.io.Serializable;

import uos.ai.jam.NameSpace;
import uos.ai.jam.exception.AgentRuntimeException;

/**
 * 
 * Predicates (expressions evaluable to true/false)
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public abstract class Predicate extends Expression implements Serializable {
	private static final long serialVersionUID = 1282748811324907627L;

	//
	// Members
	//
	protected final String 			_name;
	protected final Relation 		_relation;
	protected NameSpace		_namespace;

	//
	// Constructors
	//

	/**
	 * Primary constructor
	 * 
	 */
	public Predicate(String name, Relation relation) {
		_name 			= name;
		_relation 		= relation;
		_namespace		= null;
	}
	public Predicate(String name, Relation relation, NameSpace ns) {
		_name 			= name;
		_relation 		= relation;
		_namespace		= ns;
	}

	//
	// Member functions
	//
	
	public void setNameSpace(NameSpace ns){
		this._namespace = ns;
		
	}

	public String getOnlyName(){
		return this._name;
	}
	
	public String getName() {
		if(this._namespace==null){
			return _name;
		}else {
			return this._namespace.wrap(_name);
		}
	}

	public ExpressionType getType() {
		return ExpressionType.PREDICATE;
	}

	public Relation getRelation() {
		return _relation;
	}

	/**
	 * Output information without consideration of being inline with other
	 * information.
	 * 
	 */
	public void print(PrintStream s, Binding b) {
		s.print("Name: " + this.getName());
		s.print(",\tValue = ");
		try {
			s.println((eval(b) != null ? "True" : "False"));
		} catch (AgentRuntimeException e) {
			s.print("False");
		}
	}

	/**
	 * Output information considering that it may be inline with other
	 * information.
	 * 
	 */
	public void format(PrintStream s, Binding b) {
		s.print("(");
		s.print(_name + " ");
		_relation.format(s, b);
		s.print(")");
	}

	public abstract Value eval(Binding binding) throws AgentRuntimeException;

}

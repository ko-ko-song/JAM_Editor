//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Plan.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/Plan.java,v $
//  
//  File              : Plan.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:44 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:01 2004
//  Update Count      : 37
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

package uos.ai.jam.plan;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.List;

import uos.ai.jam.NameSpace;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Relation;
import uos.ai.jam.expression.SymbolTable;
import uos.ai.jam.expression.condition.Condition;
import uos.ai.jam.plan.action.Action;
import uos.ai.jam.plan.constructor.PlanAtomicConstruct;
import uos.ai.jam.plan.constructor.PlanSequenceConstruct;

//import java.util.*;

/**
 * 
 * Represents the basic plan within JAM
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */
public class Plan implements Serializable {
	private static final long serialVersionUID = -4987793993856586281L;

	//
	// Members
	//

	private String						_packageName;
	private String 						_id;

	// Required Plan fields
	protected Action 					_goalSpecification;
	protected Relation 					_concludeSpecification;
	protected Expression 				_utility;
	protected PlanSequenceConstruct 	_body;

	// Common optional Plan fields
	private String						_name;
	protected PlanContext 				_context;
	protected PlanContext 				_precondition;
	protected String 					_documentation;
	protected PlanAtomicConstruct 		_failure;

	// Optional Plan fields
	protected PlanAtomicConstruct 		_effects;
	protected String 					_attributes;

	// Other member fields
	protected SymbolTable 				_symbolTable;
	protected boolean 					_valid;
	
	protected NameSpace					_name_space;
	
	//editor
	private String						_fileName;
	private int						_line;
	
	//
	// Constructors
	//
	public Plan() {
		_symbolTable 			= new SymbolTable();
		_documentation 			= null;
		_valid 					= true;
		_goalSpecification 		= null;
		_concludeSpecification 	= null;
		_name					= null;
		_context 				= new PlanContext();
		_precondition 			= new PlanContext();
		_utility 				= null;
		_effects 				= null;
		_failure 				= null;
		_body 					= null;
		_attributes 			= null;
		_name_space				= null;
	}

	//
	// Member functions
	//
	
	public void setNameSpace(NameSpace ns){
		this._name_space = ns;
	}
	
	public NameSpace getNameSpace(){
		return this._name_space;
	}
	
	public void setPackage(String packageName) {
		_packageName = packageName;
	}
	
	public String getPackage() {
		return _packageName;
	}
	
	public void setId(String id) {
		_id = id;
	}
	
	public String getId() {
		return _id;
	}

	public String setName(String name) {
		return (_name = name);
	}
	
	public String getName() {
		if(_name_space != null){
			return this._name_space.wrap(_name); 
		}else {
			return _name;
		}
	}
	
	public String getOnlyName(){
		return _name;
	}
	
	public Action getGoalSpecification() {
		return _goalSpecification;
	}

	public Action setGoalSpecification(Action p) {
		return _goalSpecification = p;
	}

	public Relation getConcludeSpecification() {
		return _concludeSpecification;
	}

	public Relation setConcludeSpecification(Relation r) {
		return _concludeSpecification = r;
	}

	public Expression getUtility() {
		return _utility;
	}
	
	public Expression setUtility(Expression p) {
		return _utility = p;
	}

	public PlanSequenceConstruct getBody() {
		return _body;
	}

	public PlanContext getContext() {
		return _context;
	}

	public PlanContext setContext(PlanContext c) {
		return _context = c;
	}

	public PlanContext getPrecondition() {
		return _precondition;
	}

	public PlanContext setPrecondition(PlanContext c) {
		return _precondition = c;
	}

	public PlanAtomicConstruct getEffects() {
		return _effects;
	}

	public PlanAtomicConstruct setEffects(PlanAtomicConstruct c) {
		return _effects = c;
	}

	public PlanAtomicConstruct getFailure() {
		return _failure;
	}

	public PlanAtomicConstruct setFailure(PlanAtomicConstruct f) {
		return _failure = f;
	}

	public String getDocumentation() {
		return _documentation;
	}

	public boolean isValid() {
		return _valid;
	}

	public void enable() {
		_valid = true;
	}
	
	public void disable() {
		_valid = false;
	}

	public SymbolTable getSymbolTable() {
		return _symbolTable;
	}

	public String setDocumentation(String planDoc) {
		return _documentation = planDoc;
	}

	public String getAttributes() {
		return _attributes;
	}

	public String setAttributes(String planAtt) {
		return _attributes = planAtt;
	}

	public PlanSequenceConstruct setBody(PlanSequenceConstruct c) {
		return _body = c;
	}

	/**
	 * Calculate the utility value of the plan instance
	 * 
	 */
	public double evalUtility(Binding binding) {
		try {
			return (_utility != null) ? _utility.eval(binding).getReal() : 0.0;
		} catch (AgentRuntimeException e) {
			return 0.0;
		}
	}

	/**
	 * 
	 * 
	 */
	public PlanContext addContext(List<Condition> cl) {
		_context.addConditions(cl);
		return _context;
	}

	/**
	 * 
	 * 
	 */
	public PlanContext addPrecondition(List<Condition> cl) {
		_precondition.addConditions(cl);
		return _precondition;
	}

	/**
	 * Evaluate truth value of context expression
	 * 
	 */
	public boolean checkContext(List<Binding> bindingList) {
		return (_context != null) ? _context.check(bindingList) : true;
	}

	/**
	 * Evaluate truth value of context expression
	 * 
	 */
	public boolean confirmContext(Binding b) {
		return (_context != null) ? _context.confirm(b) : true;
	}

	/**
	 * Evaluate truth value of precondition expression
	 * 
	 */
	public boolean checkPrecondition(List<Binding> bindingList) {
		return (_precondition != null) ? _precondition.check(bindingList)
				: true;
	}

	/**
	 * Print out without worrying about being in-line with other output
	 * 
	 */
	public void print(PrintStream s) {
		s.println("Plan");
		s.println("  NAME: " + getName());
		if (_goalSpecification != null) {
			s.print("  GOAL: ");
			_goalSpecification.format(s, null);
			s.println();
		}
		if (_concludeSpecification != null) {
			s.print("  CONCLUDE: ");
			_concludeSpecification.format(s, null);
			s.println();
		}
		if (_documentation != null) {
			s.println("  DOCUMENTATION: " + _documentation);
		}
	}

	/**
	 * Print out so that it can be in-line with other output
	 * 
	 */
	
	public String toString(){
		String result = "";
		result += _precondition.toString();
		return result;
	}
	public void format(PrintStream s) {
		s.print("Plan: " + getName() + " ");
	}

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

	
	
	
}

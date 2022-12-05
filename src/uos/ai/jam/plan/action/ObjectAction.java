//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ObjectAction.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/ObjectAction.java,v $
//  
//  File              : ObjectAction.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:19:32 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:01 2004
//  Update Count      : 175
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

package uos.ai.jam.plan.action;

import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import uos.ai.jam.Goal;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Value;
import uos.ai.jam.expression.ValueType;
import uos.ai.jam.expression.Variable;

//import uos.ai.jam.examples.*;

/**
 * 
 * A simple (non-decomposable) member function action within a plan
 * 
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class ObjectAction extends Action implements Serializable {
	private static final long serialVersionUID = -7500118977834582461L;

	//
	// Members
	//

	// Note that the Action class's _name member holds the Class name
	protected String 				_functionName;
	protected int 					_arity;
	protected Variable 				_object;
	protected List<Expression> 		_args;

	//
	// Constructors
	//

	/**
	 * Static member function constructor (i.e., no object reference)
	 * 
	 */
	public ObjectAction(String className, String functionName, List<Expression> el) {
		super(className);
		_object 			= null;
		_functionName 		= new String(functionName);
		_args 				= el;
		_arity 				= (el != null) ? el.size() : 0;
		_actType 			= ACT_OBJECT;
	}

	/**
	 * Non-static member function constructor (i.e., requires an object
	 * reference)
	 * 
	 */
	public ObjectAction(String className, String functionName, Variable v, List<Expression> el) {
		super(className);
		_functionName 		= new String(functionName);
		_arity 				= 0;
		_object 			= v;
		_args 				= el;
		_actType 			= ACT_OBJECT;
	}

	//
	// Member functions
	//
	public boolean isExecutableAction() {
		return true;
	}

	// public int getType() { return ACT_PRIMITIVE; }
	public int getArity() {
		return _arity;
	}

	public List<Expression> getArgs() {
		return _args;
	}

	/**
	 * Find and invoke the class' method
	 * 
	 */
	public int execute(Binding b, Goal currentGoal) throws AgentRuntimeException {
		Class<?> c = null;
		Object obj = null;
//		Object returnedObj = null;
		boolean foundMethod = false;

		if (_name != null && _name.length() != 0) {
			try {
				c = Class.forName(_name);
			} catch (ClassNotFoundException e) {
				System.out.println("\nCould not find class \"" + _name + "\"!\n");
				return ACT_FAILED;
			}
		}

		if (_object != null) {
			obj = (Object) _object.eval(b).getObject();
			c = obj.getClass();
		}

		// Create a local instance of the specified class if there
		// isn't already one specified (i.e., if the method is a static
		// function or otherwise doesn't require an object reference)
		if (obj == null) {

			try {
				obj = c.newInstance();
			} catch (InstantiationException e) {
				System.out.println("InstantiationException");
				e.printStackTrace();
				return ACT_FAILED;
			} catch (IllegalAccessException e) {
				System.out.println("IllegalAccessException");
				e.printStackTrace();
				return ACT_FAILED;
			}
		}

		// Check for the case that the class is set up according to
		// our JAM-specified interface.
		if (obj instanceof uos.ai.jam.plan.action.PrimitiveAction) {
			Value v = ((uos.ai.jam.plan.action.PrimitiveAction) obj).execute(_functionName,	_arity, _args, b, currentGoal);
			if (v.eval(b).isTrue()) {
				return ACT_SUCCEEDED;
			} else {
				return ACT_FAILED;
			}
		}

		// Go through all the object's methods and find the method with a name
		// matching the plan's action
		Method[] methods = c.getDeclaredMethods();

		for (int i = 0; i < methods.length; i++) {
			if (methods[i] instanceof Method) {
				Method m = (Method) methods[i];

				// Perform name comparison
				if (_functionName.equals(m.getName())) {

//					Class<?> returntype = null;
					Class<?> parameters[];
//					Class<?> exceptions[];
					Object margs[];

					// Ignore return type and exceptions for now
//					returntype = m.getReturnType();
//					exceptions = m.getExceptionTypes();

					// Check to see if parameter list length and types match.
					parameters = m.getParameterTypes();
					if (parameters.length == _args.size()) {

						boolean matched = true;
						margs = new Object[_args.size()];

						// Go through each parameter and check for matching
						// types
						for (int j = 0; j < parameters.length; j++) {

							// Check each parameter for a matching type and
							// build an
							// Object array from the arguments

							//
							// JavaCC parser will be creating Value objects of
							// the appropriate
							// parameter type though, so will need to restrict
							// member functions
							// to parameters of type Long, String, Double, and
							// Object.
							
							Value argument = _args.get(j).eval(b);

							/*
							 * System.out.println("parameters[j].getName() = " +
							 * parameters[j].getName());
							 * System.out.println("argument = " + argument);
							 */
							if ((argument.type() == ValueType.LONG)
									&& ((parameters[j].getName()
											.equals("java.lang.Integer")) || (parameters[j]
											.getName().equals("int")))) {
								margs[j] = new Integer((int) argument.getLong());
							} else if ((argument.type() == ValueType.LONG)
									&& ((parameters[j].getName()
											.equals("java.lang.Long")) || (parameters[j]
											.getName().equals("long")))) {
								margs[j] = new Long(argument.getLong());
							} else if ((argument.type() == ValueType.REAL)
									&& ((parameters[j].getName()
											.equals("java.lang.Float"))
											|| (parameters[j].getName()
													.equals("java.lang.Double"))
											|| (parameters[j].getName()
													.equals("float")) || (parameters[j]
											.getName().equals("double")))) {
								margs[j] = new Double(argument.getReal());
							} else if ((argument.type() == ValueType.STRING)
									&& (parameters[j].getName()
											.equals("java.lang.String"))) {
								margs[j] = new String(argument.getString());
							} else if (argument.type() == ValueType.OBJECT) {
								// Need to see if the parameter is an object of
								// some form
								// (and not a String)
								if ((parameters[j].getName().indexOf(
										"java.lang.String") == -1)
										|| (!parameters[j].getName().equals(
												"int"))
										|| (!parameters[j].getName().equals(
												"long"))
										|| (!parameters[j].getName().equals(
												"float"))
										|| (!parameters[j].getName().equals(
												"double"))) {
									margs[j] = argument.getObject();
								}
							} else {
								matched = false;
								break;
							}
						}

						// if parameters match then invoke method
						if (matched == true) {
							try {
//								Class<?> returnedClass;
								foundMethod = true;
//								returnedObj = m.invoke(obj, margs);
								
								m.invoke(obj, margs);
								
								// System.out.println("returned object is: " +
								// returnedObj);
//								Value v = new Value(returnedObj);
								/*
								 * returnedClass = returnedObj.getClass();
								 * System.out.println("returnedClass is: " +
								 * returnedClass + ", w/ name: " +
								 * returnedClass.getName());
								 */

								// Go through each function argument and check
								// to see if any
								// of them changed value.
								/*
								 * System.out.println("Values of arguments after
								 * invoke:"); for (int j = 0; j <
								 * parameters.length; j++) {
								 * System.out.println("margs[" + j + "] = " +
								 * margs[j]); }
								 */
								/*
								 * if (v.eval(b).isTrue()) {
								 * System.out.println("Function returned
								 * SUCCESS!\n"); return ACT_SUCCEEDED; } else {
								 * System.out.println("Function returned
								 * FAILURE!\n"); return ACT_FAILED; }
								 */
								return ACT_SUCCEEDED;

							} catch (IllegalAccessException e) {
								System.out.println(e);
								return ACT_FAILED;
							} catch (IllegalArgumentException e) {
								System.out.println(e);
								return ACT_FAILED;
							} catch (InvocationTargetException e) {
								System.out.println(e);
								return ACT_FAILED;
							}
						}
					} else {
						System.out.println("Argument count mis-match (plan action had " + _args.size() + " and member function had " + parameters.length + ")");
					}
				} else {
					// System.out.println(" not a match.");
				}
			}
		}

		if (foundMethod)

			// Need to figure out how to return returned object and
			// reflect modified arguments back to their plan-level
			// variables.
			return ACT_SUCCEEDED;
		else {
			System.out.println("ERROR: Class.memberFunction of " + _name + "." + _functionName + " not found with matching argument list!");
			return ACT_FAILED;
		}
	}

	/**
	 * Output action information considering that it may be in-line with other
	 * information.
	 * 
	 */
	public void format(PrintStream s, Binding b) {
		s.print("PRIMITIVE: " + _name + " ");
		Iterator<Expression> iter = _args.iterator();
		Expression exp = null;
		while(iter.hasNext()) {
			exp = iter.next();
			
			try {
				exp.eval(b).format(s, b);
				if (iter.hasNext()) {
					s.print(" ");
				}
			} catch (AgentRuntimeException e) {}
		}
		s.println();
	}

}

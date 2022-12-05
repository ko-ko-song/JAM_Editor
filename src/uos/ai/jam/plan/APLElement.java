//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: APLElement.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/APLElement.java,v $
//  
//  File              : APLElement.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:23:11 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:02 2004
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

package uos.ai.jam.plan;

import java.io.*;

import uos.ai.jam.Goal;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Relation;

/**
 * 
 * Represents an agent's Applicable Plans List (APL)
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class APLElement implements Serializable {
	private static final long serialVersionUID = 4692125151239825837L;

	//
	// Members
	//
	protected Plan _plan;

	protected Goal _fromGoal;

	protected Binding _binding;

	//
	// Constructors
	//

	/**
	 * 
	 * 
	 */
	public APLElement() {
		_plan = null;
		_fromGoal = null;
		_binding = null;
	}

	/**
	 * 
	 * 
	 */
	public APLElement(APLElement ae) {
		_plan = ae.getPlan();
		_fromGoal = ae.getFromGoal();
		_binding = ae.getBinding();
	}

	/**
	 * 
	 * 
	 */
	public APLElement(Plan p, Goal g, Binding b) {
		_plan = p;
		_fromGoal = g;
		_binding = b;
	}

	//
	// Member functions
	//
	public Plan getPlan() {
		return _plan;
	}

	public Goal getFromGoal() {
		return _fromGoal;
	}
	
	public Binding getBinding() {
		return _binding;
	}

	/**
	 * Determine the instantiated plan's utility (defined currently as goal
	 * utility + plan utility)
	 * 
	 */
	public double evalUtility() {
		// Note that goal utility uses its own internal binding and
		// the plan uses the APLElement binding
		double goalUtility = (_fromGoal != null) ? _fromGoal.evalUtility()
				: 0.0;
		double planUtility = _plan.evalUtility(_binding);

		// Default to simple addition at this point.
		return goalUtility + planUtility;
	}

	/**
	 * Make a copy of the applicable plan
	 * 
	 */
	public void copy(APLElement ae) {
		if (this == ae)
			return;

		_plan = ae.getPlan();
		_fromGoal = ae.getFromGoal();
		_binding = new Binding(ae.getBinding());
	}

	/**
	 * Display information about the applicable plan
	 * 
	 */
	public void print(PrintStream s) {
		s.println("APLElement:");
		s.println("  From goal: " + _fromGoal);
		s.println("  Goal name: "
				+ ((_fromGoal != null) ? _fromGoal.getName() : "none"));
		s.println("  Plan name: " + _plan.getName());
		s.println("  Utility: " + _plan.evalUtility(_binding));
		_binding.print(s);
		s.println();
	}
	
	public String toString(){
		
		String resultStart = "Goal Name : " + ((_fromGoal != null) ? _fromGoal.getName() : "none") + "\n";
		StringBuilder result = new StringBuilder(resultStart);
		
		if(_fromGoal != null){
			if(_fromGoal.getGoalAction() != null){
				Relation relation = _fromGoal.getGoalAction().getRelation();
				result.append("Goal Relation : ");
				result.append(relation.getName());
				result.append(" ");
				for(int i = 0; i < relation.getArity();i++){
					result.append(relation.getArg(i));
					result.append(" ");
				}
				
			}
			
		}
		result.append("Plan name : ");
		result.append(_plan.getName());
		result.append("\n");
		result.append("Utility : ");
		result.append(_plan.evalUtility(_binding));
		result.append("\n");
		result.append("Precondition : ");
		result.append(_plan.toString());

		return result.toString();
	}

}

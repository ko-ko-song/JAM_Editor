//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanTable.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/PlanTable.java,v $
//  
//  File              : PlanTable.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:20:54 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:53:59 2004
//  Update Count      : 17
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import uos.ai.jam.NameSpace;
import uos.ai.jam.plan.action.GoalAction;

/**
 * 
 * A JAM agent's plan library
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class PlanTable implements Serializable {
	private static final long serialVersionUID = -3098848811194877559L;

	//
	// Members
	//

	private final Map<String, List<Plan>> _goalSpecPlanTable;
	private final List<Plan> _conclusePlanList;
	private final List<PlanLibraryChangeListener> _listeners;
	//
	// Constructors
	//

	public PlanTable() {
		_goalSpecPlanTable = new HashMap<String, List<Plan>>();
		_conclusePlanList = new LinkedList<Plan>();
		_listeners = new CopyOnWriteArrayList<PlanLibraryChangeListener>();
	}

	//
	// Member functions
	//

	public void addChangeListener(PlanLibraryChangeListener listener) {
		_listeners.add(listener);
	}

	public void removeChangeListener(PlanLibraryChangeListener listener) {
		_listeners.remove(listener);
	}

	private void firePlanAddedEvent(Plan plan) {
		for (PlanLibraryChangeListener listener : _listeners) {
			listener.planAdded(plan);
		}
	}

	public void add(Plan plan, NameSpace ns) {
		if (ns == null) {
			this.add(plan);
		} else {
			plan.setNameSpace(ns);
			this.add(plan);
		}
	}

	public void add(Plan plan) {
		// plan.setId(makeUniqueId());
		// make id check
		if (planIDCheck(plan) == false) {
			return;
		} else {
			if (plan.getGoalSpecification() != null) {
				String goalName = plan.getGoalSpecification().getName();
				List<Plan> plans = _goalSpecPlanTable.get(goalName);
				if (plans == null) {
					plans = new LinkedList<Plan>();
					_goalSpecPlanTable.put(goalName, plans);
				}
				plans.add(plan);
			} else if (plan.getConcludeSpecification() != null) {
				_conclusePlanList.add(plan);
			}
			firePlanAddedEvent(plan);
		}
	}

	private boolean planIDCheck(Plan plan) {
		boolean idValidity = true;

		if (plan.getId() == null) {
			System.out.println("ID not found. Please insert Plan ID");
			idValidity = false;
		} else {
			for (int i = 0; i < _conclusePlanList.size(); i++) {
				if (_conclusePlanList.get(i).getId().equals(plan.getId())) {
					idValidity = false;
					System.out.println("Plan ID duplicated. Please insert another ID");
					return false;
				}
			}

			for (List<Plan> plans : _goalSpecPlanTable.values()) {
				for (int i = 0; i < plans.size(); i++) {
					Plan planToCompare = plans.get(i);
					if (planToCompare.getId().equals(plan.getId())) {
						idValidity = false;
						System.out.println("Plan ID duplicated. Please insert another ID");
						return false;
					}
				}
			}

		}

		return idValidity;
	}

	private String makeUniqueId() {
		return "PLAN_" + System.nanoTime();
	}

	/**
	 * 
	 * 
	 */

	public Plan getGoalSpecPlan(String id) {
		for (List<Plan> planList : _goalSpecPlanTable.values()) {
			for (Plan plan : planList) {
				if (plan.getId() != null && plan.getId().equals(id)) {
					return plan;
				}
			}
		}
		return null;
	}

	public List<Plan> getGoalSpecPlans(String goalName) {
		return _goalSpecPlanTable.get(goalName);
	}

	public List<Plan> getGoalSpecPlans() {
		List<Plan> result = new ArrayList<Plan>();
		for (List<Plan> planList : _goalSpecPlanTable.values()) {
			for (Plan plan : planList) {
				result.add(plan);
			}
		}
		return result;
	}

	public List<Plan> getConcludePlans() {
		return _conclusePlanList;
	}

	public List<GoalAction> getAvailableGoals() {
		List<GoalAction> result = new ArrayList<GoalAction>();
		for (List<Plan> plans : _goalSpecPlanTable.values()) {
			for (Plan plan : plans) {
				result.add((GoalAction) plan.getGoalSpecification());
			}
		}
		return result;
	}

	/**
	 * 
	 * 
	 */
	public void disable(String name) {
		for (List<Plan> plans : _goalSpecPlanTable.values()) {
			for (Plan plan : plans) {
				if (plan.getName().equals(name)) {
					plan.disable();
				}
			}
		}
		for (Plan plan : _conclusePlanList) {
			if (plan.getName().equals(name)) {
				plan.disable();
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public void print(PrintStream s) {
		// super.print(s);
		s.println("PlanTable: ");
		for (List<Plan> plans : _goalSpecPlanTable.values()) {
			for (Plan plan : plans) {
				s.println("Plan :");
				s.println("  Name:\t" + plan.getName());
				s.println("  #Constructs:\t" + plan.getBody().getNumConstructs());
			}
		}
		for (Plan plan : _conclusePlanList) {
			s.println("Plan :");
			s.println("  Name:\t" + plan.getName());
			s.println("  #Constructs:\t" + plan.getBody().getNumConstructs());
		}
	}

	public Set<String> getGoalList() {
		return _goalSpecPlanTable.keySet();
	}

	public void removePlan(String id) {
		for (List<Plan> plans : _goalSpecPlanTable.values()) {
			for (int i = 0; i < plans.size(); i++) {
				Plan plan = plans.get(i);
				if (plan.getId().equals(id)) {
					plans.remove(plan);
				}
			}
		}
	}
}

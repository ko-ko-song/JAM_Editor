//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: APL.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/APL.java,v $
//  
//  File              : APL.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:23:13 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:02 2004
//  Update Count      : 143
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
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import uos.ai.jam.JAM;

/**
 * 
 * A JAM agent's Applicable Plans List (APL)
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class APL implements Serializable {
	private static final long serialVersionUID = 5166301534709641114L;

	//
	// Members
	//
	
	private final List<APLElement>			_intentions;

	//
	// Constructors
	//

	public APL(List<APLElement> intentions) {
		_intentions = intentions;
	}
	
	/**
	 * Return a random number betweeon 0 and the indicated range
	 * 
	 */
	protected int randomUniform(int range) {
		Random ran = new Random();
		return Math.abs(ran.nextInt() % range);
	}

	/**
	 * Determine the number of applicable plans
	 * 
	 */
	public int getSize() {
		return _intentions.size();
	}

	/**
	 * Retrieve the first applicable plan in the list
	 * 
	 */
	public APLElement getFirst() {
		return (_intentions.size() > 0) ? _intentions.get(0) : null;
	}

	/**
	 * Retrieve the nth element in the list
	 * 
	 */
	public APLElement get(int index) {
		return (_intentions.size() > index) ? _intentions.get(index) : null;
	}

	/**
	 * Retrieve a random applicable plan in the list
	 * 
	 */
	public APLElement getRandom() {
		Random rand = new Random();
		return _intentions.get(Math.abs(rand.nextInt() % _intentions.size()));
	}

	/*
	 * -------------------------------------------------------------------
	 */
	
	public List<APLElement> getConcludeBased() {
		List<APLElement> result = new LinkedList<APLElement>();
		for (APLElement intention : _intentions) {
			if (intention.getFromGoal().getGoalAction() == null) {
				result.add(intention);
			}
		}
		return result;
	}
	
	public APLElement getGoalbasedUtilityRandom() {
		if (JAM.getShowAPL()) {
			System.out.println("APL: Searching through " + _intentions.size() + " plans for maximal utility");
		}
		
		double maxUtility = -Double.MAX_VALUE;
		double utility = 0;
		int maxCount = 0;
		int[] index = new int[_intentions.size()];
		index[0] = 0;

		for (int i=0, n=_intentions.size(); i<n; i++) {
			if (_intentions.get(i).getFromGoal().getGoalAction() != null) {
				utility = _intentions.get(i).evalUtility();
				if (utility > maxUtility) {
					if (JAM.getShowAPL()) {
						System.out.println("APL: Plan " + i	+ " has new maxUtility utility of: " + utility);
					}
					maxUtility 				= utility;
					maxCount 				= 1;
					index[maxCount - 1] 	= i;
				} else if (utility == maxUtility) {
					index[++maxCount - 1] 	= i;
				}
			}
		}

		if (maxCount > 0) {
			if (JAM.getShowAPL()) {
				System.out.println("APL: " + maxCount + " plans found with maxUtility of: " + maxUtility);
			}
			
			Random rand = new Random();
			int maxIndex = index[Math.abs(rand.nextInt() % maxCount)];
			
			if (JAM.getShowAPL()) {
				System.out.println("APL: randomly selected item was index: " + maxIndex);
			}
			
			return _intentions.get(maxIndex);
		} else {
			return null;
		}
	}
	
	/*
	 * -------------------------------------------------------------------
	 */

	/**
	 * Retrieve a random applicable plan from a list of those with the highest
	 * utility
	 * 
	 */
	public APLElement getUtilityRandom() {
		if (JAM.getShowAPL()) {
			System.out.println("APL: Searching through " + _intentions.size() + " plans for maximal utility");
		}
		
		double maxUtility = -Double.MAX_VALUE;
		double utility = 0;
		int maxCount = 0;
		int[] index = new int[_intentions.size()];
		index[0] = 0;

		for (int i=0, n=_intentions.size(); i<n; i++) {
			utility = _intentions.get(i).evalUtility();
			if (utility > maxUtility) {
				if (JAM.getShowAPL()) {
					System.out.println("APL: Plan " + i	+ " has new maxUtility utility of: " + utility);
				}
				maxUtility 				= utility;
				maxCount 				= 1;
				index[maxCount - 1] 	= i;
			} else if (utility == maxUtility) {
				index[++maxCount - 1] 	= i;
			}
			
		}
		
		if (JAM.getShowAPL()) {
			System.out.println("APL: " + maxCount + " plans found with maxUtility of: " + maxUtility);
		}

		Random rand = new Random();
		int maxIndex = index[Math.abs(rand.nextInt() % maxCount)];
		
		if (JAM.getShowAPL()) {
			System.out.println("APL: randomly selected item was index: " + maxIndex);
		}

		return _intentions.get(maxIndex);
	}

	/**
	 * Retrieve the first applicable plan from a list of those with the highest utility
	 * 
	 */
	public APLElement getUtilityFirst() {
		APLElement maxUtilityIntention = null;
		double maxUtility = -Double.MAX_VALUE;
		for (APLElement intention : _intentions) {
			if (intention.evalUtility() > maxUtility) {
				maxUtilityIntention = intention;
			}
		}
		return maxUtilityIntention;
	}

	/**
	 * Display information about the applicable plans
	 * 
	 */
	public void print(PrintStream s) {
		s.println("Applicable Plan List:\nSize: " + _intentions.size());
		for (APLElement intention : _intentions) {
			intention.print(s);
		}
	}

}

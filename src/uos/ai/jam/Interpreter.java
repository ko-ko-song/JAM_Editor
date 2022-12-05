//-*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//
//$Id: Interpreter.java,v 1.2 2006/07/25 00:06:13 semix2 Exp $
//$Source: /home/CVS/semix2/jam-060722/uos/ai/jam/Interpreter.java,v $
//
//File              : Interpreter.java
//Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//: Marcus J. Huber <marcush@irs.home.com>
//Created On        : Tue Sep 30 14:21:53 1997
//Last Modified By  : Jaeho Lee <jaeho@david>
//Last Modified On  : Mon Sep  6 17:54:01 2004
//Update Count      : 107
//
//Copyright (C) 1997-2004 Jaeho Lee and Marcus J. Huber.
//
//////////////////////////////////////////////////////////////////////////////
//
//JAM agent architecture
//
//Copyright (C) 1997-2004 Jaeho Lee and Marcus J. Huber.
//
//Permission is granted to copy and redistribute this software so long
//as no fee is charged, and so long as the copyright notice above, this
//grant of permission, and the disclaimer below appear in all copies
//made.
//
//This software is provided as is, without representation as to its
//fitness for any purpose, and without warranty of any kind, either
//express or implied, including without limitation the implied
//warranties of merchantability and fitness for a particular purpose.
//Jaeho Lee and Marcus J. Huber shall not be liable for any damages,
//including special, indirect, incidental, or consequential damages,
//with respect to any claim arising out of or in connection with the
//use of the software, even if they have been or are hereafter advised
//of the possibility of such damages.
//
//////////////////////////////////////////////////////////////////////////////

package uos.ai.jam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import uos.ai.jam.exception.DefaultExceptionModelFactory;
import uos.ai.jam.exception.ExceptionManager;
import uos.ai.jam.exception.ExceptionManagerImpl;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Relation;
import uos.ai.jam.expression.Value;
import uos.ai.jam.message.MessageQueue;
import uos.ai.jam.plan.APL;
import uos.ai.jam.plan.APLElement;
import uos.ai.jam.plan.Plan;
import uos.ai.jam.plan.PlanTable;
import uos.ai.jam.planManager.PlanManager;

/**
 * 
 * The JAM agent's architectural core
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class Interpreter implements Serializable {
	private static final long serialVersionUID = 7393751612649029693L;

	//
	// Members
	//
	protected PlanTable 			_planTable;
	protected WorldModelManager		_worldModelManager;
	protected WorldModelTable 		_worldModel;
	protected NameSpaceTable		_namespaceTable;
	protected IntentionStructure 	_intentionStructure;
	protected Plan 					_observer;
	private final MessageQueue		_messageQueue;
	private MoniterInterface		_debugFrame;
	private ArrayList<APLChangeListener> aplChangeListener;
	
	protected final ExceptionManager		exceptionManager;

	// Runtime Statistic
	private int 					_numAPLs;
	private int 					_numNullAPLs;
	private int 					_numGoals;
	private int 					_numCycles;
	private String					_worldModelName;

	// new methods//
	
	public void addNameSpace(String url){
		this._namespaceTable.addNameSpace(url);
	}
	
	public void printTest(String value){
		System.out.println("test");
	}
	
	//////////////////
	
	
	//
	// Constructors
	//

	/**
	 * Default constructor
	 * 
	 */
	public Interpreter() {
		_planTable 				= new PlanTable();
		_namespaceTable			= new NameSpaceTable();
		_worldModel 			= new WorldModelTable(_namespaceTable);
		_worldModelManager		= new WorldModelManager(_namespaceTable);
		
		_intentionStructure	 	= new IntentionStructure(this);
		//_debugFrame 			= new MoniterInterface(_worldModelManager.getWorldModel(),_intentionStructure);
		_observer 				= null;
		_messageQueue			= new MessageQueue();
		this.exceptionManager	= new ExceptionManagerImpl(new DefaultExceptionModelFactory());
		aplChangeListener 		= new ArrayList<APLChangeListener>();
		
		_numAPLs 				= 0;
		_numNullAPLs 			= 0;
		_numGoals 				= 0;
		_numCycles 				= 0;
		
	}

	//
	// Member functions
	//
	
	public void addAPLListener(APLChangeListener apl){
		this.aplChangeListener.add(apl);
	}
	
	public void notifyListener(APL apl){
		for(APLChangeListener aplListener : aplChangeListener){
			aplListener.aplChanged(apl);
		}
	}
	
	public PlanTable getPlanLibrary() {
		return _planTable;
	}

	public void setPlanLibrary(PlanTable t) {
		_planTable = t;
	}

	public WorldModelTable getWorldModel() {
		return _worldModelManager.getWorldModel();
	}
	public WorldModelTable getWorldModel(String name) {
		return _worldModelManager.getWorldModel(name);
	}
	public WorldModelManager getWorldModelManager(){
		return this._worldModelManager;
	}
	public NameSpaceTable getNameSpaceTable(){
		return this._namespaceTable;
	}

	public void setWorldModel(WorldModelTable t) {
		_worldModel = t;
	}

	public IntentionStructure getIntentionStructure() {
		return _intentionStructure;
	}

	public void setIntentionStructure(IntentionStructure is) {
		_intentionStructure = is;
	}

	public Plan getObserver() {
		return _observer;
	}

	public void setObserver(Plan p) {
		_observer = p;
	}
	
	public ExceptionManager getExceptionManager() {
		return this.exceptionManager;
	}

	public MessageQueue getMessageQueue() {
		return _messageQueue;
	}

	public int getNumAPLsStat() {
		return _numAPLs;
	}

	public void setNumAPLsStat(int num) {
		_numAPLs = num;
	}

	public int getNumNullAPLsStat() {
		return _numNullAPLs;
	}

	public void setNumNullAPLsStat(int num) {
		_numNullAPLs = num;
	}

	public int getNumGoalsStat() {
		return _numGoals;
	}

	public void setNumGoalsStat(int num) {
		_numGoals = num;
	}

	public int getNumCyclesStat() {
		return _numCycles;
	}

	public void setNumCyclesStat(int num) {
		_numCycles = num;
	}

	/**
	 * Execute the agent's behavior
	 * 
	 */
	public int run() {
		APLGenerator generator = new APLGenerator(_intentionStructure, _worldModelManager, _planTable);
		APL apl = null;
		APL last_apl = null;
		APLElement selectedElement;
		Relation rel;
		List<Expression> ex;
		int metaLevel;

		// Loop forever until agent completes all of its goals
		while (true) { // outer, infinite loop

			Thread.yield();
//			try {
//				Thread.sleep(10);
//			} catch(InterruptedException ignore) {}
			
			setNumCyclesStat(getNumCyclesStat() + 1);

			// Execute the "execute" body here when it's implemented
			// returnValue = _intentionStructure.executePlan(_execute);

			// Execute the Observer procedure
			_intentionStructure.executeObserver(_observer);
			
			// Loop until agent completes metalevel "ramp up"
			metaLevel = 0;
			while (true) {// metalevel loop
				//_debugFrame.update();
				// Generate an Applicable Plan List (APL) if necessary

				/*
				 * APL 占쏙옙占쏙옙 占쏙옙 WorldModel.clearNewAll()占쏙옙 호占쏙옙占쌔억옙 占싹몌옙,
				 * 占쏙옙 占쏙옙占쏙옙占쏙옙 WorldModel.lock 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙퓸占쏙옙 占싹므뤄옙
				 * WorldModel.execute()占쏙옙 占쏙옙占쏙옙磯占�.
				 */
				getWorldModel().execute(generator.setMetaLevel(metaLevel));
				apl = generator.getAPL();
				
				setNumAPLsStat(getNumAPLsStat() + 1);

				if (JAM.getShowAPL()) {
					System.out.println("JAM: APL generated has " + apl.getSize() + " element(s).");
				}
				
				if(apl.getSize() != 0){
					notifyListener(apl);
				}
				
				
				// If the new or previous APL is not empty then add entry to the
				// World Model to trigger the next level of reasoning.
				// if ((apl.getSize() != 0) || (last_apl != null &&
				// last_apl.getSize() != 0)) {
				if (apl.getSize() != 0) {
					ex = new LinkedList<Expression>();
					ex.add(new Value(metaLevel));
					ex.add(new Value(apl));
					ex.add(new Value(apl.getSize()));
					rel = _worldModelManager.getWorldModel().newRelation("APL", ex); 
					getWorldModel().assertFact(rel, null);
				}

				// If this APL is empty then no new level of reasoning
				if (apl.getSize() == 0) {
					setNumNullAPLsStat(getNumNullAPLsStat() + 1);

					// System.out.println("APL: APL was empty.\n");

					// Make this run-time configurable to an alternative, where
					// the system keeps running, even after there are no goals.
					if ((_intentionStructure.allGoalsDone())) {
						System.out.println("\nJAM: All of the agent's top-level goals have been achieved!  Exiting...");
						System.out.println("\nRuntime statistics follow:\n");
						System.out.println("  Number of APLs generated:\t" + getNumAPLsStat());
						System.out.println("  Number of Null APLs:\t\t" + getNumNullAPLsStat());
						System.out.println("  Number of Goals established:\t" + getNumGoalsStat());
						System.out.println("  Number of interpreter cycles:\t" + getNumCyclesStat());
						return 0;
					}

					// If the previous APL was empty then execute something in the intention structure, 
					// otherwise select something from the previous APL, intend it, 
					// and then run something in the intention structure.
					if (last_apl == null || last_apl.getSize() == 0) {
						// System.out.println("APL: last_APL was empty
						// also.\n");
						_intentionStructure.run();
						break;
					} else {

						// System.out.println("APL: last_APL was NOT empty, picking element.\n");

						// selectedElement = apl.getRandom();
						// selectedElement = apl.getUtilityFirst();
//						selectedElement = last_apl.getUtilityRandom();

						/*
						 * 占쏙옙 占쏙옙占쏙옙클 占쏙옙占쏙옙 占쏙옙 占싱삼옙占쏙옙 fact占쏙옙 占쏙옙占쏙옙풔占� 占쏙옙占�, 占쏙옙 占싼뱄옙占쏙옙 selection 占쏙옙 wm.clearNew()占쏙옙 호占쏙옙占싹댐옙 占쌕띰옙占쏙옙
						 * 占싹놂옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 fact占쏙옙占쏙옙 conclude占쏙옙 占쏙옙占쏙옙占쏙옙占� 占십댐옙 占쏙옙占쏙옙 占쌔곤옙
						 */
						List<APLElement> concludeBased = last_apl.getConcludeBased();
						for (APLElement intention : concludeBased) {
							_intentionStructure.intend(intention);
						}
						
						selectedElement = last_apl.getGoalbasedUtilityRandom();
						if (selectedElement != null) {
							if (JAM.getShowAPL() || JAM.getShowIntentionStructure()) {
								System.out.println("\nJAM: Selected plan \"" + selectedElement.getPlan().getName() + "\" from APL.\n");
							}
							
							_intentionStructure.intend(selectedElement);
							
							if (JAM.getShowIntentionStructure()) {
								System.out.println("\nJAM: Intended element, Intention structure now:");
								_intentionStructure.print(System.out);
							}
						}

						_intentionStructure.run();
						last_apl = null;
						break;
					}

					/*
					 * if (_intentionStructure.getToplevelGoals().getCount() ==
					 * 0) {
					 *  // Need to consider the remaining top-level goals
					 * _intentionStructure.renewLeafGoals(); continue; }
					 */
					//
				} else {

					if (JAM.getShowAPL()) {
						System.out.println("JAM: APL generated:");
						apl.print(System.out);
					}

					last_apl = apl;
					metaLevel++;
					if (JAM.getShowAPL()) {
						System.out.println("JAM: Metalevel now at: " + metaLevel);
					}
				}
			} // Inner, metalevel loop

			// NOTE: Any metalevel plan should do the necessary WM clearing.
			// However, if there is no metalevel plans then we need to clear
			// the World Model.
			rel = _worldModelManager.getWorldModel().newRelation("APL", null); 
			getWorldModel().retract(rel, null);
			
			// Global Exception 처占쏙옙占쏙옙 占쏙옙占쌔쇽옙 ExceptionManager占쏙옙 execute()占쏙옙 호占쏙옙
			getExceptionManager().execute(this);
		
		} // Outer infinite loop

	}

	public void setPlanFolder(String s) {
		new PlanManager(s, this);
	}
}

//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: WorldModelTable.java,v 1.3 2006/07/25 00:06:13 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/WorldModelTable.java,v $
//  
//  File              : WorldModelTable.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:18:47 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:53:58 2004
//  Update Count      : 31
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

package uos.ai.jam;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import uos.ai.jam.WorldModelTable.WorldModelRelation;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Relation;
import uos.ai.jam.expression.SymbolTable;
import uos.ai.jam.expression.Value;
import uos.ai.jam.expression.ValueString;

/**
 * 
 * A JAM agent's knowledge about the world
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class WorldModelTable implements Serializable {
	private static final long serialVersionUID = -162748954912374803L;

	public static class WorldModelRelation implements Serializable {
		private static final long serialVersionUID = 1842785330332958283L;
		
		//
		// Members
		//
		
		private final Relation 		_relation; 	// The content of the WM Relation Table
		private final Prefix		_prefix;
		private boolean 			_newTag; 	// set to 1 for new entry and cleared at every cycle
		
		//
		// Constructors
		//

		/**
		 * Constructor based on an existing relation
		 * 
		 */
		public WorldModelRelation(Relation rel) {
			_relation 	= rel;
			_prefix		= null;
			_newTag 	= true;
		}
		
		public WorldModelRelation(Prefix pre, Relation rel){
			_relation = rel;
			_prefix   = pre;
			_newTag = true;
		}

		//
		// Member functions
		//
		public Relation getRelation() {
			return _relation;
		}
		
		public Prefix getPrefix(){
			return _prefix;
		}

		public boolean isNew() {
			return _newTag;
		}

		public void clearNew() {
			_newTag = false;
		}

		/**
		 * Return whether a match can be found for the specified relation and
		 * variable binding.
		 * 
		 */
		public boolean matchRelation(Relation pattRelation, Binding pattBinding) {
			try {
				if (_relation.unify(pattRelation, pattBinding, _relation, (Binding) null)) {
					if (pattBinding != null) {
						pattBinding.checkNewWMBinding(_newTag);
					}
					return true;
				} else {
					return false;
				}
			} catch (AgentRuntimeException e) {
				return false;
			}
		}
	}
	
	//
	// Members
	//

	private final SymbolTable 								_relationTable;
	private final Map<String, List<WorldModelRelation>>		_table;
	private final ReadWriteLock								_lock;
	private final List<WorldModelChangeListener>			_listeners;
	private final String									_name;
	private final NameSpaceTable							_namespaceTable;
	//
	// Constructors
	//
	public WorldModelTable(NameSpaceTable nt) {
		_relationTable	= new SymbolTable();
		_namespaceTable = nt;
		_table			= new HashMap<String, List<WorldModelRelation>>();
		_lock			= new ReentrantReadWriteLock();
		_listeners		= new CopyOnWriteArrayList<WorldModelChangeListener>();
		_name			= "default";
	}
	
	public WorldModelTable(NameSpaceTable nt, String name) {
		_namespaceTable = nt;
		_relationTable	= new SymbolTable();
		_table			= new HashMap<String, List<WorldModelRelation>>();
		_lock			= new ReentrantReadWriteLock();
		_listeners		= new CopyOnWriteArrayList<WorldModelChangeListener>();
		_name			= name;
	}
	
	//
	// Member functions
	//

	public SymbolTable getSymbolTable() {
		return _relationTable;
	}
	
	
	public Relation newRelation(String name, List<Expression> expList) {
		return new Relation(_relationTable.getSymbol(name), expList);
	}
	
	public void addChangeListener(WorldModelChangeListener listener) {
		_listeners.add(listener);
	}
	
	public void removeChangeListener(WorldModelChangeListener listener) {
		_listeners.remove(listener);
	}
	
	private void fireChangeEvent(Relation[] retracted, Relation asserted) {
		for (WorldModelChangeListener listener : _listeners) {
			listener.worldModelChanged(retracted, asserted);
		}
	}
	
	public int getSize() {
		int size = 0;
		_lock.readLock().lock();
		try {
			for (List<WorldModelRelation> wmRelations : _table.values()) {
				size += wmRelations.size();
			}
		} finally {
			_lock.readLock().unlock();
		}
		return size;
	}
	
	/**
	 * Check to see if any World Model entries match the specified relation and
	 * binding
	 * 
	 */
	public boolean match(Relation relation, Binding binding) {
		_lock.readLock().lock();
		try {
			List<WorldModelRelation> wmRelations = _table.get(relation.getName());
			if (wmRelations != null) {
				for (WorldModelRelation wmRelation : wmRelations) {
					if (wmRelation.matchRelation(relation, binding)) {
						return true;
					}
				}
			}
		} finally {
			_lock.readLock().unlock();
		}
		return false;
	}
	
	public List<Relation> query(Relation template, Binding binding) {
		List<Relation> result = new ArrayList<Relation>();
		_lock.readLock().lock();
		try {
			List<WorldModelRelation> wmRelations = _table.get(template.getName());
			if (wmRelations != null) {
				for (WorldModelRelation wmRelation : wmRelations) {
					Binding temp = new Binding(binding);
					if (wmRelation.matchRelation(template, temp)) {
						result.add(wmRelation.getRelation());
					}
				}
			}
		} finally {
			_lock.readLock().unlock();
		}
		return result;
	}
	
	/**
	 * �־��� relation�� binding���κ��� match�Ǵ� binding���� ���� 
	 * @param relation
	 * @param binding
	 * @return
	 */
	public List<Binding> check(Relation relation, Binding binding) {
		List<Binding> checkedBindings = new LinkedList<Binding>();
		_lock.readLock().lock();
		try {
			List<WorldModelRelation> wmRelations = _table.get(relation.getName());
			if (wmRelations != null) {
				for (WorldModelRelation wmRelation : wmRelations) {
					Binding newBinding = new Binding(binding);
					if (wmRelation.matchRelation(relation, newBinding)) {
						checkedBindings.add(newBinding);
					}
				}
			}
		} finally {
			_lock.readLock().unlock();
		}
		return checkedBindings;
	}

	public List<Binding> checkNew(Relation relation, Binding binding) {
		List<Binding> checkedBindings = new LinkedList<Binding>();
		_lock.readLock().lock();
		try {
			List<WorldModelRelation> wmRelations = _table.get(relation.getName());
			if (wmRelations != null) {
				for (WorldModelRelation wmRelation : wmRelations) {
					Binding newBinding = new Binding(binding);
					if (wmRelation.matchRelation(relation, newBinding) && newBinding.isNewWMBinding()) {
						checkedBindings.add(newBinding);
					}
				}
			}
		} finally {
			_lock.readLock().unlock();
		}
		return checkedBindings;
	}
	
	/**
	 * Add a new World Model entry
	 * 
	 */
	private Relation _assertFact(Relation r, Binding b) {
		Relation asserted = null;
		_lock.writeLock().lock();
		try {
			if (!match(r, b)) {
				// Note: Creating a new relation with args evaluated
				List<WorldModelRelation> wmRelations = _table.get(r.getName());
				if (wmRelations == null) {
					wmRelations = new LinkedList<WorldModelRelation>();
					_table.put(r.getName(), wmRelations);
				}
				try {
					asserted = new Relation(r, b);
				} catch (AgentRuntimeException e) {
					e.printStackTrace();
				}
				if (asserted != null) {
					wmRelations.add(new WorldModelRelation(asserted));
				}
			}
		} finally {
			_lock.writeLock().unlock();
		}
		if (JAM.getShowWorldModel()) {
			System.out.println("JAM::WorldModel:assert");
			print(System.out);
		}
		return asserted;
	}
	
	private static final Relation[] EMPTY_RELATIONS = new Relation[0];
	private Relation[] _retract(Relation r, Binding b) {
		List<Relation> retracted = new ArrayList<Relation>();
		_lock.writeLock().lock();
		try {
			List<WorldModelRelation> wmRelations = _table.get(r.getName());
			if (wmRelations != null) {
				ListIterator<WorldModelRelation> iter = wmRelations.listIterator();
				while (iter.hasNext()) {
					WorldModelRelation wmRelation = iter.next();
					if (wmRelation.matchRelation(r, b)) {
						retracted.add(wmRelation.getRelation());
						iter.remove();
					}
				}
			}
		} finally {
			_lock.writeLock().unlock();
		}
		if (JAM.getShowWorldModel()) {
			System.out.println("JAM::WorldModel:retract");
			print(System.out);
		}
		return retracted.toArray(EMPTY_RELATIONS);
	}
	
	public void assertFact(Relation r, Binding b) {
		Relation asserted = _assertFact(r, b);
		if (asserted != null) {
			fireChangeEvent(null, asserted);
		}
	}
	
	private String matchPrefix(String input) {
		String result = input;
		String[] fullName = input.split(":");
		if(fullName.length == 2 && input.contains(" ") == false && input.contains("(") == false && input.contains(")") == false) {
			
			//System.out.println("namespaceInput : " + input);
			NameSpace namespace = _namespaceTable.getNameSpace(fullName[0]);
			
			if(namespace != null) {
				result = namespace.wrap(fullName[1]);
			}
		}
		
		return result;
	}
	
	public void assertFact(String name, Object... args) {
		
		name = matchPrefix(name);

		LinkedList<Expression> expList = new LinkedList<Expression>();
		if(args != null) {
			for (Object o : args) {
				Value v = null;
				if(o instanceof String){
					String argument = (String)o;
					argument = matchPrefix(argument);
					v = new Value(argument);
				}else if(o instanceof Float) {
					Float a = (Float)o;
					v = new Value(a.doubleValue());
				}else if(o instanceof Integer){
					Integer i = (Integer)o;
					v = new Value(i.intValue());
				}else {
					v = new Value(o);
				}
				expList.add(v);
			}
		}
		
		Relation r = newRelation(name, expList);
		this.assertFact(r, null);
		//System.out.println("relation asserted: " +  r.toString());
	}
	
	
	/**
	 * Remove a World Model entry
	 * 
	 * 
	 */
	public void retract(Relation r, Binding b) {
		Relation[] retracted = _retract(r, b);
		if (retracted.length > 0) {
			fireChangeEvent(retracted, null);
		}
	}
	public String[][] getWorldModelName() {
		_lock.readLock().lock();
		String[][] list;
		try {
			int index = 0;
			for (List<WorldModelRelation> wmRelations : _table.values()) {
				for (WorldModelRelation wmRelation : wmRelations) {
					index++;
				}
			}

			list = new String[index][1];

			index = 0;
			for (List<WorldModelRelation> wmRelations : _table.values()) {
				for (WorldModelRelation wmRelation : wmRelations) {
					list[index][0] = wmRelation.getRelation().getPrintData();

					index++;
				}
			}

		} finally {
			_lock.readLock().unlock();
		}

		return list;
	}

	/**
	 * Change a World Model entry
	 * 
	 */
	public void update(Relation oldRel, Relation newRel, Binding b) {
		if (JAM.getShowWorldModel()) {
			System.out.println("JAM::WorldModel:update (via retract then assert)");
		}
		Relation[] retracted = null;
		Relation asserted = null;
		_lock.writeLock().lock();
		try {
			retracted = _retract(oldRel, b);
			asserted = _assertFact(newRel, b);
		} finally {
			_lock.writeLock().unlock();
		}
		if (retracted.length > 0 || asserted != null) {
			fireChangeEvent(retracted, asserted);
		}
	}

	/**
	 * See if there are ANY new World Model entries
	 * 
	 */
	public boolean anyNew() {
		_lock.readLock().lock();
		try {
			for (List<WorldModelRelation> wmRelations : _table.values()) {
				for (WorldModelRelation wmRelation : wmRelations) {
					if (wmRelation.isNew()) {
						return true;
					}
				}
			}
		} finally {
			_lock.readLock().unlock();
		}
		return false;
	}
	
	public List<Relation> getNew() {
		List<Relation> result = new LinkedList<Relation>();
		_lock.readLock().lock();
		try {
			for (List<WorldModelRelation> wmRelations : _table.values()) {
				for (WorldModelRelation wmRelation : wmRelations) {
					if (wmRelation.isNew()) {
						result.add(wmRelation.getRelation());
					}
				}
			}
		} finally {
			_lock.readLock().unlock();
		}
		return result;
	}

	/**
	 * Set all World Model entries to be "aged"
	 * 
	 */
	public void clearNewAll() {
		_lock.writeLock().lock();
		try {
			for (List<WorldModelRelation> wmRelations : _table.values()) {
				for (WorldModelRelation wmRelation : wmRelations) {
					wmRelation.clearNew();
				}
			}
		} finally {
			_lock.writeLock().unlock();
		}
	}
	
	public void execute(Runnable task) {
		_lock.writeLock().lock();
		try {
			task.run();
		} finally {
			_lock.writeLock().unlock();
		}
	}

	/*
	public List<Relation> dump() {
		ArrayList<Relation> relations = new ArrayList<Relation>();
		_lock.readLock().lock();
		try {
			for (List<WorldModelRelation> wmRelations : _table.values()) {
				for (WorldModelRelation wmRelation : wmRelations) {
					relations.add(wmRelation.getRelation());
				}
			}
		} finally {
			_lock.readLock().unlock();
		}
		return relations;
	}
	*/
	
	/**
	 * Output information related to the World Model
	 * 
	 */
	public void print(PrintStream s) {
		_lock.readLock().lock();
		try {
			s.println("JAM:Agent's World Model (" + getSize() + " entries) is now:");
			int index = 0;
			for (List<WorldModelRelation> wmRelations : _table.values()) {
				for (WorldModelRelation wmRelation : wmRelations) {
					s.print(index + ":[" + (wmRelation.isNew() ? "N" : " ") + "] : ");
					wmRelation.getRelation().print(s, null);
					index++;
				}
			}
		} finally {
			_lock.readLock().unlock();
		}
	}
}

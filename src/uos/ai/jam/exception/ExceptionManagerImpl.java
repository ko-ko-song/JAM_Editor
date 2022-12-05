package uos.ai.jam.exception;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import uos.ai.jam.Interpreter;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Value;

/**
 * ExceptionManager implementation class
 * 
 * @author	Hyunju Shin <jetzt82@gmail.com>
 * @since	2.0
 * @update	2.0
 */
public class ExceptionManagerImpl implements ExceptionManager {
	private static final String[] NULL_STR_ARRAY	= new String[0];
	
	private ExceptionModelFactory					factory;
	private ExceptionTreeNode						root;
	private final Map<String, ExceptionTreeNode>	map;
	private final Lock								readLock;
	private final Lock								writeLock;
	private final List<GlobalCatch>					gcList;
	
	private final List<AgentRuntimeException>		occuredExceptions;
	
	public ExceptionManagerImpl(ExceptionModelFactory factory) {
		this.factory			= (factory == null) ? new DefaultExceptionModelFactory() : factory;
		this.root				= this.factory.newExceptionModel();
		this.map				= new HashMap<String, ExceptionTreeNode>();
		ReadWriteLock l			= new ReentrantReadWriteLock();
		this.readLock			= l.readLock();
		this.writeLock			= l.writeLock();
		this.occuredExceptions	= new LinkedList<AgentRuntimeException>();
		this.gcList				= new LinkedList<GlobalCatch>();
		
		addAllNodeToMap(this.root);
	}
	
	public void setModelFactory(ExceptionModelFactory factory) {
		if (factory == null) return;
		this.writeLock.lock();
		try {
			this.factory	= factory;
			this.root		= this.factory.newExceptionModel();
			addAllNodeToMap(this.root);
		} finally {
			this.writeLock.unlock();
		}
	}
	
	private void addAllNodeToMap(ExceptionTreeNode node) {
		if (node == null) return;

		String name = node.getCanonicalName();
		if (!this.map.containsKey(name)) {
			this.map.put(name, node);
		}
		
		for (ExceptionTreeNode child : node.children()) {
			addAllNodeToMap(child);
		}
	}
	
	public void throwException(AgentRuntimeException ex) {
		if (ex == null) return;
		
		this.writeLock.lock();
		try {
			this.occuredExceptions.add(ex);
		} finally {
			this.writeLock.unlock();
		}
	}
	
	public AgentRuntimeException[] getAllExceptions() {
		this.readLock.lock();
		try {
			return this.occuredExceptions.toArray(AgentRuntimeException.NULL_ARRAY);
		} finally {
			this.readLock.unlock();
		}
	}
	
	public void removeAllExceptions() {
		this.writeLock.lock();
		try {
			this.occuredExceptions.clear();
		} finally {
			this.writeLock.unlock();
		}
	}

	public String getRootException() {
		this.readLock.lock();
		try {
			return this.root.getCanonicalName();
		} finally {
			this.readLock.unlock();
		}
	}
	
	public boolean addNodeInto(String child, String parent) {
		if (isEmptyStr(child)) return false;
		if (isEmptyStr(parent)) return false;
		//if (child.startsWith(parent) == false) return false;
		
		this.writeLock.lock();
		try {
			if (this.map.containsKey(parent) == false) return false;
			if (this.map.containsKey(child)) return false;
			
			ExceptionTreeNode p = this.map.get(parent);
			ExceptionTreeNode c = this.factory.newExceptionTreeNode(child);
			if (c == null) return false;
			p.addChild(c);
			
			this.map.put(c.getCanonicalName(), c);
			
		} finally {
			this.writeLock.unlock();
		}
		return true;
	}
	
	public boolean removeNodeFromParent(String child) {
		if (isEmptyStr(child)) return false;
		
		this.writeLock.lock();
		try {
			if (this.map.containsKey(child) == false) return false;
			
			ExceptionTreeNode c = this.map.remove(child);
			c.removeFromParent();
		} finally {
			this.writeLock.unlock();
		}
		return true;
	}
	
	public boolean removeAllNode(String parent) {
		if (isEmptyStr(parent)) return false;
		
		this.writeLock.lock();
		try {
			if (this.map.containsKey(parent) == false) return false;
			
			ExceptionTreeNode p = this.map.get(parent);
			p.removeAllChild();
		} finally {
			this.writeLock.unlock();
		}
		return true;
	}

	public String[] getChildrenExceptions(String name) {
		if (isEmptyStr(name)) return NULL_STR_ARRAY;
		
		List<String> list = new LinkedList<String>();
		this.readLock.lock();
		try {
			if (this.map.containsKey(name) == false) return NULL_STR_ARRAY;
			
			ExceptionTreeNode p = this.map.get(name);
			for (ExceptionTreeNode c : p.children()) {
				list.add(c.getCanonicalName());
			}
			
		} finally {
			this.readLock.unlock();
		}
		return list.toArray(NULL_STR_ARRAY);
	}

	public String[] getChildrenExceptions(AgentRuntimeException exception) {
		if (exception == null) return NULL_STR_ARRAY;
		return getChildrenExceptions(exception.getCanonicalName());
	}

	public String getParentException(String name) {
		if (isEmptyStr(name)) return null;
		
		this.readLock.lock();
		try {
			if (this.map.containsKey(name) == false) {
				return root.getCanonicalName();
			}
			
			ExceptionTreeNode c = this.map.get(name);
			ExceptionTreeNode p = c.getParent();
			return (p == null) ? null : p.getCanonicalName();
		} finally {
			this.readLock.unlock();
		}
	}

	public String getParentException(AgentRuntimeException exception) {
		if (exception == null) return null;
		return getParentException(exception.getCanonicalName());
	}

	public boolean isAncestor(String parent, String child) {
		if (isEmptyStr(parent)) return false;
		if (isEmptyStr(child)) return false;
		
		this.readLock.lock();
		try {
			if (this.map.containsKey(parent) == false) return false;
			if (this.map.containsKey(child) == false) return false;
			ExceptionTreeNode p = this.map.get(parent);

			if (p == this.root) {
				return true;
			}
			
			ExceptionTreeNode c = this.map.get(child);
			return p.isNodeAncestor(c);
		} finally {
			this.readLock.unlock();
		}
	}

	public boolean isAncestor(AgentRuntimeException parent, String child) {
		if (parent == null) return false;
		return isAncestor(parent.getCanonicalName(), child);
	}

	public boolean isDescendant(String child, String parent) {
		if (isEmptyStr(parent)) return false;
		if (isEmptyStr(child)) return false;
		
		this.readLock.lock();
		try {
		
			if (this.map.containsKey(parent) == false) return false;
			if (this.map.containsKey(child) == false) return false;
			ExceptionTreeNode p = this.map.get(parent);
			
			if (p == this.root) {
				return true;
			}
			
			ExceptionTreeNode c = this.map.get(child);
			return c.isNodeDescendant(p);

		} finally {
			this.readLock.unlock();
		}
	}

	public boolean isDescendantOrEqual(String child, String parent) {
		if (isEmptyStr(parent)) return false;
		if (isEmptyStr(child)) return false;
		
		this.readLock.lock();
		try {
		
			if (this.map.containsKey(parent) == false) {
				return false;
			}
			if (this.map.containsKey(child) == false) {
				return false;
			}
			ExceptionTreeNode p = this.map.get(parent);
			
			if (p == this.root) {
				return true;
			}
			
			ExceptionTreeNode c = this.map.get(child);
			
			if (p == c) {
				return true;
			}
			return c.isNodeDescendant(p);

		} finally {
			this.readLock.unlock();
		}
	}
	
	public boolean isDescendant(AgentRuntimeException child, String parent) {
		if (child == null) return false;
		return isDescendant(child.getCanonicalName(), parent);
	}
	
	public boolean isDescendantOrEqual(AgentRuntimeException child, String parent) {
		if (child == null) return false;
		return isDescendantOrEqual(child.getCanonicalName(), parent);
	}
	
	public String[] getPathToRoot(String name) {
		if (isEmptyStr(name)) return NULL_STR_ARRAY;
		
		List<String> result = new LinkedList<String>();
		this.readLock.lock();
		try {
			if (this.map.containsKey(name) == false) return NULL_STR_ARRAY;
			
			ExceptionTreeNode node = this.map.get(name);
			for (ExceptionTreeNode p : node.getPath()) {
				result.add(p.getCanonicalName());
			}
		} finally {
			this.readLock.unlock();
		}
		return result.toArray(NULL_STR_ARRAY);
	}
	
	private boolean isEmptyStr(String str) {
		if (str == null) return true;
		else return "".equals(str);
	}
	
	public void addGlobalCatch(GlobalCatch globalCatch) {
		if (globalCatch == null) return;
		this.writeLock.lock();
		try {
			this.gcList.add(globalCatch);
		} finally {
			this.writeLock.unlock();
		}
	}
	
	public void addAllGlobalCatch(List<GlobalCatch> list) {
		if (list == null) return;
		this.writeLock.lock();
		try {
			this.gcList.addAll(list);
		} finally {
			this.writeLock.unlock();
		}
	}
	
	public void addAllGlobalCatch(GlobalCatch[] list) {
		if (list == null) return;
		this.writeLock.lock();
		try {
			for (GlobalCatch gc : list) {
				this.gcList.add(gc);
			}
		} finally {
			this.writeLock.unlock();
		}
	}
	
	public void removeGlobalCatch(GlobalCatch globalCatch) {
		if (globalCatch == null) return;
		this.writeLock.lock();
		try {
			this.gcList.remove(globalCatch);
		} finally {
			this.writeLock.unlock();
		}
	}
	
	public GlobalCatch[] getGlobalCatchs() {
		this.writeLock.lock();
		try {
			return this.gcList.toArray(GlobalCatch.NULL_ARRAY);
		} finally {
			this.writeLock.unlock();
		}
	}
	
	public void execute(Interpreter interpreter) {
		this.writeLock.lock();
		try {
			List<GlobalExceptionHandler> list = createExceptionHandler(interpreter);
			for (GlobalExceptionHandler h : list) {
				h.execute();
			}
			this.occuredExceptions.clear();
		} finally {
			this.writeLock.unlock();
		}
	}
	
	private ExceptionTreeNode findExceptionTreeNode(String canonicalName) {
		if (canonicalName == null || "".equals(canonicalName)) return null;
		else return this.map.get(canonicalName);
	}
	
	private List<GlobalExceptionHandler> createExceptionHandler(Interpreter interpreter) {
		List<GlobalExceptionHandler> list = new LinkedList<GlobalExceptionHandler>();
		
		for (AgentRuntimeException e : this.occuredExceptions) {
			ExceptionTreeNode eNode = findExceptionTreeNode(e.getCanonicalName());
			
			//if (eNode == null) continue;
			
			if (eNode == null) {
				eNode = root;
			}
			
			GlobalCatch gc = findMatchedGlobalCatch(eNode);
			if (gc == null) continue;
			
			Binding b = new Binding();
			b.setValue(gc.getBindVariable(), new Value(e));
			list.add(new GlobalExceptionHandler(interpreter, b, gc.getBodyConstruct()));
		}
		
		return list;
	}
	
	private GlobalCatch findMatchedGlobalCatch(ExceptionTreeNode eNode) {
		for (GlobalCatch gc : this.gcList) {
			ExceptionTreeNode cNode = findExceptionTreeNode(gc.getExceptionName());
			if (cNode == null) continue;
			if (eNode.isNodeAncestor(cNode)) return gc;
		}
		return null;
	}
}

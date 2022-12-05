package uos.ai.jam.exception;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Agent exception
 * 
 * @author	Hyunju Shin <jetzt82@gmail.com>
 * @since	2.0
 * @update	2.0
 */
@XmlRootElement(name="ExceptionTreeNode")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder={"packageName", "simpleName", "children"})
public class ExceptionTreeNode {
	private static final ExceptionTreeNode[] NULL_ARRAY = new ExceptionTreeNode[0];
	
	public static ExceptionTreeNode newExceptionNode(String xml) {
		return JAXB.unmarshal(new StringReader(xml), ExceptionTreeNode.class);
	}
	
	public static ExceptionTreeNode newExceptionNode(Reader reader) {
		return JAXB.unmarshal(reader, ExceptionTreeNode.class);
	}
	
	public static ExceptionTreeNode newExceptionNode(URI uri) {
		return JAXB.unmarshal(uri, ExceptionTreeNode.class);
	}
	
	public static ExceptionTreeNode newExceptionNode(InputStream is) {
		return JAXB.unmarshal(is, ExceptionTreeNode.class);
	}
	
	@XmlTransient
	private ExceptionTreeNode				parent;

	@XmlTransient
	private final List<ExceptionTreeNode>	children;
	
	@XmlElement(name="package")
	private final String					packageName;
	
	@XmlElement(name="name")
	private final String					simpleName;
	
	@SuppressWarnings("unused")
	private ExceptionTreeNode() {
		this.packageName	= null;
		this.simpleName		= null;
		this.children		= new CopyOnWriteArrayList<ExceptionTreeNode>();
	}
	
	public ExceptionTreeNode(String packageName, String simpleName) {
		this.packageName	= packageName;
		this.simpleName		= simpleName;
		this.children		= new CopyOnWriteArrayList<ExceptionTreeNode>();
	}
	
	public ExceptionTreeNode(String canonicalName) {
		int lp = canonicalName.lastIndexOf(".");
		this.packageName	= canonicalName.substring(0, lp);
		this.simpleName		= canonicalName.substring(lp+1, canonicalName.length());
		this.children		= new CopyOnWriteArrayList<ExceptionTreeNode>();
	}
	
	@XmlTransient
	public String getCanonicalName() {
		StringBuilder sb = new StringBuilder();
		sb.append(getPackageName()).append(".").append(getSimpleName());
		return sb.toString();
	}
	
	@XmlTransient
	public String getPackageName() {
		return this.packageName;
	}
	
	@XmlTransient
	public String getSimpleName() {
		return this.simpleName;
	}
	
	@XmlTransient
	public ExceptionTreeNode getParent() {
		return this.parent;
	}
	
	public void setParent(ExceptionTreeNode parent) {
		this.parent = parent;
	}
	
	@SuppressWarnings("unused")
	@XmlElementWrapper(name="children")
	@XmlElement(name="ExceptionTreeNode")
	private List<ExceptionTreeNode> getChildren() {
		return this.children;
	}
	
	public ExceptionTreeNode[] children() {
		return this.children.toArray(NULL_ARRAY);
	}
	
	public void setChildren(List<ExceptionTreeNode> list) {
		System.out.println("SL");
		
		if (list == null) return;
		for (ExceptionTreeNode n : list) {
			n.setParent(this);
		}
		this.children.addAll(list);
	}
	
	public void setChildren(ExceptionTreeNode[] list) {
		System.out.println("SA");
		
		if (list == null) return;
		for (ExceptionTreeNode n : list) {
			n.setParent(this);
			this.children.add(n);
		}
	}
	
	public void addChild(ExceptionTreeNode child) {
		if (child == null) return;
		ExceptionTreeNode old = child.getParent();
		if (old == this) return;
		if (old != null) old.removeChild(child);
		child.setParent(this);
		this.children.add(child);
	}
	
	public void removeChild(ExceptionTreeNode child) {
		if (child == null) return;
		this.children.remove(child);
		child.setParent(null);
	}
	
	public void removeAllChild() {
		if (isLeaf()) return;
		for (ExceptionTreeNode child : this.children) {
			child.setParent(null);
		}
		this.children.clear();
	}
	
	public void removeFromParent() {
		ExceptionTreeNode parent = getParent();
		if (parent != null) parent.removeChild(this);
	}
	
	public int getChildCount() {
		if (this.children == null) return 0;
		else return this.children.size();
	}
	
	public List<ExceptionTreeNode> getPath() {
		List<ExceptionTreeNode> retNodes = new LinkedList<ExceptionTreeNode>();
		getPathToRoot(this, retNodes);
		return retNodes;
	}
	
	private void getPathToRoot(ExceptionTreeNode aNode, List<ExceptionTreeNode> retNodes) {
		if (aNode == null) {
			return;
		} else {
			retNodes.add(0, aNode);
			getPathToRoot(aNode.getParent(), retNodes);
		}
	}
	
	public ExceptionTreeNode getRoot() {
		ExceptionTreeNode ancestor = this;
		ExceptionTreeNode previous;

		do {
			previous = ancestor;
			ancestor = ancestor.getParent();
		} while (ancestor != null);

		return previous;
	}
	
	public boolean isRoot() {
		return getParent() == null;
	}
	
	public boolean isLeaf() {
		return getChildCount() == 0;
	}

	public boolean isNodeChild(ExceptionTreeNode node) {
		boolean retval;

		if (node == null) {
			retval = false;
		} else {
			if (getChildCount() == 0) {
				retval = false;
			} else {
				retval = (node.getParent() == this);
			}
		}

		return retval;
	}

	public boolean isNodeAncestor(ExceptionTreeNode anotherNode) {
		if (anotherNode == null) return false;

		ExceptionTreeNode ancestor = this;

		do {
			if (ancestor == anotherNode) return true;
		} while((ancestor = ancestor.getParent()) != null);

		return false;
	}

	public boolean isNodeDescendant(ExceptionTreeNode anotherNode) {
		if (anotherNode == null) return false;
		return anotherNode.isNodeAncestor(this);
	}
	
	public int getSiblingCount() {
		ExceptionTreeNode myParent = getParent();
		if (myParent == null) return 1;
		else return myParent.getChildCount();
	}
	
	public int getLevel() {
		ExceptionTreeNode ancestor;
		int levels = 0;

		ancestor = this;
		while((ancestor = ancestor.getParent()) != null){
			levels++;
		}

		return levels;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ExceptionNode[");
		sb.append("canonical-name=").append(getCanonicalName()).append(",");
		sb.append("parent=");
		if (isRoot()) sb.append("NULL");
		else sb.append(getParent().getCanonicalName());
		sb.append(",");
		sb.append("children=(");
		ExceptionTreeNode[] list = children();
		for (int i=0; i<list.length; i++) {
			sb.append(list[i]);
			if (i < list.length-1) sb.append(",");
		}
		sb.append(")");
		sb.append("]");
		return sb.toString();
	}
	
	public String toXML() {
		StringWriter writer = new StringWriter();
		JAXB.marshal(this, writer);
		return writer.toString();
	}
}

class ExceptionTreeNodeAdapter extends XmlAdapter<ExceptionTreeNode, ExceptionTreeNode> {
	@Override
	public ExceptionTreeNode marshal(ExceptionTreeNode v) throws Exception {
		if (v == null) return null;
		return new ExceptionTreeNode(v.getPackageName(), v.getSimpleName());
	}

	@Override
	public ExceptionTreeNode unmarshal(ExceptionTreeNode v) throws Exception {
		return v;
	}
}

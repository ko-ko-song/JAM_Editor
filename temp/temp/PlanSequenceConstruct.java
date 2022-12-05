package kr.ac.uos.ai.editor.jam.temp;

import java.util.Vector;

public class PlanSequenceConstruct extends PlanConstruct{
	//
	// Members
	//
	protected Vector<PlanConstruct> _constructs; // Vector of Constructs

	//
	// Constructors
	//
	public PlanSequenceConstruct() {
		_constructs = new Vector<PlanConstruct>(1, 1);
		_constructType = PLAN_SEQUENCE;
	}

	public PlanSequenceConstruct(PlanConstruct be) {
		_constructs = new Vector<PlanConstruct>(1, 1);
		insertConstruct(be);
		_constructType = PLAN_SEQUENCE;
	}

	//	
	// Member functions
	//
	public Vector<PlanConstruct> getConstructs() {
		return _constructs;
	}

	public int getNumConstructs() {
		return _constructs.size();
	}

	public void insertConstruct(PlanConstruct be) {
		if (be != null) {
			_constructs.addElement(be);
		}
	}

	public PlanConstruct getConstruct(int n) {
		try {
			return (PlanConstruct) _constructs.elementAt(n);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

}

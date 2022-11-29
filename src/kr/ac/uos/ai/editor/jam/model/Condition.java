package kr.ac.uos.ai.editor.jam.model;

public abstract class Condition {
	
	
//	public final static int COND_GOAL 			= 1;
	public final static int COND_EXP 			= 2;
	public final static int COND_FACT 			= 3;
	public final static int COND_RETRIEVE 		= 4;
	
	
	public abstract int getType();
	
	
}

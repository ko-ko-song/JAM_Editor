package uos.ai.jam.plan.constructor;

import uos.ai.jam.expression.Variable;

/**
 * PlanTryCatch Entry class
 * 
 * @author	Hyunju Shin <jetzt82@gmail.com>
 * @since	2.0
 * @update	2.0
 */
public class PlanTryCatchEntry {
	public static final PlanTryCatchEntry[]		NULL_ARRAY	= new PlanTryCatchEntry[0];
	
	private final String		en;
	private final Variable		ev;
	private final PlanConstruct	cc;
	
	public PlanTryCatchEntry(String exceptionName, Variable bind, PlanConstruct catchConstruct) {
		this.en		= exceptionName;
		this.ev		= bind;
		this.cc		= catchConstruct;
	}

	public String			getExceptionName()		{return this.en;}
	public Variable			getBindVariable()		{return this.ev;}
	public PlanConstruct	getCatchConstruct()		{return this.cc;}
}

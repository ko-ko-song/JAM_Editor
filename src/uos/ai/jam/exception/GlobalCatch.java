package uos.ai.jam.exception;

import uos.ai.jam.expression.Variable;
import uos.ai.jam.plan.constructor.PlanConstruct;

/**
 * Global catch block을 표현하기 위한 객체
 * 
 * @author	Hyunju Shin <jetzt82@gmail.com>
 * @since	2.0
 * @update	2.0
 */
public class GlobalCatch {
	public static final GlobalCatch[]	NULL_ARRAY	= new GlobalCatch[0];
	
	private final String			exception;
	private final Variable			bind;
	private final PlanConstruct		body;
	
	public GlobalCatch(String exception, Variable bind, PlanConstruct body) {
		this.exception	= exception;
		this.bind		= bind;
		this.body		= body;
	}
	
	public String			getExceptionName()		{return this.exception;}
	public Variable			getBindVariable()		{return this.bind;}
	public PlanConstruct	getBodyConstruct()		{return this.body;}
}

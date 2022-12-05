package uos.ai.jam.plan.constructor;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.exception.ExceptionManager;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Value;

/**
 * PlanRuntimeTryCatchState class
 * 
 * @author	Hyunju Shin <jetzt82@gmail.com>
 * @since	2.0
 * @update	2.0
 */
public class PlanRuntimeTryCatchState extends PlanRuntimeState {
	private static final long serialVersionUID = -2763044049671283299L;
	
	private static enum BodyStatus {
		INCOMPLETE,
		COMPLETE,
		FAILED,
		EXCEPTION
	}
	
	private final PlanRuntimeState			tryBody;
	private final PlanTryCatchEntry[]		catchList;

	private BodyStatus						bodyStatus;
	private PlanRuntimeState				handleBlock;

	public PlanRuntimeTryCatchState(PlanTryCatchConstruct construct) {
		this.tryBody		= construct.getTryPlanConstruct().newRuntimeState();
		this.catchList		= construct.getCatchEntry();
		this.bodyStatus		= BodyStatus.INCOMPLETE;
	}
	
	@Override
	public int execute(Binding b, Goal thisGoal, Goal prevGoal) throws AgentRuntimeException {
		switch (this.bodyStatus) {
		case COMPLETE:		return PLAN_CONSTRUCT_COMPLETE;
		case FAILED:		return PLAN_CONSTRUCT_FAILED;
		case INCOMPLETE:	return executeTryBody(b, thisGoal, prevGoal);
		case EXCEPTION:		return handleException(b, thisGoal, prevGoal);
		}
		
		this.bodyStatus = BodyStatus.COMPLETE;
		return PLAN_CONSTRUCT_COMPLETE;
	}
	
	private int executeTryBody(Binding b, Goal thisGoal, Goal prevGoal) throws AgentRuntimeException {
		int status = PLAN_CONSTRUCT_INCOMP;
		try {
			status = this.tryBody.execute(b, thisGoal, prevGoal);
		} catch (AgentRuntimeException e) {
			PlanTryCatchEntry entry = findEntry(thisGoal, e);
			if (entry == null) {
				throw e;
			} else {
				this.handleBlock = entry.getCatchConstruct().newRuntimeState();
				b.setValue(entry.getBindVariable(), new Value(e));
				this.bodyStatus = BodyStatus.EXCEPTION;
				return PLAN_CONSTRUCT_INCOMP;
			}
		}
		switch (status) {
		case PLAN_CONSTRUCT_COMPLETE:	this.bodyStatus = BodyStatus.COMPLETE;return PLAN_CONSTRUCT_COMPLETE;
		case PLAN_CONSTRUCT_FAILED:	this.bodyStatus = BodyStatus.FAILED;return PLAN_CONSTRUCT_FAILED;
		}
		this.bodyStatus = BodyStatus.INCOMPLETE;
		return PLAN_CONSTRUCT_INCOMP;
	}
	
	private PlanTryCatchEntry findEntry(Goal thisGoal, AgentRuntimeException e) {
		Interpreter interpreter = thisGoal.getIntentionStructure().getInterpreter();
		
		ExceptionManager em = interpreter.getExceptionManager();
		for (PlanTryCatchEntry entry : this.catchList) {
			if (em.isDescendantOrEqual(e, entry.getExceptionName())) return entry;
		}
		
		return null;
	}
	
	private int handleException(Binding b, Goal thisGoal, Goal prevGoal) throws AgentRuntimeException {
		return this.handleBlock.execute(b, thisGoal, prevGoal);
	}
}

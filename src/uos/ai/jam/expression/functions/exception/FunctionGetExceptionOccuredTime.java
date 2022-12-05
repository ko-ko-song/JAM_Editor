package uos.ai.jam.expression.functions.exception;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Value;

public class FunctionGetExceptionOccuredTime extends AbstractExceptionFunction {
	@Override
	public String getName() {
		return "exGetExceptionSimpleName";
	}
	
	@Override
	protected Integer getLimitMax() {
		return new Integer(1);
	}

	@Override
	protected Integer getLimitMin() {
		return null;
	}

	@Override
	public Value _execute(Interpreter i, Goal g, Binding b,
			Expression[] args, AgentRuntimeException ex) throws AgentRuntimeException {
		long time = ex.getOccuredTime();
		return new Value(time);
	}
}

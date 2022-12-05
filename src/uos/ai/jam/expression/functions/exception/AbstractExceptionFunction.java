package uos.ai.jam.expression.functions.exception;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;

public abstract class AbstractExceptionFunction  implements Function {
	protected static final Expression[]		EXP_NULL_ARRAY	= new Expression[0];
	
	public abstract String getName();
	
	protected abstract Integer getLimitMin();
	protected abstract Integer getLimitMax();

	public final Value execute(Interpreter i, Goal g, Binding b, Expression... args) throws AgentRuntimeException {
		if (args == null) args = EXP_NULL_ARRAY;
		if (!checkArity(args)) {
			System.out.println("Invalid number of arguments: " + args.length + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}
		
		AgentRuntimeException ex = toAgentRuntimeException(args[0].eval(b));
		if (ex == null) return Value.FALSE;
		
		Value v = _execute(i, g, b, args, ex);
		if (args.length >= 2) bindToVariable(b, args[1], v);
		return v;
	}
	
	protected abstract Value _execute(Interpreter i, Goal g, Binding b,
			Expression[] args, AgentRuntimeException ex) throws AgentRuntimeException;
	
	private final boolean checkArity(Expression... args) {
		int arity = args.length;
		if (getLimitMin() != null) {
			if (arity < getLimitMin().intValue()) return false;
		}
		
		if (getLimitMax() != null) {
			if (arity > getLimitMax().intValue()) return false;
		}
		
		return true;
	}
	
	private final AgentRuntimeException toAgentRuntimeException(Value value) {
		if (value == null) return null;
		Object exObj = value.getObject();
		if (exObj == null) return null;
		if (exObj instanceof AgentRuntimeException) return (AgentRuntimeException) exObj;
		else return null;
	}
	
	protected final void bindToVariable(Binding b, Expression e, Value v) {
		if (b == null || b == null || v == null) return;
		if (!e.isVariable()) return;
		b.setValue(e, v);
	}
}

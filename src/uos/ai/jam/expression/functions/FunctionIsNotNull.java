package uos.ai.jam.expression.functions;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;

public class FunctionIsNotNull implements Function {
	public Value execute(Interpreter i, Goal g, Binding b, Expression... args) throws AgentRuntimeException {
		int arity = (args != null) ? args.length : 0;
		if (arity == 0) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}
		
		Value v = args[0].eval(b);
		if (v != null && v.isDefined()) return Value.TRUE;
		else return Value.FALSE;
	}

	public String getName() {
		return "isNotNull";
	}
}
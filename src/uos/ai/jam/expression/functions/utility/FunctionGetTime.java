package uos.ai.jam.expression.functions.utility;

import java.util.Date;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;

public class FunctionGetTime implements Function {
	
	public String getName() {
		return "getTime";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// Get the current time
		// [Modified from Chip McVey at Johns Hopkins University's Applied Physics Lab]
		//
		int arity = (args != null) ? args.length : 0;
		if (arity == 0) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}

		Expression exp = args[0];

		Date date = new Date();
		binding.setValue(exp, new Value(date.getTime()));

		return Value.TRUE;
	}
}

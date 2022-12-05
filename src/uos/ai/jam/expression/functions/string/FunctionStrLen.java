package uos.ai.jam.expression.functions.string;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;

public class FunctionStrLen implements Function {
	
	public String getName() {
		return "strlen";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// String Length
		// [Modified from Chip McVey at Johns Hopkins University's Applied Physics Lab]
		//
		int arity = (args != null) ? args.length : 0;
		if ((arity == 0) || (arity > 2)) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}

		String bigString = args[0].eval(binding).getString();

		if (arity == 1) {
			return new Value(bigString.length());
		} else { // (arity == 2)
			binding.setValue(args[1], new Value(bigString.length()));
		}

		return Value.TRUE;
	}
}

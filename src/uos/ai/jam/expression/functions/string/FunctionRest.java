package uos.ai.jam.expression.functions.string;

import java.util.StringTokenizer;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;

public class FunctionRest implements Function {
	
	public String getName() {
		return "rest";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// Remaining string tokens
		// [Modified from Chip McVey at Johns Hopkins University's Applied Physics Lab]
		//
		int arity = (args != null) ? args.length : 0;
		if ((arity == 0) || (arity > 2)) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}
		
		String bigString = args[0].eval(binding).getString();
		StringTokenizer st = new StringTokenizer(bigString);

		String first = null;
		if (st.hasMoreTokens()) {
			first = st.nextToken();
		} else {
			return Value.FALSE;
		}

		Value result = null;
		if (bigString.length() >= (bigString.lastIndexOf(first)	+ first.length() + 1)) {
			result = new Value(bigString.substring(bigString.lastIndexOf(first)	+ first.length() + 1));
		} else {
			result = new Value("-");

		}

		if (arity == 1) {
			return result;
		} else { // (arity == 2)
			binding.setValue(args[1], result);
		}

		return Value.TRUE;
	}
}

package uos.ai.jam.expression.functions;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;

public class FunctionExec implements Function {
	
	public String getName() {
		return "exec";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// Execute the program and args passed in as a string
		//
		// In:String execString - The program and arguments
		//
		int arity = (args != null) ? args.length : 0;
		if (arity != 1) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}

		Expression exp = args[0];
		String execString = exp.eval(binding).getString();
		Runtime r = Runtime.getRuntime();
		try {
			r.exec(execString);
		} catch (java.io.IOException ie) {
			System.out.println(ie);
			return Value.FALSE;
		}

		return Value.TRUE;
	}
}

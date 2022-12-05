package uos.ai.jam.expression.functions.utility;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;
import uos.ai.jam.expression.ValueType;

public class FunctionSleep implements Function {
	
	public String getName() {
		return "sleep";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// Delay
		//
		int arity = (args != null) ? args.length : 0;
		if (arity == 0) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}

		for (Expression arg : args) {
			Value value = arg.eval(binding);
			if (value.type() == ValueType.LONG) {
				long second = value.getLong();
				System.out.println("SystemFunction: waiting " + second + " seconds.");
				try {
					Thread.sleep(second * 1000);
				} catch (InterruptedException e) {
					System.out.println("Interrupted sleep primitive!");
				}
			}
		}

		return Value.TRUE;
	}
}

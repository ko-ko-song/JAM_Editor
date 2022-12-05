package uos.ai.jam.expression.functions.relation;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;

public class FunctionLT implements Function {
	
	public String getName() {
		return "<";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// Smaller cardinality
		//
		int arity = (args != null) ? args.length : 0;
		if (arity != 2) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}

		Expression exp1 = args[0];
		Expression exp2 = args[1];
		
		return (exp1.lessthan(exp2, binding)) ? Value.TRUE : Value.FALSE;
	}
}

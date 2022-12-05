package uos.ai.jam.expression.functions.meta;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;
import uos.ai.jam.plan.Plan;

public class FunctionGetPlanAttributes implements Function {
	
	public String getName() {
		return "getPlanAttributes";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// Get the attributes for the Plan
		//
		int arity = (args != null) ? args.length : 0;
		if (arity != 2) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}

		Expression exp = args[0];
		Plan p = (Plan) exp.eval(binding).getObject();

		exp = args[1];
		binding.setValue(exp, new Value(p.getAttributes()));
		
		return Value.TRUE;
	}
}

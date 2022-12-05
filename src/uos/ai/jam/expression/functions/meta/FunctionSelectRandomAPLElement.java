package uos.ai.jam.expression.functions.meta;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;
import uos.ai.jam.plan.APL;
import uos.ai.jam.plan.APLElement;

public class FunctionSelectRandomAPLElement implements Function {
	
	public String getName() {
		return "selectRandomAPLElement";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// Select a random APL element
		//
		int arity = (args != null) ? args.length : 0;
		if (arity != 2) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}

		Expression exp = args[0];
		APL a = (APL) exp.eval(binding).getObject();

		APLElement selectedElement = a.getUtilityRandom();
		exp = args[1];
		binding.setValue(exp, new Value(selectedElement));

		return Value.TRUE;
	}
}

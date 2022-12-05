package uos.ai.jam.expression.functions.debug;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.JAM;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;

public class FunctionSetShowWorldModel implements Function {
	
	public String getName() {
		return "setShowWorldModel";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// Turn debug information on/off
		//
		int arity = (args != null) ? args.length : 0;
		if (arity != 1) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}

		Expression exp = args[0];
		JAM.setShowWorldModel(exp.eval(binding).isTrue());
		System.out.println("Showing World Model changes: " + JAM.getShowWorldModel());
		
		return Value.TRUE;
	}
}

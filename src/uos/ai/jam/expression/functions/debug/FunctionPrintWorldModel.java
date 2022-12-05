package uos.ai.jam.expression.functions.debug;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;

public class FunctionPrintWorldModel implements Function {
	
	public String getName() {
		return "printWorldModel";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// Display current world model state
		//
		interpreter.getWorldModel().print(System.out);
		return Value.TRUE;
	}
}

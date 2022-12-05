package uos.ai.jam.expression.functions.meta;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;
import uos.ai.jam.plan.APLElement;

public class FunctionPrintAPLElement implements Function {
	
	public String getName() {
		return "printAPLElement";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// Print the passed-in APL Element
		//
		int arity = (args != null) ? args.length : 0;
		if (arity != 1) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}

		Expression exp = args[0];
		APLElement a = (APLElement) exp.eval(binding).getObject();
		a.print(System.out);

		return Value.TRUE;
	}
}

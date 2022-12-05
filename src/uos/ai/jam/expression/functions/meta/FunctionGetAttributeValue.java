package uos.ai.jam.expression.functions.meta;

import java.util.StringTokenizer;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;
import uos.ai.jam.plan.APLElement;

public class FunctionGetAttributeValue implements Function {
	
	public String getName() {
		return "getAttributeValue";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// Get the value for a specific attribute for an APL Element
		//
		// NOTE: Assumes values are numeric and returns -1 as a special
		// return code to indicate that the attribute was not found.
		//
		// Needs more protection against improper parameters!!!!
		//
		int arity = (args != null) ? args.length : 0;
		if (arity != 3) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}

		Expression exp = args[0];
		APLElement a = (APLElement) exp.eval(binding).getObject();
		exp = args[1];
		String attribute = exp.eval(binding).getString();
		exp = args[2];

		// Find the specified Attribute
		String attributes = a.getPlan().getAttributes();
		int index = attributes.indexOf(attribute);

		if (index == -1) {
			binding.setValue(exp, new Value(-1));
			return Value.TRUE;
		}

		// Extract the corresponding value
		String tmp1 = attributes.substring(index);
		StringTokenizer st = new StringTokenizer(tmp1);

		// Advance to the token just after the attribute
		String tmp2 = st.nextToken();
		tmp2 = st.nextToken();
		System.out.println("getAttributeValue: value returned is " + tmp2);
		binding.setValue(exp, new Value(Double.valueOf(tmp2).doubleValue()));

		return Value.TRUE;
	}
}

package uos.ai.jam.expression.functions;

import java.io.BufferedReader;
import java.io.FileReader;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;
import uos.ai.jam.parser.JAMParser;

public class FunctionLoadPlan implements Function {

	public String getName() {
		return "loadPlan";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		/*
		 * Arguments:
		 * 		- pathname
		 */
		int arity = (args != null) ? args.length : 0;
		if (arity != 1) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}
		
		String pathname = args[0].eval(binding).getString();
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(pathname));
			String line = null;
			while((line = reader.readLine()) != null) {
				builder.append(line).append("\n");
			}
		} catch(Exception e) {
			e.printStackTrace();
			return Value.FALSE;
		}
		if (JAMParser.parseString(interpreter, builder.toString()) == null) {
			return Value.FALSE;
		}
		System.out.println("loadPlan: " + pathname);
		return Value.TRUE;
	}
}

package uos.ai.jam.expression.functions.communication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;

public class FunctionCheckpointAgentToFile implements Function {
	
	public String getName() {
		return "checkpointAgentToFile";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// Save a "checkpoint" of this Jam agent's run-time state to
		// a file. This has a number of possible uses, including to be
		// used later to recover from an agent failure, moved to
		// another platform to implement migration, or instantiated
		// on the local machine to create a "clone".
		//
		// In:String filename - The file in which to store the serialized
		// agent
		//
		int arity = (args != null) ? args.length : 0;
		if (arity != 1) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}

		Expression exp = args[0];
		String filename = exp.eval(binding).getString();
		
		FileOutputStream fos;
		ObjectOutputStream out;
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(interpreter);
			return Value.TRUE;
		} catch (IOException e) {
			System.out.println("I/O Error *" + e + "* writing agent to " + "\"" + filename + "\"!");
			return Value.FALSE;
		}
	}
}

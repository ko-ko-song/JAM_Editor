package uos.ai.jam.expression.functions.communication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;

public class FunctionCheckpointAgent implements Function {
	
	public String getName() {
		return "checkpointAgent";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// Save a "checkpoint" of this Jam agent's run-time state to
		// an array of bytes.
		//
		// Out:ByteArrayOutputStream outStream - Store the serialized agent
		//
		int arity = (args != null) ? args.length : 0;
		if (arity != 1) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}

		Expression outArray = args[0];

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out;

		try {
			out = new ObjectOutputStream(baos);
			out.writeObject(interpreter);

			binding.setValue(outArray, new Value(baos.toByteArray()));

			return Value.TRUE;
		} catch (IOException e) {
			System.out.println("I/O Error *" + e + "* checkpointing agent!");
			return Value.FALSE;
		}
	}
}

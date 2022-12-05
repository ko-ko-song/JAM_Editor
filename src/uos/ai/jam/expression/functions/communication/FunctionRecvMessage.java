package uos.ai.jam.expression.functions.communication;

import java.io.DataInputStream;
import java.io.IOException;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;

public class FunctionRecvMessage implements Function {
	
	public String getName() {
		return "recvMessage";
	}

	@SuppressWarnings("deprecation")
	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// Receive a string-encoded message from another agent (note; blocking)
		// IN:DataInputStream inputStream
		// OUT:String message
		//
		int arity = (args != null) ? args.length : 0;
		if (arity != 2) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}

		Expression exp = args[0];
		DataInputStream sIn = (DataInputStream) exp.eval(binding).getObject();

		try {
			exp = args[1];
			String message = sIn.readLine();
			binding.setValue(exp, new Value(message));

			return Value.TRUE;
		} catch (IOException e) {
			System.out.println("JAM::ConnectToAgentAsServer:IOException : "
					+ e);
			return Value.FALSE;
		}
	}
}

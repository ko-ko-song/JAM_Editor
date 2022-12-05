package uos.ai.jam.expression.functions.communication;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Value;

public class FunctionConnectToAgentAsClient implements Function {
	
	public String getName() {
		return "connectToAgentAsClient";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) throws AgentRuntimeException {
		//
		// Make a connection to another agent as a client
		// IN:int port
		// IN:String hostName
		// OUT:DataInputStream inputStream
		// OUT:PrintWriter outputStream
		//
		int arity = (args != null) ? args.length : 0;
		if (arity != 4) {
			System.out.println("Invalid number of arguments: " + arity + " to function \"" + getName() + "\"\n");
			return Value.FALSE;
		}

		Expression exp = args[0];
		int port = (int) exp.eval(binding).getLong();

		exp = args[1];
		String host = exp.eval(binding).getString();

		DataInputStream in;
		// PrintWriter out;
		PrintStream out;
		Socket socket;

		try {
			socket = new Socket(host, port);

			in = new DataInputStream(socket.getInputStream());
			// out = new PrintWriter(socket.getOutputStream());
			out = new PrintStream(socket.getOutputStream());

			exp = args[2];
			binding.setValue(exp, new Value(in));
			exp = args[3];
			binding.setValue(exp, new Value(out));
			return Value.TRUE;
		} catch (IOException e) {
			System.out.println("JAM::ConnectToAgentAsClient:IOException : "	+ e);
			return Value.FALSE;
		}	
	}
}

package uos.ai.jam.expression.functions.communication;

import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.WorldModelTable;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Function;
import uos.ai.jam.expression.Relation;
import uos.ai.jam.expression.Value;
import uos.ai.jam.message.MessageQueue;

public class FunctionDequeueMessage implements Function {
	
	public String getName() {
		return "dequeueMessage";
	}

	public Value execute(Interpreter interpreter, Goal currentGoal, Binding binding, Expression... args) {
		MessageQueue queue = interpreter.getMessageQueue();
		Relation message = queue.dequeue();
		if (message == null) {
			return Value.FALSE;
		}
		WorldModelTable wm = interpreter.getWorldModel();
		wm.assertFact(message, null);
		return Value.TRUE;
	}
}

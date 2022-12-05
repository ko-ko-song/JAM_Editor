package kr.ac.uos.ai.editor.jam.expression;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import kr.ac.uos.ai.editor.jam.exception.AgentRuntimeException;


public class ObjectInvokeMethod extends Expression {
	private static final long serialVersionUID = 1L;

	private final Expression			_expression;
	private final String				_methodName;
	private final List<Expression>		_args;
	
	public ObjectInvokeMethod(Expression expression, String methodName, List<Expression> args) {
		_expression		= expression;
		_methodName		= methodName;
		_args			= (args != null) ? args : new LinkedList<Expression>();
	}
	
	public String getName() {
		return "ObjectInvokeMethod";
	}

	public ExpressionType getType() {
		return ExpressionType.OBJ_INVOKE_METHOD;
	}

	public Value eval(Binding b) throws AgentRuntimeException {
		Object object = _expression.eval(b).getObject();
//		return ReflectionUtil.callMethod(object, _methodName, _args, b);
		return null;
	}
	
	public void print(PrintStream s, Binding b) {
		try {
			eval(b).print(s, b);
		} catch (AgentRuntimeException e) {}
	}

	public void format(PrintStream s, Binding b) {
		try {
			eval(b).format(s, b);
		} catch (AgentRuntimeException e) {}
	}
}

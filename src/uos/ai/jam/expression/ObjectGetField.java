package uos.ai.jam.expression;

import java.io.PrintStream;

import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.util.ReflectionUtil;

public class ObjectGetField extends Expression {
	private static final long serialVersionUID = 1L;

	private final Expression		_expression;
	private final String			_fieldName;
	
	public ObjectGetField(Expression expression, String fieldName) {
		_expression 	= expression;
		_fieldName		= fieldName;
	}
	
	public String getName() {
		return "ObjectGetField";
	}

	public ExpressionType getType() {
		return ExpressionType.OBJ_GET_FIELD;
	}

	public Value eval(Binding b) throws AgentRuntimeException {
		Object object = _expression.eval(b).getObject();
		return ReflectionUtil.getField(object, _fieldName);
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

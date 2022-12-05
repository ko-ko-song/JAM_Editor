package uos.ai.jam.expression;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.util.ReflectionUtil;

public class ObjectCreation extends Expression {
	private static final long serialVersionUID = 1L;

	private final Class<?>				_clazz;
	private final List<Expression>		_args;
	
	public ObjectCreation(Class<?> clazz, List<Expression> args) {
		_clazz		= clazz;
		_args		= (args != null) ? args : new LinkedList<Expression>();
	}
	
	public Class<?> getObjectClass() {
		return _clazz;
	}
	
	public List<Expression> getObjectArgs() {
		return _args;
	}
	
	public ExpressionType getType() {
		return ExpressionType.OBJ_CREATION;
	}

	public String getName() {
		return "ObjectCreation";
	}

	public Value eval(Binding b) throws AgentRuntimeException {
		return ReflectionUtil.newInstance(_clazz, _args, b);
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

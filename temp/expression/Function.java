package kr.ac.uos.ai.editor.jam.expression;

import kr.ac.uos.ai.editor.jam.exception.AgentRuntimeException;

public interface Function {
	public String getName();
	public Value execute(Object interpreter, Object currentGoal, Binding binding, Expression... args) throws AgentRuntimeException;
}

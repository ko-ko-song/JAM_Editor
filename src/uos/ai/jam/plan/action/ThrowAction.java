package uos.ai.jam.plan.action;

import java.io.PrintStream;

import uos.ai.jam.Goal;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Value;
import uos.ai.jam.expression.ValueType;

/**
 * PLAN BODY 내에서 강제로 exception 던지는 action
 * 
 * @author	Hyunju Shin <jetzt82@gmail.com>
 * @since	2.0
 * @update	2.0
 */
public class ThrowAction extends Action {
	private static final long serialVersionUID = 6182587176547777120L;
	
	private final Expression	expression;
	
	private final String		canonicalName;
	private final Expression[]	args;
	
	public ThrowAction(Expression expr) {
		this.expression		= expr;
		this.canonicalName	= null;
		this.args			= Expression.NULL_ARRAY;
	}
	
	public ThrowAction(String canonicalName, Expression...args) {
		this.expression		= null;
		this.canonicalName	= canonicalName;
		this.args			= (args == null) ? Expression.NULL_ARRAY : args;
	}
	
	@Override
	public boolean isExecutableAction() {
		return true;
	}
	
	@Override
	public int execute(Binding b, Goal g) throws AgentRuntimeException {
		AgentRuntimeException ex = null;
		if (this.expression != null) {
			ex = expr2Exception(this.expression, b);
		} else {
			ex = newException(b);
		}
		if (ex == null) return ACT_FAILED;
		throw ex;
	}
	
	private AgentRuntimeException newException(Binding b) {
		if (this.canonicalName == null) return null;
		
		AgentRuntimeException cause = null;
		String msg = null;
		if (this.args.length == 1) {
			Value v = evalValue(this.args[0], b);
			if (v != null) {
				if (v.type() == ValueType.OBJECT) cause = value2Exception(v);
				else if (v.type() == ValueType.STRING) msg = v.getString();
			}
		} else if (this.args.length == 2) {
			cause = expr2Exception(this.args[0], b);
			msg = expr2String(this.args[1], b);
		}
		
		return new AgentRuntimeException(this.canonicalName, cause, msg);
	}
	
	private AgentRuntimeException expr2Exception(Expression expr, Binding b) {
		if (expr == null) return null;
		return value2Exception(evalValue(expr, b));
	}
	
	private AgentRuntimeException value2Exception(Value v) {
		if (v == null) return null;
		Object obj = v.getObject();
		if (v == null) return null;
		if (obj instanceof AgentRuntimeException) {
			return (AgentRuntimeException) obj;
		} else {
			return null;
		}
	}
	
	private String expr2String(Expression expr, Binding b) {
		if (expr == null) return null;
		Value v = evalValue(expr, b);
		if (v == null) return null;
		return v.getString();
	}
	
	private Value evalValue(Expression expr, Binding b) {
		try {
			return expr.eval(b);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public void format(PrintStream s, Binding b) {
		s.print("throw new ");
		if (this.expression != null) {
			this.expression.format(s, b);
		} else {
			s.print(this.canonicalName);
			s.print("(");
			int length = this.args.length;
			for (int i=0; i<length; i++) {
				this.args[i].format(s, b);
				if (i < length-1) s.print(", ");
			}
			s.print(")");
		}
	}
}

package uos.ai.jam.exception;

/**
 * Agent 내에서 동적으로 발생하는 Exception을 표현하는 객체
 * 
 * @author	Hyunju Shin <jetzt82@gmail.com>
 * @since	2.0
 * @update	2.0
 */
public class AgentRuntimeException extends Exception {
	private static final long serialVersionUID = -4174566036057460684L;
	
	public static final AgentRuntimeException[]	NULL_ARRAY	= new AgentRuntimeException[0];

	private final String					canonicalName;
	private final long						occuredTime;
	
	private final AgentRuntimeException		cause;
	
	public AgentRuntimeException(String canonicalName) {
		super();
		this.canonicalName	= canonicalName;
		this.occuredTime	= System.currentTimeMillis();
		this.cause			= null;
	}
	
	public AgentRuntimeException(String canonicalName, String msg) {
		super(msg);
		this.canonicalName	= canonicalName;
		this.occuredTime	= System.currentTimeMillis();
		this.cause			= null;
	}
	
	public AgentRuntimeException(String canonicalName, AgentRuntimeException cause) {
		super();
		this.canonicalName	= canonicalName;
		this.occuredTime	= System.currentTimeMillis();
		this.cause			= cause;
	}
	
	public AgentRuntimeException(String canonicalName, AgentRuntimeException cause, String msg) {
		super(msg);
		this.canonicalName	= canonicalName;
		this.occuredTime	= System.currentTimeMillis();
		this.cause			= cause;
	}
	
	public String	getCanonicalName()		{return this.canonicalName;}
	public long		getOccuredTime()		{return this.occuredTime;}
	
	public String getPackageName() {
		int lp = this.canonicalName.lastIndexOf(".");
		return this.canonicalName.substring(0, lp-1);
	}
	
	public String getSimpleName() {
		int lp = this.canonicalName.lastIndexOf(".");
		return this.canonicalName.substring(lp+1, this.canonicalName.length());
	}
	
	public AgentRuntimeException cause() {
		return this.cause;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("AgentRuntimeException");
		sb.append("[");
		sb.append("canonicalName=").append(this.canonicalName).append(",");
		sb.append("occuredTime=").append(this.occuredTime).append(",");
		sb.append("message=\"").append(getMessage()).append("\"");
		sb.append("]");
		return sb.toString();
	}
}

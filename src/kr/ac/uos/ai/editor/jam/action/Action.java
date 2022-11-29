package kr.ac.uos.ai.editor.jam.action;

public class Action {
	
	
	public final static int ACT_UNDEFINED = -1;
	public final static int ACT_CANNOT_EXECUTE = -2;
	public final static int ACT_FAILED = -3;
	public final static int ACT_SUCCEEDED = -4;
	public final static int ACT_PRIMITIVE = 1;
	public final static int ACT_LOAD = 2;
	public final static int ACT_PARSE = 3;
	public final static int ACT_ASSIGN = 4;
	public final static int ACT_FACT = 5;
	public final static int ACT_RETRIEVE = 6;
	public final static int ACT_TEST = 7;
	public final static int ACT_ASSERT = 8;
	public final static int ACT_FAIL = 9;
	public final static int ACT_RETRACT = 10;
	public final static int ACT_UPDATE = 11;
	public final static int ACT_POST = 12;
	public final static int ACT_UNPOST = 13;
	public final static int ACT_GOAL_ACTION = 14;
	public final static int ACT_ACHIEVE = 15;
	public final static int ACT_MAINTAIN = 16;
	public final static int ACT_WAIT = 17;
	public final static int ACT_QUERY = 18;
	public final static int ACT_OBJECT = 19;
	public final static int ACT_PERFORM = 20;
	public final static int ACT_CONCLUDE = 21;

	protected String name;
	protected String file;
	protected int line;
	
	protected int actType;
	
	public Action() {
		actType = ACT_UNDEFINED;
	}
	
	public Action(String name) {
		name = new String(name);
		actType = ACT_UNDEFINED;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getActType() {
		return actType;
	}

	public void setActType(int actType) {
		this.actType = actType;
	}
	
	
	
}

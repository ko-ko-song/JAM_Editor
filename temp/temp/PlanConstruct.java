package kr.ac.uos.ai.editor.jam.temp;

public class PlanConstruct {
	private static final long serialVersionUID = 1622511813936818401L;

	//
	// Members
	//
	public final static int PLAN_UNDEFINED 		= 0;
	public final static int PLAN_SEQUENCE 		= 1;
	public final static int PLAN_SIMPLE 		= 2;
	public final static int PLAN_BRANCH 		= 3;
	public final static int PLAN_WHEN 			= 4;
	public final static int PLAN_WHILE 			= 5;
	public final static int PLAN_DO 			= 6;
	public final static int PLAN_ATOMIC 		= 7;
	public final static int PLAN_PARALLEL 		= 8;
	public final static int PLAN_DOANY 			= 9;
	public final static int PLAN_DOALL 			= 10;
	public final static int PLAN_WAIT 			= 11;

	protected int _constructType;

	public int getType() {
		return _constructType;
	}

}

package uos.ai.jam.plan.constructor;

/**
 * try ~ catch construct를 표현하는 객체
 * 
 * @author	Hyunju Shin <jetzt82@gmail.com>
 * @since	2.0
 * @update	2.0
 */
public class PlanTryCatchConstruct extends PlanConstruct {
	private static final long serialVersionUID = -3621947784978704122L;

	private final PlanConstruct				tryConstruct;
	private final PlanTryCatchEntry[]		catchList;
	
	public PlanTryCatchConstruct(PlanConstruct tryConstruct, PlanTryCatchEntry[] catchList) {
		this.tryConstruct	= tryConstruct;
		this.catchList		= catchList;
		
		if (!checkCatchList()) {
			throw new IllegalArgumentException("Illegal catch Exception!");
		}
	}
	
	private boolean checkCatchList() {
		if (this.catchList.length <= 0) return false;
		
		int length = this.catchList.length;
		for (int i=0; i<length; i++) {
			String ni = this.catchList[i].getExceptionName();
			for (int j=i+1;j<length; j++) {
				String nj = this.catchList[j].getExceptionName();
				if (ni.equalsIgnoreCase(nj)) return false;
			}
		}
		
		return true;
	}

	public PlanConstruct getTryPlanConstruct() {
		return this.tryConstruct;
	}
	
	public PlanTryCatchEntry[] getCatchEntry() {
		return this.catchList;
	}

	@Override
	public PlanRuntimeState newRuntimeState() {
		return new PlanRuntimeTryCatchState(this);
	}
}

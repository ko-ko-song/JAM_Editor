package uos.ai.jam;

import uos.ai.jam.plan.APLElement;

public interface IntentionStructureChangeListener {
	public void goalAdded(Goal goal);
	public void goalRemoved(Goal goal);
	public void intended(APLElement goal);
}

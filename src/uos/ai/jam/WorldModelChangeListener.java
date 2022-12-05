package uos.ai.jam;

import uos.ai.jam.expression.Relation;

public interface WorldModelChangeListener {
	public void worldModelChanged(Relation[] retracted, Relation asserted);
}

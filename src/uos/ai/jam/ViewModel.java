package uos.ai.jam;

import java.util.ArrayList;

public class ViewModel {
	private Object[][] worldModelList;
	private Object[][] intentionList;

	public ViewModel() {

	}

	public void updateData(IntentionStructure intention, WorldModelTable worldModel){
		
		
		worldModelList = worldModel.getWorldModelName();
		intentionList = new String[intention.getStacks().size()][2];
		
	
		//should updated by another way - need change
		
		for(int i = 0; i < intention.getStacks().size();i++){
			intentionList[i][0] = intention.getStacks().get(i).getName();
			intentionList[i][1] = Integer.toString(intention.getStacks().get(i).getStatus());
		}
		
		
	}

	public Object[][] getWorldModelList() {
		return worldModelList;
	}

	public void setWorldModelList(Object[][] worldModelList) {
		this.worldModelList = worldModelList;
	}

	public Object[][] getIntentionList() {
		return intentionList;
	}

	public void setIntentionList(Object[][] intentionList) {
		this.intentionList = intentionList;
	}



	
	
}

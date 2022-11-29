package kr.ac.uos.ai.editor.jam.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kr.ac.uos.ai.editor.jam.action.GoalAction;

public class PlanTable {
	
	private Map<String, List<Plan>> goalNameByPlanTable;
	private Map<String, List<Plan>> fileByPlanTable;  
	
	public PlanTable() {
		goalNameByPlanTable = new HashMap<String, List<Plan>>();
		fileByPlanTable = new HashMap<String, List<Plan>>();
	}
	
	public boolean add(Plan plan) {
		if(plan == null)
			return false;
			
		if(!this.planIDCheck(plan)) 
			return false;
		
		if(plan.getGoalAction() != null) {
			String goalName = plan.getGoalAction().getName();
			List<Plan> plans = goalNameByPlanTable.get(goalName);
			if(plans == null) {
				plans = new LinkedList<Plan>();
				goalNameByPlanTable.put(goalName, plans);
			}
			plans.add(plan);
			
			String fileName = plan.get_fileName();
			List<Plan> plansFromFile = fileByPlanTable.get(fileName);
			if(plansFromFile == null) {
				plansFromFile = new LinkedList<Plan>();
				fileByPlanTable.put(fileName, plansFromFile);
			}
			plansFromFile.add(plan);
			
			return true;
		}else {
			System.out.println("goal action is undefined");
			return false;
		}
	}

	private boolean planIDCheck(Plan plan) {
		boolean isValidity = true;
		
		if(plan.getId() == null) {
			System.out.println("plan ID not found.");
			isValidity = false;
		}
		else {
			for (List<Plan> plans: goalNameByPlanTable.values()) {
				for(int i=0; i<plans.size(); i++) {
					Plan planToCompare = plans.get(i);
					if(planToCompare.getId().equals(plan.getId())) {
						isValidity = false;
						System.out.println("plan ID duplicated.");
						return false;
					}
				}
			}
		}
		return isValidity;
	}
	
	/**
	 * plan table에서 id로부터 PLAN을 가져옴 
	 */
	public Plan getPlan(String id) {
		for (List<Plan> planList : goalNameByPlanTable.values()) {
			for (Plan plan : planList) {
				if (plan.getId() != null && plan.getId().equals(id)) {
					return plan;
				}
			}
		}
		return null;
	}
	
	/**
	 * file name으로 부터 plan을 가져옴 
	 */
	public List<Plan> getPlansFromFileName(String fileName){
		return fileByPlanTable.get(fileName);
	}

	/**
	 * plan table에서 같은 goal name을 갖는 모든 PLAN을 가져옴
	 */
	public List<Plan> getPlans(String goalName) {
		return goalNameByPlanTable.get(goalName);
	}

	/**
	 * plan table에 존재하는 모든 PLAN을 가져옴
	 */
	public List<Plan> getAllPlans() {
		List<Plan> result = new ArrayList<Plan>();
		for (List<Plan> planList : fileByPlanTable.values()) {
			for (Plan plan : planList) {
				result.add(plan);
			}
		}
		return result;
	}
	
	/**
	 * 해당 id의 plan을 plan table에서 지움
	 */
	public void removePlan(String id) {
		for (List<Plan> plans : goalNameByPlanTable.values()) {
			for (int i = 0; i < plans.size(); i++) {
				Plan plan = plans.get(i);
				if (plan.getId().equals(id)) {
					plans.remove(plan);
				}
			}
		}
		
		for (List<Plan> plans : fileByPlanTable.values()) {
			for (int i = 0; i < plans.size(); i++) {
				Plan plan = plans.get(i);
				if (plan.getId().equals(id)) {
					plans.remove(plan);
				}
			}
		}
	}
	
	/**
	 * Plan object를 plan table에서 지움
	 * @param plan
	 */
	public void removePlan(Plan plan) {
		List<Plan> plans = goalNameByPlanTable.get(plan.getGoalAction().getName());
		if(plans != null)
			plans.remove(plan);
		
		List<Plan> planByFiles = fileByPlanTable.get(plan.get_fileName());
		if(planByFiles != null)
			planByFiles.remove(plan);
	}
	
	public void printAllPlans() {
		StringBuilder sb = new StringBuilder();
		StringBuilder sbFormat = new StringBuilder();
		sbFormat.append("plan List---------------------------------------------------------------------------------------- \n");
		for (Plan plan : getAllPlans()) {
			sb.setLength(0);
			String goalName = ((GoalAction)plan.getGoalAction()).getRelation().getName();
			String[] args = ((GoalAction)plan.getGoalAction()).getRelation().getArgs();
			
			sb.append(goalName);
			sb.append("(");
			for (String arg : args) {
				sb.append(arg);
				sb.append(", ");
			}
			int length = sb.length();
			sb.delete(length -2, length);
			
			sb.append(")");
			sbFormat.append(String.format("%-60s %s %-30s %s %s", sb.toString(), "file name : ", plan.get_fileName(), "line : ", plan.get_line()));
			sbFormat.append("\n");
		}
		
		System.out.println(sbFormat.toString());
	}
	
	
	
		
}

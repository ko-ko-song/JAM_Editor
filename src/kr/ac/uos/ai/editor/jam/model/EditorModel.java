package kr.ac.uos.ai.editor.jam.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import uos.ai.jam.Interpreter;
import uos.ai.jam.NameSpace;
import uos.ai.jam.NameSpaceTable;
import uos.ai.jam.Prefix;
import uos.ai.jam.expression.Relation;
import uos.ai.jam.expression.condition.Condition;
import uos.ai.jam.expression.condition.RelationCondition;
import uos.ai.jam.parser.JAMParser;
import uos.ai.jam.plan.Plan;
import uos.ai.jam.plan.action.Action;
import uos.ai.jam.plan.action.UpdateAction;
import uos.ai.jam.plan.constructor.PlanConstruct;
import uos.ai.jam.plan.constructor.PlanSimpleConstruct;

public class EditorModel {
	
	private static EditorModel instance;
	private String projectName = "";
	
	private PlanTable 		planTable;
	private RelationTable 	relationTable;
//	private NameSpaceTable _nameSpaceTable;
	private PrefixManager	prefixManager;

	public EditorModel() {
		if(instance == null)
			instance = this;
		
	}
	
	public static EditorModel getInstance() {
		if(instance == null)
			instance = new EditorModel();
		
		return instance;
	}
	
	public PlanTable getPlanManager() {
		if(planTable == null)
			planTable = new PlanTable();
		
		return planTable;
	}
	
	public RelationTable getRelationManager() {
		if(relationTable == null)
			relationTable = new RelationTable();
		
		return relationTable;
	}
	
	public PrefixManager getPrefixManager() {
		if(prefixManager == null)
			prefixManager = new PrefixManager();
			
		return prefixManager;
	}
//
//	public NameSpaceTable getNameSpaceTable() {
//		if(_nameSpaceTable == null)
//			_nameSpaceTable = new NameSpaceTable();
//		
//		return _nameSpaceTable;
//	}
//	
//	public void addNameSpace(String url){
//		this._nameSpaceTable.addNameSpace(url);
//	}
	
	
	
	
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void init(String projectName) {
		System.out.println("model init");
		planTable = new PlanTable();
		relationTable = new RelationTable();
		prefixManager = new PrefixManager();
		
//		_nameSpaceTable = new NameSpaceTable();
		
		
		
		this.projectName = projectName;
		
	}

	public void deleteFileContentFromModel(String fileFullpath) {
		System.out.println("delete file event : " + fileFullpath);
		
		List<Plan> plans = planTable.getPlansFromFileName(fileFullpath);
		if(plans != null) {
			List<Plan> plansCopy = new ArrayList<>(plans);
			for (Plan plan : plansCopy) {
				planTable.removePlan(plan);
			}
		}
		
		
		List<Relation> relations = relationTable.getRelationsFromFile(fileFullpath);
		if(relations != null) {
			List<Relation> relationsCopy = new ArrayList<>(relations);
			for (Relation relation : relationsCopy) {
				relationTable.removeRelation(relation);
			}
		}

		
//		List<Prefix> prefixes = _nameSpaceTable.getPrefixes();
//		if(prefixes != null) {
//			List<Prefix> prefixesCopy = new ArrayList<>(prefixes);
//			for (Prefix prefix : prefixesCopy) {
//				prefix.get_fileName().equals(fileFullpath);
//				
//				_nameSpaceTable.remove(prefix);
//			}	
//		}
		
		List<Prefix> prefixes = prefixManager.getAllPrefixes();
		if(prefixes != null) {
			List<Prefix> prefixesCopy = new ArrayList<>(prefixes);
			for (Prefix prefix : prefixesCopy) {
				if(prefix.get_fileName().equals(fileFullpath)) 
					prefixManager.remove(prefix);
				
			}	
		}
	}
	
	public void addFileContentToModel(Interpreter interpreter) {
		if(interpreter == null)
			return;
//		System.out.println("add file event : " + fileFullpath);
		
		
		
		
		
		
        for (Plan plan : interpreter.getPlanLibrary().getGoalSpecPlans()) {
        	planTable.add(plan);

        	//conclude를 제외한 goal action의 plan의 relation 및 body에 assert relation에 해당하는 relation들을 저장
        	relationTable.add(plan.getGoalSpecification().getRelation());
        	if(plan.getBody() != null) {
        		for (PlanConstruct planConstruct : plan.getBody().getConstructs()) {
    				if(planConstruct.getType()== PlanConstruct.PLAN_SIMPLE) {
    					if(((PlanSimpleConstruct)planConstruct).getAction().getType() == Action.ACT_ASSERT){
    						relationTable.add(((PlanSimpleConstruct)planConstruct).getAction().getRelation());
    					}
    				}
    			}	
        	}
        }
        
        for (Plan plan : interpreter.getPlanLibrary().getConcludePlans()) {
        	planTable.add(plan);
        	
        	//conclude plan의 relation 및 body에 assert relation에 해당하는 relation들을 저장
        	relationTable.add(plan.getConcludeSpecification());
        	if(plan.getBody() != null) {
        	   	for (PlanConstruct planConstruct : plan.getBody().getConstructs()) {
    				if(planConstruct.getType()== PlanConstruct.PLAN_SIMPLE) {
    					if(((PlanSimpleConstruct)planConstruct).getAction().getType() == Action.ACT_ASSERT){
    						relationTable.add(((PlanSimpleConstruct)planConstruct).getAction().getRelation());
    					}
    					else if(((PlanSimpleConstruct)planConstruct).getAction().getType() == Action.ACT_UPDATE){
    						relationTable.add(((UpdateAction)((PlanSimpleConstruct)planConstruct).getAction()).getNewRelation());
    					}
    				}
    			}
        	}
        }
        
        for(Prefix prefix : interpreter.getNameSpaceTable().getPrefixes()) {
        	this.prefixManager.add(prefix);
        }
        
        
//        for (NameSpace ns : interpreter.getNameSpaceTable().getName_spaces_table()) {
//			this._nameSpaceTable.addNameSpace(ns);
//		}
//        
//        for (Entry<String, NameSpace> entrySet : interpreter.getNameSpaceTable().getPrefix_name_spaces_table().entrySet()) {
//			this._nameSpaceTable.addPrefix(entrySet.getKey(), entrySet.getValue());
//		}
	}
	
	
	/**
	 * 해당 파일의 relation이 model에 존재하는지 체크  
	 * @param interpreter 
	 */
	public List<Relation> checkRelationExist(String jamFilePath) {
		Interpreter interpreter = JAMParser.parseFile(null, jamFilePath);

		if(interpreter == null)
			return null;
		
		List<Relation> notExistRelations = new LinkedList<Relation>();
		
		List<Plan> plans = new LinkedList<>(interpreter.getPlanLibrary().getGoalSpecPlans());
		if(interpreter.getPlanLibrary().getConcludePlans() != null)
			plans.addAll(interpreter.getPlanLibrary().getConcludePlans());
		
		for (Plan plan : plans) {
			if(plan.getBody() != null) {
				for (PlanConstruct planConstruct : plan.getBody().getConstructs()) {
					if(planConstruct.getType()== PlanConstruct.PLAN_SIMPLE) {
						int actionType = ((PlanSimpleConstruct)planConstruct).getAction().getType();
						
						if(actionType == Action.ACT_ACHIEVE || actionType == Action.ACT_MAINTAIN || actionType == Action.ACT_PERFORM) {
							Relation relation = ((PlanSimpleConstruct)planConstruct).getAction().getRelation();
							if(!planTable.isExist(relation)) {
								notExistRelations.add(relation);
							}
						}
						
						if(actionType == Action.ACT_ASSERT || actionType == Action.ACT_FACT || actionType == Action.ACT_RETRACT 
								|| actionType == Action.ACT_RETRIEVE || actionType == Action.ACT_UPDATE) {
							
							Relation relation = ((PlanSimpleConstruct)planConstruct).getAction().getRelation();
							if(!relationTable.isExist(relation)) {
								notExistRelations.add(relation);
							}
						}
						
					}
				}
			}
				
			
			
			if(plan.getContext() != null) {
				for (Condition condition : plan.getContext().getConditions()) {
					int conditionType = condition.getType();
					if(conditionType == Condition.COND_FACT || conditionType == Condition.COND_RETRIEVE) {
						Relation r = ((RelationCondition)condition).getRelation();
						if(!relationTable.isExist(r))
							notExistRelations.add(r);
					}
				}
			}
				
			
			if(plan.getPrecondition() != null) {
				for (Condition condition : plan.getPrecondition().getConditions()) {
					int conditionType = condition.getType();
					if(conditionType == Condition.COND_FACT || conditionType == Condition.COND_RETRIEVE) {
						Relation r = ((RelationCondition)condition).getRelation();
						if(!relationTable.isExist(r))
							notExistRelations.add(r);
					}
				}
			}
		}

		return notExistRelations;
	}
	
	
	public void printEditorModel() {
		System.out.println("project : " + this.projectName + " -----------------------------------------------------------------------------------\n");
		planTable.printAllPlans();
        relationTable.printAllRelations();
        prefixManager.printAllPrefixes();
//        _nameSpaceTable.printAllPrefixes();
	}
	
}

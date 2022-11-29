package kr.ac.uos.ai.editor.jam.model;

import kr.ac.uos.ai.editor.jam.parser.JAMParser;

public class EditorModel {
	
	private static EditorModel instance;
	private String projectName = "";
	
	private PlanTable 		planTable;
	private RelationTable 	relationTable;
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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void init(String projectName) {
		planTable = new PlanTable();
		relationTable = new RelationTable();
		prefixManager = new PrefixManager();
		
		this.projectName = projectName;
		JAMParser.parseFiles(projectName + "/plan");
		this.printEditorModel();
	}
	
	public void printEditorModel() {
		System.out.println("project : " + this.projectName + " -----------------------------------------------------------------------------------\n");
		planTable.printAllPlans();
        relationTable.printAllRelations();
        prefixManager.printAllPrefixes();
	}
	
}

package kr.ac.uos.ai.editor.jam.model;

import java.util.LinkedList;
import java.util.List;


public class Plan {
	
	private String id;
	
	//plan goal action 
	private GoalAction goalAction;
	
	private String name;
	private String documentation;
	
	
	//expression, world model action�� body�ȿ� ���� �� ������, editor������ plan hierarchy�� ���� goal action�� ����
	private List<GoalAction> body;
	
	//body, precondition�� expression�� ������ ������ �𵨿��� ������ ���� �ʿ䰡 ����
	//���߿� call hierarchy�� ���� relation? goal name?�� ������ ������ ��
	private List<Relation> precondition;
	private List<Relation> context;
	
	private SymbolTable symbolTable;
	//��� ���� �ʴµ� parser���� expression �Ľ��ϴ��� ����� expression �Ľ��� ���� �� ���� ������Ʈ
	
	
	private String 	_fileName;
	private int 	_line;
	//for tracing && file update
	
	
	public GoalAction getGoalAction() {
		return goalAction;
	}

	public void setGoalAction(GoalAction goalAction) {
		this.goalAction = goalAction;
	}


	public Plan() {
		body = new LinkedList<GoalAction>();
		precondition = new LinkedList<Relation>();
		context = new LinkedList<Relation>();
		
		symbolTable = new SymbolTable();
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
	
	public List<GoalAction> getBody() {
		return body;
	}

	public void setBody(List<GoalAction> body) {
		this.body = body;
	}

	public List<Relation> getPrecondition() {
		return precondition;
	}

	public void setPrecondition(List<Relation> precondition) {
		this.precondition = precondition;
	}

	public List<Relation> getContext() {
		return context;
	}

	public void setContext(List<Relation> context) {
		this.context = context;
	}

	public SymbolTable getSymbolTable() {
		// TODO Auto-generated method stub
		return this.symbolTable;
	}

	public String get_fileName() {
		return _fileName;
	}

	public void set_fileName(String _fileName) {
		this._fileName = _fileName;
	}

	public int get_line() {
		return _line;
	}

	public void set_line(int _line) {
		this._line = _line;
	}
	
}


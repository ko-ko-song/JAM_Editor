package kr.ac.uos.ai.editor.jam.model;

import java.util.LinkedList;
import java.util.List;

import kr.ac.uos.ai.editor.jam.expression.Expression;

public class Relation {
	
	private final String		name;
	private final String[]		args;
	
	private String 	_fileName;
	private int		_line;
	//for tracing & file update
	
	private static final Expression[] EMPTY_EXPRESSION = new Expression[0];
	
	public Relation(String name, String... args) {
		this.name = name;
		this.args = (args != null) ? args.clone() : new String[0];
	}

	public Relation(String name, Expression... expList) {
		this.name = name;
		
		if(expList == null)
			this.args = new String[0];
		else {
			List<String> temp = new LinkedList<String>(); 
			for (Expression expression : expList) {
				temp.add(expression.getName());
			}
			this.args = temp.toArray(new String[0]);
		}
	}
	
	public Relation(String name, List<Expression> expList) {
		this.name = name;
		
		if(expList == null)
			this.args = new String[0];
		else {
			List<String> temp = new LinkedList<String>(); 
			for (Expression expression : expList) {
				if(expression == null) 
					temp.add("Null Expression");
				else
					temp.add(expression.getName());	
				
			}
			this.args = temp.toArray(new String[0]);
		}
	}
	
	
	
	
	public String getName() {
		return name;
	}

	public String[] getArgs() {
		return args;
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

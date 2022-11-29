package kr.ac.uos.ai.editor.jam.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RelationTable {

	private final Map<String, List<Relation>> relationTable; 
	private Map<String, List<Relation>> fileByRelationTable;  
	
	public RelationTable() {
		relationTable = new HashMap<String, List<Relation>>();
		fileByRelationTable = new HashMap<String, List<Relation>>();
	}
	
	public void add(Relation relation) {
		if(relation != null) {
			String name = relation.getName();
			List<Relation> relations = relationTable.get(name);
			if(relations == null) {
				relations = new LinkedList<Relation>();
				relationTable.put(name, relations);
			}
			relations.add(relation);
			
			String fileName = relation.get_fileName();
			List<Relation> relationsFromFile = fileByRelationTable.get(fileName);
			if(relationsFromFile == null) {
				relationsFromFile = new LinkedList<Relation>();
				fileByRelationTable.put(fileName, relationsFromFile);
			}
			relationsFromFile.add(relation);
			
			return;
		}else {
			System.out.println("relation is null");
		}
	}

	
	public List<Relation> getRelations(String relationName) {
		return relationTable.get(relationName);
	}

	public List<Relation> getRelationsFromFile(String fileName){
		return fileByRelationTable.get(fileName);
	}
	
	public List<Relation> getAllRelations() {
		List<Relation> result = new ArrayList<Relation>();
		for (List<Relation> relationList : fileByRelationTable.values()) {
			for (Relation relation : relationList) {
				result.add(relation);
			}
		}
		return result;
	}
	
	
	public void removeRelation(Relation relation) {
		
		List<Relation> relations = relationTable.get(relation.getName());
		List<Relation> relationsFromFile = fileByRelationTable.get(relation.get_fileName());
		if(relations != null) {
			for (Relation rel : relations) {
				if(rel.getArgs().length == relation.getArgs().length) {
					for(int i=0; i<rel.getArgs().length; i++) {
						if(!rel.getArgs()[i].equals(relation.getArgs()[i])) {
							break;
						}
					}
					relations.remove(rel);
					if(relationsFromFile != null)
						relationsFromFile.remove(rel);
					
					return;
				}
			}	
		}
	}
	
	public void printAllRelations() {
		StringBuilder sb = new StringBuilder();
		StringBuilder sbFormat = new StringBuilder();
		sbFormat.append("relation List------------------------------------------------------------------------------------ \n");
		for (Relation relation : getAllRelations()) {
			String relationName = relation.getName();
			String[] args = relation.getArgs();
			sb.setLength(0);
			sb.append(relationName);
			sb.append("(");
			if(args.length != 0) {
				for (String arg : args) {
					sb.append(arg);
					sb.append(", ");
				}
				int length = sb.length();
				sb.delete(length -2, length );
			}
			sb.append(")");
			sbFormat.append(String.format("%-60s %s %-30s %s %s", sb.toString(), "file name : ", relation.get_fileName(), "line : ", relation.get_line()));
			sbFormat.append("\n");
		}
		
		System.out.println(sbFormat.toString());
	}
}

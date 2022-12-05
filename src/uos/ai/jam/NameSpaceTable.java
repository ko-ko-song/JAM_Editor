package uos.ai.jam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class NameSpaceTable {

	private ArrayList<NameSpace> name_spaces_table;
	private HashMap<String,NameSpace> prefix_name_spaces_table;
	
	
	//edtior
	private List<Prefix> prefixes;
	
	public List<Prefix> getPrefixes() {
		return prefixes;
	}

	public void setPrefixes(List<Prefix> prefixes) {
		this.prefixes = prefixes;
	}
	
	public void addPrefix(Prefix prefix) {
		prefixes.add(prefix);
	}
	
	//
	
	
	
	
	public NameSpaceTable(){

		this.name_spaces_table = new ArrayList<NameSpace>();
		this.prefix_name_spaces_table = new HashMap<String,NameSpace>();
		this.prefixes = new LinkedList<Prefix>();
	}
	
	public void clearPrefixMap(){
		this.prefix_name_spaces_table.clear();
	}
	
	
	public NameSpace getNameSpace(NameSpace ns){
		for(NameSpace namespace : this.name_spaces_table){
			if(namespace.isEqual(ns)){
				return namespace;
			}
		}
		return null;
	}
	
	public NameSpace getNameSpace(String name_space){ /// method to get specific name space with the string of name space or the prefix.
														// return null if there is no name space matching the parameter.
		if (name_space.startsWith("<")||name_space.startsWith("http://")){
			NameSpace ns = new NameSpace(name_space);
			return this.getNameSpace(ns);
		}

		/*
		while(keys.hasNext()) {
			String key = keys.next();
			System.out.println("key: " + key + " value : " + prefix_name_spaces_table.get(key).asString());
		}
		
		for(int i = 0 ;i < name_spaces_table.size();i++) {
			System.out.println(name_spaces_table.get(i).asString());
		}
		*/
		if(this.prefix_name_spaces_table.containsKey(name_space)){
			return this.prefix_name_spaces_table.get(name_space);
		}else {
			//System.out.println(prefix_name_spaces_table);
			//System.out.println("There is no prefix named : " + name_space);
			//System.out.println("Please declare prefix first.");
			//System.out.println("Or, there is no namespace named : " + name_space);
			//System.out.println("Please check if the name_space has a proper format.");
			return null;
		}
	}
	
	
	public NameSpace addNameSpace(NameSpace new_name_space){
		boolean new_name = true;
		for(NameSpace ns : this.name_spaces_table){
			if(ns.isEqual(new_name_space)){
				//System.out.println("Same Namespace is already in the table.");
				new_name = false;
				new_name_space = ns;
				break;
			}
		}
		
		if(new_name == true){
			this.name_spaces_table.add(new_name_space);
		}
		return new_name_space;
	}
	
	public NameSpace addNameSpace(String url){
		NameSpace new_name_space = new NameSpace(url);
		return this.addNameSpace(new_name_space);
	}
	
	public NameSpace addPrefix(String prefix, String url){
		NameSpace new_name_space = new NameSpace(url);
		this.addPrefix(prefix, new_name_space);
		return new_name_space;
	}
	
	public NameSpace addPrefix(Prefix prefix, NameSpace ns){
		return this.addPrefix(prefix.getPrefix(), ns);
	}
	
	public NameSpace addPrefix(String prefix, NameSpace ns){
		
		if(prefix_name_spaces_table.containsKey(prefix)){
			if(prefix_name_spaces_table.get(prefix).isEqual(ns)){
				//System.out.println("The prefix /'" + prefix + "/' is already binded with : " + ns.asString());
			}else {
				//System.out.println("Not able to bind the prefix /'" + prefix + "/' with " + ns.asString());
				//System.out.println("The prefix /'" + prefix + "/' is already binded with : " + prefix_name_spaces_table.get(prefix).asString());
				//System.out.println("Please use another prefix.");
			}
		}else {
			if(this.getNameSpace(ns) == null) {
				this.addNameSpace(ns);
				prefix_name_spaces_table.put(prefix, ns);
			}
			/*
			System.out.println();
			System.out.println("prefixAdded : " + prefix);
			System.out.println(name_spaces_table.size());
			for(String key : prefix_name_spaces_table.keySet()) {
				System.out.println("key: " + key + " value : " + prefix_name_spaces_table.get(key).asString());
			}
			System.out.println();
			*/
		}		
		return ns;
	}
	
	public boolean isThere(NameSpace ns){
		boolean con = false;
		for(NameSpace n:this.name_spaces_table){
			if(n.isEqual(ns)){
				con = true;
				break;
			}
		}
		return con;
	}
	
	public boolean isThere(String url){
		NameSpace ns = new NameSpace(url);
		return this.isThere(ns);
	}
	
}

package kr.ac.uos.ai.editor.jam.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PrefixManager {

	private final Map<String, Prefix> prefixes;
	
	public PrefixManager() {
		prefixes = new HashMap<String, Prefix>();
	}
	
	
	public boolean add(Prefix prefix) {
		if(prefixes.containsKey(prefix.getPrefix()))
			return false;
		
		prefixes.put(prefix.getPrefix(), prefix);
		return true;
	}
	
	public void remove(String prefixName) {
		if(prefixes.containsKey(prefixName)) {
			prefixes.remove(prefixName);
		}
		else {
			System.out.println("delete 실패 : " + prefixName + " 가 존재하지 않습니다");
		}
	}
	
	public void remove(Prefix prefix) {
		if(prefixes.containsKey(prefix.getPrefix())) {
			if(prefix.get_fileName().equals(prefixes.get(prefix.getPrefix()).get_fileName())){
				prefixes.remove(prefix.getPrefix());	
			}
		}
		else {
			
		}
	}
	
	/**
	 * prefix로 저장된 value를 리턴한다.
	 * ex) arbi:<http://www.arbi.com//ontologies#>
	 * 
	 * @param prefix arbi:
	 * @return http://www.arbi.com//ontologies
	 */
	
	public Prefix getPrefix(String prefixName) {
		if(!prefixes.containsKey(prefixName)) {
			System.out.println(prefixName + " 가 존재하지 않습니다");
			return null;
		}
		return prefixes.get(prefixName);
	}
	
	public String getValue(String prefixName) {
		if(!prefixes.containsKey(prefixName)) {
			System.out.println(prefixName + " 가 존재하지 않습니다");
			return null;
		}
			
		return prefixes.get(prefixName).get_value();
	}
	

	public List<Prefix> getAllPrefixes() {
		List<Prefix> result = new ArrayList<Prefix>();
		for (Prefix prefix: prefixes.values()) {
			result.add(prefix);
		}
		return result;
	}
	
	public void printAllPrefixes() {
		StringBuilder sbFormat = new StringBuilder();
		sbFormat.append("prefix List------------------------------------------------------------------------------------ \n");
		for (Prefix prefix : getAllPrefixes()) {
			String prefixName = prefix.getPrefix();

			sbFormat.append(String.format("%-10s %-60s %s %-20s %s %s ", prefixName, prefix.get_value(), "file name : ", prefix.get_fileName(), "line : ", prefix.get_line()));
			sbFormat.append("\n");
		}
		
		System.out.println(sbFormat.toString());
	}
	
}

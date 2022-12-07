package kr.ac.uos.ai.editor.jam.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uos.ai.jam.Prefix;

public class PrefixManager {

//	private final Map<String, Prefix> prefixes;
	private final List<Prefix> prefixes;
	
	public PrefixManager() {
//		prefixes = new HashMap<String, Prefix>();
		prefixes = new LinkedList<Prefix>();
	}
	
	
//	public boolean add(Prefix prefix) {
//		if(prefixes.containsKey(prefix.getPrefix())) {
//			for (Prefix prefixV : prefixes.values()) {
//				if(prefix.get_fileName().contentEquals(prefixV.get_fileName()))
//					return false;
//			}
//		}
//		
//		prefixes.put(prefix.getPrefix(), prefix);
//		return true;
//	}

	public boolean add(Prefix prefix) {
		for (Prefix p : prefixes) {
			if(prefix.get_fileName().contentEquals(p.get_fileName())){
				if(prefix.getPrefix().equals(p.getPrefix()) && prefix.getValue().contentEquals(p.getValue()))
					return false;
			}
		}
		prefixes.add(prefix);
		return true;
	}
	
	
//	public void remove(String prefixName) {
//		if(prefixes.containsKey(prefixName)) {
//			prefixes.remove(prefixName);
//		}
//		else {
//			System.out.println("delete 실패 : " + prefixName + " 가 존재하지 않습니다");
//		}
//	}
	
	public void remove(Prefix prefix) {
		prefixes.remove(prefix);
		
	}
	
	/**
	 * prefix로 저장된 value를 리턴한다.
	 * ex) arbi:<http://www.arbi.com//ontologies#>
	 * 
	 * @param prefix arbi:
	 * @return http://www.arbi.com//ontologies
	 */
	
	public Prefix getPrefix(String prefixName) {
		for (Prefix prefix : prefixes) {
			if(prefixName.contentEquals(prefix.getPrefix()))
				return prefix;
		}
		return null;
	}
	
	public String getValue(String prefixName) {
		for (Prefix prefix : prefixes) {
			if(prefixName.contentEquals(prefix.getPrefix()))
				return prefix.getValue();
		}
			
		return null;
	}
	

	public List<Prefix> getAllPrefixes() {
		List<Prefix> result = new ArrayList<Prefix>();
		for (Prefix prefix: prefixes) {
			result.add(prefix);
		}
		return result;
	}

	
	public void printAllPrefixes() {
		StringBuilder sbFormat = new StringBuilder();
		sbFormat.append("prefix List------------------------------------------------------------------------------------ \n");
		for (Prefix prefix : getAllPrefixes()) {
			String prefixName = prefix.getPrefix();

			sbFormat.append(String.format("%-10s %-60s %s %-20s %s %s ", prefixName, prefix.getValue(), "file name : ", prefix.get_fileName(), "line : ", prefix.get_line()));
			sbFormat.append("\n");
		}
		
		System.out.println(sbFormat.toString());
	}
	
}

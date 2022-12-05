package kr.ac.uos.ai.editor.jamEditor.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class DocumentAssistor {
	
	private String fileName;
	private String path;
	private String content;
	private String[] lineContent;
	// 첫 줄 ==>0
	
	
	
	public void readInputStream(InputStream inputStream) {
		
	    List<String> lineText = new LinkedList<String>();
	    StringBuilder lineBuilder = new StringBuilder();
	    StringBuilder textBuilder = new StringBuilder();
	    try (Reader reader = new BufferedReader(new InputStreamReader
	      (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
	        int c = 0;
	        while ((c = reader.read()) != -1) {
	        	if(c == '\n') {
	        		lineText.add(lineBuilder.toString());
	        		lineBuilder.setLength(0);
	        	}
	        	else 
	        		lineBuilder.append((char) c);	
	        	
	            textBuilder.append((char) c);
	        }
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    lineText.add(lineBuilder.toString());
	    
	    lineContent = lineText.toArray(new String[0]);
	    this.content = textBuilder.toString();
	}
	
	
	public String getLineText(int line) {
		if(this.lineContent == null)
			return null;
		if(this.lineContent.length < line) 
			return null;
		
		return this.lineContent[line-1];
	}
	
	public char charAt(int index) {
		if(this.content == null)
			return (Character) null;
		
		return this.content.charAt(index);
	}
	
	public int getLineOffset(int line) {
		//1 --> 0
		//2 --> 1번 다 더하고 +1?
		int offset = 0;
		for(int i=0; i<line; i++) {
			offset += lineContent[i].length()+1;
		}
		return offset;
	}
	
}

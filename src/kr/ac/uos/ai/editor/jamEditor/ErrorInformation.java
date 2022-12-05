package kr.ac.uos.ai.editor.jamEditor;
/**
 * error information for jam parsing 
 * @author JiHun
 *
 */

import java.util.LinkedList;
import java.util.List;

import uos.ai.jam.parser.Token;

public class ErrorInformation {
	private Token currentToken;
	private Token nextToken;
	private int[][] expectedTokens;
	private String[] tokenImage;
	
	
	public Token getCurrentToken() {
		return currentToken;
	}
	public void setCurrentToken(Token currentToken) {
		this.currentToken = currentToken;
	}
	public Token getNextToken() {
		return nextToken;
	}
	public void setNextToken(Token nextToken) {
		this.nextToken = nextToken;
	}
	public int[][] getExpectedTokens() {
		return expectedTokens;
	}
	public void setExpectedTokens(int[][] expectedTokens) {
		this.expectedTokens = expectedTokens;
	}
	public String[] getTokenImage() {
		return tokenImage;
	}
	public void setTokenImage(String[] tokenImage) {
		this.tokenImage = tokenImage;
	}
	
	public List<String> getExpectedTokenImages(){
		List<String> expectedTokenImages = new LinkedList<String>();
		if(expectedTokens == null)
			return expectedTokenImages;
		
		for(int i=0; i<expectedTokens.length; i++) {
			for(int j=0; j<expectedTokens[i].length; j++) {
				int index = expectedTokens[i][j];
				expectedTokenImages.add(tokenImage[index]);
			}
		}
		return expectedTokenImages;
	}
	
}

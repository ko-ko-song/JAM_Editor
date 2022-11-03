package kr.ac.uos.ai.editor.jamEditor;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class PlanPropertyKeywordRule implements IRule{

	private final Token token;
	
	public final String[] keywords = {"PLAN ", "FACTS:", "OBSERVER:", "PREFIXES:", "GOALS:",
			"FACT ", "RETRIEVE", "MATCH", "PREFIX ", "UPDATE "};
	
	public PlanPropertyKeywordRule(Token token) {
		this.token = token;
	}
	
	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		int c = scanner.read();
		int count = 1;
		
		StringBuilder sb = new StringBuilder();
		
		while(c != ICharacterScanner.EOF) {
			sb.append((char)c);
			
			for (String keyword : keywords) {
				if(sb.toString().equals(keyword))
					return token;
				
				if(sb.toString().equals(keyword.toLowerCase()))
					return token;
			}
			
						
			if ('\n' == c || '\r' == c) {
                break;
            }
			
			count++;
			c = scanner.read();
		}
		
		for(int i=0; i<count; i++) {
			scanner.unread();
		}
		
		
		return Token.UNDEFINED;
	}
	
	
	
	

}

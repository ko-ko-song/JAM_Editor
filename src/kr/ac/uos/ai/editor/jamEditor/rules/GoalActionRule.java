package kr.ac.uos.ai.editor.jamEditor.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class GoalActionRule implements IRule{

	private final Token token;
	
	public final String[] GOALACTION = {"ACHIEVE ", "PERFORM ", "CONCLUDE ", "MAINTAIN "};
	
	public GoalActionRule(Token token) {
		this.token = token;
	}
	
	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		int c = scanner.read();
		int count = 1;
		
		StringBuilder sb = new StringBuilder();
		
		while(c != ICharacterScanner.EOF) {
			sb.append((char)c);
			
			
			for (String goalAction : GOALACTION) {
				if(sb.toString().equals(goalAction))
					return token;
				
				if(sb.toString().equals(goalAction.toLowerCase()))
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

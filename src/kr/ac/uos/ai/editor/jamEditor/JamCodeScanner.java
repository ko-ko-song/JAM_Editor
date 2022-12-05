package kr.ac.uos.ai.editor.jamEditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import kr.ac.uos.ai.editor.jamEditor.util.JamColorProvider;
import kr.ac.uos.ai.editor.jamEditor.util.JamWhitespaceDetector;
import kr.ac.uos.ai.editor.jamEditor.util.JamWordDetector;

public class JamCodeScanner extends RuleBasedScanner{
	
	private final String[] goalActions = {"ACHIEVE", "PERFORM", "CONCLUDE", "MAINTAIN"};
	private final String[] planProperties = {"ID:", "NAME:", "PRECONDITION:", "CONTEXT:", "BODY:", "UTILITY:"};
	private final String[] worldmodelInteractions = {"FACT", "RETRIEVE", "MATCH", "UPDATE", "ASSERT", "RETRACT"};
	private final String[] keywords = {"PLAN", "FACTS:", "OBSERVER:", "PREFIXES:", "GOALS:"};

	public JamCodeScanner(JamColorProvider provider) {
		
		Color whiteColor = provider.getColor(JamColorProvider.WHITE);
		
		IToken goalAction = new Token(new TextAttribute(provider.getColor(JamColorProvider.GOALACTION), whiteColor, SWT.BOLD));
		IToken planproperty = new Token(new TextAttribute(provider.getColor(JamColorProvider.PLANPROPERTY)));
		IToken worldmodelInteraction = new Token(new TextAttribute(provider.getColor(JamColorProvider.WORLDMODELINTERACTION), whiteColor, SWT.BOLD));
		IToken keyword = new Token(new TextAttribute(provider.getColor(JamColorProvider.KEYWORD), whiteColor, SWT.BOLD));
		
		IToken string = new Token(new TextAttribute(provider.getColor(JamColorProvider.STRING))); 
		IToken comment = new Token(new TextAttribute(provider.getColor(JamColorProvider.SINGLE_LINE_COMMENT)));
		IToken other = new Token(new TextAttribute(provider.getColor(JamColorProvider.DEFAULT)));
		
		List<IRule> rules = new ArrayList<>();
		
		// Add rule for single line comments.
		rules.add(new EndOfLineRule("//", comment)); 
		
		// Add rule for strings and character constants.
		rules.add(new SingleLineRule("\"", "\"", string, '\\')); 
		rules.add(new SingleLineRule("'", "'", string, '\\')); 
		rules.add(new SingleLineRule("<", ">", string, '\\'));
		
		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new JamWhitespaceDetector()));
		
		// Add word rule for keywords, types, and constants.
		WordRule wordRule= new WordRule(new JamWordDetector(), other, true);
		for (int i= 0; i < goalActions.length; i++)
			wordRule.addWord(goalActions[i], goalAction);
		for (int i= 0; i < planProperties.length; i++)
			wordRule.addWord(planProperties[i], planproperty);
		for (int i= 0; i < worldmodelInteractions.length; i++)
			wordRule.addWord(worldmodelInteractions[i], worldmodelInteraction);
		for (int i= 0; i < keywords.length; i++)
			wordRule.addWord(keywords[i], keyword);
		
		rules.add(wordRule);

		IRule[] result= new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}
}

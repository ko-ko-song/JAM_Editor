package kr.ac.uos.ai.editor.jamEditor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class PlanPropertiesReconciler extends PresentationReconciler{
	
	private final TextAttribute planPropertiesAttribute = new TextAttribute(
			 Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));

	private final TextAttribute goalActionAttribute = new TextAttribute(
			 Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA));

	private final TextAttribute keywordAttribute= new TextAttribute(
			 Display.getCurrent().getSystemColor(SWT.COLOR_DARK_MAGENTA));
	
	public PlanPropertiesReconciler() {
	    RuleBasedScanner scanner = new RuleBasedScanner();
	    IRule planPropertyRule = new PlanPropertyNameRule(new Token(planPropertiesAttribute));
	    IRule planGoalActionNameRule = new PlanGoalActionNameRule(new Token(goalActionAttribute));
	    IRule planPropertyKeywordRule = new PlanPropertyKeywordRule(new Token(keywordAttribute));
	    
	    scanner.setRules(new IRule[] { planPropertyRule, planGoalActionNameRule, planPropertyKeywordRule});
	    DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
	    this.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
	    this.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
	}

	
	
}

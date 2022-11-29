package kr.ac.uos.ai.editor.jamEditor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;

import kr.ac.uos.ai.editor.jamEditor.util.JamColorProvider;

public class JamPresentationReconciler extends PresentationReconciler{

	public JamPresentationReconciler() {
		JamColorProvider provider = JamEditorPlugin.getDefault().getColorProvider();
	    RuleBasedScanner scanner = new JamCodeScanner(provider);
	    
	    DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
	    this.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
	    this.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
	    
	}
}

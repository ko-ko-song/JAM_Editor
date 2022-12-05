package kr.ac.uos.ai.editor.jamEditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

import kr.ac.uos.ai.editor.jamEditor.util.DocumentAssistor;
import kr.ac.uos.ai.editor.jamEditor.util.Util;
import uos.ai.jam.Interpreter;
import uos.ai.jam.parser.JAMParser;
import uos.ai.jam.parser.ParseException;

public class MarkerDocumentSetup implements IDocumentSetupParticipant{

	@Override
	public void setup(IDocument document) {
		document.addDocumentListener(new IDocumentListener() {
			
			private Job markerJob;
			
			@Override
			public void documentChanged(DocumentEvent event) {
				IEditorPart activeEditor = Util.getActiveEditor();
				
				if(activeEditor != null) {
					IEditorInput editorInput = activeEditor.getEditorInput();
					IResource adapter = editorInput.getAdapter(IResource.class);
					
					if(!adapter.getFileExtension().equalsIgnoreCase("jam")) {
						System.out.println("MarkerDocumnetSetup : not jma file extension   " +  adapter.getName());
						return;
					}
					
					if(markerJob != null) {
						markerJob.cancel();
					}
					markerJob = Job.create("Adding Error Marker", (ICoreRunnable) monitor -> updateErrorMarker(event, adapter));
					markerJob.setUser(false);
					markerJob.setPriority(Job.DECORATE);
					markerJob.schedule(500);
				}
			}
			@Override
			public void documentAboutToBeChanged(DocumentEvent event) {
				// TODO Auto-generated method stub
			}
		});
	}

	
	private void updateErrorMarker(DocumentEvent event, IResource adapter) throws CoreException {
		ErrorInformation information = JAMParser.parseStringForErrorDetection(null, event.getDocument().get());
		adapter.deleteMarkers(IMarker.PROBLEM, true, 0);
//		List<IMarker> markers = Arrays.asList(adapter.findMarkers(IMarker.PROBLEM, true, 0));
//		for (IMarker iMarker : markers) {
//			if(iMarker.getAttribute(IMarker.SEVERITY).equals(IMarker.SEVERITY_ERROR))
//				iMarker.delete();
//		}
		
		if(information==null)
			return;
		

		List<String> expectedTokens = information.getExpectedTokenImages();
		
		String errorMessage = "exptected tokens : \n";
		for (String expectedToken : expectedTokens) {
			errorMessage += expectedToken +"\n";
		}
		int line = information.getCurrentToken().beginLine;
		
		IMarker marker = adapter.createMarker(IMarker.PROBLEM);
		
		marker.setAttribute(IMarker.LINE_NUMBER, line);
		marker.setAttribute(IMarker.MESSAGE, errorMessage);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		
		
		int start = 0;
		int end = 0;
		try {
			start = event.getDocument().getLineOffset(line - 1);
			end = event.getDocument().getLineOffset(line);
			
			while(event.getDocument().getChar(start) == ' ' || event.getDocument().getChar(start) == '\t'){                                   
			    start++;
			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		marker.setAttribute(IMarker.CHAR_START, start);
		marker.setAttribute(IMarker.CHAR_END, end);
	}

}

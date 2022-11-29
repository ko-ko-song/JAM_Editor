package kr.ac.uos.ai.editor.jamEditor.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class JamColorProvider {
//	public static final RGB MULTI_LINE_COMMENT= new RGB(128, 0, 0);
//	public static final RGB SINGLE_LINE_COMMENT= new RGB(128, 128, 0);
	
	public static final RGB KEYWORD= new RGB(153, 51, 102);
	//plan, prefixes, goals, facts, observer를 부르는 이름을 모르겠음
	
	public static final RGB GOALACTION= new RGB(153, 51, 102);
//	public static final RGB TYPE= new RGB(0, 0, 128);
	
	public static final RGB WORLDMODELINTERACTION = new RGB(153, 51, 102);
	//fact, match, retrieve, update을 부르는 이름을 모르겠음
	
	public static final RGB PLANPROPERTY = new RGB(0, 102, 0);
	public static final RGB STRING = new RGB(0, 102, 255);

	public static final RGB PREFIX = new RGB(204, 153, 051);
	public static final RGB DEFAULT= new RGB(0, 0, 0);
	
	public static final RGB MULTI_LINE_COMMENT= new RGB(0, 128, 0);
	public static final RGB SINGLE_LINE_COMMENT= new RGB(0, 128, 0);
	
	public static final RGB WHITE = new RGB(255, 255, 255);
	
//	public static final RGB JAVADOC_KEYWORD= new RGB(0, 128, 0);
//	public static final RGB JAVADOC_TAG= new RGB(128, 128, 128);
//	public static final RGB JAVADOC_LINK= new RGB(128, 128, 128);
//	public static final RGB JAVADOC_DEFAULT= new RGB(0, 128, 128);

	private Map<RGB, Color> fColorTable;

	public JamColorProvider() {
		fColorTable = new HashMap<RGB, Color>(11);
	}
	
	/**
	 * Return the Color that is stored in the Color table as rgb.
	 */
	
	public Color getColor(RGB rgb) {
		Color color= (Color) fColorTable.get(rgb);
		if (color == null) {
			color= new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}
}

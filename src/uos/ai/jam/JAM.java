//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: JAM.java,v 1.1 2006/07/22 07:03:12 semix2 Exp $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/JAM.java,v $
//  
//  File              : JAM.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//                    : Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:21:51 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:54:01 2004
//  Update Count      : 110
//  
//  Copyright (C) 1997-2004 Jaeho Lee and Marcus J. Huber.
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1997-2004 Jaeho Lee and Marcus J. Huber.
//  
//  Permission is granted to copy and redistribute this software so long
//  as no fee is charged, and so long as the copyright notice above, this
//  grant of permission, and the disclaimer below appear in all copies
//  made.
//  
//  This software is provided as is, without representation as to its
//  fitness for any purpose, and without warranty of any kind, either
//  express or implied, including without limitation the implied
//  warranties of merchantability and fitness for a particular purpose.
//  Jaeho Lee and Marcus J. Huber shall not be liable for any damages,
//  including special, indirect, incidental, or consequential damages,
//  with respect to any claim arising out of or in connection with the
//  use of the software, even if they have been or are hereafter advised
//  of the possibility of such damages.
// 
//////////////////////////////////////////////////////////////////////////////

package uos.ai.jam;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;

import uos.ai.jam.parser.JAMParser;

/**
 * 
 * The JAM Agent application interface
 * 
 * @author Jaeho Lee
 * @author Marc Huber
 * @version 1.1
 * 
 */

public class JAM implements Serializable {
	private static final long serialVersionUID = -4625068124967529703L;

	//
	// Members
	//
	protected static boolean 			_showWorldModel;
	protected static boolean 			_showGoalList;
	protected static boolean 			_showIntentionStructure;
	protected static boolean 			_showAPL;

	//
	// Constructors
	//

	/**
	 * Default constructor
	 * 
	 */
	public JAM() {
		init();
	}

	//
	// Member functions
	//

	public static boolean getShowWorldModel() {
		return _showWorldModel;
	}

	public static boolean getShowGoalList() {
		return _showGoalList;
	}

	public static boolean getShowAPL() {
		return _showAPL;
	}

	public static boolean getShowIntentionStructure() {
		return _showIntentionStructure;
	}

	public static boolean setShowWorldModel(boolean flag) {
		return _showWorldModel = flag;
	}

	public static boolean setShowGoalList(boolean flag) {
		return _showGoalList = flag;
	}

	public static boolean setShowAPL(boolean flag) {
		return _showAPL = flag;
	}

	public static boolean setShowIntentionStructure(boolean flag) {
		return _showIntentionStructure = flag;
	}

	/**
	 * Command-line interface for users to start Jam agent.
	 * 
	 */
	public static void main(String argv[]) {
		run(argv);
	}

	/**
	 * Entry-point for deserialized agents to restart.
	 * 
	 */
	public static int run(Interpreter interpreter) {
		// System.exit(interpreter.run());
		return (interpreter.run());
	}

	/**
	 * Entry-point for invoking agents from scratch from Java source code.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static int run(String argv[]) {
		Interpreter interpreter;

		int argNum;
		Vector pStrings = new Vector();
		Vector argV = new Vector();

		init();

		System.out.println("\n\nJam Agent Architecture");
		System.out.println("Version 0.60 + 0.80i");
		System.out.println("Copyright (C) 1997");
		System.out.println("Jaeho Lee and Marcus J. Huber");
		System.out.println("Copyright (C) 1997-2004");
		System.out.println("Intelligent Reasoning Systems");
		System.out.println("All Rights Reserved\n");

		// Copy argv into new Vector
		for (argNum = 0; argNum < argv.length; argNum++) {
			argV.addElement(argv[argNum]);
		}

		argNum = 0;
		while (argNum < argV.size()
				&& ((String) argV.elementAt(argNum)).charAt(0) == '-') {

			switch (((String) argV.elementAt(argNum)).charAt(1)) {

			case 'g':
				_showGoalList = true;
				System.out
						.println("*** JAM: Showing the Goal List turned ON. ***\n");
				for (int i = argNum; i < argV.size() - 1; i++) {
					argV.setElementAt(argV.elementAt(i + 1), i);
				}
				argV.removeElementAt(argV.size() - 1);
				break;

			case 'i':
				_showIntentionStructure = true;
				System.out
						.println("*** JAM: Showing the Intention Structure turned ON. ***\n");
				for (int i = argNum; i < argV.size() - 1; i++) {
					argV.setElementAt(argV.elementAt(i + 1), i);
				}
				argV.removeElementAt(argV.size() - 1);
				break;

			case 's':
				_showAPL = true;
				System.out
						.println("*** JAM: Showing APL generation turned ON. ***\n");
				for (int i = argNum; i < argV.size() - 1; i++) {
					argV.setElementAt(argV.elementAt(i + 1), i);
				}
				argV.removeElementAt(argV.size() - 1);
				break;

			case 'w':
				_showWorldModel = true;
				System.out
						.println("*** JAM: Showing the World Model turned ON. ***\n");
				for (int i = argNum; i < argV.size() - 1; i++) {
					argV.setElementAt(argV.elementAt(i + 1), i);
				}
				argV.removeElementAt(argV.size() - 1);
				break;

			case 'p':

				// Save the parse string to parse after file list. Read and
				// save between quotation marks.
				System.out
						.println("*** JAM: Parsable command-line string found. ***\n");
				String pString = ((String) argV.elementAt(argNum)).substring(2);
				pStrings.addElement(pString);

				// Remove command-line arguments
				for (int i = argNum; i < argV.size() - 1; i++) {
					argV.setElementAt(argV.elementAt(i + 1), i);
				}
				argV.removeElementAt(argV.size() - 1);
				break;

			default:
				System.out.println("*** JAM: Ignoring unknown flag '"
						+ ((String) argV.elementAt(argNum)).charAt(1)
						+ "' ***\n");
				for (int i = argNum; i < argV.size() - 1; i++) {
					argV.setElementAt(argV.elementAt(i + 1), i);
				}
				argV.removeElementAt(argV.size() - 1);
				break;
			}

		}
		
		// Rebuild argv from argV
		String[] newArgv = new String[argV.size()];
		for (int i = 0; i < argV.size(); i++) {
			newArgv[i] = new String((String) argV.elementAt(i));
		}

		// Parse primary Jam Agent definitions
		if ((interpreter = parse(newArgv)) == null) {
			System.out.println("JAM: Parser returned error.\n");
			System.exit(0);
		}

		// Parse command-line Jam agent definitions
		for (int i = 0; i < pStrings.size(); i++) {
			if ((interpreter = parseString(interpreter, (String) pStrings
					.elementAt(i))) == null) {
				System.out.println("JAM: Parser returned error.\n");
				System.exit(0);
			}
		}

		System.out.println("JAM: Parser finished.  Starting to execute.");
		// System.exit(interpreter.run());
		return (interpreter.run());
	}

	/**
	 * Parse the list of files for plans, goals, world model entries, and
	 * Observer specifications.
	 * 
	 */
	protected static final String DEFAULT_PLAN_FOLDER = "./plan";
	protected static String	planFolder; 
	
	public static Interpreter parse(){
		return parse(DEFAULT_PLAN_FOLDER);
	}
	
	public static Interpreter parse(String s){
		JAM.planFolder = s;
		File planFolder = new File(s);
		Interpreter interpreter = new Interpreter();
		
		for (File planFile : planFolder.listFiles()) {
			if(planFile.isDirectory()){
				continue;
			}
			JAMParser.parseFile(interpreter, planFile);
		}

		interpreter.setPlanFolder(s);
		return interpreter;
	}
	
	public static Interpreter parseString(String s){
		Interpreter interpreter = new Interpreter();
		
		return JAMParser.parseString(interpreter,s);
	}
	
	public static Interpreter parse(String argv[]) {
		int argNum;
		String fileBuf;
		String line;
		File planFile;
		BufferedReader dStream;
		Interpreter interpreter = null;

		for (argNum = 0; argNum < argv.length; argNum++) {
			// System.out.println("Arg[" + argNum + "] = " + argv[argNum]);
			fileBuf = "";

			try {
				planFile = new File(argv[argNum]);
				dStream = new BufferedReader(new FileReader(planFile));
				while (true) {
					line = dStream.readLine();

					if (line == null)
						break;

					fileBuf = fileBuf + "\n" + line;
					// System.out.print("Line just read: " + line + "\n");
				}
			} catch (EOFException eof) {
				System.out.println("\n--> Normal end-of-file.\n");
			} catch (FileNotFoundException noFile) {
				System.out.println("File " + argv[argNum] + " not found!");
			} catch (IOException io) {
				System.out.println("I/O error occured while reading "
						+ argv[argNum]);
			} catch (Throwable anything) {
				System.out.println("Unexpected exception caught!:" + anything);
			}
			interpreter = JAMParser.buildInterpreter(fileBuf);
		}

		return interpreter;
	}

	/**
	 * Parse the string for plans, goals, world model entries, and Observer
	 * specifications.
	 * 
	 */
	public static Interpreter parseString(Interpreter interpreter, String pString) {
		if ((interpreter = JAMParser.parseString(interpreter, pString)) == null) {
			System.out.println("\nJAM: Parser returned error!\n");
		}
		return interpreter;
	}

	/**
	 * Initialize the JAM agent's internals
	 * 
	 */
	private static void init() {
		_showWorldModel 			= false;
		_showGoalList 				= false;
		_showIntentionStructure 	= false;
		_showAPL 					= false;
	}

}

package uos.ai.jam.exception;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * 기본 Exception Model factory 객체
 * 
 * @author	Hyunju Shin <jetzt82@gmail.com>
 * @since	2.0
 * @update	2.0
 */
public class DefaultExceptionModelFactory implements ExceptionModelFactory {
	private static final String		MODEL_FILE		= "./default-exception-model.xml";

	public ExceptionTreeNode newExceptionModel() {
		try {
			return newExceptionModelFromDefaultFile();
		} catch (Exception e) {
			//e.printStackTrace();
//			System.out.println("모델 파일이 존재하지 않아 기본 모델을 작성합니다.");
			return new ExceptionTreeNode("agent.model", "Exception");
		}
	}
	
	public ExceptionTreeNode newExceptionTreeNode(String canonicalName) {
		if (canonicalName == null || "".equals(canonicalName)) return null;
		else return new ExceptionTreeNode(canonicalName);
	}
	
	private ExceptionTreeNode newExceptionModelFromDefaultFile() throws Exception {
		FileReader reader = new FileReader(MODEL_FILE);
		BufferedReader r = new BufferedReader(reader);
		
		System.out.println(r.readLine());
		System.out.println(r.readLine());
		System.out.println(r.readLine());
		
		ExceptionTreeNode n = ExceptionTreeNode.newExceptionNode(new FileReader(MODEL_FILE)); 
		System.out.println("FFF");
		
		System.out.println("NN: " + n);
		
		return n;
	}
}

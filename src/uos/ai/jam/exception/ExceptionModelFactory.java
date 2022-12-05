package uos.ai.jam.exception;

/**
 * Exception model을 생성하기 위한 factory 객체
 * 
 * @author	Hyunju Shin <jetzt82@gmail.com>
 * @since	2.0
 * @update	2.0
 */
public interface ExceptionModelFactory {
	/**
	 * ExceptionModel tree에서 최상위(root) node를 생성하는 메소드.
	 * 절대로 null을 반환해서는 안 됨.
	 */
	public ExceptionTreeNode newExceptionModel();
	
	/**
	 * 주어진 canonicalName exception node를 생성하는 메소드.
	 * 다른 node와의 link 과정은 ExceptionManager 내부에서 처리함.
	 */
	public ExceptionTreeNode newExceptionTreeNode(String canonicalName);
}

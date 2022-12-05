package uos.ai.jam.exception;

import java.util.List;

import uos.ai.jam.Interpreter;

/**
 * ExceptionManager interface
 * 
 * @author	Hyunju Shin <jetzt82@gmail.com>
 * @since	2.0
 * @update	2.0
 */
public interface ExceptionManager {
	/**
	 * ExceptionModel을 생성하기 위한 factory를 설정하는 메소드.
	 * 해당 메소드가 호출되면 ExceptionManager는 factory를 사용해서
	 * ExceptionModel을 생성하고 교체한다.
	 */
	public void setModelFactory(ExceptionModelFactory factory);
	
	/**
	 * Exception tree에서 최상위(root) Exception을 반환하는 메소드.
	 */
	public String getRootException();
	
	/**
	 * 주어진 name(canonical-name)을 가지는 exception을 찾고,
	 * 해당 exception의 부모 exception을 반환하는 메소드.
	 * 만약 주어진 name을 가지는 exception이 없으면 null을 반환.
	 */
	public String getParentException(String name);
	
	/**
	 * 주어진 exception instance로부터 exception (class)을 찾고,
	 * 해당 exception의 부모 exception을 반환하는 메소드.
	 * 만약 주어진 exception instance가 null이거나
	 * 부적절한 instance이면 null을 반환.
	 */
	public String getParentException(AgentRuntimeException exception);
	
	/**
	 * 주어진 name(canonical-name)을 가지는 exception을 찾고,
	 * 해당 exception의 자식 exceptions을 반환하는 메소드.
	 * 만약 주어진 name을 가지는 exception이 없으면 길이가 0인 array를 반환.
	 */
	public String[] getChildrenExceptions(String name);
	
	/**
	 * 주어진 exception instance로부터 exception (class)을 찾고,
	 * 해당 exception의 자식 exceptions을 반환하는 메소드.
	 * 만약 주어진 exception instance가 null이거나
	 * 부적절한 instance이면 길이가 0인 array를 반환.
	 */
	public String[] getChildrenExceptions(AgentRuntimeException exception);
	
	/**
	 * 주어진 exception instance로부터 exception (class)을 찾고,
	 * root로부터 해당 exception까지의 경로를 반환하는 메소드.
	 */
	public String[] getPathToRoot(String name);
	
	/**
	 * 주어진 name(canonical-name)을 가지는 두 exception을 찾고,
	 * parent exception이 child exception의 조상인지 확인하는 메소드.
	 */
	public boolean isAncestor(String parent, String child);
	
	/**
	 * parent exception이 child exception의 조상인지 확인하는 메소드.
	 */
	public boolean isAncestor(AgentRuntimeException parent, String child);
	
	/**
	 * 주어진 name(canonical-name)을 가지는 두 exception을 찾고,
	 * child exception이 parent exception의 후손인지 확인하는 메소드.
	 */
	public boolean isDescendant(String child, String parent);
	public boolean isDescendantOrEqual(String child, String parent);
	
	
	/**
	 * child exception이 parent exception의 후손인지 확인하는 메소드.
	 */
	public boolean isDescendant(AgentRuntimeException child, String parent);
	public boolean isDescendantOrEqual(AgentRuntimeException child, String parent);
	
	
	/**
	 * 주어진 child name을 가지는 exception node를 생성한 후,
	 * 주어진 parent name을 가지는 exception node에 추가하는 메소드.
	 */
	public boolean addNodeInto(String child, String parent);
	
	/**
	 * 주어진 name을 가지는 exception node를
	 * 부모 node로부터 제거하는 메소드.
	 */
	public boolean removeNodeFromParent(String child);
	
	/**
	 * 주어진 name을 가지는 exception node에서
	 * 모든 child node를 제거하는 메소드.
	 */
	public boolean removeAllNode(String parent);
	
	/**
	 * Global exception 발생을 알리는 메소드.
	 * 해당 메소드가 호출되면 ExceptionManager는
	 * 내부의 global exception 목록에 exception을 추가하고 유지한다.
	 */
	public void throwException(AgentRuntimeException ex);
	
	/**
	 * 현재 유지되고 있는(아직 처리되지 않은) global exception 목록을 반환하는 메소드.
	 */
	public AgentRuntimeException[] getAllExceptions();
	
	/**
	 * 현재 유지되고 있는(아직 처리되지 않은) 모든 global exception을 삭제하는 메소드.
	 * 즉, 발생한 exception을 처리없이 버리는 메소드.
	 */
	public void removeAllExceptions();
	
	/**
	 * 주어진 GlobalCatch를 추가하는 메소드.
	 */
	public void addGlobalCatch(GlobalCatch globalCatch);
	
	/**
	 * 주어진 모든 GlobalCatch들을 추가하는 메소드.
	 */
	public void addAllGlobalCatch(List<GlobalCatch> list);
	
	/**
	 * 주어진 모든 GlobalCatch들을 추가하는 메소드.
	 */
	public void addAllGlobalCatch(GlobalCatch[] list);
	
	/**
	 * 주어진 GlobalCatch를 삭제하는 메소드.
	 */
	public void removeGlobalCatch(GlobalCatch globalCatch);
	
	/**
	 * 현재 내부에서 유지되고 있는 GlobalCatch 목록을 반환하는 메소드.
	 */
	public GlobalCatch[] getGlobalCatchs();
	
	/**
	 * Global exception을 처리하는 메소드
	 */
	public void execute(Interpreter interpreter);
}

package uos.ai.jam.exception;

public class TestException {

	public static void main_1(String[] args) {
		DefaultExceptionModelFactory factory = new DefaultExceptionModelFactory();
		ExceptionTreeNode root = factory.newExceptionModel();
		
		System.out.println(root);
	}
	
	public static void main(String[] args) {
		ExceptionTreeNode root = new ExceptionTreeNode("test.Exception");
		ExceptionTreeNode c1 = new ExceptionTreeNode("test.C1");
		ExceptionTreeNode c2 = new ExceptionTreeNode("test.C2");
		ExceptionTreeNode c1_1 = new ExceptionTreeNode("test.C1_1");
		
		root.addChild(c1);
		root.addChild(c2);
		c1.addChild(c1_1);
		
		System.out.println(root.toXML());
		
		
	}
}

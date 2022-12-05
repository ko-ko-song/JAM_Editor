package uos.ai.jam;

/**
 * 
 * Represents an agent's goals
 * 
 * @author Sunyoung Yoo
 * @version 2.0
 * 
 */

public class Prefix {

	public static void main(String[] args){
		Prefix p = new Prefix("hello:");
	}
	//
	/// Members
	//
	
	protected final String _prefix;
	
	//edtior
	private String _fileName;
	private int _line;
	private String value;
	
	
	
	public Prefix(String prefix){
		_prefix = prefix;
	}
	
	public String getPrefix(){
		return _prefix;
	}

	public String get_fileName() {
		return _fileName;
	}

	public void set_fileName(String _fileName) {
		this._fileName = _fileName;
	}

	public int get_line() {
		return _line;
	}

	public void set_line(int _line) {
		this._line = _line;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}

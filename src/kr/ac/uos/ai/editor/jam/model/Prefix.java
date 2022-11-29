package kr.ac.uos.ai.editor.jam.model;


public class Prefix {

	private String _prefix;
	private String _value;
	
	private String  _fileName;
	private int     _line;
	
	public Prefix(String prefix, String fileName, int line) {
		this._prefix = prefix;
		this._fileName = fileName;
		this._line = line;
	}

	public String getPrefix() {
		return this._prefix;
	}
	
	public void setValue(String value) {
		this._value = value;
	}

	public String get_value() {
		return _value;
	}

	public void set_value(String _value) {
		this._value = _value;
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
	
	
	
	
}

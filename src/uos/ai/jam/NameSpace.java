package uos.ai.jam;

public class NameSpace {
	private String _url;
	private String[] wrapper = {"",""};
	
	public NameSpace(String url){		
		if(url.length()>1){
			if(url.startsWith("<")&&url.endsWith(">")){
				if(url.endsWith("/>")||url.endsWith("#>")){
					if(url.startsWith("http://")){
						_url = url.substring(1, url.length());
					}
				}else {System.out.println("Wrong form of namespace");}
			}else if(url.startsWith("http://")){
				if(url.endsWith("/")||url.endsWith("#")){
					_url = url;
				}else {System.out.println("Wrong form of namespace");}
			}else {
				if(url.endsWith("/")||url.endsWith("#")){	
					_url = "http://" + url;
				}else {System.out.println("Wrong form of namespace");}
			}
			
			if(_url != null){
				wrapper[0] = "<" + _url;
				wrapper[1] = ">";
			}else {
				_url = "";
				wrapper[0] = "<" + _url;
				wrapper[1] = ">";
			}
		}
	}
	
	public String wrap(String value){
		return wrapper[0] + value + wrapper[1];
	}
	
	public String asString(){
		return wrapper[0] + wrapper[1];
	}
	
	public boolean isEqual(NameSpace ns){
		if (this.asString().contentEquals(ns.asString())){
			return true;
		}else {return false;}
	}
}

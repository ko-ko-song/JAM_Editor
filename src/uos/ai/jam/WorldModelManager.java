package uos.ai.jam;

import java.util.HashMap;

public class WorldModelManager {
	
	protected WorldModelTable	_defaultWorldModel;
	
	protected HashMap<String,WorldModelTable> 	_worldModelTable;
	private NameSpaceTable 						_nameSpaceTable;
	
	public WorldModelManager(NameSpaceTable nt){
		_defaultWorldModel = new WorldModelTable(nt);
		_worldModelTable = new HashMap<String,WorldModelTable>();
	}
	
	public WorldModelTable getWorldModel(){
		return _defaultWorldModel;
	}
	
	
	public WorldModelTable getWorldModel(String name){
		WorldModelTable worldModel;
		if(_worldModelTable.get(name)!=null){
			worldModel = _worldModelTable.get(name);
		}else {
			worldModel = new WorldModelTable(_nameSpaceTable,name);
			_worldModelTable.put(name, worldModel);
		}
		return worldModel; 
	}
	
	

}

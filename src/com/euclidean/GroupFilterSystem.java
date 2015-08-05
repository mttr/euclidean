package com.euclidean;

import com.badlogic.gdx.utils.ObjectIntMap;
import com.euclidean.enums.ObjectType;

public class GroupFilterSystem {
	private ObjectIntMap<ObjectType> groupMap;
	
	public GroupFilterSystem()
	{
		groupMap = new ObjectIntMap<ObjectType>();
		
		groupMap.put(ObjectType.PLAYER, -1);
		groupMap.put(ObjectType.BULLET, -1);
		
		// TODO set to 0 if it turns out I'd rather have enemies collide with each
		// other
		groupMap.put(ObjectType.ENEMY, 0); 
	}
	
	public int GetFilterGroup(ObjectType type)
	{
		return groupMap.get(type, 0);
	}
}

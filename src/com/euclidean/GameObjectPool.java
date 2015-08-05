package com.euclidean;

import com.badlogic.gdx.utils.Pool;

public class GameObjectPool extends Pool<GameObject> {
	
	public GameObjectPool(String object_class, int max)
	{
		super(max, max);
		
		for (int i = 0; i < max; i++)
		{
			GameObject object = BaseObject.sys.objectSystem.getObject(object_class);
			this.free(object);
			
			object.setPool(this);
		}
	}

	@Override
	protected GameObject newObject() {
		// Not allowed to create new objects.
		return null;
	}

}

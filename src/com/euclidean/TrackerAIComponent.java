package com.euclidean;

import org.lwjgl.util.vector.Vector2f;

import com.euclidean.enums.ObjectType;

public class TrackerAIComponent extends BaseAIComponent {
	private GameObject currentTarget;
	
	@Override
	public void update(BaseObject parent) {
		super.update(parent);		
		
		if (currentTarget == null)
		{
			FixedArrayList<BaseObject> objectlist = sys.manager.getList();
			
			for (int x = 0; x < objectlist.size(); x++)
			{
				try 
				{
					if (((GameObject)objectlist.get(x)).getType() == ObjectType.PLAYER)
					{
						currentTarget = (GameObject) objectlist.get(x);
					}
				}
				catch (ClassCastException e)
				{
					// nothing to see here...
				}
			}
		}
		
		this.pointTo(currentTarget.getPosition());
}

	public Vector2f getPosition() {
		return position;
	}

}

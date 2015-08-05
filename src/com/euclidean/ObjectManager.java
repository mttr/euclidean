package com.euclidean;

public class ObjectManager extends BaseObject {
    
    private FixedArrayList<BaseObject> objects;
    private FixedArrayList<BaseObject> removeQueue;
    
    public ObjectManager()
    {
        objects = new FixedArrayList<BaseObject>(sys.maxObjectManagerDefault);
        removeQueue = new FixedArrayList<BaseObject>(sys.maxObjectManagerDefault);
    }
    
    public ObjectManager(int size) {
        objects = new FixedArrayList<BaseObject>(size);
        removeQueue = new FixedArrayList<BaseObject>(size);
    }

    public void add(BaseObject object)
    {
        objects.add(object);
    }
    
    @Override
    public void update(BaseObject parent) {
    	for (int i = 0; i < removeQueue.size(); i++)
    	{
    		BaseObject object = removeQueue.get(i);
    		
    		if (objects.remove(object))
    		{
    			object.reset();
    		}
    	}
    	removeQueue.clearAll();
       
        for (int i = 0; i < objects.size(); i++)
        {
        	BaseObject object = objects.get(i);
        	if (!object.getActive()) continue;
            object.update(this);
        }
    }

    @Override
    public void reset() {
    	for (int i = 0; i < objects.size(); i++)
    	{
    		objects.get(i).reset();
    	}
    }
    
    public void clearAll() {
    	reset();
    	objects.clearAll();
    	removeQueue.clearAll();
    }
    
    public BaseObject findByClass(Class<?> c) {
    	return objects.findByClass(c);
    }
    
    public FixedArrayList<BaseObject> getList() {
    	return objects;
    }
    
    public void remove(BaseObject object) {
    	removeQueue.add(object);
    }
}

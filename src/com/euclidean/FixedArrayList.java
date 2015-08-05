package com.euclidean;

public class FixedArrayList<T> {
    
    private int count;
    private T[] objects;
    private int index;
    
    @SuppressWarnings("unchecked")
    public FixedArrayList(int size)
    {
        count = 0;
        
        objects = (T[])new Object[size];
        index = 0;
    }
    
    public void add(T object)
    {
        objects[count] = object;
        count++;
    }
    
    public T get(int index)
    {
        return objects[index];
    }
    
    public int size()
    {
        return count;
    }
    
    public T next()
    {
    	index++;
        return objects[index - 1];
    }
    
    public int getPosition()
    {
    	return index;
    }
    
    public void resetPosition()
    {
        index = 0;
    }
    
    public void clearAll()
    {
        for (int i = 0; i < count; i++)
        {
            objects[i] = null;
        }
        count = 0;
    }

    public boolean addIfUnique(T object) {
        for (int i = 0; i < count; i++)
        {
            if (objects[i] == object) return false;
        }
        this.add(object);
        return true;
    }
    
    public T last() {
    	if (count == 0) return null;
    	
        return objects[count - 1];
    }
    
    public T pop() {
    	if (count == 0) return null;
    	
    	count--;
    	T object = objects[count];
    	objects[count] = null;
    	return object;
    }
    
    public boolean remove(T object) {
    	for (int i = 0; i < count; i++)
    	{
    		if (objects[i] == object)
    		{
    			objects[i] = pop();
    			return true;
    		}
    	}
    	return false;
    }
    
    public T findByClass(Class<?> c) {
    	for (int i = 0; i < count; i++)
    	{
    		if (c.equals(objects[i].getClass())) return objects[i];
    	}
    	return null;
    }
}

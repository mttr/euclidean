package com.euclidean;

import com.badlogic.gdx.Application;

public abstract class BaseObject {
    
    public static SystemRegistry sys = new SystemRegistry();
    public static Application app;
	protected boolean active = true;
    
    public void update(BaseObject parent)
    {
        
    }
    
    public abstract void reset();

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean getActive() {
		return active;
	}
}

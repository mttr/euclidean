package com.euclidean;

import java.util.jar.Attributes;

import org.lwjgl.util.vector.Vector2f;

import com.badlogic.gdx.physics.box2d.Body;
import com.euclidean.enums.ObjectType;

public class GameObject extends ObjectManager {
    private Vector2f position;
    private Vector2f velocity;
    private Vector2f acceleration;
    private Vector2f dimensions;
    private ObjectType type;
	private Body body;
    private GameObjectPool pool;
    
    public GameObject()
    {
        position = new Vector2f(0, 0);
        acceleration = new Vector2f(0, 0);
        velocity = new Vector2f(0, 0);
        active = true;
        dimensions = new Vector2f();
    }
    
    @Override
    public void update(BaseObject parent)
    {
    	super.update(parent);
    	
    	if (position.x > sys.screenWidth || position.y > sys.screenHeight
    			|| position.x < 0 || position.y < 0)
    	{
    		sys.manager.remove(this);
    	}
    }
    
    public GameObject(Attributes attributes) {
		this();
		
		String type = attributes.getValue("type");
		
		if (type != null)
		{
			this.type = ObjectType.valueOf(type.toUpperCase());
		}
	}

	public void setPosition(Vector2f position)
    {
        this.position.set(position.x, position.y);
    }
    
    public void setPosition(float x, float y)
    {
        position.set(x, y);
    }
    
    public void setVelocity(float vx, float vy)
    {
        velocity.set(vx, vy);
    }
    
	public void setVelocity(Vector2f vector) {
		velocity.set(vector);		
	}
    
    public Vector2f getPosition()
    {
        return position;
    }
    
    public Vector2f getVelocity()
    {
        return velocity;
    }
    
    public Vector2f getAcceleration()
    {
        return acceleration;
    }

    public void setAcceleration(float x, float y) {
        acceleration.set(x, y);        
    }
    
    public void setBody(Body body) {
        this.body = body;
    }
    
    public Body getBody() {
        return body;
    }
    
    public ObjectType getType() {
    	return type;
    }
    
    public void setPool(GameObjectPool pool)
    {
    	this.pool = pool;
    }
    
    public void setDimensions(float width, float height)
    {
    	this.dimensions.set(width, height);
    }
    
    public Vector2f getDimensions()
    {
    	return dimensions;
    }

	@Override
	public void reset() {
		super.reset();
		
		position.set(0, 0);
		velocity.set(0, 0);
		acceleration.set(0, 0);
		
		if (pool != null)
		{
			pool.free(this);
		}
	}
}

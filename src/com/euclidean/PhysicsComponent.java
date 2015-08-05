package com.euclidean;

import java.util.jar.Attributes;

import org.lwjgl.util.vector.Vector2f;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.euclidean.enums.ObjectType;

public class PhysicsComponent extends GameComponent {
	
    protected static final float MAX_VELOCITY = 1.5f;
    protected static final float DECELERATION = 1;
    protected static final float GRAVITY = -9.8f;
    public static float ACCELERATION = 2f;
    public static float JUMP_VELOCITY = 1.5f;
    
    protected Vector2 gravity;
    protected Vector2 dimensions;
    protected Vector2 tempVelocity;
    protected Vector2 rotate;
    protected Body body;
    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;    
    protected GameObject parent;
    
    public PhysicsComponent() {
    	
    }
    
    public PhysicsComponent(Attributes attributes) {
        final int width = Integer.parseInt(attributes.getValue("width"));
        final int height = Integer.parseInt(attributes.getValue("height"));
        gravity = new Vector2(0, GRAVITY);
        dimensions = new Vector2(width, height);
        tempVelocity = new Vector2();
        
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = false;
        bodyDef.angularDamping = 5f;
        bodyDef.linearDamping = 5f;
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width * sys.pixelsPerMeter) / 2, (height * sys.pixelsPerMeter) / 2);
        
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
    }
    
    @Override
    public void update(BaseObject parent) {
    	if (this.parent == null)
    	{
    		this.parent = (GameObject) parent;
    		this.parent.setDimensions(dimensions.x, dimensions.y);
    	}
        Vector2f position = ((GameObject)parent).getPosition();
        
        if (body == null)
        {
			bodyDef.position.x = position.x * sys.pixelsPerMeter + (dimensions.x * sys.pixelsPerMeter) / 2;
			bodyDef.position.y = position.y * sys.pixelsPerMeter + (dimensions.y * sys.pixelsPerMeter) / 2;
			
			fixtureDef.filter.groupIndex = (short) sys.groupFilters.GetFilterGroup(((GameObject)parent).getType());
			
        	body = sys.world.createBody(bodyDef);
        	body.createFixture(fixtureDef);
        	body.setUserData(this);
            ((GameObject)parent).setBody(body);
        }
        
        updateVelocity(((GameObject)parent).getAcceleration());
        rotateToVelocity();
        
        Vector2 b2position = body.getPosition();
        position.x = b2position.x / sys.pixelsPerMeter - dimensions.x / 2;
        position.y = b2position.y / sys.pixelsPerMeter - dimensions.y / 2;
    }
    
    public void collisionResponse(PhysicsComponent object)
    {
    	if (parent.getType() == ObjectType.PLAYER)
    	{
    		if (object != null)
    		{
    			if (object.getParent().getType() == ObjectType.ENEMY)
    			{
    				sys.scoreSystem.loseLife(parent);
    			}
    		}
    	}
    	
    	if (parent.getType() == ObjectType.ENEMY)
    	{
    		if (object != null)
    		{
	    		if (object.getParent().getType() == ObjectType.BULLET)
	    		{
	    			sys.manager.remove(parent);
	    			sys.scoreSystem.addKill(10);
	    		}
    		}
    	}
    }

	private void updateVelocity(Vector2f acceleration) {
		Vector2 b2velocity = body.getLinearVelocity();
        tempVelocity.set(b2velocity);
        tempVelocity.x += acceleration.x;
        tempVelocity.y += acceleration.y;
        
        float velocity = b2velocity.len();
        if (velocity < MAX_VELOCITY || tempVelocity.len() < velocity)
        {
            body.applyForce(15 * acceleration.x, 15 * acceleration.y, body.getPosition().x, body.getPosition().y);
        }
        else if (velocity > MAX_VELOCITY)
        {
        	b2velocity.set(b2velocity.nor());
        	b2velocity.mul(velocity);
        }
	}
	
	protected void rotateToVelocity()
	{
		float bodyAngle = body.getAngle();
		
		Vector2 velocity = body.getLinearVelocity();
		float desiredAngle = (float) Math.atan2( (double) velocity.x, (double )-velocity.y);
		
		float nextAngle = bodyAngle + body.getAngularVelocity() / 3.0f;
		float totalRotation = desiredAngle - nextAngle;
		while ( totalRotation < -180 * MathUtils.degreesToRadians ) totalRotation += 360 * MathUtils.degreesToRadians;
		while ( totalRotation >  180 * MathUtils.degreesToRadians ) totalRotation -= 360 * MathUtils.degreesToRadians;
		float desiredAngularVelocity = totalRotation * 60;
		float change = 80 * MathUtils.degreesToRadians; //allow 1 degree rotation per time step
		desiredAngularVelocity = Math.min( change, Math.max(-change, desiredAngularVelocity));
		float impulse = body.getInertia() * desiredAngularVelocity;
		body.applyAngularImpulse( impulse );
	}

	@Override
	public void reset() {
		if (body != null)
		{
			sys.world.destroyBody(body);
		}
		body = null;
	}
	
	public GameObject getParent() {
		return parent;
	}
}

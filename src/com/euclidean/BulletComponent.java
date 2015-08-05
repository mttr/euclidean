package com.euclidean;

import java.util.jar.Attributes;

import org.lwjgl.util.vector.Vector2f;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.euclidean.enums.ObjectType;

public class BulletComponent extends PhysicsComponent {

	public BulletComponent(Attributes attributes) {    	
        final int width = Integer.parseInt(attributes.getValue("width"));
        final int height = Integer.parseInt(attributes.getValue("height"));
        dimensions = new Vector2(width, height);
        
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = false;
        bodyDef.active = true;
        bodyDef.bullet = true;
        bodyDef.angularDamping = 0f;
        bodyDef.linearDamping = 0f;
              
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width * sys.pixelsPerMeter) / 2, (height * sys.pixelsPerMeter) / 2);
        
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;        
        fixtureDef.filter.groupIndex = (short) sys.groupFilters.GetFilterGroup(ObjectType.PLAYER);
    }

	@Override
	public void update(BaseObject parent) {
    	if (this.parent == null)
    	{
    		this.parent = (GameObject) parent;
    	}
		Vector2f position = ((GameObject)parent).getPosition();
		
		if (body == null)
		{
			Vector2f velocity = ((GameObject)parent).getVelocity();
			bodyDef.position.x = position.x * sys.pixelsPerMeter + (dimensions.x * sys.pixelsPerMeter) / 2;
			bodyDef.position.y = position.y * sys.pixelsPerMeter + (dimensions.y * sys.pixelsPerMeter) / 2;
			bodyDef.linearVelocity.set(velocity.x, velocity.y);
			bodyDef.angle = MathUtils.atan2( -velocity.x, velocity.y);
			
			body = sys.world.createBody(bodyDef);
			body.createFixture(fixtureDef);
			body.setUserData(this);
			((GameObject)parent).setBody(body);
		}
		
        Vector2 b2position = body.getPosition();
        position.x = b2position.x / sys.pixelsPerMeter - dimensions.x / 2;
        position.y = b2position.y / sys.pixelsPerMeter - dimensions.y / 2;
	}
	
	@Override
	public void collisionResponse(PhysicsComponent object)
	{
		sys.manager.remove(parent);
	}
}

package com.euclidean;

import org.lwjgl.util.vector.Vector2f;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class BaseAIComponent extends GameComponent {
	protected Vector2f position;
	protected Rectangle rectangle;
	protected Vector2f rotateTo;
	protected Vector2f rotate;
	protected boolean rotating;
	
	public BaseAIComponent()
	{
		position = new Vector2f();
		rotateTo = new Vector2f();
		rotate = new Vector2f();
		rectangle = new Rectangle();
		
		rectangle.width = 32;
		rectangle.height = 32;
		
		rotating = false;
	}

	@Override
	public void update(BaseObject parent) {
		position.set(((GameObject)parent).getPosition());
		
		rectangle.x = position.x;
		rectangle.y = position.y;
		
		rotate(parent);
	}

	protected void rotate(BaseObject parent) {
		if (rotating)
		{
			
			Body body = ((GameObject)parent).getBody();
			
			float bodyAngle = body.getAngle();
			Vector2 b_pos = body.getPosition();

			rotate.x = b_pos.x - rotateTo.x;
			rotate.y = b_pos.y - rotateTo.y;
			//float new_angle = Vector2f.angle(rotateTo, position);
			float desiredAngle = MathUtils.atan2( -rotate.x, rotate.y);
			
			float nextAngle = bodyAngle + body.getAngularVelocity() / 3.0f;
			float totalRotation = desiredAngle - nextAngle;
			while ( totalRotation < -180 * MathUtils.degreesToRadians ) totalRotation += 360 * MathUtils.degreesToRadians;
			while ( totalRotation >  180 * MathUtils.degreesToRadians ) totalRotation -= 360 * MathUtils.degreesToRadians;
			float desiredAngularVelocity = totalRotation * 60;
			float change = 5 * MathUtils.degreesToRadians; //allow 1 degree rotation per time step
			desiredAngularVelocity = Math.min( change, Math.max(-change, desiredAngularVelocity));
			float impulse = body.getInertia() * desiredAngularVelocity;
			body.applyAngularImpulse( impulse );
			 
			rotate.normalise(rotate);
			rotate.scale(.1f);
			 
			((GameObject)parent).getAcceleration().set(-rotate.x, -rotate.y);
		}
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public void pointTo(Vector2f point) {
		rotating = true;
		rotateTo.set(point);
		rotateTo.scale(sys.pixelsPerMeter);
	}

}

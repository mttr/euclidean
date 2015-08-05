package com.euclidean;

import java.util.Random;

import org.lwjgl.util.vector.Vector2f;

import com.badlogic.gdx.physics.box2d.Body;

public class WandererAIComponent extends GameComponent {
	private Body body;
	private Random rand;
	private Vector2f velocity;
	
	public WandererAIComponent()
	{
		rand = new Random();
		velocity = new Vector2f();
	}

	@Override
	public void update(BaseObject parent) {
		Body body = ((GameObject)parent).getBody();
		
		if (body != null && this.body == null	)
		{
			velocity.x = rand.nextInt();
			velocity.y = rand.nextInt();
			velocity.normalise();
			velocity.scale(2f);
						
			body.setLinearDamping(0f);
			body.setLinearVelocity(velocity.x, velocity.y);
			body.getFixtureList().get(0).setRestitution(1.001f);
			body.getFixtureList().get(0).setDensity(0.1f);
			body.resetMassData();
			
			this.body = body;
		}
	}
	
	@Override
	public void reset() {
		body = null;
	}
}

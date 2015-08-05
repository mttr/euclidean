package com.euclidean;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactSystem extends BaseObject implements ContactListener {

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void beginContact(Contact contact) {
		PhysicsComponent physCompA = (PhysicsComponent) contact.getFixtureA().getBody().getUserData();
		PhysicsComponent physCompB = (PhysicsComponent) contact.getFixtureB().getBody().getUserData();
		
		if (physCompA != null)
		{
			physCompA.collisionResponse(physCompB);
		}
		
		if (physCompB != null)
		{
			physCompB.collisionResponse(physCompA);
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact arg0, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

}

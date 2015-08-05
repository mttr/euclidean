package com.euclidean;

import org.lwjgl.util.vector.Vector2f;
import com.badlogic.gdx.math.Vector2;
import com.euclidean.enums.ButtonActions;

public class InputComponent extends GameComponent {
	private static float ACCELERATION = .1f;
	private static float BULLET_VELOCITY = 5f;
	private static float spreadDegree = 6;
	private static float fireRate = .05f;
	private static int spreadCount = 5;
	
	private GameObjectPool bulletPool;
	private Vector2 bulletVelocity;
	private Vector2 bulletPosition;
	private float lastTime;
	private int bulletCounter;
	
	public InputComponent()
	{
		bulletPool = new GameObjectPool("bullet", 100);
		
		bulletVelocity = new Vector2();
		bulletPosition = new Vector2();
		lastTime = sys.currentTime;
		bulletCounter = 0;
	}

    @Override
    public void reset() {

    }

    @Override
    public void update(BaseObject parent) {        
        Vector2f acceleration = ((GameObject)parent).getAcceleration();
        acceleration.set(0, 0);
        
        if (sys.keyboard.isKeyPressed(ButtonActions.MOVE_UP))
        {
        	acceleration.y = ACCELERATION;
        }
        if (sys.keyboard.isKeyPressed(ButtonActions.MOVE_DOWN))
        {
        	acceleration.y = -ACCELERATION;
        }
        if (sys.keyboard.isKeyPressed(ButtonActions.MOVE_LEFT))
        {
        	acceleration.x = -ACCELERATION;
        }
        if (sys.keyboard.isKeyPressed(ButtonActions.MOVE_RIGHT))
        {
        	acceleration.x = ACCELERATION;
        }
        
        if (sys.currentTime - lastTime < fireRate)
        {
        	return;
        }
        
        // FIXME I really shouldn't be using hardcoded values to figure out where to
        // shoot the bullets from.
        // FIXME I also REALLY REALLY need to get my vectors sorted out so that I don't have to
        // resort to this ugliness.
        bulletVelocity.set(((GameObject)parent).getVelocity().x, ((GameObject)parent).getVelocity().x);
        bulletPosition.set(16f, 0f);
        
        if (sys.keyboard.isKeyPressed(ButtonActions.FIRE_LEFT))
        {
        	bulletVelocity.x -= BULLET_VELOCITY;
        	bulletPosition.y = 16f;
        	bulletPosition.x = 0f;
        }
        
        if (sys.keyboard.isKeyPressed(ButtonActions.FIRE_RIGHT))
        {
        	bulletVelocity.x += BULLET_VELOCITY;
        	bulletPosition.y = 16f;
        	bulletPosition.x = 32f;
        }
        
        if (sys.keyboard.isKeyPressed(ButtonActions.FIRE_UP))
        {
        	bulletVelocity.y += BULLET_VELOCITY;
        	bulletPosition.y = 32f;
        }
        
        if (sys.keyboard.isKeyPressed(ButtonActions.FIRE_DOWN))
        {
        	bulletVelocity.y -= BULLET_VELOCITY;
        	bulletPosition.y = 0f;
        }
      
       
        if (bulletVelocity.x != 0f || bulletVelocity.y != 0f)
        {
    		Vector2f position = ((GameObject)parent).getPosition();
    		bulletPosition.x += position.x;
    		bulletPosition.y += position.y;
    		
			GameObject bullet = bulletPool.obtain();
	        
        	if (bullet != null)
        	{
	        	bullet.setPosition(bulletPosition.x, bulletPosition.y);

	        	
        		bulletVelocity.rotate(bulletCounter * spreadDegree);
	        	
	        	bullet.setVelocity(bulletVelocity.x, bulletVelocity.y);
	        	
	        	sys.manager.add(bullet);
	        	
	        	if (bulletCounter == spreadCount / 2)
	        	{
	        		bulletCounter = -bulletCounter;
	        	}
	        	else
	        	{
	        		bulletCounter++;
	        	}
        	}
        }
    	lastTime = sys.currentTime;
    }
}


package com.euclidean;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.utils.Array;

// The place particle effects go to die (gracefully).
public class ParticleGraveyard extends BaseObject {
	private FixedArrayList<ParticleEffectPool.PooledEffect> particleList;
	private FixedArrayList<ParticleEffectPool.PooledEffect> removeList;
	float lastTime;
	
	public ParticleGraveyard() {
		particleList = new FixedArrayList<ParticleEffectPool.PooledEffect>(400);		
		removeList = new FixedArrayList<ParticleEffectPool.PooledEffect>(400);
	}
	
	@Override
	public void update(BaseObject parent)
	{
		float deltatime = sys.currentTime - lastTime;
		
		for (int i = 0; i < particleList.size(); i++)
		{
			ParticleEffectPool.PooledEffect effect = particleList.get(i);
			
			if (effect.isComplete())
			{
				removeList.add(effect);
			}
			else
			{
				effect.update(deltatime);
				effect.draw(sys.spriteBatch);
			}
		}
		
		for (int i = 0; i < removeList.size(); i++)
		{
			ParticleEffectPool.PooledEffect effect = removeList.get(i);
			
			Array<ParticleEmitter> emitters = effect.getEmitters();
			
			for (int j = 0; j < emitters.size; j++)
			{
				emitters.get(j).setContinuous(true);
			}
			
			particleList.remove(effect);
			
			effect.free();
		}
		
		removeList.clearAll();
		
		lastTime = sys.currentTime;
	}
	
	public void add(ParticleEffectPool.PooledEffect effect) {
		Array<ParticleEmitter> emitters = effect.getEmitters();
		
		for (int i = 0; i < emitters.size; i++)
		{
			emitters.get(i).setContinuous(false);
		}
		
		particleList.add(effect);
	}
	
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}

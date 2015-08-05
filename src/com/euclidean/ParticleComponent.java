package com.euclidean;

import java.util.jar.Attributes;

import org.lwjgl.util.vector.Vector2f;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap;
import com.euclidean.enums.EffectClass;

public class ParticleComponent extends GameComponent {
	private static IdentityMap<String, ParticleEffectPool> effectMap = new IdentityMap<String, ParticleEffectPool>();
	
	private ParticleEffectPool.PooledEffect effect;
	private String effectId;
	private float lastTime;
	private float lastAngle;
	private EffectClass effectClass;
	private Sound effectSound;
	
    public ParticleComponent(Attributes attributes) {
        effectId = attributes.getValue("effect");
        effectClass = EffectClass.valueOf(attributes.getValue("class").toUpperCase());
        
        String soundname = attributes.getValue("sound"); 
        
        ParticleEffectPool pool = effectMap.get(effectId);
        
        if (pool == null)
        {
        	ParticleEffect effect = new ParticleEffect();
        	effect.load(Gdx.files.internal("effects/" + effectId), Gdx.files.internal("images/particles/"));
        	
        	pool = new ParticleEffectPool(effect, 100, 100);
        	
        	effectMap.put(effectId, pool);
        }
        
        if (soundname != null && !soundname.isEmpty())
        {
        	effectSound = sys.soundSystem.load(soundname);
        }
    }

	@Override
	public void reset() {
		if (effect != null)
		{
			if (effectClass == EffectClass.ON_DEATH)
			{
				start();
			}
			sys.particleGraveyard.add(effect);
			effect = null;
		}
	}

	@Override
	public void update(BaseObject parent) {
		if (effect == null)
		{
			effect = effectMap.get(effectId).obtain();
			
			if (effectClass == EffectClass.CONTINUOUS)
			{
				start();
			}
			lastTime = sys.currentTime;
		}
		
		Vector2f position = ((GameObject)parent).getPosition();
		effect.setPosition(position.x, position.y);
		
		if (effectClass == EffectClass.CONTINUOUS)
		{
			float angle = ((GameObject)parent).getBody().getAngle();
			angle *= MathUtils.radiansToDegrees;
			
			if (angle != lastAngle)
			{
				Array<ParticleEmitter> emitters = effect.getEmitters();
				
				for (int i = 0; i < emitters.size; i++)
				{
					emitters.get(i).getAngle().setHigh(angle);
					
					lastAngle = angle;
				}
			}
			
			float timedelta = sys.currentTime - lastTime;
	
			effect.draw(sys.spriteBatch, timedelta);
		}
	
		
		lastTime = sys.currentTime;
	}
	
	private void start() {
		effect.start();
		
		if (effectSound != null)
		{
			effectSound.play();
		}
	}
}

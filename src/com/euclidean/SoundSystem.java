package com.euclidean;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.IdentityMap.Entries;

public class SoundSystem extends BaseObject {
	private IdentityMap<String, Sound> soundMap;
	
	public SoundSystem() {
		soundMap = new IdentityMap<String, Sound>();
	}
	
	public Sound load(String name)
	{
		Sound sound = soundMap.get(name);
		
		if (sound == null)
		{
			sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + name));
			soundMap.put(name, sound);
		}
		
		return sound;
	}

	@Override
	public void reset() {
		Entries<String, Sound> entries = soundMap.entries();
		
		for (int i = 0; i < soundMap.size; i++)
		{
			soundMap.get(entries.next().key).dispose();			
		}
		
		soundMap.clear();
	}

}

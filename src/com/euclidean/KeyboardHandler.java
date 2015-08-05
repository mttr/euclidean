package com.euclidean;

import java.util.NoSuchElementException;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.euclidean.enums.ButtonActions;

public class KeyboardHandler extends BaseObject implements InputProcessor {
	private ObjectIntMap<ButtonActions> actionMap;
	private IntMap<Boolean> keyMap;
	
	public KeyboardHandler()
	{
		keyMap = new IntMap<Boolean>();
		actionMap = new ObjectIntMap<ButtonActions>();
		
		actionMap.put(ButtonActions.MOVE_UP, Input.Keys.W);
		actionMap.put(ButtonActions.MOVE_DOWN, Input.Keys.S);
		actionMap.put(ButtonActions.MOVE_LEFT, Input.Keys.A);
		actionMap.put(ButtonActions.MOVE_RIGHT, Input.Keys.D);
		
		actionMap.put(ButtonActions.FIRE_UP, Input.Keys.UP);
		actionMap.put(ButtonActions.FIRE_DOWN, Input.Keys.DOWN);
		actionMap.put(ButtonActions.FIRE_LEFT, Input.Keys.LEFT);
		actionMap.put(ButtonActions.FIRE_RIGHT, Input.Keys.RIGHT);
		
		ObjectIntMap.Entries<ButtonActions> entries = actionMap.entries();
		
		for (int i = 0; i < actionMap.size; i++)
		{
			keyMap.put(entries.next().value, false);
		}
	}
	
	public boolean isKeyPressed(ButtonActions action)
	{
		return keyMap.get(actionMap.get(action, 0));
	}
	
	public void flushKeys()
	{
		IntMap.Entries<Boolean> entries = keyMap.entries();
		
		for (int i = 0; i < keyMap.size; i++)
		{
			try
			{
				keyMap.put(entries.next().key, false);
			}
			catch (NoSuchElementException e)
			{
				// FIXME WTF WOULD THIS HAPPEN. Oh well. Take a deep breath and move on.
				continue;
			}
		}
	}
	
	@Override
	public void reset() {
	}

	@Override
	public boolean keyDown(int keycode) {
		keyMap.put(keycode, true);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		keyMap.put(keycode, false);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}

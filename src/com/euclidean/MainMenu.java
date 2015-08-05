package com.euclidean;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureDict;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.euclidean.enums.State;

public class MainMenu extends BaseObject {
	private static final int START_GAME = 0;
	private static final int HIGH_SCORES = 1;
	private static final int CREDITS = 2;
	
	private Texture selectLine;
	private Texture menuText;
	private Vector2 linePosition;
	private int currentSelection;
	private float lastInput;
	
	public MainMenu() {
		//background = TextureDict.loadTexture("images/background.png").get();
		selectLine = TextureDict.loadTexture("images/selectline.png").get();
		menuText = TextureDict.loadTexture("images/menutext.png").get();
		linePosition = new Vector2(697, 352);
		
		currentSelection = 0;
		lastInput = 0;
	}
	
	@Override
	public void update(BaseObject parent)
	{
		if (sys.currentTime - lastInput > .15f)
		{
			if (Gdx.input.isKeyPressed(Input.Keys.UP))
			{
				currentSelection = MathUtils.clamp(currentSelection - 1, 0, 2);
				lastInput = sys.currentTime;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			{
				currentSelection = MathUtils.clamp(currentSelection + 1, 0, 2);
				lastInput = sys.currentTime;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.ENTER))
			{
				selectItem();
				lastInput = sys.currentTime;
			}
		}
		
		switch(currentSelection)
		{
		case START_GAME: linePosition.set(697, sys.screenHeight - 352); break;
		case HIGH_SCORES: linePosition.set(697, sys.screenHeight - 405); break;
		case CREDITS: linePosition.set(697, sys.screenHeight - 459); break;
		}
		
		sys.spriteBatch.draw(selectLine, linePosition.x, linePosition.y);
		sys.spriteBatch.draw(menuText, 0, 0);
	}

	private void selectItem() {
		switch(currentSelection)
		{
		case START_GAME:
			sys.gameState.addState(State.GAME);
			break;
		case HIGH_SCORES:
			sys.gameState.addState(State.SCORES);
			break;
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}

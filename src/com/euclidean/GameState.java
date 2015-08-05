package com.euclidean;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.TextureDict;
import com.euclidean.enums.State;

public class GameState extends BaseObject {
	private FixedArrayList<State> stateStack;
	private Object data;
	private boolean stateChanged;
	private Music menuMusic;
	private Music gameMusic;

	public GameState(State initialState) {
		stateStack = new FixedArrayList<State>(5);
		stateStack.add(initialState);
		stateChanged = true;
		
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/start_menu.ogg"));
		menuMusic.setLooping(true);
		menuMusic.setVolume(0.20f);		
		
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music/main.ogg"));
		gameMusic.setLooping(true);
		gameMusic.setVolume(0.20f);
	}
	
	@Override
	public void update(BaseObject parent)
	{
		if (stateChanged)
		{
			State current = currentState();
			
			sys.manager.clearAll();
			sys.staticManager.clearAll();
			sys.keyboard.flushKeys();
			
			if (current == null)
			{
				return;
			}
			
			switch(current)
			{
			case MENU:
				menuMusic.play();
				gameMusic.stop();
				
				sys.manager.add(new MainMenu());
				sys.backgroundManager.add(background("images/background3.png"));
				break;
			case GAME:
				sys.backgroundManager.clearAll();
				sys.backgroundManager.add(background("images/background5.png"));
				menuMusic.stop();
				gameMusic.play();
				
				// FIXME shouldn't do this here, but whatever.
		        XMLLoader loader = new XMLLoader();
		        loader.setHandler(new XMLObjectHandler());
		        loader.load("init.xml");
		        
		        sys.spawnSystem = new SpawnSystem(5,5);		        
		        sys.staticManager.add(sys.particleGraveyard);
		        sys.staticManager.add(sys.spawnSystem);
		        sys.staticManager.add(sys.scoreSystem);
				break;
			case SCORES:
				sys.manager.add(new HighScores());
				break;
			case CREDITS:
				break;
			}
			
			stateChanged = false;
		}			
	}
	
    private GameObject background(String file)
    {
    	GameObject background = new GameObject();
    	DrawComponent draw = new DrawComponent(TextureDict.loadTexture(file).get());
    	
    	background.add(draw);
    	
    	return background;
    }
	
	public void addState(State state)
	{
		stateStack.add(state);
		stateChanged = true;
	}
	public State currentState() {
		return stateStack.last();
	}
	
	public State popState() {
		stateChanged = true;
		return stateStack.pop();
	}
	
	public void swapState(State newState)
	{
		stateStack.pop();
		stateStack.add(newState);
		stateChanged = true;
	}
	
	public void setData(Object object)
	{
		data = object;
	}
	
	public Object getData()
	{
		return data;
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}

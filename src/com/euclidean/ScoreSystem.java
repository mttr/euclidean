package com.euclidean;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.euclidean.HighScores.ScoreEntry;
import com.euclidean.enums.State;


public class ScoreSystem extends BaseObject {
	private static int killsPerMultIncrease = 20;
	private static int scorePerLife = 20000;
	
	private BitmapFont bitmapFont;
	private int score;
	private int lifeFill;
	private int multiplier;
	private int kills;
	private int lives;
	private boolean lostLife;
	private boolean delaySpawn;
	private GameObject player;
	private float lastTime;
	private String notifyString;
	private float notifyTime;
	private Sound lifeUpSound;
	private Sound multUpSound;
	
	public ScoreSystem()
	{
		bitmapFont = new BitmapFont();
		lifeUpSound = sys.soundSystem.load("life_up.wav");
		multUpSound = sys.soundSystem.load("mult_up.wav");
		reset();
	}
	
	@Override
	public void update(BaseObject parent)
	{
		bitmapFont.draw(sys.spriteBatch, "Score " + 
						Integer.toString(score) + " x" + Integer.toString(multiplier), 
						sys.screenWidth - 150, sys.screenHeight - 5);
		bitmapFont.draw(sys.spriteBatch, "Lives " + 
				Integer.toString(lives), 10, sys.screenHeight - 5);
		
		if (sys.currentTime - notifyTime < 2f && notifyString != null)
		{
			bitmapFont.draw(sys.spriteBatch, notifyString, sys.screenWidth / 2, sys.screenHeight / 2);
		}
	}
	
	@Override
	public void reset() {
		score = 0;
		lifeFill = 0;
		multiplier = 1;
		kills = 0;
		lives = 3;
		lostLife = false;
		delaySpawn = false;
		notifyTime = 0;
	}
	
	public void addKill(int points)
	{
		score += points * multiplier;
		lifeFill += points * multiplier;
		kills++;
		
		if (kills != 0 && kills % killsPerMultIncrease == 0)
		{
			multiplier += 2;
			notify ("x" + Integer.toString(multiplier));
			multUpSound.play();
		}
		
		if (lifeFill >= scorePerLife)
		{
			lives++;
			lifeFill = score % scorePerLife;
			notify("Lives +1");
			lifeUpSound.play();
		}
	}
	
	public void loseLife(GameObject player)
	{
		lives--;
		lostLife = true;
		this.player = player;
		lastTime = sys.currentTime;
		kills = 0;
	}
	
	public void notify(String string)
	{
		notifyString = string;
		notifyTime = sys.currentTime;
	}
	
	public void updateLives() {
		if (lostLife)
		{
			multiplier = 1;
				
			sys.manager.clearAll();
			
			delaySpawn = true;
			
			sys.spawnSystem.pause();
		}
		
		lostLife = false;
		
		if (delaySpawn)
		{
			if (sys.currentTime - lastTime < 2f)
			{
				return;
			}
			
			if (lives != 0)
			{
				player.setPosition(sys.screenWidth / 2, sys.screenHeight / 2);
				
				sys.manager.add(player);
				
				sys.spawnSystem.resume();
				
				delaySpawn = false;
			}
			else
			{
				sys.gameState.swapState(State.SCORES);
				
				ScoreEntry entry = new ScoreEntry();
				
				entry.score = score;
				Gdx.input.getTextInput(new HighScores.TextInputListener(), "name", "");
				
				sys.gameState.setData(entry);
			}
		}
	}

}

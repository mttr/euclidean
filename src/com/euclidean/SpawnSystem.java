package com.euclidean;

import java.util.Random;

import org.lwjgl.util.vector.Vector2f;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.euclidean.enums.ObjectType;

public class SpawnSystem extends BaseObject {
	private static final float spawnRound = 1000f;
	
	public int difficultyMultiplier;
	
	private Rectangle[][] spawnZones;
	private int width;
	private int height;
	private float floodCount;
	private int floodZone;
	private int floodMax;
	private float zoneWidth;
	private float zoneHeight;
	private Random rand;
	private float lastTime;
	private float nextSpawn;
	private float spawnTime;
	private float floodRate;
	private float spawnRoundTime;
	private GameObjectPool wandererPool;
	private GameObjectPool trackerPool;
	private GameObject player;
	private Rectangle playerRect;
	private Vector2f spawnPosition;
	private boolean flooding;
	private boolean paused;
	
	public SpawnSystem(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.rand = new Random();
		this.wandererPool = new GameObjectPool("wanderership", 100);
		this.trackerPool = new GameObjectPool("enemyship", 100);
		this.spawnPosition = new Vector2f();
		this.zoneWidth = (sys.screenWidth / (float) width);
		this.zoneHeight = (sys.screenHeight / (float) height);
		this.playerRect = new Rectangle();
		
		this.playerRect.width = zoneWidth * 1.5f;
		this.playerRect.height = zoneHeight * 1.5f;
		
		this.paused = false;
		reset();
		
		spawnZones = new Rectangle[width][height];
		
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				spawnZones[x][y] = new Rectangle(x * zoneWidth, y * zoneHeight, zoneWidth, zoneHeight);
			}
		}
	}

	@Override
	public void update(BaseObject parent) {
		float deltatime = sys.currentTime - lastTime;
		spawnRoundTime += deltatime;
		
		if (paused)
		{
			return;
		}
		
		if (deltatime < nextSpawn)
		{
			return;
		}
		
		if (player == null)
		{
			FixedArrayList<BaseObject> list = sys.manager.getList();
			
			for (int i = 0; i < list.size(); i++)
			{
				GameObject object = (GameObject) list.get(i);
				
				if (object.getType() == ObjectType.PLAYER)
				{
					player = object;
					break;
				}
			}
			return;
		}
		Vector2f position = player.getPosition();
		
		playerRect.x = position.x - (zoneWidth / 2);
		playerRect.y = position.y - (zoneHeight / 2);
		
		Rectangle spawnRect;
		
		if (!flooding)
		{
			spawnRect = spawnZones[rand.nextInt(width)][rand.nextInt(height)];
		}
		else
		{
			spawnRect = spawnZones[floodZone % width][floodZone / width];
			floodZone = (floodZone + 1) % (width * height);
		}
		
		if (spawnRect.contains(playerRect) || spawnRect.overlaps(playerRect))
		{
			return;
		}
		spawnPosition.set(spawnRect.x + rand.nextInt((int)zoneWidth), 
									spawnRect.y + rand.nextInt((int)zoneHeight));
		
		if (spawnPosition.x + 32 > sys.screenWidth) spawnPosition.x -= 32;
		if (spawnPosition.y + 32 > sys.screenHeight) spawnPosition.y -= 32;
		
		GameObject tracker = trackerPool.obtain();
		
		if (tracker != null)
		{
			tracker.setPosition(spawnPosition);
			sys.manager.add(tracker);
		}
		
		GameObject wanderer = wandererPool.obtain();
		
		if (wanderer != null)
		{
			wanderer.setPosition(spawnRect.x, spawnRect.y);
			sys.manager.add(wanderer);
		}
		
		if (flooding)
		{
			floodCount++;
			
			if (floodCount == floodMax)
			{
				flooding = false;
				spawnTime = MathUtils.clamp(spawnTime - (.1f), .5f, 3f);
				floodRate = MathUtils.clamp(floodRate - (.2f), .05f, 3f);
				spawnRoundTime = 0f;
				nextSpawn = spawnTime;
				floodMax++;
				floodCount = 0;				
			}
		}
		
		if (spawnRoundTime >= spawnRound)
		{
			flooding = true;
			nextSpawn = floodRate;
			floodZone = rand.nextInt((width - 1) * (height - 1));
		}
		
		lastTime = sys.currentTime;
	}
	
	@Override
	public void reset() {
		this.difficultyMultiplier = 0;
		this.spawnTime = 5f;
		this.spawnRoundTime = 0;
		this.floodCount = 0;
		this.floodMax = 5;
		this.floodRate = 3f;
		this.flooding = false;
		this.nextSpawn = spawnTime;
		this.lastTime = sys.currentTime;
		this.paused = false;
	}

	public void pause() {
		paused = true;		
	}

	public void resume() {
		paused = false;
		lastTime = sys.currentTime;
	}
}

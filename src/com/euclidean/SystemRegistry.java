package com.euclidean;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

public class SystemRegistry extends BaseObject {
    // FIXME A lot of this is laziness. I should try to keep this as
    // minimal as possible. 
    
    public static final int MAX_TOUCH_POINTS = 3;
    
    public OrthographicCamera camera;
    public ObjectLibrary objectSystem;    
    public ObjectManager manager;
	public ObjectManager staticManager;
	public ObjectManager backgroundManager;
    public SpriteBatch spriteBatch;
    public KeyboardHandler keyboard;
	public GroupFilterSystem groupFilters;
	public ContactSystem contactSystem;
	public SpawnSystem spawnSystem;
	public ScoreSystem scoreSystem;
	public SoundSystem soundSystem;
	public ParticleGraveyard particleGraveyard;
	public GameState gameState;
	
	public float currentTime;

	// TODO In the future, we wont need to know any of this. I just want stuff
    // to bounce around the screen!!    
    public String packageString = "org.euclidean";
    public float screenWidth;
    public float screenHeight;
    public float realScreenHeight;
    public float targetScreenWidth = 800;
    public float targetScreenHeight = 480;
    
    // FIXME
    public float pixelsPerMeter = 1f / 128f;
    
    public int maxObjectLibraryObjects = 100;
    public int maxTextureLibraryObjects = 100;
    public int maxXMLAttributes = 10;
    public int maxXMLObjectComponents = 10;
	public int maxObjectManagerDefault = 100;

	////////////////
    
    public float scalingFactor = 1;

    public String assetsPrefix;

    public World world;

    public boolean debug;



	@Override
    public void reset() {
        // TODO Auto-generated method stub

    }

}

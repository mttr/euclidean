package com.euclidean;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.TextureDict;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.euclidean.enums.State;

public class GameClass extends BaseObject implements ApplicationListener {
    private static float FPS = 60;
    private static float FRAME_DELAY = 1f / FPS;
    
    public boolean debug = false;
    
    private ObjectManager manager;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private OrthographicCamera debugCamera;
    private World world;
    private float timeAccumulation;
    private float lastInput;
    private Box2DDebugRenderer debugRenderer;
    
    @Override
    public void create() {
        manager = new ObjectManager(400);
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera(sys.screenWidth, sys.screenHeight);
        debugCamera = new OrthographicCamera(sys.screenWidth * sys.pixelsPerMeter, sys.screenHeight * sys.pixelsPerMeter);
        debugRenderer = new Box2DDebugRenderer();
        
        Vector2 gravity = new Vector2(0, 0);
        world = new World(gravity, true); // A WHOLE NEW WORLD
        world.setContactListener(sys.contactSystem);

        sys.manager = manager;
        sys.soundSystem = new SoundSystem();
        sys.gameState = new GameState(State.MENU);
        sys.staticManager = new ObjectManager();
        sys.backgroundManager = new ObjectManager();
        sys.camera = camera;
        sys.world = world;
        sys.spriteBatch = spriteBatch;
        sys.debug = debug;
        sys.currentTime = 0f;
        sys.scoreSystem = new ScoreSystem();
        sys.particleGraveyard = new ParticleGraveyard();
        
        Gdx.input.setInputProcessor(sys.keyboard);
        
        camera.position.set(sys.screenWidth / 2, sys.screenHeight / 2, 0);
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render() {
        timeAccumulation += Gdx.graphics.getDeltaTime();
        sys.currentTime += Gdx.graphics.getDeltaTime();
        
        if (timeAccumulation >= FRAME_DELAY)
        {
            world.step(FRAME_DELAY, 5, 10);
            timeAccumulation -= FRAME_DELAY;
        }
        else
        {
        	return;
        }        
        
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        sys.gameState.update(null);
        
        spriteBatch.begin();
        sys.backgroundManager.update(null);
        manager.update(null);
        sys.staticManager.update(null);
        sys.scoreSystem.updateLives();
        spriteBatch.end();
        
        camera.update();
        camera.apply(Gdx.gl10);
        
        if (debug == true)
        {           
            debugCamera.position.set(camera.position.x * sys.pixelsPerMeter, camera.position.y * sys.pixelsPerMeter, 0);
            debugCamera.zoom = camera.zoom;
            debugCamera.update();
            debugCamera.apply(Gdx.gl10);
            spriteBatch.setProjectionMatrix(debugCamera.combined);
            debugRenderer.render(world, debugCamera.combined);
        }
        
        // FIXME
        spriteBatch.setProjectionMatrix(camera.combined);
        
        if (sys.currentTime - lastInput > 1f)
        {
	        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
	        {
	        	sys.gameState.popState();
	        	
	        	if (sys.gameState.currentState() == null)
	        	{
	        		app.exit();
	        	}
		        lastInput = sys.currentTime;
	        }
        }
    }
    


    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        
    }

}

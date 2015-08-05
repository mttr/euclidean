package com.euclidean;

import java.util.jar.Attributes;
import com.badlogic.gdx.math.MathUtils;

import org.lwjgl.util.vector.Vector2f;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureDict;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

public class DrawComponent extends GameComponent {
    protected Sprite sprite;
    protected Vector2f position;
    
    public DrawComponent() {
        position = new Vector2f();
    }
    
    public DrawComponent(Texture texture)
    {
        sprite = new Sprite(texture);
        position = new Vector2f();
    }
    
    public DrawComponent(Attributes attributes) {
        String name = attributes.getValue("texture");
        
        Texture texture = TextureDict.loadTexture("images/" + name + ".png").get();
        
        position = new Vector2f();
        
        sprite = new Sprite(texture);
    }
    
    public DrawComponent(Texture texture, boolean relative_to_camera) {
        this(texture);      
    }
    
    @Override
    public void update(BaseObject parent) {
    	//FIXME Redundant.
        position.set(((GameObject)parent).getPosition());
        
        sprite.setPosition(position.x, position.y);
        
        Body body = ((GameObject)parent).getBody();
        
        if (body != null)
        {
        	float bodyAngle = body.getAngle() * MathUtils.radiansToDegrees;
        	sprite.setRotation(bodyAngle - 180);
        }
        
        //FIXME So I can view the debugrenderer without the graphics. I probably want
        // to be able to have three ways of doing this-- graphics only, graphics+debug,
        // debug only.
        if (!sys.debug) sprite.draw(sys.spriteBatch);
    }
}

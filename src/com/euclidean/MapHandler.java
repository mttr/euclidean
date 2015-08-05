package com.euclidean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class MapHandler extends DefaultHandler {

	@Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
    	if (qName == "object")
        {
            GameObject object = BaseObject.sys.objectSystem.getObject(attributes.getValue("type"));
            float x = Integer.parseInt(attributes.getValue("x"));
            float y = Integer.parseInt(attributes.getValue("y"));
          
            object.setPosition(x, y);
            
            BaseObject.sys.manager.add(object);
        }
    	SystemRegistry sys = BaseObject.sys;
    	
    	Vector2[] boundaries = new Vector2[4];
    	float multiplier = BaseObject.sys.pixelsPerMeter;
    	
    	boundaries[0] = new Vector2(0f, 0f);
    	boundaries[1] = new Vector2(sys.screenWidth * multiplier, 0f);
    	boundaries[2] = new Vector2(sys.screenWidth * multiplier, sys.screenHeight * multiplier);
    	boundaries[3] = new Vector2(0f, sys.screenHeight * multiplier);
    	
    	ChainShape shape = new ChainShape();
    	shape.createLoop(boundaries);
    	
    	Body body = BaseObject.sys.world.createBody(new BodyDef());
    	
    	FixtureDef fixture = new FixtureDef();
    	
    	fixture.shape = shape;
    	fixture.restitution = 0;
    	fixture.density = 0;
    	
    	body.createFixture(fixture);
    }
}

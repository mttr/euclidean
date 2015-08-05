package com.euclidean;

import java.util.jar.Attributes;

public class XMLGameObject extends ObjectManager {
    private FixedArrayList<XMLGameComponent> gameComponentList;
    private Attributes attributes;
    
    public XMLGameObject() {
        gameComponentList = new FixedArrayList<XMLGameComponent>(BaseObject.sys.maxXMLObjectComponents);
    }

    public XMLGameObject(Attributes attributes) {
    	this();
		this.attributes = attributes;
	}

	public void add(XMLGameComponent xml_gamecomponent) {
        gameComponentList.add(xml_gamecomponent);
    }

    public GameObject buildObject() {
    	
    	GameObject object;

    	if (attributes != null)
    	{
    		object = new GameObject(attributes);
    	}
    	else
    	{
    		object = new GameObject();
    	}
        
        for (int i = 0; i < gameComponentList.size(); i++)
        {
            object.add(gameComponentList.get(i).buildComponent());
        }
        return object;
    }
}

package com.euclidean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLObjectHandler extends DefaultHandler {
    private boolean inGameObject;
    private XMLGameObject gameObject;
    private String objectName;
    private boolean attributeSet;
    private boolean inInit;

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        
        if (qName == "GameObject")
        {
        	if (attributes.getLength() > 0)
        	{
	        	java.util.jar.Attributes attributes_copy = copyAttributes(attributes);
	            gameObject = new XMLGameObject(attributes_copy);
        	}
        	else
        	{
        		gameObject = new XMLGameObject();
        	}
            inGameObject = true;
            return;
        }
        else if (qName == "AnimationSet" || qName == "InputGameInterface")
        {
            gameObject = new XMLAttributesSet();
            attributeSet = true;
            return;
        }
        else if (qName == "Init")
        {
            inInit = true;
        }
        
        if (inInit)
        {
            initElement(qName, attributes);
            return;
        }
        
        java.util.jar.Attributes attributes_copy = copyAttributes(attributes);
                
        if (inGameObject) 
        {            
            XMLGameComponent game_component = new XMLGameComponent(qName, attributes_copy);
            gameObject.add(game_component);            
        }
        else if (attributeSet)
        {
            ((XMLAttributesSet)gameObject).add(attributes_copy);
        }
     }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        
        if (qName == "GameObject")
        {
            inGameObject = false;
        }
        else if (qName == "AnimationSet")
        {
            attributeSet = false;
        }
        else if (qName == "Init")
        {
            inInit = false;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        BaseObject.sys.objectSystem.add(objectName, gameObject);
    }
    
    public void setName(String name) {
        objectName = name;
    }

    private java.util.jar.Attributes copyAttributes(Attributes attributes) {
        java.util.jar.Attributes attributes_copy = new java.util.jar.Attributes();
        
        for (int i = 0; i < attributes.getLength(); i++)
        {
            attributes_copy.putValue(attributes.getQName(i), attributes.getValue(i));
        }
        return attributes_copy;
    }

    private void initElement(String qName, Attributes attributes) {
        if (qName == "Object")
        {
            GameObject object = BaseObject.sys.objectSystem.getObject(attributes.getValue("type"));
            BaseObject.sys.manager.add(object);
        }
        else if (qName == "Map")
        {
            XMLLoader loader = new XMLLoader();
            MapHandler handler = new MapHandler();
            loader.setHandler(handler);
            loader.load("maps/" + attributes.getValue("file"));
        }
    }
}

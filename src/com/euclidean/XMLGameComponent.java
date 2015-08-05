package com.euclidean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.jar.Attributes;

public class XMLGameComponent extends BaseObject {
    private String name;
    private Attributes attributes;

    public XMLGameComponent(String qName, Attributes attributes) {
        name = qName;
        this.attributes = attributes;
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

    public GameComponent buildComponent() {
        GameComponent object = null;
        Class<?> cl = null;
        Class<?>[] argument_types = null;
        Object[] arguments = null;
        Constructor<?> constructor = null;
        Object result = null;
        
        if (attributes.size() != 0) {
            argument_types = new Class[] { Attributes.class };
            arguments = new Object[] { attributes };
        }
        
        try {
            cl = Class.forName("org.euclidean." + name);
        } catch (ClassNotFoundException e) {
        	try {
                cl = Class.forName(BaseObject.sys.packageString + "." + name);
        	} catch (ClassNotFoundException e1) {
        	    BaseObject.app.log("euclidean", e.toString() + " Target component: " + name);
        		BaseObject.app.log("euclidean", e1.toString() + " Target component: " + name);
        		System.exit(1);
        	}
        }
        
        try {
            constructor = cl.getConstructor(argument_types);
            result = constructor.newInstance(arguments);
        } catch (InvocationTargetException e) {
            BaseObject.app.log("euclidean", e.toString() + " Target component: " + name + " Cause: " + e.getCause().toString());
            System.exit(1);
        } catch (Exception e) {
            BaseObject.app.log("euclidean", e.toString() + " Target component: " + name);
            System.exit(1);
        }
        
        object = (GameComponent) result;
        // FIXME Should handle better if null;
        return object;
    }
}

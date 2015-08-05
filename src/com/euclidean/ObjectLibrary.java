package com.euclidean;


public class ObjectLibrary extends BaseObject {
    private FixedArrayList<String> nameRegistry;
    private FixedArrayList<XMLGameObject> objectRegistry;
    private XMLObjectHandler handler;

    public ObjectLibrary() {
        nameRegistry = new FixedArrayList<String>(sys.maxObjectLibraryObjects);
        objectRegistry = new FixedArrayList<XMLGameObject>(sys.maxObjectLibraryObjects);
        handler = new XMLObjectHandler();
    }
    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }
    
    //FIXME overlap should not be allowed!!!!!!!!!
    public void add(String mObjectId, XMLGameObject mGameObject) {
        nameRegistry.add(mObjectId);
        objectRegistry.add(mGameObject);
    }
    
    public GameObject getObject(String name) {
        XMLGameObject object = find(name);
        
        if (object != null) return object.buildObject();

        load(name, "objects");
        return objectRegistry.last().buildObject();
    }
    
    public XMLAttributesSet getAttributesSet(String name, String directory) {
        XMLAttributesSet set = (XMLAttributesSet) find(name);
        
        if (set != null) return set;
        
        load(name, directory);
        return (XMLAttributesSet)objectRegistry.last();
    }
    
    private XMLGameObject find(String name) {
        for (int i = 0; i < nameRegistry.size(); i++)
        {
            if (nameRegistry.get(i) == name)
            {
                return objectRegistry.get(i);
            }
        } 
        return null;
    }
    
    private void load(String name, String prefix) {
        XMLLoader loader = new XMLLoader();
        handler.setName(name);
        loader.setHandler(handler);
        loader.load(prefix + "/" + name + ".xml");
    }
}

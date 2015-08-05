package com.euclidean;

import java.util.jar.Attributes;

public class XMLAttributesSet extends XMLGameObject {
    FixedArrayList<Attributes> attributes;
    
    public XMLAttributesSet() {
        attributes = new FixedArrayList<Attributes>(BaseObject.sys.maxXMLAttributes);
    }
    
    public void add(Attributes attributes) {
        this.attributes.add(attributes);
    }
    
    public FixedArrayList<Attributes> getAttributesSet() {
        return attributes;
    }
}

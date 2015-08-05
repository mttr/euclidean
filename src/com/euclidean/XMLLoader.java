package com.euclidean;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.badlogic.gdx.Gdx;

public class XMLLoader extends BaseObject {
    private DefaultHandler handler;
    
    public void setHandler(DefaultHandler handler) {
        this.handler = handler;
    }
    public void load(String file) {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            
            xr.setContentHandler(handler);
            
            InputStream inputstream = Gdx.files.internal(file).read();
            xr.parse(new InputSource(inputstream));
            inputstream.close();
        } catch (IOException e) {
            app.log("youkon", e.toString());
        } catch (SAXException e) {
            app.log("youkon", e.toString());
        } catch (ParserConfigurationException e) {
            app.log("youkon", e.toString());
        }
    }
    @Override
    public void reset() {
        // TODO Auto-generated method stub
        
    }
}

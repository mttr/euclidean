package com.euclidean;


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Euclidean extends BaseObject {
    
    public static void main(String[] args) {       
        initRegistry();
        BaseObject.app = new LwjglApplication(new GameClass(), "Euclidean Wars", (int) sys.screenWidth, (int) sys.screenHeight, false);
    }

    public static void initRegistry() {
        sys.objectSystem = new ObjectLibrary();
        sys.keyboard = new KeyboardHandler();
        sys.screenWidth = 1024;
        sys.screenHeight = 768;
        sys.packageString = "com.euclidean";
        sys.groupFilters = new GroupFilterSystem();
        sys.contactSystem = new ContactSystem();
    }

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}
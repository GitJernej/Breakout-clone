package com.jernej.erman.brickgame.util;

public class Constants {
	
	// visible world dimensions
	public static final float VIEWPORT_WIDTH = 16.0f;
	public static final float VIEWPORT_HEIGHT = 9.0f;
	
	// GUI width and height
	public static final float VIEWPORT_GUI_WIDTH = 1280.0f;
	public static final float VIEWPORT_GUI_HEIGHT = 720.0f;
	
	// max bounce angle
	public static final float MAX_BOUNCE_ANGLE = 75.0f;
	
	// ball max velocity
	public static final float MAX_BALL_VELOCITY = 5.0f;
	
	// lives
	public static final int STARTING_LIVES = 3;
	
	// level start delay
	public static final float LEVEL_START_DELAY = 0.1f;
	
	// atlas path
	public static final String TEXTURE_ATLAS_OBJECTS = "images/brickgame.atlas";
	
	// scene2d resources
	public static final String TEXTURE_ATLAS_UI = "images/brickgame-ui.atlas";
	
	public static final String SKIN_BRICKGAME_UI = "images/brickgame-ui.json";
	
	// level path
	public static final String LEVEL = "levels/level_color.png";
	
	// show fps
	public static final boolean SHOW_FPS = true;
	
	// is camera helper active
	public static final boolean DEBUG = false;

}
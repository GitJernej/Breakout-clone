package com.jernej.erman.brickgame.util;

public class Constants {
	
	// visible world dimensions
	public static final float VIEWPORT_WIDTH = 16.0f;
	public static final float VIEWPORT_HEIGHT = 9.0f;
	
	// GUI width and height
	public static final float VIEWPORT_GUI_WIDTH = 1280.0f;
	public static final float VIEWPORT_GUI_HEIGHT = 720.0f;
	
	// max bounce angle
	public static final float MAX_BOUNCE_ANGLE = 60.0f;
	
	// ball velocity limits
	public static final float MAX_BALL_VELOCITY = 15;
	public static final float MIN_BALL_VELOCITY = 5;
	public static final float START_BALL_BELOCITY = 7.5f;
	
	// lives
	public static final int STARTING_LIVES = 3;
	
	// score value of lives
	public static final int LIVES_VALUE = 500;
	
	// score value of bricks
	public static final int BRICK_VALUE = 75;
	
	// game over delay
	public static final float TIME_DELAY_GAME_OVER = 3;
		
	// atlas path
	public static final String TEXTURE_ATLAS_OBJECTS = "images/brickgame.atlas";
	
	// scene2d resources (main menu...)
	public static final String TEXTURE_ATLAS_UI = "images/brickgame-ui.atlas";	
	public static final String SKIN_BRICKGAME_UI = "images/brickgame-ui.json";
	
	// skins resources (options menu....)
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";
	public static final String SKIN_LIBGDX_UI = "images/uiskin.json";
	
	// TEST LEVEL
	//public static final String LEVEL = "levels/level_test.png";
	
	// actual level
	public static final String LEVEL = "levels/simple.png";
	
	// show fps
	public static final boolean SHOW_FPS = true;
	
	// is camera helper active
	public static final boolean DEBUG = false;
	
	// game preferences file
	public static final String PREFERENCES = "bricks.prefs";

}
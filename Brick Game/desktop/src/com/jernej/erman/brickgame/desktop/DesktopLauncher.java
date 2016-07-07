package com.jernej.erman.brickgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jernej.erman.brickgame.BrickGame;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class DesktopLauncher {
	
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;	
	
	public static void main (String[] arg) {
		
		if(rebuildAtlas){
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "assets-raw/images", "assets/images", "brickgame");
			TexturePacker.process(settings, "assets-raw/images-ui", "assets/images", "brickgame-ui");
		}
		
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Brick Game";
		config.width = 1280;
		config.height = 720;
		config.resizable = false;
		
		new LwjglApplication(new BrickGame(), config);
	}
}

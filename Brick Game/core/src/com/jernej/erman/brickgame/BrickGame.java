package com.jernej.erman.brickgame;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.jernej.erman.brickgame.game.Assets;
import com.jernej.erman.brickgame.screens.MenuScreen;

public class BrickGame extends Game {
		
	@Override
	public void create() {
		// set libGDX log level to debug
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		// load assets
		Assets.instance.init(new AssetManager());
		
		// start game at menu screen
		setScreen(new MenuScreen(this));
	}
}

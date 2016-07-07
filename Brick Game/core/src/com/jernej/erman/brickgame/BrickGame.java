package com.jernej.erman.brickgame;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.jernej.erman.brickgame.game.Assets;
import com.jernej.erman.brickgame.screens.DirectedGame;
import com.jernej.erman.brickgame.screens.MenuScreen;
import com.jernej.erman.brickgame.screens.transitions.ScreenTransition;
import com.jernej.erman.brickgame.screens.transitions.ScreenTransitionFade;
import com.jernej.erman.brickgame.util.AudioManager;
import com.jernej.erman.brickgame.util.GamePreferences;

public class BrickGame extends DirectedGame {
		
	@Override
	public void create() {
		// set libGDX log level to debug
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		// load assets
		Assets.instance.init(new AssetManager());
		
		// load preferences for audio settings and star playing music
		GamePreferences.instance.load();
		AudioManager.instance.play(Assets.instance.music.song01);
		
		// start game at menu screen
		ScreenTransition transition = ScreenTransitionFade.init(1.5f);
		setScreen(new MenuScreen(this), transition);
	}
}

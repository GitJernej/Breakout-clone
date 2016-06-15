package com.jernej.erman.brickgame;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.jernej.erman.brickgame.game.Assets;
import com.jernej.erman.brickgame.game.WorldController;
import com.jernej.erman.brickgame.game.WorldRenderer;

public class BrickGame implements ApplicationListener {

	private static final String TAG = BrickGame.class.getName();
	
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	@Override
	public void create() {
		// set Libgdx log level to debug
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		// load assets
		Assets.instance.init(new AssetManager());
		
		Gdx.app.debug(TAG,  "Log level set. \ninit controller and renderer");
		// initialize world Controller and Renderer
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.debug(TAG,  "resizing to: " + width + " width, " + height + " height.");
		worldRenderer.resize(width, height);		
	}

	@Override
	public void render() {
		// update the game by the time that has passed since last render
		worldController.update(Gdx.graphics.getDeltaTime());
		
		// sets the clear screen color to RGB 0, 0, 0, alpha 1 - black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		// clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// render the game world on screen
		worldRenderer.render();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		Gdx.app.debug(TAG,  "disposing world renderer");
		worldRenderer.dispose();

		Gdx.app.debug(TAG,  "disposing assets instance");
		Assets.instance.dispose();
	}
}

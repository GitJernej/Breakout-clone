package com.jernej.erman.brickgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jernej.erman.brickgame.game.WorldController;
import com.jernej.erman.brickgame.game.WorldRenderer;
import com.jernej.erman.brickgame.util.Constants;

public class GameScreen extends AbstractGameScreen{
	
	private static final String TAG = GameScreen.class.getName();
	
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	private boolean paused;
	
	private boolean debugEnabled = false;
	
	
	public GameScreen (DirectedGame game) {
		super(game);
	}	

	@Override
	public void render(float deltaTime) {
		// don't update if paused
		if (!paused){
			// update the game world by the time that has passed since last render
			worldController.update(deltaTime);
		}
		// sets the clear screen color
		Gdx.gl.glClearColor(46/255.0f, 46/255.0f, 46/255.0f, 1.0f);
		// clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// render the game world to screen
		worldRenderer.render();
	}

	protected void onMenuClicked() {
		worldController.backToMenu();
		
	}

	protected void onResumeClicked() {
		resume();		
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void show() {
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void hide() {
		
		worldRenderer.dispose();
		Gdx.input.setCatchBackKey(false);
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		super.resume();
		// only called on Android
		paused = false;
	}
	
	@Override
	public InputProcessor getInputProcessor () {
		return worldController;
	}
		
	
}

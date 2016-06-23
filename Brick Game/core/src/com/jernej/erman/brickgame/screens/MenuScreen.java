package com.jernej.erman.brickgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jernej.erman.brickgame.util.Constants;

public class MenuScreen extends AbstractGameScreen {
	private static final String TAG = MenuScreen.class.getName();
	
	private Stage stage;
	private Skin skinBrickgame;
		
	// menu
	private Image imgMenuArt;
	private Image imgTitle;
	private Button btnPlay;
	private Button btnOptions;
	private Button btnQuit;
	
	// debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;

	public MenuScreen(Game game) {
		super(game);
	}
	
	private void rebuildStage () {
		skinBrickgame = new Skin(
				Gdx.files.internal(Constants.SKIN_BRICKGAME_UI), 
				new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
		
		// build all layers
		Table layerBackground = buildBackgroundLayer();
		Table layerButton = buildButtonLayer();
		
		// assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerBackground);
		stack.add(layerButton);
		
	}
	


	private Table buildBackgroundLayer() {
		Table layer = new Table();
		imgTitle = new Image(skinBrickgame, "bricks-title");
		layer.addActor(imgTitle);
		imgTitle.setPosition(160, 450);
		
		imgMenuArt = new Image(skinBrickgame, "menu-art");
		layer.addActor(imgMenuArt);
		imgMenuArt.setPosition(1280 - imgMenuArt.getWidth(), 0);
		
		
		return layer;
	}

	private Table buildButtonLayer() {
		
		Table layer = new Table();
		
		btnPlay = new Button(skinBrickgame, "play");
		layer.addActor(btnPlay);
		btnPlay.setPosition(160, imgTitle.getY() - btnPlay.getHeight() - 20);	
		btnPlay.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked();
				
			}
			
		});
		
		btnOptions = new Button(skinBrickgame, "options");
		layer.addActor(btnOptions);
		btnOptions.setPosition(160, btnPlay.getY() - btnOptions.getHeight() - 20);			
		btnOptions.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onOptionsClicked();
				
			}
			
		});

		btnQuit = new Button(skinBrickgame, "quit");
		layer.addActor(btnQuit);
		btnQuit.setPosition(160, btnOptions.getY() - btnQuit.getHeight() - 20);
		btnQuit.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onQuitClicked();
				
			}
			
		});
		
		return layer;
	}

	protected void onQuitClicked() {
		Gdx.app.exit();
		
	}

	protected void onOptionsClicked() {
		// TODO Auto-generated method stub
		
	}

	protected void onPlayClicked() {
		game.setScreen(new GameScreen(game));
		
	}

	@Override	
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(46/255.0f, 46/255.0f, 46/255.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (debugEnabled) {
			debugRebuildStage -= deltaTime;
			if (debugRebuildStage <= 0){
				debugRebuildStage = DEBUG_REBUILD_INTERVAL;
				rebuildStage();
			}
		}
		
		stage.act(deltaTime);
		stage.draw();
		stage.setDebugAll(debugEnabled);

	}
	
	
	@Override	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	
	@Override	public void show() {
		stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		rebuildStage();
	}
	
	@Override	public void hide() {
		stage.dispose();
		skinBrickgame.dispose();
	}
	
	@Override	public void pause() {}

}

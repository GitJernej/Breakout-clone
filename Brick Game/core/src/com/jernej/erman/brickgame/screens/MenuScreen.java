package com.jernej.erman.brickgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jernej.erman.brickgame.screens.transitions.ScreenTransition;
import com.jernej.erman.brickgame.screens.transitions.ScreenTransitionSlide;
import com.jernej.erman.brickgame.util.AudioManager;
import com.jernej.erman.brickgame.util.Constants;
import com.jernej.erman.brickgame.util.GamePreferences;

public class MenuScreen extends AbstractGameScreen {
	private static final String TAG = MenuScreen.class.getName();
	
	private Stage stage;
	private Skin skinBrickgame;
	private Skin skinLibgdx;
		
	// menu
	private Image imgTitle;
	private Button btnPlay;
	private Button btnOptions;
	private Button btnQuit;
	
	// levels
	private Array<ImageButton> levelButtons;
	
	// options
	private Window winOptions;
	private TextButton btnWinOptSave;
	private TextButton btnWinOptCancel;
	private CheckBox chkSound;
	private Slider sldSound;
	private CheckBox chkMusic;
	private Slider sldMusic;
	
	private Slider sldBallSpeed;
	
	private CheckBox chkShowFpsCounter;
	
	
	// debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;

	public MenuScreen(DirectedGame game) {
		super(game);
	}
	
	private void loadSettings () {
		Gdx.app.debug("TAG", "load settings started");
		GamePreferences prefs = GamePreferences.instance;
		prefs.load();
		chkSound.setChecked(prefs.sound);
		sldSound.setValue(prefs.volSound);
		chkMusic.setChecked(prefs.music);
		sldMusic.setValue(prefs.volMusic);
		
		sldBallSpeed.setValue(prefs.ballSpeed);
		
		chkShowFpsCounter.setChecked(prefs.showFpsCounter);
	}

	private void saveSettings () {
		GamePreferences prefs = GamePreferences.instance;
		
		prefs.sound = chkSound.isChecked();
		prefs.volSound = sldSound.getValue();
		prefs.music = chkMusic.isChecked();
		prefs.volMusic = sldMusic.getValue();
		
		prefs.ballSpeed = sldBallSpeed.getValue();
		
		prefs.showFpsCounter = chkShowFpsCounter.isChecked();
		prefs.save();
	}
	
	private void onSaveClicked() {
		saveSettings();
		onCancelClicked();
		AudioManager.instance.onSettingsUpdated();
	}
	
	private void onCancelClicked() {
		winOptions.setVisible(false);
		AudioManager.instance.onSettingsUpdated();
	}

	private void rebuildStage () {
		skinBrickgame = new Skin(
				Gdx.files.internal(Constants.SKIN_BRICKGAME_UI), 
				new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
		skinLibgdx = new Skin(
				Gdx.files.internal(Constants.SKIN_LIBGDX_UI), 
				new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
		
		// scroll test
		ScrollPane scrollLevelsLayer = buildLevelsLayer();
		
		// build all layers
		Table layerBackground = buildBackgroundLayer();
		Table layerButton = buildButtonLayer();
		
		Table layerOptionsWindow = buildOptionsWindowLayer();
		
		// assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerBackground);
		
		stack.add(scrollLevelsLayer);
		
		stack.add(layerButton);
		stage.addActor(layerOptionsWindow);
		
	}

	private Table buildOptWinAudioGameSettings() {
		Table tbl = new Table();
		// title: "audio"
		tbl.pad(10,10,0,10);
		tbl.add(new Label("Audio", skinLibgdx, "default-font", Color.CYAN)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		
		// checkbox, "sound" label, sound volume slider
		chkSound = new CheckBox("", skinLibgdx);
		tbl.add(chkSound);
		tbl.add(new Label ("Sound", skinLibgdx));
		
		sldSound = new Slider (0.0f, 1.0f, 0.1f, false, skinLibgdx);
		tbl.add(sldSound);
		tbl.row();
		
		// checkbox, "music" label, music volume slider
		chkMusic = new CheckBox("", skinLibgdx);
		tbl.add(chkMusic);
		tbl.add(new Label("Music", skinLibgdx));
		
		sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		tbl.add(sldMusic);
		tbl.row();
		
		// title: "game"
		tbl.add(new Label("Game", skinLibgdx, "default-font", Color.CYAN)).colspan(3);
		tbl.row();
		
		// ball speed slider
		sldBallSpeed = new Slider(0.1f, 1.1f, 0.1f, false, skinLibgdx);
		tbl.add();
		tbl.add(new Label ("Ball Speed", skinLibgdx));
		tbl.add(sldBallSpeed);
		tbl.row();
		
		return tbl;
	}
	
	private Table buildOptWinDebug() {
		Table tbl = new Table();
		// title "debug"
		tbl.pad(10,10,0,10);
		tbl.add(new Label ("Debug", skinLibgdx, "default-font", Color.WHITE)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		
		// checkbox, "show fps counter" label
		chkShowFpsCounter = new CheckBox("", skinLibgdx);
		tbl.add(new Label ("Show FPS Counter", skinLibgdx));
		tbl.add(chkShowFpsCounter);
		tbl.row();
		return tbl;
	}
	
	private Table buildOptWinButtons () {
		Table tbl = new Table();
		// separator
		Label lbl = null;
		lbl = new Label("", skinLibgdx);
		lbl.setColor(0.75f, 0.75f, 0.75f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0,0,0,1);
		tbl.row();
		
		lbl=new Label("", skinLibgdx);
		lbl.setColor(0.5f, 0.5f, 0.5f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
		tbl.row();
		
		// save button with event handler
		btnWinOptSave = new TextButton("Save", skinLibgdx);
		tbl.add(btnWinOptSave).padRight(30);
		btnWinOptSave.addListener(new ChangeListener () {
			@Override
			public void changed (ChangeEvent event, Actor actor){
				onSaveClicked();
			}
		});
		
		// Cancel button with event handler
		btnWinOptCancel = new TextButton("Cancel", skinLibgdx);
		tbl.add(btnWinOptCancel);
		btnWinOptCancel.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor){
				onCancelClicked();
			}
		});
		return tbl;
	}
	
	private Table buildOptionsWindowLayer() {
		winOptions = new Window("Options", skinLibgdx);
		
		// audio settings: sound/music checkbox with slider
		winOptions.add(buildOptWinAudioGameSettings()).row();
		
		// debug: show fps counter
		winOptions.add(buildOptWinDebug()).row();
		
		// seperator and buttons: save, cancel.
		winOptions.add(buildOptWinButtons()).pad(10,0,10,0);
		
		// make options window slighty transparent
		winOptions.setColor(1,1,1,0.8f);
		// hide options window by default
		winOptions.setVisible(false);
		if (debugEnabled) winOptions.debug();
		// let TableLayout recalculate widget sizes and positions
		winOptions.pack();
		// move options window to the center
		winOptions.setPosition((Constants.VIEWPORT_GUI_WIDTH / 2) - (winOptions.getWidth() / 2), 
				(Constants.VIEWPORT_GUI_HEIGHT / 2) - (winOptions.getHeight() / 2));
		
		return winOptions;
	}


	private Table buildBackgroundLayer() {
		Table layer = new Table();
		imgTitle = new Image(skinBrickgame, "bricks-title");
		layer.addActor(imgTitle);
		imgTitle.setPosition(160, 450);		
		return layer;
	}

	private ScrollPane buildLevelsLayer() {
		Table tbl = new Table();
		levelButtons = new Array<ImageButton>();
		
		tbl.add(new Label("Selected: " + GamePreferences.instance.levelName, skinLibgdx)).colspan(3);
		tbl.row();
		
		tbl.add(buildLevelButton("level 1", GamePreferences.instance.level_00)).pad(10, 10, 10, 10);
		tbl.add(buildLevelButton("level 2", GamePreferences.instance.level_01)).pad(10, 10, 10, 10);
		tbl.add(buildLevelButton("level 3", GamePreferences.instance.level_02)).pad(10, 10, 10, 10);
		tbl.row();
		tbl.add(buildLevelButton("level 4", GamePreferences.instance.level_03)).pad(10, 10, 10, 10);
		tbl.add(buildLevelButton("level 5", GamePreferences.instance.level_04)).pad(10, 10, 10, 10);
		tbl.add(buildLevelButton("level 6", GamePreferences.instance.level_05)).pad(10, 10, 10, 10);
		tbl.row();
		tbl.add(buildLevelButton("level 7", GamePreferences.instance.level_06)).pad(10, 10, 10, 10);
		tbl.add(buildLevelButton("level 8", GamePreferences.instance.level_07)).pad(10, 10, 10, 10);
		tbl.add(buildLevelButton("level 9", GamePreferences.instance.level_08)).pad(10, 10, 10, 10);
		tbl.row();
		
		
		tbl.pad(10,10,10,10);
		tbl.padRight(150);
		tbl.align(Align.right);

		ScrollPane scrollPane = new ScrollPane(tbl);
		// set scroll pane size/scrolling and so on...
		scrollPane.setOrigin(150, 150);
		return scrollPane;
	}
	
	private Button buildLevelButton(final String levelName, final String levelFile){
		ImageButton button = new ImageButton(skinBrickgame, levelName);
		
		// TODO - buttons are too small. 
		
		button.getImageCell().expand().fill();
		
		button.pad(15, 15, 15, 15);
		button.setName(levelFile);
		button.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.debug(TAG, event.getListenerActor().getName());
				GamePreferences.instance.gameLevel = levelFile;
				GamePreferences.instance.levelName = levelName;
				rebuildStage();
			}
		});	
		return button;
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
		loadSettings();
		winOptions.setVisible(true);
		
	}

	protected void onPlayClicked() {
		ScreenTransition transition = ScreenTransitionSlide.init(1.0f, 
				ScreenTransitionSlide.DOWN, false, Interpolation.fade);
		game.setScreen(new GameScreen(game), transition);
		
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
		rebuildStage();
	}
	
	@Override	public void hide() {
		stage.dispose();
		skinBrickgame.dispose();
		skinLibgdx.dispose();
	}
	
	@Override	public void pause() {}
	
	@Override
	public InputProcessor getInputProcessor () {
		return stage;
	}

}

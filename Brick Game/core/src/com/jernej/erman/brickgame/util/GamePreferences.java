package com.jernej.erman.brickgame.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GamePreferences {
	public static final String TAG = GamePreferences.class.getName();
	
	public static final GamePreferences instance = new GamePreferences();
	
	public String level_00, level_01, level_02, level_03, level_04, level_05, level_06, level_07, level_08;
	public String gameLevel;
	
	public boolean sound;
	public boolean music;
	public float volSound;
	public float volMusic;
	
	public float ballSpeed;
	
	public boolean showFpsCounter;
	
	private Preferences prefs;
	
	private GamePreferences () {
		prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
	}
	
	public void load () {
		level_00 = prefs.getString("level_00", "level_00.png");
		level_01 = prefs.getString("level_01", "level_01.png");
		level_02 = prefs.getString("level_02", "level_02.png");
		level_03 = prefs.getString("level_03", "level_03.png");
		level_04 = prefs.getString("level_04", "level_04.png");
		level_05 = prefs.getString("level_05", "level_05.png");
		level_06 = prefs.getString("level_06", "level_06.png");
		level_07 = prefs.getString("level_07", "level_07.png");
		level_08 = prefs.getString("level_08", "level_08.png");
		
		gameLevel = prefs.getString("gameLevel", "level_00.png");
		
		sound = prefs.getBoolean("sound", true);
		music = prefs.getBoolean("music", true);
		
		volSound = MathUtils.clamp(prefs.getFloat("volSound",  0.5f), 0.0f, 1.0f);
		volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0.0f, 1.0f);
		
		ballSpeed = MathUtils.clamp(prefs.getFloat("ballSpeed", 0.5f), 0.0f, 1.0f);
		
		showFpsCounter = prefs.getBoolean("showFpsCounter", false);
	}
	
	public void save () {
		prefs.putString("level_00", level_00);
		prefs.putString("level_01", level_01);
		prefs.putString("level_02", level_02);
		prefs.putString("level_03", level_03);
		prefs.putString("level_04", level_04);
		prefs.putString("level_05", level_05);
		prefs.putString("level_06", level_06);
		prefs.putString("level_07", level_07);
		prefs.putString("level_08", level_08);
		
		prefs.putString("gameLevel", gameLevel);
		
		prefs.putBoolean("sound", sound);
		prefs.putBoolean("music", music);
		prefs.putFloat("volSound", volSound);
		prefs.putFloat("volMusic", volMusic);
		
		prefs.putFloat("ballSpeed", ballSpeed);
		
		prefs.putBoolean("showFpsCounter", showFpsCounter);
		prefs.flush();
	}
}

package com.jernej.erman.brickgame.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jernej.erman.brickgame.screens.transitions.ScreenTransition;

public abstract class DirectedGame implements ApplicationListener {
	private boolean init;
	private AbstractGameScreen currentScreen;
	private AbstractGameScreen nextScreen;
	private FrameBuffer currentFbo;
	private FrameBuffer nextFbo;
	private SpriteBatch batch;
	private float t;
	private ScreenTransition screenTransition;
	
	public void setScreen (AbstractGameScreen screen){
		setScreen(screen, null);
	}
	
	public void setScreen(AbstractGameScreen screen, ScreenTransition screenTransition){
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		
		if(!init) {
			currentFbo = new FrameBuffer(Format.RGB888, w, h, false);
			nextFbo = new FrameBuffer(Format.RGB888, w, h, false);
			batch = new SpriteBatch();
			init = true;
		}
		// start new transition
		nextScreen = screen;
		nextScreen.show(); // activate next screen
		nextScreen.resize(w, h);
		nextScreen.render(0); // let screen update() once
		if(currentScreen != null) currentScreen.pause();
		nextScreen.pause();
		Gdx.input.setInputProcessor(null); // disable input
		this.screenTransition = screenTransition;
		t=0;
	}
	
	@Override
	public void render(){
		// get delta time and ensure an upper limit of one 60th second
		float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f/60.0f);
		if(nextScreen == null){
			// no ongoing transition
			if(currentScreen != null) currentScreen.render(deltaTime);
		} else {
			// ongoing transition
			float duration = 0;
			if(screenTransition != null)
				duration = screenTransition.getDuration();
			// update progress of ongoing transition
			t = Math.min(t + deltaTime, duration);
			if(screenTransition == null || t >= duration){
				//no transition effect set or transition has just finished
				if(currentScreen != null) currentScreen.hide();
				nextScreen.resume();
				// enable input for next screen
				Gdx.input.setInputProcessor(nextScreen.getInputProcessor());
				// switch screens
				currentScreen = nextScreen;
				nextScreen = null;
				screenTransition = null;				
			} else {
				// render screens to FBOs
				currentFbo.begin();
				if(currentScreen != null) currentScreen.render(deltaTime);				
				currentFbo.end();
				nextFbo.begin();
				nextScreen.render(deltaTime);
				nextFbo.end();
				// render transition effect to screen
				float alpha = t / duration;
				screenTransition.render(batch, currentFbo.getColorBufferTexture(), 
						nextFbo.getColorBufferTexture(), alpha);
			}
		}
	}
	
	@Override
	public void resize(int width, int height){
		if(currentScreen != null) currentScreen.resize(width, height);
		if(nextScreen != null) nextScreen.resize(width, height);
	}
	
	@Override
	public void pause (){
		if(currentScreen != null) currentScreen.pause();
	}
	
	@Override
	public void resume (){
		if(currentScreen != null) currentScreen.resume();
	}
	
	@Override
	public void dispose () {
		if(currentScreen != null) currentScreen.hide();
		if(nextScreen != null) nextScreen.hide();
		if(init){
			currentFbo.dispose();
			currentScreen = null;
			nextFbo.dispose();
			nextScreen = null;
			batch.dispose();
			init = false;
		}
	}
	
}
















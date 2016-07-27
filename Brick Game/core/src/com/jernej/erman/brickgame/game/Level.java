package com.jernej.erman.brickgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.jernej.erman.brickgame.game.objects.AbstractGameObject;
import com.jernej.erman.brickgame.game.objects.Ball;
import com.jernej.erman.brickgame.game.objects.Brick;
import com.jernej.erman.brickgame.game.objects.Pad;
import com.jernej.erman.brickgame.game.objects.PowerUp;

public class Level {
	public static final String TAG = Level.class.getName();
	
	public enum BLOCK_TYPE {
		EMPTY (0, 0, 0); // black
		
		private int color;
		
		private BLOCK_TYPE (int r, int g, int b) {
			color = r << 24 | g << 16 | b << 8 | 255;
		}
		
		public boolean sameColor (int color) {
			return this.color == color;
		}
		
		public int getColor () {
			return color;
		}
	}
	
	public Array<PowerUp> powerUps;
	public Array<Brick> bricks;
	public Pad pad;
	public Ball ball;
	
	public Level (String filename) {
		init(filename);
	}
	
	private void init (String filename){
		powerUps = new Array<PowerUp>();
		bricks = new Array<Brick>();
		pad = new Pad();
		ball = new Ball();
		
		// set pad
		pad.setPosition();
		// set ball
		ball.setPosition(pad);
				
		// load image file that has level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		
		// scan pixels from top-left to bottom right
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++){
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++){
				AbstractGameObject obj = null;
				
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				
				if (!BLOCK_TYPE.EMPTY.sameColor(currentPixel)){
					obj = new Brick();
					// 8 away from center, 0.025f away from the vertical edge
					// 4 away from center, 0.175f away from top horizontal edge.
					obj.position.set(pixelX - 8 + 0.025f, 4 + 0.175f - pixelY * 0.35f);
					obj.color.set(currentPixel);
					bricks.add((Brick)obj);
				}
			}
		}		
		
		// free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");		
	}
	
	public void update (float deltaTime) {
		for (Brick brick : bricks)
			brick.update(deltaTime);
		
		for (PowerUp powerUp : powerUps)
			powerUp.update(deltaTime);
		
		pad.update(deltaTime);

		ball.update(deltaTime);
		
		if(ball.ballLocked) 
			ball.setPosition(pad);
	}
	
	public void render (SpriteBatch batch) {
		for (Brick brick : bricks)
			brick.render(batch);
		
		for (PowerUp powerUp : powerUps)
			powerUp.render(batch);
		
		pad.render(batch);
		ball.render(batch);
		
	}
	
}

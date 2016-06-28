package com.jernej.erman.brickgame.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractGameObject {
	
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	public Color color;
	
	public Vector2 velocity;
	//public Vector2 terminalVelocity;
	//public Vector2 friction;
	
	public Rectangle bounds;
	
	public AbstractGameObject (){
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0; // no rotation.
		color = new Color();
		
		velocity = new Vector2();
		
		bounds = new Rectangle();
	}	
	
	public float centerX(){
		return position.x + dimension.x / 2;
	}
	
	public float centerY(){
		return position.y + dimension.y / 2;
	}
	
	public void update (float deltaTime) {
		// move to new position
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
	}
 
	public abstract void render (SpriteBatch batch);
	
}

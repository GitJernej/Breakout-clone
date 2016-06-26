package com.jernej.erman.brickgame.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jernej.erman.brickgame.game.Assets;
import com.jernej.erman.brickgame.util.Constants;

public class Ball extends AbstractGameObject {

	private TextureRegion regBall;
	public boolean ballLocked;
		
	public Ball () {
		init();
	}
	
	private void init(){
		ballLocked = true;
		
		dimension.set(0.2f, 0.2f);
		
		regBall = Assets.instance.ball.ball;
		
		// center image on the object
		origin.set(dimension.x / 2, dimension.y / 2);
		
		// bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
				
		// set physics values
		velocity.set(0.0f, - Constants.MAX_BALL_VELOCITY);
	}
	
	public void setPosition (AbstractGameObject pad) {
		position.set(pad.position.x + pad.dimension.x / 2, pad.position.y + pad.dimension.y);
	}
	
	public void bounceX () {
		velocity.x *= -1;
	}
	public void bounceY () {
		velocity.y *= -1;
	}
	
	
	@Override
	public void update (float deltaTime){
		if(ballLocked) return;
		super.update(deltaTime);
	}
	
	@Override
	public void render(SpriteBatch batch) {

		TextureRegion reg = null;
		
		reg = regBall;
		
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, 
				dimension.x, dimension.y, scale.x, scale.y, rotation, 
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), 
				false, false);		
	}

}

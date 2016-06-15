package com.jernej.erman.brickgame.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jernej.erman.brickgame.game.Assets;

public class Brick extends AbstractGameObject {

	private TextureRegion regBrick;
	
	public boolean destroyed;
	
	public Brick () {
		init();
	}
	
	private void init () {
		dimension.set(0.95f, 0.3f);
		destroyed = false;
		
		// center image on object
		origin.set(dimension.x / 2, dimension.y / 2);
		
		// bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		
		regBrick = Assets.instance.brick.brick;
	}
	
	
	@Override
	public void render(SpriteBatch batch) {
		
		if(destroyed) return;
		TextureRegion reg = null;
		
		reg = regBrick;
		// set color
		batch.setColor(color);
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, 
				dimension.x, dimension.y, scale.x, scale.y, rotation, 
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), 
				false, false);
		// reset color
		batch.setColor(1, 1, 1, 1);
	}

}

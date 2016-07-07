package com.jernej.erman.brickgame.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jernej.erman.brickgame.game.Assets;

public class Pad extends AbstractGameObject {

	
	private TextureRegion regPadCore;
	private TextureRegion regPadEdge;
	
	public Pad () {
		init();
	}
	
	private void init () {
		dimension.set(1.6f, 0.2f);

		regPadCore = Assets.instance.plainTexture.plainTexture;
		regPadEdge = Assets.instance.plainTexture.plainTexture;
		
		// center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		
		// bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
	}
	
	public void resizePad(float factor){
		if(dimension.x > 2.4f && factor > 1) return;
		else if (dimension.x < 0.8f && factor < 1 ) return;

		position.x = position.x - (dimension.x * factor - dimension.x)/2;
		dimension.x = dimension.x * factor;
		bounds.setWidth(dimension.x);
	}
	
	public void setPosition() {
		position.set( - dimension.x / 2, -4f);
	}
	
	@Override
	public void render(SpriteBatch batch) {

		TextureRegion reg = null;
		
		reg = regPadCore;
		
		// set core color
		batch.setColor(140/255.0f, 140/255.0f, 140/255.0f, 1);	
		
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, 
				dimension.x, dimension.y, scale.x, scale.y, rotation, 
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), 
				false, false);
		
		reg = regPadEdge;
		// left edge		
		
		// set color for edges
		batch.setColor(69/255.0f, 61/255.0f, 183/255.0f, 1.0f);
		
		float edgeWidth = 0.1f;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, 
				edgeWidth, dimension.y, scale.x, scale.y, rotation, 
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), 
				false, false);
		// right edge
		batch.draw(reg.getTexture(), position.x + dimension.x-edgeWidth, position.y, origin.x, origin.y, 
				edgeWidth, dimension.y, scale.x, scale.y, rotation, 
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), 
				false, false);
		
		// reset color to white
		batch.setColor(1, 1, 1, 1);
		
	}

}

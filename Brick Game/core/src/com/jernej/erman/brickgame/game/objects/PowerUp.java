package com.jernej.erman.brickgame.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jernej.erman.brickgame.game.Assets;

public class PowerUp extends AbstractGameObject {
		
	public enum PowerType {
		SHORT_PAD, LONG_PAD, EXTRA_LIFE, SPEED_UP, SPEED_DOWN;
	}
	
	public PowerType type;
	public boolean activated;
	
	private TextureRegion regPower;
	
	public PowerUp(Brick brick){
		init(brick);
	}
	
	private void init(Brick brick){		
		dimension.set(0.57f, 0.27f);
		// random roll for the power-up type
		rollForType();
		// load the proper image depending on type
		switch(type){
			case SHORT_PAD:
				regPower = Assets.instance.shortPad.shortPad;
				break;
			case LONG_PAD:
				regPower = Assets.instance.longPad.longPad;
				break;
			case EXTRA_LIFE:
				regPower = Assets.instance.extraLife.extraLife;
				break;
			case SPEED_UP:
				regPower = Assets.instance.speedUp.speedUp;
				break;
			case SPEED_DOWN:
				regPower = Assets.instance.speedDown.speedDown;
				break;
			default:
				regPower = Assets.instance.plainTexture.plainTexture;
		}
		// power-up is not activated/picked up
		activated = false;
		
		origin.set(dimension.x / 2, dimension.y / 2);
		
		position.x = brick.position.x + (brick.dimension.x - dimension.x)/2;
		position.y = brick.position.y + (brick.dimension.y - dimension.y)/2;
		
		bounds.set(0, 0, dimension.x, dimension.y);
		
		velocity.set(0, - (int) (Math.random()* (4-1) + 1));
		
		
	}
	
	private void rollForType(){
		int rand = (int) (Math.random() * (6 - 1) + 1);
		switch(rand){
		case 1:
			type = PowerType.SHORT_PAD;
			break;
		case 2:
			type = PowerType.LONG_PAD;
			break;
		case 3:
			type = PowerType.EXTRA_LIFE;
			break;
		case 4:
			type = PowerType.SPEED_UP;
			break;
		case 5:
			type = PowerType.SPEED_DOWN;
			break;
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		
		if(activated) return;
		
		TextureRegion reg = null;
		
		reg = regPower;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, 
				dimension.x, dimension.y, scale.x, scale.y, rotation, 
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), 
				false, false);
	}
	
}

package com.jernej.erman.brickgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.jernej.erman.brickgame.util.Constants;

public class Assets implements Disposable, AssetErrorListener {

	public static final String TAG = Assets.class.getName();
	
	public static final Assets instance = new Assets();
	
	private AssetManager assetManager;
	
	// singleton: prevents instantiation from other classes
	private Assets() {}
	
	public AssetPlainTexture plainTexture;
	
	public AssetPowerShortPad shortPad;
	public AssetPowerLongPad longPad;
	public AssetPowerSpeedUp speedUp;
	public AssetPowerSpeedDown speedDown;
	public AssetPowerExtraLife extraLife;
	
	public AssetFonts fonts;
	
	public AssetSounds sounds;
	public AssetMusic music;
	
	public void init (AssetManager assetManager) {
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);		
		//load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);

		// load sounds
		assetManager.load("sounds/Explosion.wav", Sound.class);
		assetManager.load("sounds/Bounce.wav", Sound.class);
		assetManager.load("sounds/Powerup.wav", Sound.class);
		assetManager.load("sounds/LifeLost.wav", Sound.class);
		// load music
		assetManager.load("music/Slips & Slurs - Divided VIP.mp3", Music.class);
		
		//start loading assets and wait until finished
		assetManager.finishLoading();		
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a: assetManager.getAssetNames()) Gdx.app.debug(TAG, "asset: " + a);		
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);		
		// enable texture filtering for pixel smoothing
		for (Texture t : atlas.getTextures()){
			t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		}		
		
		// create game resource objects		
		plainTexture = new AssetPlainTexture(atlas);
		shortPad = new AssetPowerShortPad(atlas);
		longPad = new AssetPowerLongPad(atlas);
		speedUp = new AssetPowerSpeedUp(atlas);
		speedDown = new AssetPowerSpeedDown(atlas);
		extraLife = new AssetPowerExtraLife(atlas);
		
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
		
		fonts = new AssetFonts();
	}
	
	public class AssetSounds {
		public final Sound brickDestroyed;
		public final Sound bounce;
		public final Sound powerUp;
		public final Sound lostLife;
		
		public AssetSounds (AssetManager am){
			brickDestroyed = am.get("sounds/Explosion.wav", Sound.class);
			bounce = am.get("sounds/Bounce.wav", Sound.class);
			powerUp = am.get("sounds/Powerup.wav", Sound.class);
			lostLife = am.get("sounds/LifeLost.wav", Sound.class);
		}
	}
	
	public class AssetMusic {
		public final Music song01;
		
		public AssetMusic (AssetManager am){
			song01 = am.get("music/Slips & Slurs - Divided VIP.mp3", Music.class);
		}
	}
	
	public class AssetFonts {
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		
		public AssetFonts () {
			// create three fonts using bitmap font Wendy
			defaultSmall = new BitmapFont (Gdx.files.internal("images/wendy.fnt"), false);
			defaultNormal = new BitmapFont (Gdx.files.internal("images/wendy.fnt"), false);
			defaultBig = new BitmapFont (Gdx.files.internal("images/wendy.fnt"), false);
			// set font sizes
			defaultSmall.getData().setScale(1.0f, 1.0f);
			defaultNormal.getData().setScale(2.0f, 2.0f);
			defaultBig.getData().setScale(4.0f, 4.0f);
		}
	}
	
	
	public class AssetPlainTexture{
		public final AtlasRegion plainTexture;
		
		public AssetPlainTexture (TextureAtlas atlas){
			plainTexture = atlas.findRegion("plainTexture");
		}
	}
	
	public class AssetPowerShortPad{
		public final AtlasRegion shortPad;
		
		public AssetPowerShortPad (TextureAtlas atlas){
			shortPad = atlas.findRegion("shortPad");
		}
	}
	
	public class AssetPowerLongPad{
		public final AtlasRegion longPad;
		
		public AssetPowerLongPad (TextureAtlas atlas){
			longPad = atlas.findRegion("longPad");
		}
	}
	
	public class AssetPowerSpeedUp{
		public final AtlasRegion speedUp;
		
		public AssetPowerSpeedUp (TextureAtlas atlas){
			speedUp = atlas.findRegion("speedUp");
		}
	}
	
	public class AssetPowerSpeedDown{
		public final AtlasRegion speedDown;
		
		public AssetPowerSpeedDown (TextureAtlas atlas){
			speedDown = atlas.findRegion("speedDown");
		}
	}
	
	public class AssetPowerExtraLife{
		public final AtlasRegion extraLife;
		
		public AssetPowerExtraLife (TextureAtlas atlas){
			extraLife = atlas.findRegion("extraLife");
		}
	}
		
	
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
	}
}

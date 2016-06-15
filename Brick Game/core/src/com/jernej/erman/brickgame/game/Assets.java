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
import com.badlogic.gdx.utils.Disposable;
import com.jernej.erman.brickgame.util.Constants;

public class Assets implements Disposable, AssetErrorListener {

	public static final String TAG = Assets.class.getName();
	
	public static final Assets instance = new Assets();
	
	private AssetManager assetManager;
	
	// singleton: prevents instantiation from other classes
	private Assets() {}
	
	public AssetBall ball;
	public AssetBrick brick;
	public AssetPadCore padCore;
	public AssetPadEdge padEdge;
	
	public AssetFonts fonts;
	
		
	
	public void init (AssetManager assetManager) {
		this.assetManager = assetManager;
		
		// set asset manager error handler
		assetManager.setErrorListener(this);
		
		//load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		
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
		ball = new AssetBall(atlas);
		brick = new AssetBrick(atlas);
		padCore = new AssetPadCore(atlas);
		padEdge = new AssetPadEdge(atlas);
		
		fonts = new AssetFonts();
		
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
			defaultSmall.getData().setScale(0.75f, 0.75f);
			defaultNormal.getData().setScale(1.0f, 1.0f);
			defaultBig.getData().setScale(2.0f, 2.0f);
			
		}
		
		
	}
	
	
	public class AssetBall {
		public final AtlasRegion ball;
		
		public AssetBall (TextureAtlas atlas){
			ball = atlas.findRegion("ball");
		}
	}
	
	
	public class AssetBrick {
		public final AtlasRegion brick;
		
		public AssetBrick (TextureAtlas atlas){
			brick = atlas.findRegion("brick");
		}
	}
	
	public class AssetPadCore {
		public final AtlasRegion padCore;
		
		public AssetPadCore (TextureAtlas atlas){
			padCore = atlas.findRegion("pad_core");
		}
	}
	
	
	public class AssetPadEdge {
		public final AtlasRegion padEdge;
		
		public AssetPadEdge (TextureAtlas atlas){
			padEdge = atlas.findRegion("pad_edge");
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

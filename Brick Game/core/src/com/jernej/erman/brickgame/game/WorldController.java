package com.jernej.erman.brickgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Game;
import com.jernej.erman.brickgame.screens.MenuScreen;
import com.jernej.erman.brickgame.game.objects.Ball;
import com.jernej.erman.brickgame.game.objects.Brick;
import com.jernej.erman.brickgame.game.objects.Pad;
import com.jernej.erman.brickgame.util.CameraHelper;
import com.jernej.erman.brickgame.util.Constants;

public class WorldController extends InputAdapter {

	private static final String TAG = WorldController.class.getName();
	
	boolean paused = false;
	
	boolean hasGameEnded;
	
	public Level level;
	public int score;
	public int lives;
	
	// number of blocks
	public int bricksNumber;
	
	public CameraHelper cameraHelper;
	
	// rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
		
	// delays
	private float timeLeftGameOverDelay;
	
	public float scoreVisual;
	
	private Game game;
	
	private void backToMenu() {
		// switch to menu screen
		game.setScreen(new MenuScreen(game));
	}	
		
	public WorldController(Game game){
		this.game = game;
		init();
	}
	
	private void init(){

		Gdx.input.setCursorCatched(true);
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.STARTING_LIVES;
		
		timeLeftGameOverDelay = 0;
		hasGameEnded = false;
		//create level first...
		initLevel();
		// then get the number of blocks.
		
	}
	
	private void initLevel () {
		score = 0;
		scoreVisual = score;
		level = new Level(Constants.LEVEL);
		bricksNumber = level.bricks.size;
		Gdx.app.debug(TAG, "number of bricks in level: " + bricksNumber);	

	}
	

	public void update(float deltaTime){
		if (Constants.DEBUG) handleDebugInput(deltaTime);	
				
		if((areBricksGone() || isGameOver() ) && !hasGameEnded){
			score += (Constants.LIVES_VALUE * lives);
			hasGameEnded = true;
			paused = true;
			timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
		}
		
		if(hasGameEnded && timeLeftGameOverDelay < 0){
			Gdx.input.setCursorCatched(false);
			backToMenu();
		} else {
			timeLeftGameOverDelay -= deltaTime;
		}
		
		if (paused) return;	
		
		handleGameInput(deltaTime);	
				
		level.update(deltaTime);
		testColisions(deltaTime);
	
		if (scoreVisual < score)
			scoreVisual = Math.min(score, scoreVisual + Constants.BRICK_VALUE * deltaTime);
		
	}

	private boolean areBricksGone() {
		if(bricksNumber == 0) return true;
		return false;
	}

	private boolean isGameOver() {
		return lives < 1;
	}

	private void testColisions(float deltaTime) {
		
		// test collision: pad <-> game bounds
		testCollisionPadWithBounds(level.pad);
				
		// Test collision: ball <-> game bounds
		testCollisionBallWithBounds(level.ball);
		
		// ball rectangle 
		r1.set(level.ball.position.x + level.ball.velocity.x * deltaTime, 
				level.ball.position.y + level.ball.velocity.y * deltaTime, 
				level.ball.bounds.width, level.ball.bounds.height);
		// pad rectangle
		r2.set(level.pad.position.x, level.pad.position.y, level.pad.bounds.width, level.pad.bounds.height);
		
		// collision ball with pad.
		if(r1.overlaps(r2))
			onCollisionBallWithPad(level.ball, level.pad);
		
		for (Brick brick : level.bricks) {
			if (brick.destroyed) continue;
			r2.set(brick.position.x, brick.position.y, brick.bounds.width, brick.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBallWithBrick(level.ball, brick);
			break;
		}
	}

	private void testCollisionBallWithBounds(Ball ball) {
		// right side collision
		if (ball.position.x > Constants.VIEWPORT_WIDTH / 2 - ball.dimension.x ){
			ball.bounceX();
			ball.position.x = Constants.VIEWPORT_WIDTH / 2 - ball.dimension.x;
		}
		// left side collision
		else if (ball.position.x < - Constants.VIEWPORT_WIDTH / 2 ){
			ball.bounceX();
			ball.position.x = - Constants.VIEWPORT_WIDTH / 2;
		}
		
		// top collision
		if (ball.position.y > Constants.VIEWPORT_HEIGHT / 2 - ball.dimension.y ){
			ball.bounceY();
			ball.position.y = Constants.VIEWPORT_HEIGHT / 2 - ball.dimension.y;
		}
		
		// bottom collision
		if(ball.position.y < - Constants.VIEWPORT_HEIGHT / 2){
			level.ball.ballLocked = true;
			level.ball.setPosition(level.pad);
			lives--;	
		}
	}

	private void testCollisionPadWithBounds(Pad pad) {
		if (pad.position.x + pad.dimension.x > Constants.VIEWPORT_WIDTH / 2)
			pad.position.x = Constants.VIEWPORT_WIDTH / 2 - pad.dimension.x;
		else if (pad.position.x < - Constants.VIEWPORT_WIDTH / 2)
			pad.position.x = - Constants.VIEWPORT_WIDTH / 2;
		
	}

	private void onCollisionBallWithPad(Ball ball, Pad pad) {		
	
		// variables for calculating side of collision on brick
		float wy = 0;
		float hx = 0;
		
		// math # Minkowski addition
		wy = (ball.dimension.x + pad.dimension.x) * (ball.centerY() - pad.centerY()); 
		hx = (ball.dimension.y + pad.dimension.y) * (ball.centerX() - pad.centerX()); 
		
		if(wy > hx)
			if(wy > -hx) { // top
				calculateBallAngle(ball, pad);
				ball.position.y = pad.position.y + pad.dimension.y;
			} else  // left
				ball.position.x = pad.position.x - ball.dimension.x;
			
		else
			if (wy > -hx)  // right
				ball.position.x = pad.position.x + pad.dimension.x;
			
		/*
			else // bottom ~ this should not happen.
				ball.bounceY();
		*/
	}

	private void calculateBallAngle(Ball ball, Pad pad) {		
		// variables for calculating ball angle bounce
		float relativeIntersectionX = 0;
		float normalizedRelativeIntersectionX = 0;
		float bounceAngle = 0;
		
		// math #.#		
		// we get a negative or positive number of distance between the centers of pad and ball
		relativeIntersectionX = (pad.position.x + (pad.dimension.x / 2)) - 
				(ball.position.x + (ball.dimension.x / 2));
		
		// we normalize the distance, so it's between, from -1 to 1 (important for proper angles)
		normalizedRelativeIntersectionX = (relativeIntersectionX/(pad.dimension.x / 2));
		// we get a bounce angle in degrees
		bounceAngle = - normalizedRelativeIntersectionX * Constants.MAX_BOUNCE_ANGLE;
		// we convert it to radians, math.sin/cos take in radians and not angle degrees
		bounceAngle = (float) Math.toRadians(bounceAngle);
		
		// we set the x and y to proper velocity for the so 
		// their angle between their vectors in our bounce angle	
		ball.velocity.x = (float) (Constants.MAX_BALL_VELOCITY * Math.sin(bounceAngle));
		ball.velocity.y = (float) (Constants.MAX_BALL_VELOCITY * Math.cos(bounceAngle));
		// math.end()
	}

	private void onCollisionBallWithBrick(Ball ball, Brick brick) {		
		// variables for calculating side of collision on brick
		float wy = 0;
		float hx = 0;
				
		// math # Minkowski addition
		wy = (ball.dimension.x + brick.dimension.x) * (ball.centerY() - brick.centerY()); 
		hx = (ball.dimension.y + brick.dimension.y) * (ball.centerX() - brick.centerX()); 
		
		if(wy > hx)
			if(wy > -hx){
				ball.bounceY(); // top
				ball.position.y = brick.position.y + brick.dimension.y;
			}else{
				ball.bounceX(); // left
				ball.position.x = brick.position.x - ball.dimension.x;
			}
		else
			if (wy > -hx){
				ball.bounceX(); // right
				ball.position.x = brick.position.x + brick.dimension.x;
			}else{
				ball.bounceY(); // bottom
				ball.position.y = brick.position.y - ball.dimension.y;
			}
		
		brick.destroyed = true;
		bricksNumber--;
		
		score += Constants.BRICK_VALUE;	
	}


	private void handleDebugInput(float deltaTime) {
		float camMoveSpeed = 50 * deltaTime;
		
		if (Gdx.input.isKeyPressed(Keys.LEFT )) moveCamera(-camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.RIGHT )) moveCamera(camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.UP )) moveCamera(0, camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
		
		float camZoomSpeed = 1 * deltaTime;
		if (Gdx.input.isKeyPressed(Keys.COMMA )) cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD )) cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH )) cameraHelper.setZoom(1);		
		
	}

	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);		
	}
	
	@Override
	public boolean keyUp (int keycode) {
		
		if (keycode == Keys.P || keycode == Keys.ESCAPE){
			paused = !paused;
			// if game gets paused, then cursor is not catched.
			
			Gdx.input.setCursorCatched(!paused);
			Gdx.app.debug(TAG, "Game paused/unpaused");
		}
		
		// Reset game world
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world reset");
		}
		return false;
	}

	private void handleGameInput(float deltaTime) {

		// pad mouse movement
		if(Gdx.input.getDeltaX() != 0) 
			level.pad.position.x += Gdx.input.getDeltaX() / 160.0f;  // possible sensitivity option.
		
		// ball un-lock
		if(level.ball.ballLocked)
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
				level.ball.ballLocked = false;
	}
}

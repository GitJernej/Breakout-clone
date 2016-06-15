package com.jernej.erman.brickgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input.Keys;
import com.jernej.erman.brickgame.game.objects.Ball;
import com.jernej.erman.brickgame.game.objects.Brick;
import com.jernej.erman.brickgame.game.objects.Pad;
import com.jernej.erman.brickgame.util.CameraHelper;
import com.jernej.erman.brickgame.util.Constants;

public class WorldController extends InputAdapter {

	private static final String TAG = WorldController.class.getName();
	
	boolean paused = false;
	
	
	public Level level;
	public int score;
	public int lives;
	
	public CameraHelper cameraHelper;
	
	// rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	
	// variables for calculating ball angle bounce
	float intersectionX = 0;
	float normalizedIntersectionX = 0;
	float bounceAngle = 0;
	
	// variables for calculating side of collision on brick
	float wy = 0;
	float hx = 0;
	
	
		
	public WorldController(){
		init();
	}
	
	private void init(){

		Gdx.input.setCursorCatched(true);
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.STARTING_LIVES;
		score = 0;
		initLevel();
	}
	
	private void initLevel () {
		level = new Level(Constants.LEVEL);
	}
	

	public void update(float deltaTime){
		if (Constants.DEBUG) handleDebugInput(deltaTime);		
		if (paused) return;
		handleGameInput(deltaTime);	
		level.update(deltaTime);
		testColisions(deltaTime);
		
		if(!isGameOver() && isBallLost())
			ballLost();
		else if (isGameOver() && isBallLost())
			init();
		
	}

	private void ballLost() {
		level.ball.ballLocked = true;
		level.ball.setPosition(level.pad);
		lives--;		
	}

	private boolean isBallLost() {
		return level.ball.position.y < - Constants.VIEWPORT_HEIGHT / 2;
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
		
		// there is no bottom collision, it's a isBallLost condition.
	}

	private void testCollisionPadWithBounds(Pad pad) {
		if (pad.position.x + pad.dimension.x > Constants.VIEWPORT_WIDTH / 2)
			pad.position.x = Constants.VIEWPORT_WIDTH / 2 - pad.dimension.x;
		else if (pad.position.x < - Constants.VIEWPORT_WIDTH / 2)
			pad.position.x = - Constants.VIEWPORT_WIDTH / 2;
		
	}

	private void onCollisionBallWithPad(Ball ball, Pad pad) {		
	
		
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
		
		intersectionX = (pad.position.x + (pad.dimension.x / 2)) - 
				(ball.position.x + (ball.dimension.x / 2));
		
		normalizedIntersectionX = intersectionX;
		bounceAngle = - normalizedIntersectionX * Constants.MAX_BOUNCE_ANGLE;
		bounceAngle = (float) Math.toRadians(bounceAngle);
			
		ball.velocity.x = (float) (Constants.MAX_BALL_VELOCITY * Math.sin(bounceAngle));
		ball.velocity.y = (float) (Constants.MAX_BALL_VELOCITY * Math.cos(bounceAngle));
		
	}

	private void onCollisionBallWithBrick(Ball ball, Brick brick) {		
		
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
		score++;
		Gdx.app.log(TAG, "brick destroyed");		
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
			level.pad.position.x += Gdx.input.getDeltaX() / 160.0f;
		
		// ball un-lock
		if(level.ball.ballLocked)
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
				level.ball.ballLocked = false;
	}
}

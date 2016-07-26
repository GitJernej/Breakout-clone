package com.jernej.erman.brickgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Input.Keys;
import com.jernej.erman.brickgame.screens.DirectedGame;
import com.jernej.erman.brickgame.screens.GameScreen;
import com.jernej.erman.brickgame.screens.MenuScreen;
import com.jernej.erman.brickgame.screens.transitions.ScreenTransition;
import com.jernej.erman.brickgame.screens.transitions.ScreenTransitionSlide;
import com.jernej.erman.brickgame.game.objects.Ball;
import com.jernej.erman.brickgame.game.objects.Brick;
import com.jernej.erman.brickgame.game.objects.Pad;
import com.jernej.erman.brickgame.game.objects.PowerUp;
import com.jernej.erman.brickgame.util.AudioManager;
import com.jernej.erman.brickgame.util.CameraHelper;
import com.jernej.erman.brickgame.util.Constants;
import com.jernej.erman.brickgame.util.GamePreferences;

public class WorldController extends InputAdapter {

	private static final String TAG = WorldController.class.getName();	
	
	boolean paused = false;
	
	boolean hasGameEnded;
		
	public Level level;
	public int score;
	public int lives;
		
	// number of blocks
	public int bricksNumber;
	
	public Array<Brick> colidedBricks;
	
	public CameraHelper cameraHelper;
	
	// rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
		
	// delays
	private float timeLeftGameOverDelay;
	
	public float scoreVisual;
	
	private DirectedGame game;
	
	public void backToMenu() {
		// switch to menu screen
		ScreenTransition transition = ScreenTransitionSlide.init(1.0f, 
				ScreenTransitionSlide.DOWN, false, Interpolation.fade);
		game.setScreen(new MenuScreen(game), transition);
	}	
		
	public WorldController(DirectedGame game){
		this.game = game;
		init();
	}
	
	private void init(){

		Gdx.input.setCursorCatched(true);
		cameraHelper = new CameraHelper();
		lives = Constants.STARTING_LIVES;
		colidedBricks = new Array<Brick>();
		timeLeftGameOverDelay = 0;
		hasGameEnded = false;
		// create level first...
		initLevel();
		// then get the number of blocks.
		
	}
	
	private void initLevel () {
		score = 0;
		scoreVisual = score;
		level = new Level("levels/" + GamePreferences.instance.gameLevel);
		bricksNumber = level.bricks.size;
		Gdx.app.debug(TAG, "number of bricks in level: " + bricksNumber);	

	}
	

	public void update(float deltaTime){
		if (Constants.DEBUG) handleDebugInput(deltaTime);	
				
		if((areBricksGone() || isGameOver() ) && !hasGameEnded){
			score += (Constants.LIVES_VALUE * lives);
			hasGameEnded = true;
			//paused = true;
			timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
		}
		
		if(hasGameEnded && timeLeftGameOverDelay < 0){
			Gdx.input.setCursorCatched(false);
			backToMenu();
		} else {
			timeLeftGameOverDelay -= deltaTime;
			if(hasGameEnded){
				level.ball.velocity.x *= 0.93f;
				level.ball.velocity.y *= 0.93f;
			}
		}
		
		//if (paused) return;	
		
		handleGameInput(deltaTime);	

		level.update(deltaTime);
		testColisions(deltaTime);
		
		if (scoreVisual < score)
			scoreVisual = Math.min(score, scoreVisual + Constants.BRICK_VALUE * deltaTime * 10);
		
	}

	private boolean areBricksGone() {
		if(bricksNumber == 0) return true;
		return false;
	}

	public boolean isGameOver() {
		return lives < 1;
	}

	private void testColisions(float deltaTime) {
				
		// test collision: pad <-> game bounds
		testCollisionPadWithBounds(level.pad);
				
		// Test collision: ball <-> game bounds
		testCollisionBallWithBounds(level.ball);
		
		
		// pad rectangle
		r2.set(level.pad.position.x, level.pad.position.y, level.pad.bounds.width, level.pad.bounds.height);


		// collision powerUp with pad
		for(PowerUp powerUp : level.powerUps){
			if(powerUp.activated) continue;
			r1.set(powerUp.position.x, powerUp.position.y, powerUp.dimension.x, powerUp.dimension.y);
			if(r1.overlaps(r2)) activatePowerUp(powerUp);
			if(powerUp.position.y < - Constants.VIEWPORT_HEIGHT / 2) removePowerUp(powerUp);
		}
		// ball rectangle 
		r1.set(level.ball.position.x + level.ball.velocity.x * (deltaTime/2), 
				level.ball.position.y + level.ball.velocity.y * (deltaTime/2), 
				level.ball.bounds.width, level.ball.bounds.height);
		
		// collision ball with pad.
		if(r1.overlaps(r2))
			calculateBallAngle(level.ball, level.pad);
		
		
		// collision ball with bricks
		for (Brick brick : level.bricks) {
			if (brick.isDestroyed()) continue;
			r2.set(brick.position.x, brick.position.y, brick.bounds.width, brick.bounds.height);
			if (!r1.overlaps(r2)) continue;
			spawnPowerUp(brick);
			colidedBricks.add(brick);
			AudioManager.instance.play(Assets.instance.sounds.brickDestroyed);
			brick.destroyed();
			bricksNumber--;		
			score += Constants.BRICK_VALUE;	
		}		
		if(colidedBricks.size > 0) handleBallBrickCollisions(level.ball);
	}

	private void activatePowerUp(PowerUp powerUp) {

		Gdx.app.debug(TAG, "number of active power ups" + level.powerUps.size);
		switch(powerUp.type){
		case SHORT_PAD:
			level.pad.resizePad(-0.2f);
			break;
		case LONG_PAD:
			level.pad.resizePad(0.2f);
			break;
		case EXTRA_LIFE:
			lives ++;
			break;
		case SPEED_UP:
			level.ball.currentVelocity *= 1.25f;
			break;
		case SPEED_DOWN:
			level.ball.currentVelocity *= 0.75f;
			break;
		}
		powerUp.activated = true;
		removePowerUp(powerUp);
		
		AudioManager.instance.play(Assets.instance.sounds.powerUp);		
	}

	private void removePowerUp(PowerUp powerUp) {	
		level.powerUps.removeIndex(level.powerUps.indexOf(powerUp, true));
		//Gdx.app.debug(TAG, "number of active power ups" + level.powerUps.size);
	}

	private void spawnPowerUp(Brick brick) {
		// chance for power up spawn 0.00 to 1.00
		if(Math.random() < 0.15){
			level.powerUps.add(new PowerUp(brick));
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
		else if (ball.position.y > Constants.VIEWPORT_HEIGHT / 2 - ball.dimension.y ){
			ball.bounceY();
			ball.position.y = Constants.VIEWPORT_HEIGHT / 2 - ball.dimension.y;
		}
		
		// bottom collision
		else if(ball.position.y < - Constants.VIEWPORT_HEIGHT / 2){

			AudioManager.instance.play(Assets.instance.sounds.lostLife);
			
			level.ball.ballLocked = true;
			level.ball.currentVelocity = Constants.MAX_BALL_VELOCITY * GamePreferences.instance.ballSpeed;
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


	private void calculateBallAngle(Ball ball, Pad pad) {

		if(!ball.ballLocked)
			AudioManager.instance.play(Assets.instance.sounds.bounce);
		
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
		
		// we set the velocity x and y to their proper values so the
		// angle is what we want and the speed is consistent
		ball.velocity.x = (float) (ball.currentVelocity * Math.sin(bounceAngle));
		ball.velocity.y = (float) (ball.currentVelocity * Math.cos(bounceAngle));
		// math.end()
	}

	private void handleBallBrickCollisions(Ball ball) {		
		
		switch(colidedBricks.size){
		case 1:
			onBallOneBrick(ball, colidedBricks.get(0));
			break;
		case 2: 
			onBallTwoBricks(ball, colidedBricks.get(0), colidedBricks.get(1));
			break;
		case 3: // on collision with three bricks, only happens when ball hits a corner
				// and should in all cases bounce back in the same direction in came from
			ball.bounceX();
			ball.bounceY();
			break;
		default:
			Gdx.app.debug(TAG, "Unknown collision with " + colidedBricks.size + "bricks");
		}		
		colidedBricks.clear();
	}

	private void onBallTwoBricks(Ball ball, Brick brick1, Brick brick2) {
		
		if(brick1.centerX() == brick2.centerX())
			ball.bounceX();
		else
			ball.bounceY();
		
		
	}

	private void onBallOneBrick(Ball ball, Brick brick) {
		
		float wy = 0.5f * (ball.dimension.x + brick.dimension.x) * (ball.centerY() - brick.centerY());
		float hx = 0.5f * (ball.dimension.y + brick.dimension.y) * (ball.centerX() - brick.centerX());
		
		if(wy > hx)
			if(wy > -hx){
				ball.bounceY();
				}
			else{
				ball.bounceX();
				}
		else{
			if(wy > -hx){
				ball.bounceX();
				}
			else{
				ball.bounceY();
				}
		}	
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
		
		if (keycode == Keys.P && paused){
			paused = false;
			game.resume();
			Gdx.input.setCursorCatched(!paused);
			Gdx.app.debug(TAG, "Unpaused");
		}else if( keycode == Keys.ESCAPE && paused){
			paused = false;
			backToMenu();
			Gdx.app.debug(TAG, "Returning to menu");
		}		
		else if((keycode == Keys.P || keycode == Keys.ESCAPE) && !paused){
			paused = true;
			game.pause();
			Gdx.input.setCursorCatched(!paused);
			Gdx.app.debug(TAG, "Paused");
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

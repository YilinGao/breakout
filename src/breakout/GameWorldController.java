package breakout;

import java.util.ArrayList;

import collisions.Collision;
import javafx.scene.input.KeyCode;
import pages.LevelPage;
import spirits.Ball;
import spirits.Brick;
import spirits.Paddle;
import spirits.Powerup;

/**
 * Controlling actions in each frame of game loop. 
 * Depends on breakout.GameWorld, spirits.Ball, spirits.Paddle, spirits.Brick, spirtis.powerup.
 * @author Yilin Gao
 *
 */
public class GameWorldController {
	private GameWorld gameWorld;	
	private Collision collisionDetector;
	private ArrayList<Ball> balls;
	private ArrayList<Powerup> powerups;
	private Paddle paddle;
	private ArrayList<Brick> bricks;
	private long numOfStickyFrames = 0;
	
	/**
	 * Constructor of the GameWorldController class.
	 * @param theGameWorld: a GameWorld variable
	 */
	public GameWorldController(GameWorld theGameWorld){
		gameWorld = theGameWorld;
		collisionDetector = new Collision(gameWorld);
		balls = ((LevelPage) gameWorld.getPage(1)).getBalls();
		powerups = ((LevelPage) gameWorld.getPage(1)).getPowerups();
		paddle = ((LevelPage) gameWorld.getPage(1)).getPaddle();
		bricks = ((LevelPage) gameWorld.getPage(1)).getBricks();
	}

	/**
	 * Animations of nodes on the scene during one frame, check level end and move to the next level.
	 * @param elapsedTime: the duration of each frame
	 */
	void actionsPerFrame(double elapsedTime){		
		if (((LevelPage) gameWorld.getPage(1)).getStarted() & !((LevelPage) gameWorld.getPage(1)).getPaused()){
			for (Ball ball: balls){
				if (!ball.getStuck()){
					ball.ballMove(elapsedTime);		
				}
				else{
					numOfStickyFrames++;
					if (numOfStickyFrames == 3 * GameWorld.framesPerSecond){   // the paddle sticks the ball for 3 seconds
						ball.ballMove(elapsedTime);	
						ball.setStuck(false);
						numOfStickyFrames = 0;
					}
				}
			}
			for (Powerup powerup: powerups)
				powerup.powerupMove(elapsedTime);
			collisionDetector.ballBounceOnPaddle(balls, paddle);
			collisionDetector.ballBounceOnWalls(balls);
			collisionDetector.ballHitAllBricks(balls, bricks);
			collisionDetector.ballFallDown(balls);	
			collisionDetector.powerupHitPaddle(powerups, paddle);
			lifeEnd();
			levelEnd();
			if (gameWorld.getLevel() == 3){
				for (Brick brick: bricks){
					brick.brickMove(elapsedTime);
				}
			}
		}		
		gameWorld.getPage(1).getScene().setOnKeyPressed(e -> handleKeyInputsFrame(e.getCode(), elapsedTime));
	}

	/**
	 * Handle key events in each frame.
	 * @param code: KeyEvent's KeyCode
	 * @param elapsedTime: the duration of each frame
	 */
	private void handleKeyInputsFrame(KeyCode code, double elapsedTime){
		int direction = 0;
		if (code == KeyCode.LEFT){
			direction = -1;
		}
		else if (code == KeyCode.RIGHT){
			direction = 1;
		}			
		
		if (!((LevelPage) gameWorld.getPage(1)).getPaused()) {
			paddle.paddleMove(direction, elapsedTime);
			for (Ball ball : balls)
				if (!((LevelPage) gameWorld.getPage(1)).getStarted() || (((LevelPage) gameWorld.getPage(1)).getStarted() && ball.getStuck()))
					ball.ballMoveWithPaddle(paddle);
		}
	}

	/**
	 * Call isLifeEnd() to test if during this frame, the current life is dead.
	 * If so, call actionsLifeEnd() to do relevant actions.
	 */
	private void lifeEnd() {
		if (isLifeEnd()){
			actionsLifeEnd();
		}
	}

	/**
	 * Test if the current life is dead.
	 * @return true or false
	 */
	private boolean isLifeEnd() {
		return (0 == balls.size());
	}
	
	/**
	 * Actions to perform when the current life is dead.
	 */
	private void actionsLifeEnd(){
		gameWorld.setLives(gameWorld.getLives() - 1);
		gameWorld.setScore(0);
		if (!isDead()){
			((LevelPage) gameWorld.getPage(1)).setupFixedItems();
			((LevelPage) gameWorld.getPage(1)).setupMovableItems();
			((LevelPage) gameWorld.getPage(1)).addNodesToRoot();
		}
		else{
			gameWorld.stopGameLoop();
			gameWorld.initializeResultPage();
		}
	}

	/**
	 * Call isLevelEnd() to test if during this frame, the current level has finished.
	 * If so, call actionsLevelEnd() to do relevant actions.
	 */
	private void levelEnd() {
		if (isLevelEnd()){
			actionsLevelEnd();
		}
	}
	
	/**
	 * Test if the current level has finished.
	 * @return true or false
	 */
	private boolean isLevelEnd(){
		return (0 == bricks.size());
	}
	
	/**
	 * Actions to perform when the current level has finished.
	 */
	private void actionsLevelEnd() {
		if (gameWorld.getLevel() < 3){
			gameWorld.stopGameLoop();
			gameWorld.initializeLevel(gameWorld.getLevel() + 1);
		}
		else{
			gameWorld.stopGameLoop();
			gameWorld.initializeResultPage();
		}
	}

	/**
	 * Test if the player is dead.
	 * @return true or false
	 */
	public boolean isDead(){
		return (gameWorld.getLives()== 0);
	}
}

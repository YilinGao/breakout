package collisions;

import java.util.ArrayList;
import java.util.Random;

import breakout.GameWorld;
import spirits.Ball;
import spirits.Brick;
import spirits.Paddle;
import spirits.Powerup;

/**
 * dealing with all possible kinds of collisions between items,
 * depends on breakout.Breakout, spirits.Ball, spirits.Brick, spirits.Paddle, spirits.Powerup
 * @author Yilin Gao
 *
 */
public class Collision {
	
	ArrayList<Ball> ballsToBeAdded = new ArrayList<>();
	private ArrayList<Ball> ballsToBeRemoved = new ArrayList<Ball>();
	ArrayList<Brick> bricksToBeRemoved = new ArrayList<>();
	ArrayList<Powerup> powerupsToBeRemoved = new ArrayList<>();
	GameWorld gameWorld;
	
	/**
	 * constructor of Collision class
	 * @param originalGameWorld: the GameWorld instant
	 */
	public Collision(GameWorld originalGameWorld){
		gameWorld = originalGameWorld;
	}
	
	/**
	 * test if any ball hits the paddle,
	 * actions to do when a ball hits the paddle
	 * @param balls: all balls in the game
	 * @param paddle: the paddle in the game
	 */
	public void ballBounceOnPaddle(ArrayList<Ball> balls, Paddle paddle){
		for (Ball ball: balls){
			int ballDirectionHorizontal = ball.getDirectionHorizontal();
			int ballDirectionVertical = ball.getDirectionVertical();
			double ballMinX = ball.getMinX();
			double ballMaxX = ball.getMaxX();
			double ballMinY = ball.getMinY();
			double ballMaxY = ball.getMaxY();
			double paddleMinX = paddle.getMinX();
			double paddleMaxX = paddle.getMaxX();
			double paddleMinY = paddle.getMinY();
			double paddleMaxY = paddle.getMaxY();
			
			// check for hit on the upper edge
			if (ballDirectionVertical > 0 && ballMaxY >= paddleMinY && ballMinY <= paddleMinY && ((ballMaxX >= paddleMinX) && (ballMaxX <= paddleMaxX) || (ballMinX >= paddleMinX && ballMinX <= paddleMaxX))){
				ballBounceOnPaddleEffect(paddle, ball, 1);
			}
			// check for hit on the left edge
			if (ballDirectionHorizontal > 0 && ballMaxX >= paddleMinX && ballMinX <= paddleMinX && ((ballMaxY >= paddleMinY && ballMaxY <= paddleMaxY) || (ballMinY >= paddleMinY && ballMinY <= paddleMaxY))){
				ballBounceOnPaddleEffect(paddle, ball, -1);
			}
			// check for hit on the left edge
			if (ballDirectionHorizontal < 0 && ballMinX <= paddleMaxX && ballMaxX >= paddleMaxX && ((ballMaxY >= paddleMinY && ballMaxY <= paddleMaxY) || (ballMinY >= paddleMinY && ballMinY <= paddleMaxY))){
				ballBounceOnPaddleEffect(paddle, ball, -1);
			}
		}
	}

	/**
	 * the actual actions when the ball bounces on the paddle
	 * @param paddle
	 * @param ball
	 * @param indicator: to indicate to bounce horizontally or vertically
	 */
	private void ballBounceOnPaddleEffect(Paddle paddle, Ball ball, int indicator) {
		if (!paddle.getSticky()){
			if (1 == indicator)
				ball.ballBounceVertical();
			else if (-1 == indicator)
				ball.ballBounceHorizontal();
		}
		else{
			ball.setSticked(true);
			paddle.setSticky(false);
		}
	}
	
	/**
	 * test if any ball hits walls,
	 * actions to do when a ball hits walls
	 * @param balls: all balls in the game
	 */
	public void ballBounceOnWalls(ArrayList<Ball> balls){
		for (Ball ball: balls){
			double ballMinX = ball.getMinX();
			double ballMaxX = ball.getMaxX();
			double ballMinY = ball.getMinY();
			double ballMaxY = ball.getMaxY();
			if ((ballMinX < 0 && ballMaxX > 0) || (ballMaxX > GameWorld.WIDTH && ballMinX < GameWorld.WIDTH))
				ball.ballBounceHorizontal();
			else if (ballMinY < 0 && ballMaxY > 0)
				ball.ballBounceVertical();
		}
	}
	
	/**
	 * test if any ball falls down,
	 * actions to do when a ball falls down
	 * @param balls: all balls in the game
	 */
	public void ballFallDown(ArrayList<Ball> balls){
		for (Ball ball: balls){
			double ballMinY = ball.getBall().getBoundsInParent().getMinY();
			if (ballMinY >= GameWorld.HEIGHT){
				ball.setRemovalMark();
				gameWorld.getRootLevel().getChildren().remove(ball.getBall());
			}
		}
		for (Ball ball: balls){
			if (ball.getRemovalMark())
				ballsToBeRemoved.add(ball);
		}
		balls.removeAll(ballsToBeRemoved);
	}	

	/**
	 * test if any ball hits any brick,
	 * remove bricks hit
	 * @param balls: all balls in the game
	 * @param bricks: all bricks in the game
	 */
	public void ballHitAllBricks(ArrayList<Ball> balls, ArrayList<Brick> bricks) {
		bricksToBeRemoved.clear();
		for (Brick brick: bricks){
			for (Ball ball: balls){
				ballHitBrick(ball, brick);
			}
		}
		for (Brick brick: bricks){
			if (brick.getRemovalMark()){
				bricksToBeRemoved.add(brick);
				gameWorld.getRootLevel().getChildren().remove(brick.getBrick());
			}
		}
		bricks.removeAll(bricksToBeRemoved);
	}	
	
	/**
	 * test if one ball hits one brick,
	 * actions to do when the ball hits the brick
	 * @param ball: the tested ball
	 * @param brick: the tested brick
	 */
	private void ballHitBrick(Ball ball, Brick brick){
		boolean hit = false;
		int ballDirectionHorizontal = ball.getDirectionHorizontal();
		int ballDirectionVertical = ball.getDirectionVertical();
		double ballMinX = ball.getMinX();
		double ballMaxX = ball.getMaxX();
		double ballMinY = ball.getMinY();
		double ballMaxY = ball.getMaxY();
		double brickMinX = brick.getMinX();
		double brickMaxX = brick.getMaxX();
		double brickMinY = brick.getMinY();
		double brickMaxY = brick.getMaxY();
		
		// check for hit on the upper edge
		if (ballDirectionVertical > 0 && ballMaxY >= brickMinY && ballMinY <= brickMinY && ((ballMaxX >= brickMinX) && (ballMaxX <= brickMaxX) || (ballMinX >= brickMinX && ballMinX <= brickMaxX))){
			ball.ballBounceVertical();
			hit = firstHit(brick, hit);
		}
		// check for hit on the lower edge
		if (ballDirectionVertical < 0 && ballMinY <= brickMaxY && ballMaxY >= brickMaxY && ((ballMaxX >= brickMinX) && (ballMaxX <= brickMaxX) || (ballMinX >= brickMinX && ballMinX <= brickMaxX))){
			ball.ballBounceVertical();
			hit = firstHit(brick, hit);
		}
		// check for hit on the left edge
		if (ballDirectionHorizontal > 0 && ballMaxX >= brickMinX && ballMinX <= brickMinX && ((ballMaxY >= brickMinY && ballMaxY <= brickMaxY) || (ballMinY >= brickMinY && ballMinY <= brickMaxY))){
			ball.ballBounceHorizontal();
			hit = firstHit(brick, hit);
		}
		// check for hit on the left edge
		if (ballDirectionHorizontal < 0 && ballMinX <= brickMaxX && ballMaxX >= brickMaxX && ((ballMaxY >= brickMinY && ballMaxY <= brickMaxY) || (ballMinY >= brickMinY && ballMinY <= brickMaxY))){
			ball.ballBounceHorizontal();
			hit = firstHit(brick, hit);
		}
	}

	/**
	 * check if this is the first hit between the ball and the brick in this frame
	 * @param brick
	 * @param hit
	 * @return
	 */
	private boolean firstHit(Brick brick, boolean hit) {
		if (!hit){
			hit = true;
			int type = brick.hitBrick();
			increaseScore(type);
			dropPowerup(brick);
		}
		return hit;
	}
	
	/**
	 * increase score when a brick is hit
	 * @param type: type of the hit brick
	 */
	private void increaseScore(int type){
		switch (type){
		case 1:
			gameWorld.setScore(gameWorld.getScore() + 100);
			break;
		case 2:
			gameWorld.setScore(gameWorld.getScore() + 200);
			break;
		case 3:
			gameWorld.setScore(gameWorld.getScore() + 300);
			break;
		}
		gameWorld.setCurrentScore("Current Score: "+ gameWorld.getScore());
	}
	
	/**
	 * determine if a powerup will drop up when a brick is hit
	 * @param brick: the hit brick
	 */
	private void dropPowerup(Brick brick){
		Random rn = new Random();
		double indicator = rn.nextDouble();
		if (indicator <= 0.4){
			Powerup newPowerup = new Powerup(indicator, brick);
			gameWorld.getPowerups().add(newPowerup);
			gameWorld.getRootLevel().getChildren().add(newPowerup.getPowerup());
		}
	}
	
	/**
	 * test if any powerup hits the paddle,
	 * actions to do when a powerup hits the paddle
	 * @param powerups: all powerups in the game
	 * @param paddle: the paddle in the game
	 */
	public void powerupHitPaddle(ArrayList<Powerup> powerups, Paddle paddle) {
		for (Powerup powerup: powerups){
			double powerupMinX = powerup.getMinX();
			double powerupMaxX = powerup.getMaxX();
			double powerupMinY = powerup.getMinY();
			double powerupMaxY = powerup.getMaxY();
			double paddleMinX = paddle.getMinX();
			double paddleMaxX = paddle.getMaxX();
			double paddleMinY = paddle.getMinY();
			// only check for collisions on the upper surface
			if (powerupMaxY >= paddleMinY && powerupMinY <= paddleMinY){
				if ((powerupMaxX >= paddleMinX && powerupMaxX <= paddleMaxX) || (powerupMinX >= paddleMinX && powerupMinX <= paddleMaxX)){
					powerupEffect(powerup);
				}
			}
			else if (powerupMinY >= GameWorld.HEIGHT){
				powerup.setRemovalMark();
			}
		}
		for (Powerup powerup: powerups){
			if (powerup.getRemovalMark()){
				gameWorld.getRootLevel().getChildren().remove(powerup.getPowerup());
				powerupsToBeRemoved.add(powerup);
			}
		}
		powerups.removeAll(powerupsToBeRemoved);
	}
	
	/**
	 * the effect when a powerup hits the paddle
	 * @param powerup: the powerup
	 */
	private void powerupEffect(Powerup powerup) {
		powerup.setRemovalMark();
		switch (powerup.getType()){
		case 1:
			speedUpBall(1.2);
			break;
		case 2:
			splitBall(3);
			break;
		case 3:
			stickyPaddle(3);
			break;
		case 4:
			increaseLife(1);
			break;
		}
	}

	/**
	 * powerup effect 4: increase 1 life
	 * @param i: the increased number of lives
	 */
	private void increaseLife(int i) {
		gameWorld.setLives(gameWorld.getLives() + i);
		gameWorld.setRemainingLives("Remaining lives: " + gameWorld.getLives());
	}

	/**
	 * powerup effect 3: make the paddle sticky
	 * @param i: the duration of sticky effect (in second)
	 */
	private void stickyPaddle(int i) {
		gameWorld.getPaddle().setSticky(true);
	}

	/**
	 * powerup effect 2: split each ball into several balls
	 * @param number: the number of balls out of each ball
	 */
	private void splitBall(int number) {
		ArrayList<Ball> balls = gameWorld.getBalls();
		for (Ball ball: balls){
			for (int i = 1; i < number; i++){
				Ball newBall = new Ball();
				newBall.setX(ball.getMinX());
				newBall.setY(ball.getMinY());
				ballsToBeAdded.add(newBall);
				gameWorld.getRootLevel().getChildren().add(newBall.getBall());
			}
		}
		balls.addAll(ballsToBeAdded);
	}

	/**
	 * powerup effect 1: speed the ball up by certain times
	 * @param times: the times of ball speeding up
	 */
	private void speedUpBall(double times) {
		ArrayList<Ball> balls = gameWorld.getBalls();
		for (Ball ball: balls){
			ball.setSpeed(times);
		}
	}
}

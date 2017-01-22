package collisions;

import java.util.ArrayList;
import java.util.Random;

import breakout.GameWorld;
import spirits.Ball;
import spirits.Brick;
import spirits.Paddle;
import spirits.Powerup;

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
	
	public void ballBounceOnPaddle(ArrayList<Ball> balls, Paddle paddle){
		for (Ball ball: balls){
			double ballCenterX = ball.getBall().getBoundsInParent().getMinX() + ball.getBall().getBoundsInParent().getWidth() / 2;
			double paddleMinX = paddle.getPaddle().getBoundsInParent().getMinX();
			double paddleMaxX = paddle.getPaddle().getBoundsInParent().getMaxX();
			double ballMinY = ball.getBall().getBoundsInParent().getMinY();
			double ballMaxY = ball.getBall().getBoundsInParent().getMaxY();
			double paddleMinY = paddle.getPaddle().getBoundsInParent().getMinY();
			double paddleOneThirdsX = paddle.getPaddle().getBoundsInParent().getMinX() + paddle.getPaddle().getBoundsInParent().getWidth() / 3;
			double paddleTwoThirdsX = paddle.getPaddle().getBoundsInParent().getMinX() + paddle.getPaddle().getBoundsInParent().getWidth() * 2 / 3;
			if (ballMaxY >= paddleMinY && ballMinY <= paddleMinY){
				if ((ballCenterX >= paddleMinX && ballCenterX <= paddleOneThirdsX) || (ballCenterX <= paddleMaxX && ballCenterX >= paddleTwoThirdsX)){				
					if (!paddle.getSticky()){
						ball.ballBounceVertical();
						ball.ballBounceHorizontal();
					}
					else{
						ball.setSticked(true);
						paddle.setSticky(false);
					}
				}
				else if (ballCenterX > paddleOneThirdsX && ballCenterX < paddleTwoThirdsX){
					if (!paddle.getSticky()){
						ball.ballBounceVertical();
					}
					else{
						ball.setSticked(true);
						paddle.setSticky(false);
					}
				}
			}
		}
	}
	
	public void ballBounceOnWalls(ArrayList<Ball> balls){
		for (Ball ball: balls){
			double ballMinX = ball.getBall().getBoundsInParent().getMinX();
			double ballMaxX = ball.getBall().getBoundsInParent().getMaxX();
			double ballMinY = ball.getBall().getBoundsInParent().getMinY();
			double ballMaxY = ball.getBall().getBoundsInParent().getMaxY();
			if ((ballMinX < 0 && ballMaxX > 0) || (ballMaxX > GameWorld.WIDTH && ballMinX < GameWorld.WIDTH))
				ball.ballBounceHorizontal();
			else if (ballMinY < 0 && ballMaxY > 0)
				ball.ballBounceVertical();
		}
	}

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

	public void ballHitOnAllBricks(ArrayList<Ball> balls, ArrayList<Brick> bricks) {
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
	
	private void ballHitBrick(Ball ball, Brick brick){
		int ballDirectionHorizontal = ball.getDirectionHorizontal();
		int ballDirectionVertical = ball.getDirectionVertical();
		double ballMinX = ball.getBall().getBoundsInParent().getMinX();
		double ballMaxX = ball.getBall().getBoundsInParent().getMaxX();
		double ballMinY = ball.getBall().getBoundsInParent().getMinY();
		double ballMaxY = ball.getBall().getBoundsInParent().getMaxY();
		double brickMinX = brick.getBrick().getBoundsInParent().getMinX();
		double brickMaxX = brick.getBrick().getBoundsInParent().getMaxX();
		double brickMinY = brick.getBrick().getBoundsInParent().getMinY();
		double brickMaxY = brick.getBrick().getBoundsInParent().getMaxY();
		
		// check for hit on the upper edge
		if (ballDirectionVertical > 0 && ballMaxY >= brickMinY && ballMinY <= brickMaxY && ballMaxX <= brickMaxX && ballMinX >= brickMinX){
			int type = brick.hitBrick();
			increaseScore(type);
			ball.ballBounceVertical();
			dropPowerup(brick);
		}
		// check for hit on the lower edge
		else if (ballDirectionVertical < 0 && ballMinY <= brickMaxY && ballMaxY >= brickMaxY && ballMaxX <= brickMaxX && ballMinX >= brickMinX){
			int type = brick.hitBrick();
			increaseScore(type);
			ball.ballBounceVertical();
			dropPowerup(brick);
		}
		// check for hit on the left edge
		else if (ballDirectionHorizontal > 0 && ballMaxX >= brickMinX && ballMinX <= brickMinX && ballMaxY <= brickMaxY && ballMinY >= brickMinY){
			int type = brick.hitBrick();
			increaseScore(type);
			ball.ballBounceHorizontal();
			dropPowerup(brick);
		}
		// check for hit on the left edge
		else if (ballDirectionHorizontal < 0 && ballMinX <= brickMaxX && ballMaxX >= brickMaxX && ballMaxY <= brickMaxY && ballMinY >= brickMinY){
			int type = brick.hitBrick();
			increaseScore(type);
			ball.ballBounceHorizontal();
			dropPowerup(brick);
		}
	}
	
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
	
	private void dropPowerup(Brick brick){
		Random rn = new Random();
		double indicator = rn.nextDouble();
		if (indicator <= 0.4){
			Powerup newPowerup = new Powerup(indicator, brick);
			gameWorld.getPowerups().add(newPowerup);
			gameWorld.getRootLevel().getChildren().add(newPowerup.getPowerup());
		}
	}
	
	public void powerupHitPaddle(ArrayList<Powerup> powerups, Paddle paddle) {
		for (Powerup powerup: powerups){
			double powerupCenterX = powerup.getPowerup().getBoundsInParent().getMinX() + powerup.getPowerup().getBoundsInParent().getWidth() / 2;
			double powerupMinY = powerup.getPowerup().getBoundsInParent().getMinY();
			double powerupMaxY = powerup.getPowerup().getBoundsInParent().getMaxY();
			double paddleMinX = paddle.getPaddle().getBoundsInParent().getMinX();
			double paddleMaxX = paddle.getPaddle().getBoundsInParent().getMaxX();
			double paddleMinY = paddle.getPaddle().getBoundsInParent().getMinY();
			if (powerupMaxY >= paddleMinY && powerupMinY <= paddleMinY){
				if (powerupCenterX >= paddleMinX && powerupCenterX <= paddleMaxX){
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

	private void increaseLife(int i) {
		gameWorld.setLives(gameWorld.getLives() + 1);
		gameWorld.setRemainingLives("Remaining lives: " + gameWorld.getLives());
	}

	private void stickyPaddle(int i) {
		gameWorld.getPaddle().setSticky(true);
	}

	private void splitBall(int number) {
		ArrayList<Ball> balls = gameWorld.getBalls();
		for (Ball ball: balls){
			for (int i = 1; i < number; i++){
				Ball newBall = new Ball();
				newBall.setX(ball.getX());
				newBall.setY(ball.getY());
				ballsToBeAdded.add(newBall);
				gameWorld.getRootLevel().getChildren().add(newBall.getBall());
			}
		}
		balls.addAll(ballsToBeAdded);
	}

	private void speedUpBall(double times) {
		ArrayList<Ball> balls = gameWorld.getBalls();
		for (Ball ball: balls){
			ball.setSpeed(times);
		}
	}
}

package spirits;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * the class to implement balls in the game,
 * a wrapper of the ImageView class,
 * depends on spirits.Paddle
 * @author ygao
 *
 */
public class Ball {

	private static final String BALL_IMAGE = "ball.gif";
	private int ballSpeedHorizontal;
	private int ballSpeedVertical;
	private boolean toBeRemoved = false;
	private boolean sticked = false;
	private ImageView ball;
	
	/**
	 * the constructor of the class Ball
	 */
	public Ball(){
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		ball = new ImageView(image);
		Random rn = new Random();
		ballSpeedHorizontal = rn.nextInt(50) + 100;
		ballSpeedHorizontal *= ((rn.nextInt(2) == 0) ? -1 : 1);
		ballSpeedVertical = - rn.nextInt(50) - 100;
	}
	
	public ImageView getBall(){
		return ball;
	}
	
	public double getMinX(){
		return ball.getBoundsInParent().getMinX();
	}
	
	public double getMaxX(){
		return ball.getBoundsInParent().getMaxX();
	}
	
	public double getMinY(){
		return ball.getBoundsInParent().getMinY();
	}
	
	public double getMaxY(){
		return ball.getBoundsInParent().getMaxY();
	}
	
	public double getWidth(){
		return ball.getBoundsInParent().getWidth();
	}
	
	public double getHeight(){
		return ball.getBoundsInParent().getHeight();
	}
	
	public int getDirectionHorizontal(){
		return ((ballSpeedHorizontal > 0) ? 1: -1);
	}
	
	public int getDirectionVertical(){
		return ((ballSpeedVertical > 0) ? 1: -1);
	}
	
	public boolean getRemovalMark(){
		return toBeRemoved;
	}
	
	public boolean getSticked(){
		return sticked;
	}
	
	public void setX(double x){
		ball.setX(x);
	}
	
	public void setY(double y){
		ball.setY(y);
	}
	
	public void setRemovalMark(){
		toBeRemoved = true;
	}
	
	public void setSticked(boolean status){
		sticked = status;
	}
	
	public void setSpeed(double times){
		ballSpeedHorizontal *= times;
		ballSpeedHorizontal *= times;
	}

	/**
	 * the ball moves with the paddle
	 * @param paddle
	 */
	public void ballMoveWithPaddle(Paddle paddle){
		double paddleCenterX = (paddle.getMinX() + paddle.getMaxX()) / 2;
		ball.setX(paddleCenterX - getWidth() / 2);
		ball.setY(480);
	}
	
	/** 
	 * set up the movement of the ball
	 * @param elapsedTime
	 */
	public void ballMove(double elapsedTime){
		ball.setX(ball.getX() + elapsedTime * ballSpeedHorizontal);
		ball.setY(ball.getY() + elapsedTime * ballSpeedVertical);
	}
	
	/**
	 * set up the horizontal bounce of the ball
	 */
	public void ballBounceHorizontal(){
		ballSpeedHorizontal *= -1;
	}
	
	/**
	 * set up the vertical bounce of the ball
	 */
	public void ballBounceVertical(){
		ballSpeedVertical *= -1;
	}
}

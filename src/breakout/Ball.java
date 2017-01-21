package breakout;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball {

	private static final String BALL_IMAGE = "ball.gif";
	private int ball_speed_horizontal;
	private int ball_speed_vertical;
	private boolean toBeRemoved = false;
	private boolean sticked = false;
	private ImageView ball;
	
	public Ball(){
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		ball = new ImageView(image);
		Random rn = new Random();
		ball_speed_horizontal = rn.nextInt(50) + 100;
		ball_speed_vertical = - rn.nextInt(50) - 100;
	}
	
	public ImageView getBall(){
		return ball;
	}
	
	public double getX(){
		return ball.getBoundsInParent().getMinX();
	}
	
	public double getY(){
		return ball.getBoundsInParent().getMinY();
	}
	
	public double getWidth(){
		return ball.getBoundsInParent().getWidth();
	}
	
	public double getHeight(){
		return ball.getBoundsInParent().getHeight();
	}
	
	public int getDirectionHorizontal(){
		if (ball_speed_horizontal > 0)
			return 1;
		return -1;
	}
	
	public int getDirectionVertical(){
		if (ball_speed_vertical > 0)
			return 1;
		return -1;
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
		ball_speed_horizontal *= times;
		ball_speed_horizontal *= times;
	}
	public void ballResetInitialDirection(){
		if (ball_speed_horizontal < 0)
			ball_speed_horizontal *= -1;
		if (ball_speed_vertical > 0)
			ball_speed_vertical *= -1;
	}
	
	public void ballMoveWithPaddle(Paddle paddle){
		double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
		ball.setX(paddleCenterX - getWidth() / 2);
		ball.setY(480);
	}
	
	public void ballMove(double elapsedTime){
		ball.setX(ball.getX() + elapsedTime * ball_speed_horizontal);
		ball.setY(ball.getY() + elapsedTime * ball_speed_vertical);
	}
	
	public void ballBounceHorizontal(){
		ball_speed_horizontal *= -1;
	}
	
	public void ballBounceVertical(){
		ball_speed_vertical *= -1;
	}
}

package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball {

	private static final String BALL_IMAGE = "ball.gif";
	private int ball_speed_horizontal = 150;
	private int ball_speed_vertical = 150;
	private ImageView ball;
	
	public Ball(){
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		ball = new ImageView(image);
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
	
	public void setX(double x){
		ball.setX(x);
	}
	
	public void setY(double y){
		ball.setY(y);
	}
	
	public void ballResetInitialDirection(){
		if (ball_speed_horizontal < 0)
			ball_speed_horizontal *= -1;
		if (ball_speed_vertical < 0)
			ball_speed_vertical *= -1;
	}
	
	public void ballMoveWithPaddle(Paddle paddle){
		double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
		ball.setX(paddleCenterX - getWidth() / 2);
	}
	
	public void ballMove(double elapsedTime){
		ball.setX(ball.getX() + elapsedTime * ball_speed_horizontal);
		ball.setY(ball.getY() - elapsedTime * ball_speed_vertical);
	}
	
	public void ballBounceHorizontal(){
		ball_speed_horizontal *= -1;
	}
	
	public void ballBounceVertical(){
		ball_speed_vertical *= -1;
	}
	
	public void ballDie(){
		
	}
}

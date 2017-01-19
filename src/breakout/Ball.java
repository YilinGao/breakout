package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball {

	private static final String BALL_IMAGE = "ball.gif";
	private int ball_speed_horizontal = 150;
	private int ball_speed_vertical = 150;
	private int ball_move_speed = 300;
	private ImageView ball;
	
	public Ball(){
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		ball = new ImageView(image);
	}
	
	public ImageView getBall(){
		return ball;
	}
	
	public void ballReset(){
		if (ball_speed_horizontal < 0)
			ball_speed_horizontal *= -1;
		if (ball_speed_vertical < 0)
			ball_speed_vertical *= -1;
	}
	
	public void updateBall(int direction, double elapsedTime){
		ball.setX(ball.getX() + elapsedTime * direction * ball_move_speed);
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

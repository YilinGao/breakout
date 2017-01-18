package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball {

	private static final String BALL_IMAGE = "ball.gif";
	private ImageView ball;
	
	public Ball(){
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		ball = new ImageView(image);
	}
	
	public ImageView getBall(){
		return ball;
	}
	
	public void updateBall(double elapsedTime){
		ball.setX(ball.getX() + elapsedTime * GameWorld.BALL_SPEED * GameWorld.BALL_DIRECTION_HORIZONTAL);
		ball.setY(ball.getY() + elapsedTime * GameWorld.BALL_SPEED * GameWorld.BALL_DIRECTION_VERTICAL);
	}
}

package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Paddle {

	private static final String PADDLE_IMAGE = "paddle.gif";
	private static final int PADDLE_SPEED = 300;
	private ImageView paddle;
	
	public Paddle(){
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE));
		paddle = new ImageView(image);
	}
	
	public ImageView getPaddle(){
		return paddle;
	}
	
	public double getX(){
		return paddle.getBoundsInParent().getMinX();
	}
	
	public double getY(){
		return paddle.getBoundsInParent().getMinY();
	}
	
	public double getWidth(){
		return paddle.getBoundsInParent().getWidth();
	}
	
	public void setX(double x){
		paddle.setX(x);
	}
	
	public void setY(double y){
		paddle.setY(y);
	}
	
	public void paddleMove(int direction, double elapsedTime){
		if (getX() >= GameWorld.WIDTH){
			paddle.setX(0 + getX() - GameWorld.WIDTH);
		}
		else if (getX() + getWidth() <= 0){
			paddle.setX(GameWorld.WIDTH + getX());
		}
		else{
			paddle.setX(getX() + PADDLE_SPEED * direction * elapsedTime);
		}
	}
}

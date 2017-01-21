package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Paddle {
	private static final String PADDLE_IMAGE = "paddle.gif";
	private int paddle_speed = 1600;
	private boolean sticky = false;
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
	
	public double getHeight(){
		return paddle.getBoundsInParent().getHeight();
	}
	
	public boolean getSticky(){
		return sticky;
	}
	
	public void setX(double x){
		paddle.setX(x);
	}
	
	public void setY(double y){
		paddle.setY(y);
	}
	
	public void setSpeed(double times){
		paddle_speed *= times;
	}
	
	public void setSticky(boolean status){
		sticky = status;
	}
	
	public void paddleMove(int direction, double elapsedTime){
		if (getX() >= GameWorld.WIDTH){
			paddle.setX(0 + getX() - GameWorld.WIDTH);
		}
		else if (getX() + getWidth() <= 0){
			paddle.setX(GameWorld.WIDTH + getX());
		}
		else{
			paddle.setX(getX() + paddle_speed * direction * elapsedTime);
		}
	}
}

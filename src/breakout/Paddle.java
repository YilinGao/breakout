package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Paddle {

	private static final String PADDLE_IMAGE = "paddle.gif";
	private ImageView paddle;
	
	public Paddle(){
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE));
		paddle = new ImageView(image);
	}
	
	public ImageView getPaddle(){
		return paddle;
	}
	
	public void updatePaddle(int direction, double elapsedTime){
		paddle.setX(paddle.getX() + GameWorld.PADDLE_SPEED * direction * elapsedTime);
	}
}

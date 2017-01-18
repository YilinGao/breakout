package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
}

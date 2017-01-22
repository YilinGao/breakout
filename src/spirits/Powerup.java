package spirits;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Powerup {

	private static final String POWERUP_IMAGE1 = "speedpower.gif";
	private static final String POWERUP_IMAGE2 = "extraballpower.gif";
	private static final String POWERUP_IMAGE3 = "paddlepower.gif";
	private static final String POWERUP_IMAGE4 = "lifepower.gif";
	
	private int powerup_speed;
	private ImageView powerup;
	private int type;
	private boolean toBeRemoved = false;
	
	public Powerup(double theType, Brick brick){
		String path = POWERUP_IMAGE1;
		if (theType <= 0.1){
			type = 1;
			path = POWERUP_IMAGE1;
		}
		else if (theType <= 0.2){
			type = 2;
			path = POWERUP_IMAGE2;
		}			
		else if (theType <= 0.3){
			type = 3;
			path = POWERUP_IMAGE3;
		}
		else{
			type = 4;
			path = POWERUP_IMAGE4;
		}
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(path));
		powerup = new ImageView(image);
		
		Random rn = new Random();
		powerup_speed = rn.nextInt(101) + 50;
		
		double x = brick.getX() + brick.getWidth() / 2;
		double y = brick.getY() + brick.getHeight();
		powerup.setX(x);
		powerup.setY(y);
	}
	
	public ImageView getPowerup(){
		return powerup;
	}
	
	public int getType(){
		return type;
	}
	
	public double getX(){
		return powerup.getBoundsInParent().getMinX();
	}
	
	public double getY(){
		return powerup.getBoundsInParent().getMinY();
	}
	
	public double getWidth(){
		return powerup.getBoundsInParent().getWidth();
	}
	
	public double getHeight(){
		return powerup.getBoundsInParent().getHeight();
	}
	
	public boolean getRemovalMark(){
		return toBeRemoved;
	}
	public void powerupMove(double elapsedTime){
		powerup.setY(getY() + elapsedTime * powerup_speed);
	}
	
	public void setRemovalMark(){
		toBeRemoved = true;
	}
}

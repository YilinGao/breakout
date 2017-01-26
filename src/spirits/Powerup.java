package spirits;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Class to implement power-ups in the game.
 * Wrapper for the class ImageView.
 * Depends on spirits.Brick
 * @author Yilin Gao
 *
 */
public class Powerup {

	private static final String POWERUP_IMAGE1 = "speedpower.gif";
	private static final String POWERUP_IMAGE2 = "extraballpower.gif";
	private static final String POWERUP_IMAGE3 = "paddlepower.gif";
	private static final String POWERUP_IMAGE4 = "lifepower.gif";
	
	private int powerup_speed;
	private ImageView powerup;
	private int type;
	private boolean toBeRemoved = false;
	
	/**
	 * Constructor of the class Powerup.
	 * @param theType: the random number generated in GameWorld to determine the powerup's type
	 * @param brick: the brick which was hit and generates this power-up
	 */
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
		
		double x = brick.getMinX() + brick.getWidth() / 2;
		double y = brick.getMinY() + brick.getHeight();
		powerup.setX(x);
		powerup.setY(y);
	}
	
	public ImageView getPowerup(){
		return powerup;
	}
	
	public int getType(){
		return type;
	}
	
	public double getMinX(){
		return powerup.getBoundsInParent().getMinX();
	}
	
	public double getMaxX(){
		return powerup.getBoundsInParent().getMaxX();
	}
	
	public double getMinY(){
		return powerup.getBoundsInParent().getMinY();
	}

	public double getMaxY(){
		return powerup.getBoundsInParent().getMaxY();
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
	
	/**
	 * Define movement of Powerup.
	 * @param elapsedTime
	 */
	public void powerupMove(double elapsedTime){
		powerup.setY(getMinY() + elapsedTime * powerup_speed);
	}
	
	public void setRemovalMark(){
		toBeRemoved = true;
	}
}

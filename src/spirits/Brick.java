package spirits;

import breakout.GameWorld;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Class to implement bricks in the game.
 * A wrapper of the ImageView class.
 * Depends on breakout.GameWorld.
 * @author Yilin Gao
 *
 */
public class Brick {

	private ImageView brick;
	private int type;
	private int layer;
	private int indexInLayer;
	private int totalBricksInLayer;
	private boolean toBeRemoved = false;

	private static final int BRICK_SPEED = 100;
	private static final String BRICK1_IMAGE= "brick7.gif";
	private static final String BRICK2_IMAGE = "brick8.gif";
	private static final String BRICK3_IMAGE = "brick9.gif";
	
	/**
	 * Constructor for the Brick class.
	 * @param rn: the random number indicating the type of the Brick
	 * @param l: the current layer (starting from 0)
	 * @param i: the index in the layer (starting from 0)
	 * @param n: number of bricks in the layer
	 * @param m: the index in all bricks (starting from 0)
	 */
	public Brick(int rn, int l, int i, int n){
		String path = "";
		if (rn <= 5){
			type = 1;
			path = BRICK1_IMAGE;			
		}
		else if (rn <= 8){
			type = 2;
			path = BRICK2_IMAGE;			
		}
		else{
			type = 3;
			path = BRICK3_IMAGE;			
		}
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(path));
		brick = new ImageView(image);
		brick.setFitWidth(70);
		brick.setPreserveRatio(false);
		brick.setSmooth(true);
		layer = l;
		indexInLayer = i;
		totalBricksInLayer = n;
	}
	
	public ImageView getBrick(){
		return brick;
	}
	
	public double getMinX(){
		return brick.getBoundsInParent().getMinX();
	}
	
	public double getMaxX(){
		return brick.getBoundsInParent().getMaxX();
	}
	
	public double getMinY(){
		return brick.getBoundsInParent().getMinY();
	}
	
	public double getMaxY(){
		return brick.getBoundsInParent().getMaxY();
	}
	
	public double getWidth(){
		return brick.getBoundsInParent().getWidth();
	}
	
	public double getHeight(){
		return brick.getBoundsInLocal().getHeight();
	}
	
	public int getType(){
		return type;
	}
	
	public boolean getRemovalMark(){
		return toBeRemoved;
	}
	
	public void setPosition(double startingX, double startingY){
		double x, y;
		x = startingX - getWidth() * totalBricksInLayer / 2.0 + getWidth() * indexInLayer;
		y = startingY + getHeight() * layer;
		brick.setX(x);
		brick.setY(y);
	}
	
	/**
	 * Define actions to do when a brick is hit by a ball.
	 * @return type of this brick
	 */
	public int hitBrick(){
		if (type == 1){
			toBeRemoved = true;
			return type;
		}
		else if (type == 2){
			Image image = new Image(getClass().getClassLoader().getResourceAsStream(BRICK1_IMAGE));
			brick.setImage(image);
			return (type--);
		}
		else{
			Image image = new Image(getClass().getClassLoader().getResourceAsStream(BRICK2_IMAGE));
			brick.setImage(image);
			return (type--);
		}
	}
	
	/**
	 * Define brick movements in level 3.
	 * @param elapsedTime
	 */
	public void brickMove(double elapsedTime){
		int direction = 1;
		if (layer % 4 == 2){
			direction = -1;
		}
		if (layer % 4 == 0){
			if (getMinX() >= GameWorld.WIDTH / 2 + getWidth() * totalBricksInLayer / 2){
				brick.setX(GameWorld.WIDTH / 2 - getWidth() * totalBricksInLayer / 2 + direction * elapsedTime * BRICK_SPEED);
			}
			else{
				brick.setX(getMinX() + direction * elapsedTime * BRICK_SPEED);
			}
		}
		else if (layer % 4 == 2){
			if (getMinX() <= GameWorld.WIDTH / 2 - getWidth() * totalBricksInLayer / 2 - getWidth()){
				brick.setX(GameWorld.WIDTH / 2 + getWidth() * totalBricksInLayer / 2 - getWidth() + direction * elapsedTime * BRICK_SPEED);
			}
			else{
				brick.setX(getMinX() + direction * elapsedTime * BRICK_SPEED); 
			}
		}
	}
}

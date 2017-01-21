package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Brick {

	private ImageView brick;
	private int type;
	private int layer;
	private int indexInLayer;
	private int totalBricksInLayer;
	private int index;
	private boolean toBeRemoved = false;

	private static final int BRICK_SPEED = 100;
	private static final String BRICK1_IMAGE= "brick7.gif";
	private static final String BRICK2_IMAGE = "brick8.gif";
	private static final String BRICK3_IMAGE = "brick9.gif";
	
	/**
	 * 
	 * @param rn: the random number indicating the type of the Brick
	 * @param l: the current layer (starting from 0)
	 * @param i: the index in the layer (starting from 0)
	 * @param n: number of bricks in the layer
	 * @param m: the index in all bricks (starting from 0)
	 */
	public Brick(int rn, int l, int i, int n, int m){
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
		index = m;
	}
	
	public ImageView getBrick(){
		return brick;
	}
	
	public double getX(){
		return brick.getBoundsInParent().getMinX();
	}
	
	public double getY(){
		return brick.getBoundsInParent().getMinY();
	}
	
	public double getWidth(){
		return brick.getBoundsInParent().getWidth();
	}
	
	public double getHeight(){
		return brick.getBoundsInLocal().getHeight();
	}
	
	public int getIndex(){
		return index;
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
	
	public int hitBrick(){
//		toBeRemoved = true;
//		return type;
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
	
	// used in level 3
	public void brickMove(double elapsedTime){
		int direction = 1;
		if (layer % 4 == 2){
			direction = -1;
		}
		if (layer % 4 == 0){
			if (getX() >= GameWorld.WIDTH / 2 + getWidth() * totalBricksInLayer / 2){
				brick.setX(GameWorld.WIDTH / 2 - getWidth() * totalBricksInLayer / 2 + direction * elapsedTime * BRICK_SPEED);
			}
			else{
				brick.setX(getX() + direction * elapsedTime * BRICK_SPEED);
			}
		}
		else if (layer % 4 == 2){
			if (getX() <= GameWorld.WIDTH / 2 - getWidth() * totalBricksInLayer / 2 - getWidth()){
				brick.setX(GameWorld.WIDTH / 2 + getWidth() * totalBricksInLayer / 2 - getWidth() + direction * elapsedTime * BRICK_SPEED);
			}
			else{
				brick.setX(getX() + direction * elapsedTime * BRICK_SPEED); 
			}
		}
	}
}

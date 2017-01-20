package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Block {

	private ImageView block;
	private int type;
	private int layer;
	private int indexInLayer;
	private int totalBlocksInLayer;

	private static final String BRICK1_IMAGE= "brick7.gif";
	private static final String BRICK2_IMAGE = "brick8.gif";
	private static final String BRICK3_IMAGE = "brick9.gif";
	
	public Block(int t, int l, int i, int n){
		String path = "";
		if (t <= 5)
			path = BRICK1_IMAGE;
		else if (t <= 8)
			path = BRICK2_IMAGE;
		else
			path = BRICK3_IMAGE;
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(path));
		block = new ImageView(image);
		type = t;
		layer = l;
		indexInLayer = i;
		totalBlocksInLayer = n;
	}
	
	public ImageView getBlock(){
		return block;
	}
	
	public double getX(){
		return block.getBoundsInParent().getMinX();
	}
	
	public double getY(){
		return block.getBoundsInParent().getMinY();
	}
	
	public double getWidth(){
		return block.getBoundsInParent().getWidth();
	}
	
	public double getHeight(){
		return block.getBoundsInLocal().getHeight();
	}
	
	public void setPosition(double startingX, double startingY){
		double x, y;
		x = startingX - getWidth() * totalBlocksInLayer / 2.0 + getWidth() * indexInLayer;
		y = startingY + getHeight() * layer;
		block.setX(x);
		block.setY(y);
	}
}

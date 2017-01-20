package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Block {

	private ImageView block;
	private int layer;
	private int indexInLayer;
	private int totalBlocksInLayer;
	
	public Block(String path, int l, int i, int n){
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(path));
		block = new ImageView(image);
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
	
	public double getHeight(){
		return block.getBoundsInLocal().getHeight();
	}
	
	public double getWidth(){
		return block.getBoundsInParent().getWidth();
	}
	
	public void setPosition(double startingX, double startingY){
		double x, y;
		x = startingX - getWidth() * totalBlocksInLayer / 2.0 + getWidth() * indexInLayer;
		y = startingY + getHeight() * layer;
		block.setX(x);
		block.setY(y);
	}
}

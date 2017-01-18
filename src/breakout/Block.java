package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Block {

	private static ImageView block;
	
	public Block(String path){
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(path));
		block = new ImageView(image);
	}
	
	public ImageView getBlock(){
		return block;
	}
}

package pages;

import breakout.GameWorld;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public abstract class Page {
	
	protected Stage stage;
	protected GameWorld gameWorld;
	protected Group root;
	protected Scene scene;
	
	public Page(Stage theStage, GameWorld theGameWorld){
		stage = theStage;
		gameWorld = theGameWorld;
		root = new Group();
		scene = new Scene(root, GameWorld.WIDTH, GameWorld.HEIGHT, GameWorld.BACKGROUND);
	}
	
	public abstract void initializePage();
	
	protected abstract void handleKeyInput(KeyEvent event);
	
	protected abstract void handleMouseInput(MouseEvent event);
	
	public Group getRoot(){
		return root;
	}
	
	public Scene getScene(){
		return scene;
	}
}

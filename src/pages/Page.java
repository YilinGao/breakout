package pages;

import breakout.GameWorld;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Super class (abstract) of all kinds of pages.
 * Depends on breakout.GameWorld.
 * @author Yilin Gao
 *
 */
public abstract class Page {
	
	private Stage stage;
	private GameWorld gameWorld;
	private Group root;
	private Scene scene;
	
	/**
	 * Constructor of the super class Page.
	 * @param theStage
	 * @param theGameWorld
	 */
	public Page(Stage theStage, GameWorld theGameWorld){
		stage = theStage;
		gameWorld = theGameWorld;
		root = new Group();
		scene = new Scene(root, GameWorld.WIDTH, GameWorld.HEIGHT, GameWorld.BACKGROUND);
	}
	
	public Stage getStage(){
		return stage;
	}
	
	public GameWorld getGameWorld(){
		return gameWorld;
	}
	
	public Group getRoot(){
		return root;
	}
	
	public Scene getScene(){
		return scene;
	}
	
	/**
	 * Abstract method to initialize nodes on the page's scene.
	 * To be realized by each sub class.
	 */
	public abstract void initializePage();
	
	/**
	 * Abstract method to handle key inputs in this Scene.
	 * @param event
	 */
	protected abstract void handleKeyInput(KeyEvent event);
	
	/**
	 * Abstract method to handle mouse inputs in this Scene.
	 * @param event
	 */
	protected abstract void handleMouseInput(MouseEvent event);

}

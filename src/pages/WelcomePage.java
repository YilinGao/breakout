package pages;

import breakout.GameWorld;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * A sub class of the class Page.
 * Implement the welcome Scene.
 * @author Yilin Gao
 *
 */
public class WelcomePage extends Page {
	
	private Button start;
	private Text gameTitle;
	private Text instructions;
	private Text tips;
	
	/**
	 * Constructor of the WelcomePage class.
	 * @param theStage
	 * @param theGameWorld
	 */
	public WelcomePage(Stage theStage, GameWorld theGameWorld){
		super(theStage, theGameWorld);
	}

	@Override
	public void initializePage() {
		gameTitle = new Text(50, 100, "Breakout\nBy Sandy");			
		instructions = new Text(50, 200, "Press SPACE to start.\nUse \"<-\" and \"->\" to control the paddle.\nYou have 3 lives to start.");
		tips = new Text(50, 300, "Tips:\nGet control of your paddle!\nGood luck!");
		start = new Button("Click or press SPACE to start game!");
		start.setLayoutX(50);
		start.setLayoutY(400);
		start.setOnMouseReleased(e -> handleMouseInput(e));
		start.setOnKeyReleased(e -> handleKeyInput(e));
		this.getRoot().getChildren().add(gameTitle);
		this.getRoot().getChildren().add(instructions);
		this.getRoot().getChildren().add(tips);
		this.getRoot().getChildren().add(start);
		this.getStage().setScene(this.getScene());
		this.getStage().setTitle(GameWorld.TITLE);
		this.getStage().show();
	}

	@Override
	protected void handleKeyInput(KeyEvent event) {
		if (event.getCode() == KeyCode.SPACE) {
			this.getGameWorld().initializeLevel(1);
		}		
	}

	@Override
	protected void handleMouseInput(MouseEvent event) {
		this.getGameWorld().initializeLevel(1);
	}

}

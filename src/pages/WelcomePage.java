package pages;

import breakout.GameWorld;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WelcomePage extends Page {
	
	private Button start;
	private Text gameTitle;
	private Text instructions;
	private Text tips;
	
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
		root.getChildren().add(gameTitle);
		root.getChildren().add(instructions);
		root.getChildren().add(tips);
		root.getChildren().add(start);
		stage.setScene(scene);
		stage.setTitle(GameWorld.TITLE);
		stage.show();
	}

	@Override
	protected void handleKeyInput(KeyEvent event) {
		if (event.getCode() == KeyCode.SPACE) {
			gameWorld.initializeLevel(1);
		}		
	}

	@Override
	protected void handleMouseInput(MouseEvent event) {
		gameWorld.initializeLevel(1);
	}

}

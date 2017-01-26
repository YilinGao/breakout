package pages;

import breakout.GameWorld;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ResultPage extends Page {
	
	private Button start;
	private Text resultReport;
	
	public ResultPage(Stage theStage, GameWorld theGameWorld) {
		super(theStage, theGameWorld);
	}

	@Override
	public void initializePage() {
		root = new Group();
		scene = new Scene(root, GameWorld.WIDTH, GameWorld.HEIGHT, GameWorld.BACKGROUND);
		
		resultReport = new Text();
		if (gameWorld.getController().isDead()) {
			resultReport.setText("Sorry you lose :(");
		}
		else{
			resultReport.setText("Wow you win! :)\nYour score is " + gameWorld.getScore() + ".");
		}
		resultReport.setX(50);
		resultReport.setY(300);
		gameWorld.setLives(3);
		gameWorld.setScore(0);
		start = new Button("Have another try? Click or press SPACE.");
		start.setLayoutX(50);
		start.setLayoutY(400);
		start.setOnMouseReleased(e -> handleMouseInput(e));
		start.setOnKeyReleased(e -> handleKeyInput(e));
		root.getChildren().add(resultReport);
		root.getChildren().add(start);
		stage.setScene(scene);
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

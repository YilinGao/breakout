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
 * Implement the Scene to show results.
 * @author Yilin Gao
 *
 */
public class ResultPage extends Page {
	
	private Button start;
	private Text resultReport;
	
	/**
	 * Constructor of the class ResultPage.
	 * @param theStage
	 * @param theGameWorld
	 */
	public ResultPage(Stage theStage, GameWorld theGameWorld) {
		super(theStage, theGameWorld);
	}

	@Override
	public void initializePage() {		
		resultReport = new Text();
		if (this.getGameWorld().getController().isDead()) {
			resultReport.setText("Sorry you lose :(");
		}
		else{
			resultReport.setText("Wow you win! :)\nYour score is " + this.getGameWorld().getScore() + ".");
		}
		resultReport.setX(50);
		resultReport.setY(300);
		this.getGameWorld().setLives(3);
		this.getGameWorld().setScore(0);
		start = new Button("Have another try? Click or press SPACE.");
		start.setLayoutX(50);
		start.setLayoutY(400);
		start.setOnMouseReleased(e -> handleMouseInput(e));
		start.setOnKeyReleased(e -> handleKeyInput(e));
		this.getRoot().getChildren().add(resultReport);
		this.getRoot().getChildren().add(start);
		this.getStage().setScene(this.getScene());
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

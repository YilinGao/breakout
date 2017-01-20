package breakout;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * 
 * @author ygao
 *
 */
public class Breakout extends Application {
	private static final int framesPerSecond = 60;
	private static final String title = "Breakout";
	private GameWorld gameWorld = new GameWorld(framesPerSecond);
	private Stage stage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Auto-generated method stub
		stage = primaryStage;
		
		gameWorld.initializeWelcome(stage);	
		gameWorld.getWelcomeButton().setOnMouseReleased(e -> gameWorld.initializeLevel(1));

		primaryStage.setTitle(title);
		primaryStage.show();
	}
	
	
	
	public static void main(String[] args) {
		// Auto-generated method stub
		launch(args);
	}
}

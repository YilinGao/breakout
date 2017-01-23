package breakout;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * the entrance of the program,
 * depends on the class breakout.GameWorld
 * @author Yilin Gao
 *
 */
public class Breakout extends Application {
	private GameWorld gameWorld = new GameWorld();

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Auto-generated method stub	
		gameWorld.initializeWelcome(primaryStage);
	}
	
	public static void main(String[] args) {
		// Auto-generated method stub
		launch(args);
	}
}

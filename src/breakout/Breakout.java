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
	private GameWorld gameWorld = new GameWorld(framesPerSecond, title);

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		gameWorld.initialize(primaryStage);
		gameWorld.buildAndSetGameLoop();
		gameWorld.beginGameLoop();
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		// Auto-generated method stub
		launch(args);
	}
}

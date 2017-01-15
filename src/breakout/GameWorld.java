package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameWorld {
	private static Timeline timeline;
	
	private static final int WIDTH = 420;
	private static final int HEIGHT = 600;	
	private static final Paint BACKGROUND = Color.WHITE;
	private static Group root;
	private static Scene scene;
	
	private static Text gameTitle;
	private static Text instructions;
	private static Paddle paddle;
	private static Ball[] balls;
	private static Block[] blocks;
	private static int lives = 3;
	
	private static int framesPerSecond;
	private static String title;
	private static int millisecondDelay;
	private static int secondDelay;

	/**
	 * constructor of the class GameWorld
	 * @param fps: number of frames per second
	 * @param t: title of the stage
	 */
	public GameWorld(int fps, String t){
		framesPerSecond = fps;
		title = t;
		millisecondDelay = 1000 / framesPerSecond;
		secondDelay = 1 / framesPerSecond;
	}
	
	/**
	 * set up game loop (timeline),
	 * call actionsPerFrame(double elapsedTime) to handle animations of nodes during each frame
	 */
	public void buildAndSetGameLoop(){
		Duration oneFrameDuration = Duration.millis(millisecondDelay);
		KeyFrame oneFrame = new KeyFrame(oneFrameDuration, new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				actionsPerFrame(secondDelay);
			}
			
		});
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(oneFrame);
	}
	
	/**
	 * Build up stage, scene, and nodes in the scene
	 * @param stage: primaryStage in Application's start() method
	 */
	public void initialize(Stage stage){
		setupGame(WIDTH, HEIGHT, BACKGROUND);
		
		stage.setScene(scene);
		stage.setTitle(title);
	}
	
	private void setupGame(int width, int height, Paint background){
		// TODO add other nodes
		root = new Group();
		scene = new Scene(root, width, height, background);
		
		gameTitle = new Text(50, 200, "Breakout\nBy Sandy");			
		instructions = new Text(50, 300, "Use \"<-\" and \"->\" to control the paddle.\nYou have 3 lives to start.");
				
		root.getChildren().add(gameTitle);
		root.getChildren().add(instructions);
				
		scene.setOnKeyPressed(e -> loadLevel1());
		scene.setOnMouseClicked(e -> loadLevel1());
	}
	
	/**
	 * start game loop (timeline)
	 */
	public void beginGameLoop(){
		getGameLoop().play();
	}
	
	private Timeline getGameLoop(){
		return timeline;
	}
	
	private void loadLevel1(){
		root.getChildren().clear();
		// TODO load ball, paddle, bricks for level 1
	}
	
	private boolean isLevelEnd(){
		// TODO check if the number of bricks on the screen is 0
		
		return false;
	}
	
	private boolean isDead(){
		// TODO check if the number of lives is 0
		
		return false;
	}
	
	/**
	 * animations of nodes on the scene during one frame, check level end and move to the next level
	 * @param elapsedTime: the duration of each frame
	 */
	private void actionsPerFrame(double elapsedTime){
		// TODO
	}
	
	private void ballBounceOnPaddle(){
		// TODO
	}
	
	private void ballBounceOnWalls(){
		// TODO
	}
	
	private void ballHitBlock(){
		// TODO
	}
	
	private void dropPowerup(){
		// TODO
	}
	
	private void ballFallDown(){
		
	}
}

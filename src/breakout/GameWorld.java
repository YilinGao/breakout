package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Box;
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
	private static Text remainingLives;
	private static Text currentLevel;
	private static Text currentScore;
	private static Paddle paddle;
	private static Ball[] balls = new Ball[1];
	private static int numOfBlocks;
	private static int currentNumOfBlocks = numOfBlocks;
	private static Block[] blocks = new Block[numOfBlocks];
	
	private static int lives = 3;
	private static int level = 1;
	private static int score = 0;
	
	private static final String BRICK1_IMAGE= "brick7.gif";
	private static final String BRICK2_IMAGE = "brick8.gif";
	private static final String BRICK3_IMAGE = "brick9.gif";
	
	public static final int BALL_SPEED = 50;
	public static int BALL_DIRECTION_VERTICAL = -1;
	public static int BALL_DIRECTION_HORIZONTAL = 1;
	private static final int PADDLE_SPEED = 5;
	private static int PADDLE_DIRECTION = 1;
	
	private static int framesPerSecond;
	private static String title;
	private static double millisecondDelay;
	public static double secondDelay;

	/**
	 * constructor of the class GameWorld
	 * @param fps: number of frames per second
	 * @param t: title of the stage
	 */
	public GameWorld(int fps, String t){
		framesPerSecond = fps;
		title = t;
		millisecondDelay = 1000.0 / framesPerSecond;
		secondDelay = 1.0 / framesPerSecond;
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
		timeline.play();
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
		getGameLoop().play();
		
		root.getChildren().clear();
		
		balls[0] = new Ball();
		balls[0].getBall().setX(WIDTH / 2 - balls[0].getBall().getBoundsInParent().getWidth() / 2);
		balls[0].getBall().setY(480);
		root.getChildren().add(balls[0].getBall());
		
		paddle = new Paddle();
		paddle.getPaddle().setX(WIDTH / 2 - paddle.getPaddle().getBoundsInParent().getWidth() / 2);
		paddle.getPaddle().setY(480 + balls[0].getBall().getBoundsInParent().getHeight());
		root.getChildren().add(paddle.getPaddle());
		
		currentScore = new Text("Current Score: "+ score);
		currentScore.setX(30);
		currentScore.setY(50);
		root.getChildren().add(currentScore);
		
		currentLevel = new Text("Level: " + level);
		currentLevel.setX(390 - currentLevel.getBoundsInLocal().getWidth());
		currentLevel.setY(50);
		root.getChildren().add(currentLevel);
		
		remainingLives = new Text("Remaining lives: " + lives);
		remainingLives.setX(30);
		remainingLives.setY(550);
		root.getChildren().add(remainingLives);
		
		// TODO load bricks for level 1
		
	}
	
	private boolean isLevelEnd(){
		return (currentNumOfBlocks == 0);
	}
	
	private boolean isDead(){
		return (lives == 0);
	}
	
	/**
	 * animations of nodes on the scene during one frame, check level end and move to the next level
	 * @param elapsedTime: the duration of each frame
	 */
	private void actionsPerFrame(double elapsedTime){
		// TODO in each frame, the ball moves, the paddle moves, check for hit
		// update ball
		balls[0].updateBall(elapsedTime);
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

package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameWorld {
	private Timeline timeline; 
	
	public static final int WIDTH = 420;
	public static final int HEIGHT = 600;	
	public static final Paint BACKGROUND = Color.WHITE;
	private Stage theStage;
	private Scene currentScene;
	private Scene welcomePage, level1, level2, level3, resultPage;
	
	private Button start;
	private Text gameTitle;
	private Text instructions;
	private Text remainingLives;
	private Text currentLevel;
	private Text currentScore;
	private Paddle paddle;
	private Ball[] balls = new Ball[1];
	private int numOfBlocks = 0;
	private int currentNumOfBlocks = numOfBlocks;
	private Block[] blocks = new Block[numOfBlocks];
	
	private boolean started = false;
	private int lives = 3;
	private int level = 1;
	private int score = 0;
	
	private final String BRICK1_IMAGE= "brick7.gif";
	private final String BRICK2_IMAGE = "brick8.gif";
	private final String BRICK3_IMAGE = "brick9.gif";
	
	public static final int PADDLE_SPEED = 150;
	
	private int framesPerSecond;
	private double millisecondDelay;
	private double secondDelay;

	/**
	 * constructor of the class GameWorld
	 * @param fps: number of frames per second
	 * @param t: title of the stage
	 */
	public GameWorld(int fps){
		framesPerSecond = fps;
		millisecondDelay = 1000.0 / framesPerSecond;
		secondDelay = 1.0 / framesPerSecond;
	}
	
	/**
	 * Build up stage, scene, and nodes in the scene
	 * @param stage: primaryStage in Application's start() method
	 */
	public void initializeWelcome(Stage stage){
		theStage = stage;
		
		Group rootWelcome = new Group();
		welcomePage = new Scene(rootWelcome, WIDTH, HEIGHT, BACKGROUND);
		
		gameTitle = new Text(50, 200, "Breakout\nBy Sandy");			
		instructions = new Text(50, 300, "Use \"<-\" and \"->\" to control the paddle.\nYou have 3 lives to start.");
		start = new Button("Click to start game!");
		start.setLayoutX(50);
		start.setLayoutY(400);
		rootWelcome.getChildren().add(gameTitle);
		rootWelcome.getChildren().add(instructions);
		rootWelcome.getChildren().add(start);
		stage.setScene(welcomePage);
		currentScene = welcomePage;
	}
	
	public void initializeLevel1(){
		level = 1;
		
		Group rootLevel1 = new Group();
		level1 = new Scene(rootLevel1, WIDTH, HEIGHT, BACKGROUND);
		
		balls[0] = new Ball();		
		paddle = new Paddle();
		// TODO load bricks for level 1		

		setFixedItems();
		setMovableItems();
		
		rootLevel1.getChildren().add(balls[0].getBall());
		rootLevel1.getChildren().add(paddle.getPaddle());				
		rootLevel1.getChildren().add(currentScore);		
		rootLevel1.getChildren().add(currentLevel);		
		rootLevel1.getChildren().add(remainingLives);

		
		theStage.setScene(level1);
		currentScene = level1;
		// TODO what's the difference between Pressed and Released?
		currentScene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.SPACE)
					started = true;				
			}
			
		});
		buildAndSetGameLoop();
		beginGameLoop();
	}

	private void initializeLevel2(){
		level = 2;
		
		Group rootLevel2 = new Group();
		level2 = new Scene(rootLevel2, WIDTH, HEIGHT, BACKGROUND);
		
		balls[0] = new Ball();		
		paddle = new Paddle();
		// TODO load bricks for level 2
		
		rootLevel2.getChildren().add(balls[0].getBall());
		rootLevel2.getChildren().add(paddle.getPaddle());				
		rootLevel2.getChildren().add(currentScore);		
		rootLevel2.getChildren().add(currentLevel);		
		rootLevel2.getChildren().add(remainingLives);

		setFixedItems();
		setMovableItems();
		
		theStage.setScene(level2);
		currentScene = level2;
		currentScene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.SPACE)
					started = true;				
			}
			
		});
	}

	private void initializeLevel3(){
		level = 3;
		
		Group rootLevel3 = new Group();
		level3 = new Scene(rootLevel3, WIDTH, HEIGHT, BACKGROUND);
		
		balls[0] = new Ball();		
		paddle = new Paddle();
		// TODO load bricks for level 2
		
		rootLevel3.getChildren().add(balls[0].getBall());
		rootLevel3.getChildren().add(paddle.getPaddle());				
		rootLevel3.getChildren().add(currentScore);		
		rootLevel3.getChildren().add(currentLevel);		
		rootLevel3.getChildren().add(remainingLives);

		setFixedItems();
		setMovableItems();
		
		theStage.setScene(level3);
		currentScene = level3;
		currentScene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.SPACE)
					started = true;				
			}
			
		});
	}
	
	private void initializeResultPage(){
		stopGameLoop();
		
		Group rootResult = new Group();
		resultPage = new Scene(rootResult, WIDTH, HEIGHT, BACKGROUND);
		
		Text report = new Text();
		if (isDead()){
			report.setText("Sorry you lose :(");
		}
		else{
			report.setText("Wow you win! :)");
		}
		report.setX(50);
		report.setY(300);
		
		rootResult.getChildren().add(report);
		theStage.setScene(resultPage);
		currentScene = resultPage;
	}

	private void setFixedItems(){
		currentScore = new Text("Current Score: "+ score);
		currentScore.setX(30);
		currentScore.setY(50);
		
		currentLevel = new Text("Level: " + level);
		currentLevel.setX(390 - currentLevel.getBoundsInLocal().getWidth());
		currentLevel.setY(50);
		
		remainingLives = new Text("Remaining lives: " + lives);
		remainingLives.setX(30);
		remainingLives.setY(550);
	}
	
	private void setMovableItems(){
		started = false;
		
		balls[0].getBall().setX(WIDTH / 2 - balls[0].getBall().getBoundsInParent().getWidth() / 2);
		balls[0].getBall().setY(480);
		balls[0].ballReset();

		paddle.getPaddle().setX(WIDTH / 2 - paddle.getPaddle().getBoundsInParent().getWidth() / 2);
		paddle.getPaddle().setY(balls[0].getBall().getBoundsInParent().getMaxY());
		// TODO load bricks for each level
	}
		
	public Button getWelcomeButton(){
		return start;
	}
	
	/**
	 * set up game loop (time line),
	 * call actionsPerFrame(double elapsedTime) to handle animations of nodes during each frame
	 */
	private void buildAndSetGameLoop(){
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
	 * start game loop (time line)
	 */
	private void beginGameLoop(){
		timeline.play();
	}
	
	private void stopGameLoop(){
		timeline.stop();
	}
	
	/**
	 * animations of nodes on the scene during one frame, check level end and move to the next level
	 * @param elapsedTime: the duration of each frame
	 */
	private void actionsPerFrame(double elapsedTime){
		// TODO check for hit between ball and bricks
		if (started){
			balls[0].ballMove(elapsedTime);
			ballBounceOnPaddle();
			ballBounceOnWalls();
//			ballHitBlock();
			ballFallDown();
		}
		currentScene.setOnKeyPressed(e -> handleKeyInput(e.getCode(), elapsedTime));
	}
	
	private void handleKeyInput(KeyCode code, double elapsedTime){
		if (code == KeyCode.LEFT){
			if (!started)
				balls[0].updateBall(-1, elapsedTime);
			paddle.updatePaddle(-1, elapsedTime);
		}
		else if (code == KeyCode.RIGHT){
			if (!started)
				balls[0].updateBall(1, elapsedTime);
			paddle.updatePaddle(1, elapsedTime);
		}
	}
	
	private void ballBounceOnPaddle(){
		double ballMinX = balls[0].getBall().getBoundsInParent().getMinX();
		double ballMaxX = balls[0].getBall().getBoundsInParent().getMaxX();
		double paddleMinX = paddle.getPaddle().getBoundsInParent().getMinX();
		double paddleMaxX = paddle.getPaddle().getBoundsInParent().getMaxX();
		double ballMaxY = balls[0].getBall().getBoundsInParent().getMaxY();
		double paddleMinY = paddle.getPaddle().getBoundsInParent().getMinY();
		System.out.println("ballMaxY: " + ballMaxY + ", paddleMinY: " + paddleMinY);
		System.out.println("ballMinX: " + ballMinX + ", paddleMinX: " + paddleMinX);
		System.out.println("ballMaxX: " + ballMaxX + ", paddleMaxX: " + paddleMaxX + "\n");
		if (ballMaxY >= paddleMinY && ballMinX >= paddleMinX && ballMaxX <= paddleMaxX)
			balls[0].ballBounceVertical();
	}
	
	private void ballBounceOnWalls(){
		double ballMinX = balls[0].getBall().getBoundsInParent().getMinX();
		double ballMaxX = balls[0].getBall().getBoundsInParent().getMaxX();
		double ballMinY = balls[0].getBall().getBoundsInParent().getMinY();
		if (ballMinX < 0 || ballMaxX > WIDTH)
			balls[0].ballBounceHorizontal();
		else if (ballMinY < 0)
			balls[0].ballBounceVertical();
	}
	
	private void ballHitBlock(){
		// TODO
		if (isLevelEnd()){
			if (level == 1)
				initializeLevel2();
		}
	}
	
	private void dropPowerup(){
		// TODO
	}
	
	private void ballFallDown(){
		double ballMinX = balls[0].getBall().getBoundsInParent().getMinX();
		double ballMaxX = balls[0].getBall().getBoundsInParent().getMaxX();
		double paddleMinX = paddle.getPaddle().getBoundsInParent().getMinX();
		double paddleMaxX = paddle.getPaddle().getBoundsInParent().getMaxX();
		double ballMaxY = balls[0].getBall().getBoundsInParent().getMaxY();
		double paddleMinY = paddle.getPaddle().getBoundsInParent().getMinY();
		if (ballMaxY >= paddleMinY && (ballMinX < paddleMinX || ballMaxX > paddleMaxX)){
			endOfLife();
		}
	}
	
	private void endOfLife(){
		lives--;
		if (!isDead()){
			remainingLives.setText("Remaining lives: " + lives);
			setMovableItems();
		}
		else{
			initializeResultPage();
		}
	}

	private boolean isLevelEnd(){
		return (currentNumOfBlocks == 0);
	}
	
	private boolean isDead(){
		return (lives == 0);
	}
}

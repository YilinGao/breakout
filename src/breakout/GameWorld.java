package breakout;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
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
	private static final String title = "Breakout";
	private static final int framesPerSecond = 60;
	
	private Timeline timeline; 
	private double millisecondDelay;
	private double secondDelay;
	
	public static final int WIDTH = 420;
	public static final int HEIGHT = 600;	
	public static final Paint BACKGROUND = Color.WHITE;
	
	private Stage theStage;
	private Group rootWelcome, rootLevel, rootResult;
	private Scene currentScene;
	private Scene welcomeScene, levelScene, resultScene;
	
	private Button start;
	private Text gameTitle;
	private Text instructions;
	private Text remainingLives;
	private Text currentLevel;
	private Text currentScore;
	private Paddle paddle;
	private ArrayList<Ball> balls = new ArrayList<Ball>();
	private int numOfBricks, currentNumOfBricks;
	private ArrayList<Block> bricks = new ArrayList<Block>();
	
	private ArrayList<Integer> levelLayout = new ArrayList<>();
	
	private boolean started = false;
	private boolean paused = false;
	private int lives = 3;
	private int level = 1;
	private int score = 0;
	
	private static final String levelOneInput = "level1.txt";
	private static final String levelTwoInput = "level2.txt";
	private static final String levelThreeInput = "level3.txt";
	private static final String BRICK1_IMAGE= "brick7.gif";
	private static final String BRICK2_IMAGE = "brick8.gif";
	private static final String BRICK3_IMAGE = "brick9.gif";

	/**
	 * constructor of the class GameWorld
	 * @param fps: number of frames per second
	 * @param t: title of the stage
	 */
	public GameWorld() {
		millisecondDelay = 1000.0 / framesPerSecond;
		secondDelay = 1.0 / framesPerSecond;
	}

	/**
	 * Build up stage, scene, and nodes in the scene
	 * @param stage: primaryStage in Application's start() method
	 */
	public void initializeWelcome(Stage stage){
		theStage = stage;
		
		rootWelcome = new Group();
		welcomeScene = new Scene(rootWelcome, WIDTH, HEIGHT, BACKGROUND);
		
		gameTitle = new Text(50, 200, "Breakout\nBy Sandy");			
		instructions = new Text(50, 300, "Press SPACE to start.\nUse \"<-\" and \"->\" to control the paddle.\nYou have 3 lives to start.");
		start = new Button("Click to start game!");
		start.setLayoutX(50);
		start.setLayoutY(400);
		start.setOnMouseReleased(e -> initializeLevel(1));
		rootWelcome.getChildren().add(gameTitle);
		rootWelcome.getChildren().add(instructions);
		rootWelcome.getChildren().add(start);
		stage.setScene(welcomeScene);
		currentScene = welcomeScene;
		stage.setTitle(title);
		stage.show();
	}
	
	public void initializeLevel(int targetLevel){
		level = targetLevel;
		rootLevel = new Group();
		levelScene = new Scene(rootLevel, WIDTH, HEIGHT, BACKGROUND);
		
		switch (level) {
		case 1:
			readInput(levelOneInput, levelLayout);
			break;
		case 2:
			readInput(levelTwoInput, levelLayout);
			break;
		case 3:
			readInput(levelThreeInput, levelLayout);
			break;
		default:
			readInput(levelOneInput, levelLayout);
			break;
		}

		setupFixedItems();
		setupMovableItems(levelLayout);
		addNodesToRoot(rootLevel);		
		theStage.setScene(levelScene);
		currentScene = levelScene;
		currentScene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.SPACE) {
					if (!started)
						started = true;
					else
						paused = !paused;
				}
			}
			
		});
		buildAndSetGameLoop();
		beginGameLoop();
	}
	
	private void initializeResultPage(){
		stopGameLoop();
		
		rootResult = new Group();
		resultScene = new Scene(rootResult, WIDTH, HEIGHT, BACKGROUND);
		
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
		theStage.setScene(resultScene);
		currentScene = resultScene;
	}
	
	private void readInput(String inputPath, ArrayList<Integer> outputList){
		File file = new File(inputPath);
		Scanner sc = null;
		try{
			sc = new Scanner(file);
			while (sc.hasNextLine()){
				outputList.add(sc.nextInt());
			}
			sc.close();
		}
		catch (Exception e){
			e.printStackTrace();
		} finally{
			if (sc != null) sc.close();
		}
	}

	private void setupFixedItems(){
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
	
	private void setupMovableItems(ArrayList<Integer> bricksLayout){
		started = false;

		Ball newBall = new Ball();		
		paddle = new Paddle();
		
		newBall.getBall().setX(WIDTH / 2 - newBall.getBall().getBoundsInParent().getWidth() / 2);
		newBall.getBall().setY(480);
		newBall.ballReset();
		balls.clear();
		balls.add(newBall);

		paddle.getPaddle().setX(WIDTH / 2 - paddle.getPaddle().getBoundsInParent().getWidth() / 2);
		paddle.getPaddle().setY(newBall.getBall().getBoundsInParent().getMaxY());
		
		setupBricks(bricksLayout);		
	}
	
	private void setupBricks(ArrayList<Integer> bricksLayout){
		int currentLayer = 1;
		Random rd = new Random();
		double type;
		String path;
		Block newBrick;
		double xPosition = WIDTH / 2;
		double yPosition = 100;
		
		for (int num : bricksLayout){
			for (int i = 0; i < num; i++){
				type = rd.nextDouble();
				if (type < 0.5)
					path = BRICK1_IMAGE;
				else if (type < 0.8)
					path = BRICK2_IMAGE;
				else
					path = BRICK3_IMAGE;
				newBrick = new Block(path, currentLayer, i, num); 
				newBrick.setPosition(xPosition, yPosition);
				bricks.add(newBrick);
				numOfBricks++;
			}
			currentLayer++;
		}
		currentNumOfBricks = numOfBricks;
	}
	
	private void addNodesToRoot(Group root){
		root.getChildren().clear();
		for (Ball ball: balls){
			root.getChildren().add(ball.getBall());
		}
		root.getChildren().add(paddle.getPaddle());				
		root.getChildren().add(currentScore);		
		root.getChildren().add(currentLevel);		
		root.getChildren().add(remainingLives);
		for (Block brick: bricks){
			root.getChildren().add(brick.getBlock());
		}
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
		if (started & !paused){
			for (Ball ball: balls)
				ball.ballMove(elapsedTime);
			ballBounceOnPaddle();
			ballBounceOnWalls();
//			ballHitBlock();
			ballFallDown();
		}
		currentScene.setOnKeyPressed(e -> handleKeyInput(e.getCode(), elapsedTime));
	}

	private void handleKeyInput(KeyCode code, double elapsedTime){
		int direction = 0;

		if (code == KeyCode.LEFT){
			direction = -1;
		}
		else if (code == KeyCode.RIGHT){
			direction = 1;
		}
		
		switch (paddleBounceOnWalls()) {
		case 1:
			if (-1 == direction)
				direction = 0;
			break;
		case 2:
			if (1 == direction)
				direction = 0;
			break;
		}
		
		if (!started)
			for (Ball ball : balls)
				ball.updateBall(direction, elapsedTime);
		if (!paused)
			paddle.updatePaddle(direction, elapsedTime);
	}
	
	private int paddleBounceOnWalls() {
		double paddleMinX = paddle.getPaddle().getBoundsInParent().getMinX();
		double paddleMaxX = paddle.getPaddle().getBoundsInParent().getMaxX();
		
		if (paddleMinX < 0)
			return 1;
		else if (paddleMaxX > WIDTH)
			return 2;
		return 0;
	}
	
	private void ballBounceOnPaddle(){
		for (Ball ball: balls){
			double ballMinX = ball.getBall().getBoundsInParent().getMinX();
			double ballCenterX = ballMinX + ball.getBall().getBoundsInParent().getWidth() / 2;
			double paddleMinX = paddle.getPaddle().getBoundsInParent().getMinX();
			double paddleMaxX = paddle.getPaddle().getBoundsInParent().getMaxX();
			double ballMaxY = ball.getBall().getBoundsInParent().getMaxY();
			double paddleMinY = paddle.getPaddle().getBoundsInParent().getMinY();
			double paddleOneThirdsX = paddleMinX + paddle.getPaddle().getBoundsInParent().getWidth() / 3;
			double paddleTwoThirdsX = paddleMinX + paddle.getPaddle().getBoundsInParent().getWidth() * 2/3;
	//		System.out.println("ballMaxY: " + ballMaxY + ", paddleMinY: " + paddleMinY);
	//		System.out.println("paddleMinX: " + paddleMinX + ", paddleMaxX: " + paddleMaxX);
	//		System.out.println("ballCenterX: " + ballCenterX  + "\n");
			if (ballMaxY >= paddleMinY){
				if ((ballCenterX >= paddleMinX && ballCenterX <= paddleOneThirdsX) || (ballCenterX <= paddleMaxX && ballCenterX >= paddleTwoThirdsX)){
					ball.ballBounceHorizontal();
					ball.ballBounceVertical();
				}
				else if (ballCenterX > paddleOneThirdsX && ballCenterX < paddleTwoThirdsX){
					ball.ballBounceVertical();
				}
			}
		}
	}
	
	private void ballBounceOnWalls(){
		for (Ball ball: balls){
			double ballMinX = ball.getBall().getBoundsInParent().getMinX();
			double ballMaxX = ball.getBall().getBoundsInParent().getMaxX();
			double ballMinY = ball.getBall().getBoundsInParent().getMinY();
			if (ballMinX < 0 || ballMaxX > WIDTH)
				ball.ballBounceHorizontal();
			else if (ballMinY < 0)
				ball.ballBounceVertical();
		}
	}
	
	private void ballHitBlock(){
		// TODO
		if (isLevelEnd()){
			if (level == 1)
				initializeLevel(2);
		}
	}
	
	private void dropPowerup(){
		// TODO
	}
	
	private void ballFallDown(){
		for (Ball ball: balls){
			double ballMinX = ball.getBall().getBoundsInParent().getMinX();
			double ballCenterX = ballMinX + ball.getBall().getBoundsInParent().getWidth() / 2;
			double paddleMinX = paddle.getPaddle().getBoundsInParent().getMinX();
			double paddleMaxX = paddle.getPaddle().getBoundsInParent().getMaxX();
			double ballMaxY = ball.getBall().getBoundsInParent().getMaxY();
			double paddleMinY = paddle.getPaddle().getBoundsInParent().getMinY();
			if (ballMaxY >= paddleMinY && (ballCenterX < paddleMinX || ballCenterX > paddleMaxX)){
				endOfLife();
			}
		}
	}
	
	private void endOfLife(){
		lives--;
		if (!isDead()){
			remainingLives.setText("Remaining lives: " + lives);
			
			setupFixedItems();
			setupMovableItems(levelLayout);
			addNodesToRoot(rootLevel);	
		}
		else{
			initializeResultPage();
		}
	}

	private boolean isLevelEnd(){
		return (currentNumOfBricks == 0);
	}
	
	private boolean isDead(){
		return (lives == 0);
	}
}

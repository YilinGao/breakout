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
	private static final int framesPerSecond = 120;
	
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
	private Text tips;
	private Text remainingLives;
	private Text currentLevel;
	private Text currentScore;
	private Paddle paddle;
	private ArrayList<Ball> balls = new ArrayList<Ball>();
	private int numOfBricks, currentNumOfBricks;
	private ArrayList<Brick> bricks = new ArrayList<Brick>();
	private ArrayList<Brick> bricksToBeRemoved = new ArrayList<Brick>();
	
	private ArrayList<Integer> levelLayout = new ArrayList<>();
	
	private boolean started = false;
	private int lives = 3;
	private int level;
	private int score = 0;
	
	private static final String levelOneInput = "level1.txt";
	private static final String levelTwoInput = "level2.txt";
	private static final String levelThreeInput = "level3.txt";

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
		
		gameTitle = new Text(50, 100, "Breakout\nBy Sandy");			
		instructions = new Text(50, 200, "Press SPACE to start.\nUse \"<-\" and \"->\" to control the paddle.\nYou have 3 lives to start.");
		tips = new Text(50, 300, "Tips:\nGet control of your paddle!\nGood luck!");
		start = new Button("Click to start game!");
		start.setLayoutX(50);
		start.setLayoutY(400);
		start.setOnMouseReleased(e -> initializeLevel(3));
		rootWelcome.getChildren().add(gameTitle);
		rootWelcome.getChildren().add(instructions);
		rootWelcome.getChildren().add(tips);
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
					started = true;
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
		outputList.clear();
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
		
		newBall.setX(WIDTH / 2 - newBall.getBall().getBoundsInParent().getWidth() / 2);
		newBall.setY(480);
		newBall.ballResetInitialDirection();
		balls.clear();
		balls.add(newBall);

		paddle.setX(WIDTH / 2 - paddle.getPaddle().getBoundsInParent().getWidth() / 2);
		paddle.setY(newBall.getBall().getBoundsInParent().getMaxY());
		
		setupBricks(bricksLayout);		
	}
	
	private void setupBricks(ArrayList<Integer> bricksLayout){
		int currentLayer = 0;
		Random rd = new Random();
		int rn;
		Brick newBrick;
		double xPosition = WIDTH / 2;
		double yPosition = 100;

		numOfBricks = 0;
		currentNumOfBricks = 0;
		bricks.clear();
		for (int totalBricksInLayer : bricksLayout){
			for (int indexInLayer = 0; indexInLayer < totalBricksInLayer; indexInLayer++){
				rn = rd.nextInt(10) + 1;
				newBrick = new Brick(rn, currentLayer, indexInLayer , totalBricksInLayer, numOfBricks++);
				newBrick.setPosition(xPosition, yPosition);
				bricks.add(newBrick);
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
		for (Brick brick: bricks){
			root.getChildren().add(brick.getBrick());
		}
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
		// TODO drop power-ups
		if (started){
			for (Ball ball: balls)
				ball.ballMove(elapsedTime);
			ballBounceOnPaddle();
			ballBounceOnWalls();
			ballHitOnAllBricks();
			ballFallDown();			
			if (isLevelEnd()){
				initializeLevel(++level);
			}
		}
		for (Brick brick: bricks){
			brick.brickMove(elapsedTime);
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
		paddle.paddleMove(direction, elapsedTime);
		if (!started)
			for (Ball ball : balls)
				ball.ballMoveWithPaddle(paddle);
	}
	
	private void ballBounceOnPaddle(){
		for (Ball ball: balls){
			double ballCenterX = ball.getX() + ball.getWidth() / 2;
			double paddleMinX = paddle.getX();
			double paddleMaxX = paddle.getX() + paddle.getWidth();
			double ballMaxY = ball.getY() + ball.getHeight();
			double paddleMinY = paddle.getY();
			double paddleOneThirdsX = paddle.getX() + paddle.getWidth() / 3;
			double paddleTwoThirdsX = paddle.getX() + paddle.getWidth() * 2 / 3;
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
			double ballMinX = ball.getX();
			double ballMaxX = ball.getX() + ball.getWidth();
			double ballMinY = ball.getY();
			double ballMaxY = ball.getY() + ball.getHeight();
			System.out.println("ballMinX: " + ballMinX + ", ballMaxX: " + ballMaxX + ", ballMinY: " + ballMinY + ", ballMaxY: " + ballMaxY);
			if ((ballMinX < 0 && ballMaxX > 0) || (ballMaxX > WIDTH && ballMinX < WIDTH))
				ball.ballBounceHorizontal();
			else if (ballMinY < 0 && ballMaxY > 0)
				ball.ballBounceVertical();
		}
	}

	private void ballHitOnAllBricks() {
		bricksToBeRemoved.clear();
		for (Brick brick: bricks){
			for (Ball ball: balls){
				ballHitBrick(ball, brick);
			}
		}
		for (Brick brick: bricks){
			if (brick.getRemovalMark()){
				bricksToBeRemoved.add(brick);
				rootLevel.getChildren().remove(brick.getBrick());
				currentNumOfBricks--;
				System.out.println("brick " + brick.getIndex() + " is added into the array. There are " + currentNumOfBricks + " bricks.");
			}
		}
		bricks.removeAll(bricksToBeRemoved);
	}
	
	private void ballHitBrick(Ball ball, Brick brick){
		int ballDirectionHorizontal = ball.getDirectionHorizontal();
		int ballDirectionVertical = ball.getDirectionVertical();
		double ballMinX = ball.getX();
		double ballMaxX = ball.getX() + ball.getWidth();
		double ballMinY = ball.getY();
		double ballMaxY = ball.getY() + ball.getHeight();
		double brickMinX = brick.getX();
		double brickMaxX = brick.getX() + brick.getWidth();
		double brickMinY = brick.getY();
		double brickMaxY = brick.getY() + brick.getHeight();
		
		// check for hit on the upper edge
		if (ballDirectionVertical > 0 && ballMaxY >= brickMinY && ballMinY <= brickMaxY && ballMaxX <= brickMaxX && ballMinX >= brickMinX){
			int type = brick.hitBrick();
			increaseScore(type);
			ball.ballBounceVertical();
		}
		// check for hit on the lower edge
		else if (ballDirectionVertical < 0 && ballMinY <= brickMaxY && ballMaxY >= brickMaxY && ballMaxX <= brickMaxX && ballMinX >= brickMinX){
			int type = brick.hitBrick();
			increaseScore(type);
			ball.ballBounceVertical();
		}
		// check for hit on the left edge
		else if (ballDirectionHorizontal > 0 && ballMaxX >= brickMinX && ballMinX <= brickMinX && ballMaxY <= brickMaxY && ballMinY >= brickMinY){
			int type = brick.hitBrick();
			increaseScore(type);
			ball.ballBounceHorizontal();
		}
		// check for hit on the left edge
		else if (ballDirectionHorizontal < 0 && ballMinX <= brickMaxX && ballMaxX >= brickMaxX && ballMaxY <= brickMaxY && ballMinY >= brickMinY){
			int type = brick.hitBrick();
			increaseScore(type);
			ball.ballBounceHorizontal();
		}
	}
	
	private void increaseScore(int type){
		switch (type){
		case 1:
			score += 100;
			break;
		case 2:
			score += 200;
			break;
		case 3:
			score += 300;
			break;
		}
		currentScore.setText("Current Score: "+ score);
	}
	
	private void dropPowerup(){
		// TODO
	}
	
	private void ballFallDown(){
		for (Ball ball: balls){
			double ballCenterX = ball.getX() + ball.getWidth() / 2;
			double paddleMinX = paddle.getX();
			double paddleMaxX = paddle.getX() + paddle.getWidth();
			double ballMaxY = ball.getY() + ball.getHeight();
			double paddleMinY = paddle.getY();
			if (ballMaxY >= paddleMinY && (ballCenterX < paddleMinX || ballCenterX > paddleMaxX)){
				endOfLife();
			}
		}
	}
	
	private void endOfLife(){
		lives--;
		score = 0;
		if (!isDead()){
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

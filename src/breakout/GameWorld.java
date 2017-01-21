package breakout;

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
	private long numOfStickyFrames = 0;
	
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
	private Text resultReport;
	private Paddle paddle;
	private ArrayList<Ball> balls = new ArrayList<Ball>();
	private ArrayList<Ball> ballsToBeAdded = new ArrayList<Ball>();
	private ArrayList<Ball> ballsToBeRemoved = new ArrayList<Ball>();
	private int numOfBricks;
	private ArrayList<Brick> bricks = new ArrayList<Brick>();
	private ArrayList<Brick> bricksToBeRemoved = new ArrayList<Brick>();
	private ArrayList<Powerup> powerups = new ArrayList<Powerup>();
	private ArrayList<Powerup> powerupsToBeRemoved = new ArrayList<Powerup>();
	
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
		start.setOnMouseReleased(e -> initializeLevel(1));
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
		chooseAndReadInput();
		setupFixedItems();
		setupMovableItems(levelLayout);
		addNodesToRoot(rootLevel);		
		theStage.setScene(levelScene);
		currentScene = levelScene;
		currentScene.setOnKeyReleased(e -> handleKeyInputsScene(e));
		buildAndSetGameLoop();
		beginGameLoop();
	}

	private void initializeResultPage(){
		rootResult = new Group();
		resultScene = new Scene(rootResult, WIDTH, HEIGHT, BACKGROUND);
		
		resultReport = new Text();
		if (isDead()) {
			resultReport.setText("Sorry you lose :(");
		}
		else{
			resultReport.setText("Wow you win! :)\nYour score is " + score + ".");
		}
		resultReport.setX(50);
		resultReport.setY(300);
		resetGameArguments();
		start.setText("Have another try?");
		start.setOnMouseReleased(e -> initializeLevel(1));
		rootResult.getChildren().add(resultReport);
		rootResult.getChildren().add(start);
		theStage.setScene(resultScene);
		currentScene = resultScene;
	}

	private void resetGameArguments() {
		lives = 3;
		score = 0;
	}

	private void chooseAndReadInput() {
		switch (level) {
		case 1:
			readFileInput(levelOneInput, levelLayout);
			break;
		case 2:
			readFileInput(levelTwoInput, levelLayout);
			break;
		case 3:
			readFileInput(levelThreeInput, levelLayout);
			break;
		default:
			readFileInput(levelOneInput, levelLayout);
			break;
		}
	}
		
	private void readFileInput(String inputPath, ArrayList<Integer> outputList){
		outputList.clear();
		Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream(inputPath));
		try{
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
	
	private void handleKeyInputsScene(KeyEvent event){
		if (event.getCode() == KeyCode.SPACE) {
			started = true;
		}
		else if (event.getCode() == KeyCode.R){
			stopGameLoop();
			score = 0;
			initializeLevel(level);
		}
		else if (event.getCode() == KeyCode.B){
			paddle.setX(WIDTH / 2 - paddle.getWidth() / 2); 
			for (Ball ball: balls){
				if (!started || ball.getSticked())
					ball.ballMoveWithPaddle(paddle);
			}
		}
		else if (event.getCode() == KeyCode.DIGIT1){
			stopGameLoop();
			score = 0;
			initializeLevel(1);
		}
		else if (event.getCode() == KeyCode.DIGIT2){
			stopGameLoop();
			score = 0;
			initializeLevel(2);
		}
		else if (event.getCode() == KeyCode.DIGIT3){
			stopGameLoop();
			score = 0;
			initializeLevel(3);
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
		if (started){
			for (Ball ball: balls){
				if (!ball.getSticked()){
					ball.ballMove(elapsedTime);		
				}
				else{
					numOfStickyFrames++;
					if (numOfStickyFrames == 3 * framesPerSecond){   // the paddle sticks the ball for 3 seconds
						ball.ballMove(elapsedTime);	
						ball.setSticked(false);
						numOfStickyFrames = 0;
					}
				}
			}
			for (Powerup powerup: powerups)
				powerup.powerupMove(elapsedTime);
			ballBounceOnPaddle();
			ballBounceOnWalls();
			ballHitOnAllBricks();
			ballFallDown();	
			powerupHitPaddle();
			levelEnd();
		}
		if (level == 3){
			for (Brick brick: bricks){
				brick.brickMove(elapsedTime);
			}
		}
		currentScene.setOnKeyPressed(e -> handleKeyInputsFrame(e.getCode(), elapsedTime));
	}

	private void handleKeyInputsFrame(KeyCode code, double elapsedTime){
		int direction = 0;
		if (code == KeyCode.LEFT){
			direction = -1;
		}
		else if (code == KeyCode.RIGHT){
			direction = 1;
		}			
		paddle.paddleMove(direction, elapsedTime);
		for (Ball ball : balls)
			if (!started || (started && ball.getSticked()))
				ball.ballMoveWithPaddle(paddle);
	}
	
	private void ballBounceOnPaddle(){
		for (Ball ball: balls){
			double ballCenterX = ball.getX() + ball.getWidth() / 2;
			double paddleMinX = paddle.getX();
			double paddleMaxX = paddle.getX() + paddle.getWidth();
			double ballMinY = ball.getY();
			double ballMaxY = ball.getY() + ball.getHeight();
			double paddleMinY = paddle.getY();
			double paddleOneThirdsX = paddle.getX() + paddle.getWidth() / 3;
			double paddleTwoThirdsX = paddle.getX() + paddle.getWidth() * 2 / 3;
			if (ballMaxY >= paddleMinY && ballMinY <= paddleMinY){
				if ((ballCenterX >= paddleMinX && ballCenterX <= paddleOneThirdsX) || (ballCenterX <= paddleMaxX && ballCenterX >= paddleTwoThirdsX)){				
					if (!paddle.getSticky()){
						ball.ballBounceVertical();
					}
					else{
						ball.setSticked(true);
						paddle.setSticky(false);
					}
				}
				else if (ballCenterX > paddleOneThirdsX && ballCenterX < paddleTwoThirdsX){
					if (!paddle.getSticky()){
						ball.ballBounceVertical();
						ball.ballBounceHorizontal();
					}
					else{
						ball.setSticked(true);
						paddle.setSticky(false);
					}
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
			dropPowerup(brick);
		}
		// check for hit on the lower edge
		else if (ballDirectionVertical < 0 && ballMinY <= brickMaxY && ballMaxY >= brickMaxY && ballMaxX <= brickMaxX && ballMinX >= brickMinX){
			int type = brick.hitBrick();
			increaseScore(type);
			ball.ballBounceVertical();
			dropPowerup(brick);
		}
		// check for hit on the left edge
		else if (ballDirectionHorizontal > 0 && ballMaxX >= brickMinX && ballMinX <= brickMinX && ballMaxY <= brickMaxY && ballMinY >= brickMinY){
			int type = brick.hitBrick();
			increaseScore(type);
			ball.ballBounceHorizontal();
			dropPowerup(brick);
		}
		// check for hit on the left edge
		else if (ballDirectionHorizontal < 0 && ballMinX <= brickMaxX && ballMaxX >= brickMaxX && ballMaxY <= brickMaxY && ballMinY >= brickMinY){
			int type = brick.hitBrick();
			increaseScore(type);
			ball.ballBounceHorizontal();
			dropPowerup(brick);
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
	
	private void dropPowerup(Brick brick){
		Random rn = new Random();
		double indicator = rn.nextDouble();
		int type = rn.nextInt(4) + 1;
		if (indicator <= 0.3){
			Powerup newPowerup = new Powerup(type, brick);
			powerups.add(newPowerup);
			rootLevel.getChildren().add(newPowerup.getPowerup());
		}
	}
	
	private void ballFallDown(){
		for (Ball ball: balls){
			double ballMinY = ball.getY();
			if (ballMinY >= HEIGHT){
				ball.setRemovalMark();
				rootLevel.getChildren().remove(ball.getBall());
			}
		}
		for (Ball ball: balls){
			if (ball.getRemovalMark())
				ballsToBeRemoved.add(ball);
		}
		balls.removeAll(ballsToBeRemoved);
		if (balls.size() == 0){
			endOfLife();
		}
	}
	
	private void powerupHitPaddle() {
		for (Powerup powerup: powerups){
			double powerupCenterX = powerup.getX() + powerup.getWidth() / 2;
			double powerupMinY = powerup.getY();
			double powerupMaxY = powerup.getY() + powerup.getHeight();
			double paddleMinX = paddle.getX();
			double paddleMaxX = paddle.getX() + paddle.getWidth();
			double paddleMinY = paddle.getY();
			if (powerupMaxY >= paddleMinY && powerupMinY <= paddleMinY){
				if (powerupCenterX >= paddleMinX && powerupCenterX <= paddleMaxX){
					powerupEffect(powerup);
				}
			}
			else if (powerupMinY >= HEIGHT){
				powerup.setRemovalMark();
			}
		}
		for (Powerup powerup: powerups){
			if (powerup.getRemovalMark()){
				rootLevel.getChildren().remove(powerup.getPowerup());
				powerupsToBeRemoved.add(powerup);
			}
		}
		powerups.removeAll(powerupsToBeRemoved);
	}
	
	private void powerupEffect(Powerup powerup) {
		powerup.setRemovalMark();
		switch (powerup.getType()){
		case 1:
			speedUpBall(1.2);
			break;
		case 2:
			splitBall(3);
			break;
		case 3:
			stickyPaddle(3);
			break;
		case 4:
			increaseLife(1);
			break;
		}
	}

	private void increaseLife(int i) {
		lives++;
		remainingLives.setText("Remaining lives: " + lives);
	}

	private void stickyPaddle(int i) {
		paddle.setSticky(true);
	}

	private void splitBall(int number) {
		for (Ball ball: balls){
			for (int i = 1; i < number; i++){
				Ball newBall = new Ball();
				newBall.setX(ball.getX());
				newBall.setY(ball.getY());
				ballsToBeAdded.add(newBall);
				rootLevel.getChildren().add(newBall.getBall());
			}
		}
		balls.addAll(ballsToBeAdded);
	}

	private void speedUpBall(double times) {
		for (Ball ball: balls){
			ball.setSpeed(times);
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
			stopGameLoop();
			initializeResultPage();
		}
	}

	private void levelEnd() {
		if (isLevelEnd()){
			stopGameLoop();
			if (level < 3)
				initializeLevel(++level);
			else
				initializeResultPage();
		}
	}
	
	private boolean isLevelEnd(){
		return (bricks.size() == 0);
	}
	
	private boolean isDead(){
		return (lives == 0);
	}
}

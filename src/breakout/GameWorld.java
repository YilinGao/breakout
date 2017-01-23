package breakout;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import collisions.Collision;
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
import spirits.Ball;
import spirits.Brick;
import spirits.Paddle;
import spirits.Powerup;

/**
 * the controller of the program, running game loops, managing items on each scene,
 * depends on collisions.Collision, spirits.Ball, spirits.Brick, spirits.Paddle, spirits.Powerup
 * @author Yilin Gao
 *
 */
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
	private Scene welcomeScene, levelScene, resultScene;
	
	private Collision collisionDetector = new Collision(this);
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
	private ArrayList<Brick> bricks = new ArrayList<Brick>();
	private ArrayList<Powerup> powerups = new ArrayList<Powerup>();
	
	private ArrayList<Integer> levelLayout = new ArrayList<>();
	
	private boolean started = false;
	private boolean paused = false;
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
	
	public Group getRootLevel(){
		return rootLevel;
	}
	
	public ArrayList<Ball> getBalls(){
		return balls;
	}
	public Paddle getPaddle(){
		return paddle;
	}
	
	public ArrayList<Powerup> getPowerups(){
		return powerups;
	}
	
	public int getScore(){
		return score;
	}
	
	public int getLives(){
		return lives;
	}
	
	public void setScore(int newScore){
		score = newScore;
	}
	
	public void setLives(int newLives){
		lives = newLives;
	}
	
	public void setCurrentScore(String text){
		currentScore.setText(text);
	}
	
	public void setRemainingLives(String text){
		remainingLives.setText(text);
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
		start = new Button("Click or press SPACE to start game!");
		start.setLayoutX(50);
		start.setLayoutY(400);
		start.setOnMouseReleased(e -> initializeLevel(1));
		start.setOnKeyReleased(e -> handleStartGameKeyInputs(e));
		rootWelcome.getChildren().add(gameTitle);
		rootWelcome.getChildren().add(instructions);
		rootWelcome.getChildren().add(tips);
		rootWelcome.getChildren().add(start);
		stage.setScene(welcomeScene);
		stage.setTitle(title);
		stage.show();
	}
	
	/**
	 * initialize a certain level scene
	 * @param targetLevel: the level to be initialized
	 */
	public void initializeLevel(int targetLevel){
		level = targetLevel;
		rootLevel = new Group();
		levelScene = new Scene(rootLevel, WIDTH, HEIGHT, BACKGROUND);
		chooseAndReadInput();
		setupFixedItems();
		setupMovableItems();
		setupBricks(levelLayout);
		addNodesToRoot(rootLevel);		
		theStage.setScene(levelScene);
		levelScene.setOnKeyReleased(e -> handleKeyInputsScene(e));
		buildAndSetGameLoop();
		beginGameLoop();
	}

	/**
	 * initialize the result scene
	 */
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
		start.setText("Have another try? Click or press SPACE.");
		start.setOnMouseReleased(e -> initializeLevel(1));
		start.setOnKeyReleased(e -> handleStartGameKeyInputs(e));
		rootResult.getChildren().add(resultReport);
		rootResult.getChildren().add(start);
		theStage.setScene(resultScene);
	}
	
	private void handleStartGameKeyInputs(KeyEvent event){
		if (event.getCode() == KeyCode.SPACE) {
			initializeLevel(1);
		}
	}

	private void resetGameArguments() {
		lives = 3;
		score = 0;
	}

	/**
	 * read text file inputs as brick layouts
	 */
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
	
	/**
	 * implement detailed file input reading procedure
	 * @param inputPath: the String variable of the file path
	 * @param outputList: the ArrayList to store file contents
	 */
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

	/**
	 * set up status display: score, level, lives
	 */
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
	
	/**
	 * set up balls, paddle,
	 * call setupBricks()
	 */
	private void setupMovableItems(){
		started = false;

		Ball newBall = new Ball();		
		paddle = new Paddle();
		
		newBall.setX(WIDTH / 2 - newBall.getBall().getBoundsInParent().getWidth() / 2);
		newBall.setY(480);
		balls.clear();
		balls.add(newBall);

		paddle.setX(WIDTH / 2 - paddle.getPaddle().getBoundsInParent().getWidth() / 2);
		paddle.setY(newBall.getBall().getBoundsInParent().getMaxY());
	}
	
	/**
	 * set up bricks
	 * @param bricksLayout: the brick layout read from text file input
	 */
	private void setupBricks(ArrayList<Integer> bricksLayout){
		int currentLayer = 0;
		Random rd = new Random();
		int rn;
		Brick newBrick;
		double xPosition = WIDTH / 2;
		double yPosition = 100;

		bricks.clear();
		for (int totalBricksInLayer : bricksLayout){
			for (int indexInLayer = 0; indexInLayer < totalBricksInLayer; indexInLayer++){
				rn = rd.nextInt(10) + 1;
				newBrick = new Brick(rn, currentLayer, indexInLayer , totalBricksInLayer);
				newBrick.setPosition(xPosition, yPosition);
				bricks.add(newBrick);
			}
			currentLayer++;
		}
	}
	
	/**
	 * add all nodes to the scene's root
	 * @param root: the root for the scene, a Group class variable
	 */
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
	 * handle key events during each scene
	 * @param event: KeyEvent
	 */
	private void handleKeyInputsScene(KeyEvent event){
		if (event.getCode() == KeyCode.SPACE) {
			if (!started)
				started = true;
			else
				paused = ! paused;
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
	 * set up game loop (timeline),
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
	 * begin game loop (timeline)
	 */
	private void beginGameLoop(){
		timeline.play();
	}
	
	/**
	 * stop game loop (timeline)
	 */
	private void stopGameLoop(){
		timeline.stop();
	}
	
	/**
	 * animations of nodes on the scene during one frame, check level end and move to the next level
	 * @param elapsedTime: the duration of each frame
	 */
	private void actionsPerFrame(double elapsedTime){
		if (started & !paused){
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
			collisionDetector.ballBounceOnPaddle(balls, paddle);
			collisionDetector.ballBounceOnWalls(balls);
			collisionDetector.ballHitAllBricks(balls, bricks);
			collisionDetector.ballFallDown(balls);	
			collisionDetector.powerupHitPaddle(powerups, paddle);
			lifeEnd();
			levelEnd();
			if (level == 3){
				for (Brick brick: bricks){
					brick.brickMove(elapsedTime);
				}
			}
		}		
		levelScene.setOnKeyPressed(e -> handleKeyInputsFrame(e.getCode(), elapsedTime));
	}

	/**
	 * handle key events in each fram
	 * @param code: KeyEvent's KeyCode
	 * @param elapsedTime: the duration of each frame
	 */
	private void handleKeyInputsFrame(KeyCode code, double elapsedTime){
		int direction = 0;
		if (code == KeyCode.LEFT){
			direction = -1;
		}
		else if (code == KeyCode.RIGHT){
			direction = 1;
		}			
		
		if (!paused) {
			paddle.paddleMove(direction, elapsedTime);
			for (Ball ball : balls)
				if (!started || (started && ball.getSticked()))
					ball.ballMoveWithPaddle(paddle);
		}
	}

	/**
	 * call isLifeEnd() to test if during this frame, the current life is dead,
	 * if so, call actionsLifeEnd() to do relevant actions
	 */
	private void lifeEnd() {
		if (isLifeEnd()){
			actionsLifeEnd();
		}
	}

	/**
	 * test if the current life is dead
	 * @return true or false
	 */
	private boolean isLifeEnd() {
		return (0 == balls.size());
	}
	
	/**
	 * actions to perform when the current life is dead
	 */
	private void actionsLifeEnd(){
		lives--;
		score = 0;
		if (!isDead()){
			setupFixedItems();
			setupMovableItems();
			addNodesToRoot(rootLevel);
		}
		else{
			stopGameLoop();
			initializeResultPage();
		}
	}

	/**
	 * call isLevelEnd() to test if during this frame, the current level has finished,
	 * if so, call actionsLevelEnd() to do relevant actions
	 */
	private void levelEnd() {
		if (isLevelEnd()){
			actionsLevelEnd();
		}
	}
	
	/**
	 * test if the current level has finished
	 * @return true or false
	 */
	private boolean isLevelEnd(){
		return (0 == bricks.size());
	}
	
	/**
	 * actions to perform when the current level has finished
	 */
	private void actionsLevelEnd() {
		if (level < 3){
			stopGameLoop();
			initializeLevel(++level);
		}
		else{
			stopGameLoop();
			initializeResultPage();
		}
	}

	/**
	 * test if the player is dead
	 * @return true or false
	 */
	private boolean isDead(){
		return (lives == 0);
	}
}

package pages;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import breakout.GameWorld;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import spirits.Ball;
import spirits.Brick;
import spirits.Paddle;
import spirits.Powerup;

/**
 * A subclass of the class Page.
 * Implement Scene for each level.
 * Depends on spirits.Ball, spirits.Brick, spirits.Paddle, spirits.Powerup.
 * @author Yilin Gao
 *
 */
public class LevelPage extends Page {
	
	private Text remainingLives;
	private Text currentLevel;
	private Text currentScore;
	private Paddle paddle = new Paddle();
	private ArrayList<Ball> balls = new ArrayList<Ball>();
	private ArrayList<Brick> bricks = new ArrayList<Brick>();
	private ArrayList<Powerup> powerups = new ArrayList<Powerup>();
	
	private ArrayList<Integer> levelLayout = new ArrayList<>();
	
	private boolean started = false;
	private boolean paused = false;
	
	/**
	 * Constructor of the LevelPage class.
	 * @param theStage
	 * @param theGameWorld
	 */
	public LevelPage(Stage theStage, GameWorld theGameWorld){
		super(theStage, theGameWorld);
	}
	
	public Paddle getPaddle(){
		return paddle;
	}
	
	public ArrayList<Ball> getBalls(){
		return balls;
	}
	
	public ArrayList<Brick> getBricks(){
		return bricks;
	}
	
	public ArrayList<Powerup> getPowerups(){
		return powerups;
	}
	
	public boolean getStarted(){
		return started;
	}
	
	public boolean getPaused(){
		return paused;
	}
	
	public void setCurrentScore(String text){
		currentScore.setText(text);
	}
	
	public void setRemainingLives(String text){
		remainingLives.setText(text);
	}
	
	@Override
	public void initializePage() {
		chooseAndReadInput();
		setupFixedItems();
		setupMovableItems();
		setupBricks();
		addNodesToRoot();		
		this.getStage().setScene(this.getScene());
		this.getScene().setOnKeyReleased(e -> handleKeyInput(e));
		this.getGameWorld().buildAndSetGameLoop();
		this.getGameWorld().beginGameLoop();
	}

	@Override
	protected void handleKeyInput(KeyEvent event) {
		if (event.getCode() == KeyCode.SPACE) {
			if (!started)
				started = true;
			else
				paused = ! paused;
		}
		else if (event.getCode() == KeyCode.R){
			this.getGameWorld().stopGameLoop();
			this.getGameWorld().setScore(0);
			this.getGameWorld().initializeLevel(this.getGameWorld().getLevel());
		}
		else if (event.getCode() == KeyCode.B){
			paddle.setX(GameWorld.WIDTH / 2 - paddle.getWidth() / 2); 
			for (Ball ball: balls){
				if (!started || ball.getStuck())
					ball.ballMoveWithPaddle(paddle);
			}
		}
		else if (event.getCode() == KeyCode.DIGIT1){
			this.getGameWorld().stopGameLoop();
			this.getGameWorld().setScore(0);
			this.getGameWorld().initializeLevel(1);
		}
		else if (event.getCode() == KeyCode.DIGIT2){
			this.getGameWorld().stopGameLoop();
			this.getGameWorld().setScore(0);
			this.getGameWorld().initializeLevel(2);
		}
		else if (event.getCode() == KeyCode.DIGIT3){
			this.getGameWorld().stopGameLoop();
			this.getGameWorld().setScore(0);
			this.getGameWorld().initializeLevel(3);
		}
	}

	@Override
	protected void handleMouseInput(MouseEvent event) {
		
	}
	
	/**
	 * read text file inputs as brick layouts
	 */
	private void chooseAndReadInput() {
		switch (this.getGameWorld().getLevel()) {
		case 1:
			readFileInput(GameWorld.levelOneInput, levelLayout);
			break;
		case 2:
			readFileInput(GameWorld.levelTwoInput, levelLayout);
			break;
		case 3:
			readFileInput(GameWorld.levelThreeInput, levelLayout);
			break;
		default:
			readFileInput(GameWorld.levelOneInput, levelLayout);
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
	public void setupFixedItems(){
		currentScore = new Text("Current Score: "+ this.getGameWorld().getScore());
		currentScore.setX(30);
		currentScore.setY(50);
		
		currentLevel = new Text("Level: " + this.getGameWorld().getLevel());
		currentLevel.setX(390 - currentLevel.getBoundsInLocal().getWidth());
		currentLevel.setY(50);
		
		remainingLives = new Text("Remaining lives: " + this.getGameWorld().getLives());
		remainingLives.setX(30);
		remainingLives.setY(550);
	}
	
	/**
	 * set up balls, paddle,
	 * call setupBricks()
	 */
	public void setupMovableItems(){
		started = false;

		Ball newBall = new Ball();
		
		newBall.setX(GameWorld.WIDTH / 2 - newBall.getWidth() / 2);
		newBall.setY(480);
		balls.clear();
		balls.add(newBall);

		paddle.setX(GameWorld.WIDTH / 2 - paddle.getWidth() / 2);
		paddle.setY(newBall.getBall().getBoundsInParent().getMaxY());
	}
	
	/**
	 * set up bricks
	 * @param bricksLayout: the brick layout read from text file input
	 */
	private void setupBricks(){
		int currentLayer = 0;
		Random rd = new Random();
		int rn;
		Brick newBrick;
		double xPosition = GameWorld.WIDTH / 2;
		double yPosition = 100;

		bricks.clear();
		for (int totalBricksInLayer : levelLayout){
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
	public void addNodesToRoot(){
		this.getRoot().getChildren().clear();
		for (Ball ball: balls){
			this.getRoot().getChildren().add(ball.getBall());
		}
		this.getRoot().getChildren().add(paddle.getPaddle());	
		for (Brick brick: bricks){
			this.getRoot().getChildren().add(brick.getBrick());
		}			
		this.getRoot().getChildren().add(currentScore);		
		this.getRoot().getChildren().add(currentLevel);		
		this.getRoot().getChildren().add(remainingLives);
	}

}

package breakout;

import java.util.Hashtable;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import pages.LevelPage;
import pages.Page;
import pages.ResultPage;
import pages.WelcomePage;

//This entire file is my masterpiece.
//Yilin Gao

/**
 * The top level class of the program, managing pages, running game loops.
 * Depends on breakout.GameWorldController, collisions.Collision, pages.Page, pages.WelcomePage, pages.LevelPage, pages.ResultPage.
 * @author Yilin Gao
 *
 */
public class GameWorld {
	public static final int WIDTH = 420;
	public static final int HEIGHT = 600;	
	public static final Paint BACKGROUND = Color.WHITE;
	public static final String TITLE = "Breakout";	
	public static final int framesPerSecond = 120;	
	
	private Timeline timeline; 
	private double millisecondDelay;
	private double secondDelay;
	
	private Stage theStage;	
	private Hashtable<Integer, Page> pages = new Hashtable<Integer, Page>();
	private GameWorldController gameWorldController;
	
	private int level = 1;
	private int score = 0;
	private int lives = 3;
	
	public static final String levelOneInput = "level1.txt";
	public static final String levelTwoInput = "level2.txt";
	public static final String levelThreeInput = "level3.txt";

	/**
	 * Constructor of the class GameWorld.
	 */
	public GameWorld() {
		millisecondDelay = 1000.0 / framesPerSecond;
		secondDelay = 1.0 / framesPerSecond;
	}
	
	public GameWorldController getController(){
		return gameWorldController;
	}
	
	public Page getPage(int index){
		return pages.get(index);
	}
	
	public int getLevel(){
		return level;
	}
	
	public int getLives(){
		return lives;
	}
	
	public int getScore(){
		return score;
	}	

	public void setLevel(int newLevel){
		level = newLevel;
	}
	
	public void setLives(int newLives){
		lives = newLives;
	}
	
	public void setScore(int newScore){
		score = newScore;
	}
	

	/**
	 * Initialize a welcome page.
	 * @param stage: primaryStage in Application's start() method
	 */
	public void initializeWelcome(Stage stage){
		Page welcomePage = new WelcomePage(stage, this);
		if (pages.contains(0))
			pages.remove(0);
		pages.put(0, welcomePage);
		welcomePage.initializePage();
		theStage = stage;
	}
	
	/**
	 * Initialize a certain level page.
	 * @param targetLevel: the level to be initialized
	 */
	public void initializeLevel(int targetLevel){
		setLevel(targetLevel);
		Page levelPage = new LevelPage(theStage, this);
		if (pages.containsKey(1))
			pages.remove(1);
		pages.put(1, levelPage);
		levelPage.initializePage();
	}

	/**
	 * Initialize a result scene.
	 */
	public void initializeResultPage(){
		Page resultPage = new ResultPage(theStage, this);
		if (pages.containsKey(2))
			pages.remove(2);
		pages.put(2, resultPage);
		resultPage.initializePage();
	}
	
	/**
	 * Set up game loop (time line).
	 * call actionsPerFrame(double elapsedTime) in breakout.GameWorldController to handle animations of nodes during each frame.
	 */
	public void buildAndSetGameLoop(){
		gameWorldController = new GameWorldController(this);
		Duration oneFrameDuration = Duration.millis(millisecondDelay);
		KeyFrame oneFrame = new KeyFrame(oneFrameDuration, new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				gameWorldController.actionsPerFrame(secondDelay);
			}
			
		});
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(oneFrame);
	}
	
	/**
	 * Begin game loop (time line).
	 */
	public void beginGameLoop(){
		timeline.play();
	}
	
	/**
	 * Stop game loop (time line).
	 */
	public void stopGameLoop(){
		timeline.stop();
	}
}

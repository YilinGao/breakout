# Design Analysis of Breakout
----------
## Project Design Goals

 - For game design

	 - Create a variant of the classic Breakout game, mainly as a
	   combination of interesting traits taken from several existing
	   variants. 
	 - Make all items, animations, and game logic as real as possible. Avoid obvious violations of real-world rules of mathematics and physics.

 - For code design

	 - Create one class for every kind of item or action (including the game setup, collision detection, and each item), to avoid long and messy classes.
	 - Create methods for different functions. Try my best to realize one function in one method.
	 - Encapsulate private member variables under public methods. Avoid getting access to or making changes to private variables directly.

## Future Improvements Guidance

As far as stated in the `README.md` file, there are 3 meaningful future improvement directions, which are to add ball speed modification,  to refine items representation, and to add mouse control.

 - Add ball speed minor modification
 
   Currently during the life of a ball, its direction is "hard-coded" by initial horizontal and vertical speeds set up, and no matter how it bounces the direction won't change accordingly. The most ideal and authentic situation is that whenever a ball bounces onto the paddle, its speed can change as the position of collision on the paddle differs, so that players can better get better control game results.

   To make this happen, a method `ballBounces(Ball ball, Paddle paddle)` should be defined in the class `Ball`, which will be called in the method `ballBounceOnPaddle(ArrayList<Ball> balls, Paddle paddle)` from the class `Collision`, instead of current methods like `ballBounceVertical()` and `ballBounceHorizontal()`.
    
 - Refine item representation
 
   Currently all items in the game are regarded as rectangulars by the program. And all animations and collisions are based on this. However, this simplification loses some properties of the real world. For the ball is actually a sphere, and the paddle may have some curves at two ends. 
	 
   For refinement, we can represent the ball as a circle in JavaFX, the brick as a rectangular, and the paddle as two half balls sticked to two short edges of a rectangular. And all collisions can be simplified to intersection between two balls, and between one ball and one line. 
 
 - Add mouse control into the game
	
   In the method `actionsPerFrame(double elapsedTime)` from the `GameWorld` class, the method `setOnMouseReleased(e -> handleMouseInputsFrame (e, elapsedTime))` can be called on the `Scene` instant `levelScene`. 

   And in the method `handleMouseInputsFrame(MouseEvent e, double elapsedTime)`, detailed functions can be realized for mouse control. For example, the movement of the mouse can also control that of the paddle, and mouse clicks can change the direction of the ball.
	

## Design Choices Justification

 - Use the JavaFX class `Timeline` to maintain the game loop
	
   This is the most popular method to keep all animations in the game running. In this method, each second is divided into a given number of frames, and programmers need to design actions of all items in each frame. 

   Advantages of this method include:

	 - It is easy to loop over actions in one frame, and we only need to focus on how things develop in each frame.
	 - There are a lot of online resces on timeline-based game loop setup, maintenance, and closure because of its popularity. 

 - Use private boolean variables `started` and `paused` in the `GameWorld` class to represent if the current level is started, and if paused. And in the key method `actionsPerFrame(double elapsedTime)` called by `timeline`, actions of all moving items and detection of all possible collsions are only carried out when the game is started (`started == true`) and is not paused (`paused == false`). 
 
   Advantages of this method includes:

	 - It is very clear to tell all 3 possible situations (not started, started and resuming, started but paused) apart with these 2 boolean variables. And different actions can be defined for each scenario.

   Disadvantages of this method include:

	 - Multiple situations can lead to unavoidable complex code structures and code duplication. 

 - At the end of each life, if the player is not dead (`lives > 0`), all bricks will remain the same as what they are before the life ends, but the paddle and the ball will be reloaded and reset to their initial positions. 
	 
   At the end of each level, when entering into the next level, all balls, paddle, and bricks will be reloaded and reset to their initial positions.

   If the player is dead (`lives == 0`), the result page will be loaded and possible guidance to a new game is given.
	
   This game design has its advantage that it is similar to most popular games. If all bricks get reloaded after the end of each life, players will be discaged too much, thus more likely to abandon the game.

 - All as movable items, bricks are not setup (`setupBricks()`) in the same method as balls and paddles (`setupMovableItems()`). This is because in different scenarios, not all methods need to be called. Like for the previous end-of-life case, if these two methods are combined, bricks have to be reloaded when the player loses one life.

 - For all item classes (`Ball`, `Brick`, `Powerup`) whose instances may be removed after construction, I use a boolean variable `toBeRemoved` as a member variable in the class as a mark for future removal. This mark is demanded because in the `ArrayList` class in Java items cannot be removed immediately. This processing method requires a second loop over items in the `ArrayList` when removing items, which degrades performance.

 - When checking for collisions between two items, both items are recognized as rectangulars. And there are multiple situations of rectangular relative positions. In my program, I mainly uses `minX`, `maxX`, `minY`, `maY` to identify position relationships. 
 
 	Moreover, for collisions happening between different items, the detecting criteria are with small but significant differences. For example, when the ball bounces on the paddle, I designed my code logic as following:

	```
	if (collide on the upper edge of the paddle){
		do something
	}
	if (collide on the left edge of the paddle){
		do something
	}
	if (collide on the right edge of the paddle){
		do something
	}
	```
   When a ball hits a brick, the code is as following:

	 	```
		 if (collide on the upper edge of the paddle){
			do something
		}
		else if (collide on the left edge of the paddle){
			do something
		}
		...
	 	```
	These differences are because that when a ball hits the paddle, the only effect is the ball changing direction according to the collision position. What matters is to get the direction correct, even with false detection of "multiple" collisions between them. But when a ball hits a brick, the effect is that not only the ball needs to change to the correct direction, but scores are added and possible powerups are generated. So no false "multiple" collisions can show up here. With different targets, codes are written in a different way.

 	This collision detection method is naive and awkward. But it can fulfill its targets correctly. Future developments can also be applied on collision detection.

## Simplification Assumptions

 - Rectangular assumption
 
 	Because all classes of items in the game are wrappers of the JavaFX class `ImageView`, all items are assumed to be rectangles when determining their positions, and detecting possible collisions. 
 
 	Items are pinned down on the scene by the `(x, y)` coordinates of their f vertices. Collisions between items are detected by comparing `(x, y)` coordinates of their four vertices. 

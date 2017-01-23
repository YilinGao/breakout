#A variant of the classic game Breakout

-  Names of all people who worked on the project: Yilin Gao.
- Date to start: Jan 13, date to finish: Jan 22. Estimated number of hours of work: 40 hours.
- Since it is a personal project, I have taken up all roles during project development.
- I referred a lot to JavaFX's online documentations, teacher's answers on Piazza, and several specific questions on Stack Overflow.
- Files used to start the project: `Breakout.java`.
- Files used to test the project: the project can be tested when run as an entire application.
- Required data and files: 

	- `breakout/images` includes images used to represent entities in the game.
	
	- `breakout/data` includes 3 text files to indicate the layout of bricks in each level.

- 	Instructions:

	- Press `SPACE` to start each level. Press `SPACE` to pause and resume during the level.
	- Use the left and right keys on the keyboard to control the paddle.
	- 3 kinds of bricks. Blue bricks diminish after 1 hit, and give you 100 points. Green bricks turn to blue after the first hit, and give you 200 points. Red bricks turn to green after the first hit, and give you 300 points.
	- In each level, the ball has a random initial speed.
	- The ball bounce normally (reflect) when it hits on the paddle's upper, left and right surface, or when it hits on a brick's all four surfaces. But the ball bounces back in its original direction when it hits on the paddle or a brick's corner.
	- The paddle can warp from one side of the screen to the other side when it hits the wall.

	Cheat keys:

	- `R`: reset the current level (with scores turned to 0 and lives unchanged)
	- `B`: restore the paddle to its initial position
	- `1`, `2`, `3`: skip the current level and start the corresponding level (with scores turned to 0 and lives unchanged)

	Power-ups:
	
	- red: speed all balls up by 1.2 times
	- yellow: the paddle becomes sticky (next time a ball bounces onto it, it will catch the ball for 3 seconds)
	- blue: add 1 life
	- greed: split each ball into 3 balls

- Known bugs, crashes, or problems: currently no.
- Extra feature: there is a restart button on the result page. The player can press `SPACE` during game to pause and resume game.
- My impression of the project: currently the project can fulfill its basic functions. Future improvement can focus on speed variation of the ball, to make it closer to reality.

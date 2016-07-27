# Breakout-clone
My personal project for diploma thesis. Created with Java using the libGDX framework.

Game version v0.5 can be downloaded [here] (https://drive.google.com/file/d/0B0rlu-lDfzw9eE9Qc0FieWlETWc/view?usp=sharing).

###Upcomming changes
* TBA

######Lessons learned:
- Don't fix what's not broken.
- Don't reinvent the wheel, "borrow" the blueprint and make your own!

###Change notes

v0.6
* white ball particles instead of white-yellow
* powerups appear in the front of bricks and not behind them

v0.5
* replaced level buttons with level images
 * above the images, the selected level is written 
* better powerup icons

v0.4
* animation for destroyed bricks
* when game has ended, the ball gradualy slows down
* improved the looks of game won/lost message
* improvised a meaningful pause/unpause screen
* LEVEL SELECTION!
 * 9 levels to select from 

v0.3
* new level
* rough options window
* game over/win text message with total score
* refactored the code for textures and atlases to be less wasteful
* reworked colision detection 
 * ball does not teleport anymore
 * more accurate colision detection (can destroy multiple blocks at the same time)
* power-ups (15% chance on destroyed block to spawn a random power-up)
 * short/long pad
 * extra life
 * speed up/down
* transitions between screens/scenes
* sound effects!
 * block destroyed
 * bounce pad/wall
 * life lost
 * power-up picked up
* background music!
* working options for sound/music volume level and on/off

v0.2
* changed the background color of the game screen from black to dark grey
* added temporary particles to the ball
* tried a new score counting mechanic
* fixed the way ball got locked on the pad

v0.1
* removed the 0.1 sec delay at the start of level
* default level changed for testing purposes
* reworked lose conditon, added win condition
  * when lives are lost, a 3 second delay is started
  * when all bricks are destroyed, the same delay is started
  * when delay is over, you are put back to the menu

v0.0
* added main menu with play, options, quit buttons
  * play starts a preset level
  * options does nothing yet
  * quit closes the application
* upon losing lives, you are put back to menu
* fps counter is visible


Bricks
* the basic main gameplay is created
* no victory condition
* if game is lost, level resets

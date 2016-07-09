# Breakout-clone
My personal project for diploma thesis. Created with Java using the libGDX framework.

###to-do list: 
#####Main important things
* <s>options menu</s>
 * change starting ball speed
 * <s>sounds on/off</s>
* pause menu with return to continue/menu/quit buttons
* make the game over/win text look/work better
 
#####Optional things
- a way to select levels
- <s>Rework movement/rendering and colision detection.</s>
- more polishing

###Upcomming changes
* animation for destroyed bricks
* pause opens a window with options to either return to menu or resume the game
* better win/lose events

######Lessons learned:
- Don't fix what's not broken.
- Don't reinvent the wheel, "borrow" the blueprint and make your own!

###Change notes

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

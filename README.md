# Breakout-clone
My personal project for diploma thesis. Created with Java using the libGDX framework.

###to-do list: 
#####Main important things
* <s>win conditions/game-over</s> event
* <s>power-ups 
 * speed up/down
 * extra life
 * pad size increase/decrease</s>
* <s>sound effects</s>
* <s>transitions between scenes</s>
* options menu
 * change starting ball speed
 * sounds on/off
* pause menu with return to menu/continue buttons
* make the game over/win text prettier
 
#####Optional things
- a way to select levels
- <s>Rework movement/rendering and colision detection.</s>
- more polishing

###Upcomming changes
* temporary options window (foundation for later)
* game over text message
* win message
* refactored the textures and atlases to be less wasteful
* reworked colision detection
* set up foundation for power-ups
 * short/long pad
 * extra life
 * speed up/down
* transisions between screens/scenes
* sound effects!
 * block destroyed
 * bounce pad/wall
 * life lost
 * power-up picked up
* background music!
* working options for sound/music volume level and on/off

######Lessons learned:
- Don't fix what's not broken.
- Don't reinvent the wheel, "borrow" the blueprint and make your own!

###Change notes

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

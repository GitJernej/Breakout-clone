# Breakout-clone
My personal project for diploma thesis. Created with Java using the libGDX framework.

###to-do list: 
#####Main important things
* <s>win conditions/game-over</s> event
* power-ups 
 * speed up/down
 * pad size increase/decrease
* sound effects
* transitions between scenes
* options menu
 * change starting ball speed
 * sounds on/off
 
#####Optional things
- a way to select levels
- Rework movement/rendering and colision detection.
- more polishing

###Change notes

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

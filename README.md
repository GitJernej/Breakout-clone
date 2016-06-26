# Breakout-clone
My personal project for diploma thesis. Created with Java using the libGDX framework.

to-do list: 
- <s>win conditions/game-over</s> event
- increase ball speed according to blocks destroyed
- a way to select levels
- sound effects
- options menu
- screen transitions
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

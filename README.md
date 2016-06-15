# Breakout-clone
Written in Java using LibGDX framework

This is a project I'm working on for my graduation.
The end product is expected to be a full game, with menus, level selections and sound effects. (with maybe other bits and pieces to make it more interesting)

The purpose of this project is also to learn to use the LibGDX framework.

Bricks Game folder is where all the source code and assets are, the project was created with LibGDX gradle setup, (gdx-setup).

Bricks.jar is the game executable, I will only update that file when I'll be happy with the changes I do in the code.

Game controls: mouse moves the pad, left click releases the ball. Esc and P keys (toggle) pause the game, and free the mouse cursor.

You can play and create different levels. Inside the Bricks.jar is a map called /levels/, the picture that is named level.png is the level that will be created in the game. So either rename the pictures or create your own. The picture needs to be 16x16 pixels, any colored pixel except for black will result in a block of that color, full black pixels result in an empty space.

# Java & SQL Game - Neverending Platformer

A final college project written in Java and SQL using Microsoft's Access Database.<br>
Description: a real-time randomly generated 2D platformer, also includes a create&load levels mechanic.

# Installation

1. Requires JDK 8 and Microsoft Access 2016.
2. Launch `NEP_1_0.jar` from folder Exported or compile `NetBeans Files` folder via NetBeans IDE 8.2 or higher.
3. Raw code is in NetBeans Files->src->main_pkg.

# Usage
 
## Random Level:

1. Press space to jump and avoid traps, your score is the amount of time you played before losing.
2. Game can be paused but not saved. Score can be saved upon losing or exiting the level, and later can be viewed in the leaderboard.
3. Difficulty can be adjusted in the game options.

## Load Level:

1. Choose a level file from the levels folder, only files made by the game can be executed.
2. Level files can be imported via being pasted into the `levels` folder.

## Level Creation:

1. Choose the difficulty you want to create for (Easy, Normal, Hard).
2. Pick tiles(traps) from the drop menu and adjust the length to your liking.
3. Press "Finish" when done.
4. Name your file and provide a creator name.
5. The level file will be exported to the levels folder.

## Options:

1. Change resolution, difficulty, or stop music.
2. Get an in-game rundown of the controls.

# Known bugs

The game speed is currently dependent on the cpu, and will run too fast on good computers.

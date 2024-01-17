# Neverending Platformer

<img align="right" src="/Exported/res/icon.png">

A final college project written in Java and SQL using Microsoft's Access Database.<br>
Description: a real-time randomly generated 2D platformer with a create & load custom levels mechanic.

<details>
  <summary><h3>Content</h3></summary>

- [Dependencies](#dependencies)
- [Installation](#installation)
- [Usage](#usage)
  - [Random Level](#random-level)
  - [Load Level](#load-level)
  - [Level Creation](#level-creation)
  - [Options](#options)
- [Known bugs](#known-bugs)

</details>
<hr>

# Dependencies

1. JDK 8
2. Microsoft Access 2016

# Installation

1. Install the dependencies.
2. Launch `Exported/NEP_1_0.jar` or compile the `NetBeans Files` folder via NetBeans IDE 8.2 or higher.

Raw code is in `NetBeans Files/src/main_pkg`.

# Usage

<img align="right" style="width:400px; height:auto;" src="https://github.com/ElenaChes/Java-SQL-Game--Neverending-Platformer/assets/54331769/9700df83-6216-4429-9354-0b0b5ff51786">

## Random Level:

- Press space to jump and avoid traps, your score is the amount of time you played before losing.
- Game can be paused but not saved. Score can be saved upon losing or exiting the level, and later can be viewed in the leaderboard.
- Difficulty can be adjusted in the game options.

## Load Level:

- Choose a level file from the levels folder, only files made by the game can be executed.
- Level files can be imported via being pasted into the `Exported/levels` folder.

<img align="right" style="width:400px; height:auto;" src="https://github.com/ElenaChes/Java-SQL-Game--Neverending-Platformer/assets/54331769/86369bce-d784-49e7-ba78-7e5a8f7137f6">

## Level Creation:

1. Choose the difficulty you want to create for (Easy, Normal, Hard).
2. Pick tiles (traps) from the drop menu and adjust the length to your liking, the game will make sure that you add safe tiles (ground) between trap tiles.
3. Press "Finish" when done.
4. Name your file and provide a creator name.
5. The level file will be exported to the levels folder.

## Options:

- Change resolution, difficulty, or stop music.
- Get an in-game rundown of the controls.

# Known bugs

The game speed is currently dependent on the cpu, and will run too fast on good computers.

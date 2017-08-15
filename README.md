Pakku
==
A hurried rip-off of a popular 1980 arcade game. 

The architecture is roughly MVC.  Controllers are the player and ghosts (time prevented me from adding the fruit). The controllers and Model are effectively the Game, but for the purposes of being able to play it, Views observe this model to generate the visible representation of the game and keep it in sync.

MODELS:
--
    - GameBoard holds the state of the playfield. It can be observed by any class willing to implement GameBoard.IGameBoardObserver.

CONTROLLERS:
--
    - Game is the class that co-ordinates the game.
        - IGameControlSource defines the input to the game: in this case, a "joystick" control.
        - IGameObserver is the interface by which Views can react to changes to the game state
    - IActor is the interface offered by any moving object in the game (e.g. player, ghost); particularly the tick() method through whcih gameplay is progressed.
    - BasicActor provides the common functions for both kinds of object
    - Player is the user's player, whom we call Pakku in a nod to the copyright we're infringing ;)
     - Ghost is any of the four ghosts that follow the player
     - Ghost and Player are simple Finite State Machines. 
     - Ghost is aware of Player in its decision making (it obtains the Player by inspecting Game). I have made an effort to implement Iwatu's original heuristics for each Ghost type, but in the time available it wasn't possible to do an exact simulation. 

VIEWS:
--
    - Display represents the graphics output 
    - PlayerActorView draws the player onto the display
    - GhostView draws the ghost onto the display
    - GameBoardView draws the playfield into the Display and keeps it updated (observes GameBoard).
    - Tile is the general view for a "tile" on the playfield (wall, whitespace, pill, etc..)
    - WallTile is a special-case for drawing the playfiled maze walls.  It chooses a different appearance for the tile depending on the neighbouring tiles. This converts the binary "wall"/"not wall" into the curved islands and walls of the original game (the original ued a hand-chosen characters, so the algorithm here isn't perfect).
GameView would gathers these together into one co-ordinated collection... if I had a bit more time to finish it.

--- View-to-graphics abstraction

To allow different graphics technologies, drawing to the screen is abstracted, via the interface IDisplay.  In this demo, IDisplay uses Java2D primitives to draw.  It turns out that the entire graphical repertoire of Pacman can be drawn with ellipses, pieslices and rectangles, so that's all that IDisplay offers.   IDisplay also abstracts colours, which would allow a display to adopt light/dark theming or run in black&white mode (basically, the View doesn't need to care what actual RGB colours are used for items)

Display is to an offscreen bitmap, which is selectively updated to screen.  View objects mark areas as "dirty", and the Display.flush() method goes through these and copies them to the screen.  No optimisation is made for overlapping regions.

A note on positions
--
All co-ordinates in the game are expressed in "tiles". This makes the game logic simpler, and also has the beneficial side effect of allowing the display to be scaled to arbitrary size.
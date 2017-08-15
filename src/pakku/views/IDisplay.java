package pakku.views;

/** expected functions for any object capable of displaying the game.
 * 
 * Note that all co-ordinates passed to these methods are assumed to be measured in "Tiles"
 * 
 * @author Kristian
 * 
 */
public interface IDisplay
{ 
  
  public enum GameColour 
  {
    BACKGROUND(0),
    WALL_EDGE(1),
    WALL_FILL(2),
    PILL(3),
    PAKKU(4),
    GATE(5),
    GHOST_RED(6),
    GHOST_PINK(7),
    GHOST_BLUE(8),
    GHOST_ORANGE(9),
    GHOST_WORRIED_DIM(10),
    GHOST_WORRIED_BRIGHT(11),
    GHOST_EYEBALL(12),
    GHOST_PUPIL(13),
    
    NUMBER_OF_COLOURS(14);
    
    /** allow conversion to integer, for the convenience of IDisplay implementors */
    private final int value;
    private GameColour(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
  }
  
  void clear();
  
  void ellipse(double left, double top, double right, double bottom, GameColour which);
  void pie(double left, double top, double right, double bottom, double startAtDegree, double sweep, GameColour which);
  void rectangle(double left, double top, double right, double bottom, GameColour which);  
  
  /** mark a region of the display as needing to be flushed to screen (note: specific implementations may not use this) 
   * as ever, parameters are in "tiles" */
  void markDirty(int left, int top, int belowBottom, int beyondRight);

}

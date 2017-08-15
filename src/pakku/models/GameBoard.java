package pakku.models;

import java.util.HashSet;

public class GameBoard
{
  public enum Item
  {
    SPACE,
    WALL,
    OFF_BOARD,
    BOX,
    GATE,
    GATE_OPEN,
    PILL,
    POWERPILL;

    public Boolean isSolidToGhost( Boolean allowGate )
    {
      return (this==WALL) || (this==OFF_BOARD) || (allowGate && this==GATE);
    }
    
    public Boolean isSolidToPlayer(  )
    {
      return !( this==SPACE || this==PILL || this==POWERPILL);
    }

  };
  
  public static final int BOARD_WIDTH=28;
  public static final int BOARD_HEIGHT=31;
  
  
  public interface IGameBoardObserver
  {
    void onReset(GameBoard b);
    void onChange(GameBoard b, int x, int y, GameBoard.Item oldContent, GameBoard.Item newContent );
  }
  
  private HashSet<IGameBoardObserver> observers;
  public void addObserver(IGameBoardObserver observer)
  {
    observers.add(observer);
  }
  public void removeObserver(IGameBoardObserver observer)
  {
    observers.remove(observer);
  }
  
  
  private GameBoard.Item cells[][];
  public GameBoard.Item getItemAt(int x, int y)
  {
    if (x<0 || x>=BOARD_WIDTH || y<0 || y>=BOARD_HEIGHT)
    { // asked for something off the board. 
      // To make the "tunnels" work, it's necessary to
      // extend the edges of the board
      if (y>=0 && y<BOARD_WIDTH)
      { // off left/right edge - extrapolate the tunnel part infinitely
        // TODO: using leftmost column for both edges assumes boards are vertically symmetrical!
        Item i = cells[y][0];
        return (i==Item.SPACE)? i : Item.OFF_BOARD;
      }
      return GameBoard.Item.OFF_BOARD;
    }
    else
    { 
      return cells[y][x];
    }
  }

  public void RemoveItemAt(int x, int y)
  {
    if (x>=0 && x<(BOARD_WIDTH) && y>=0 && y<BOARD_HEIGHT)
    {
      Item old=cells[y][x];
      cells[y][x]=Item.SPACE;
      
      for (IGameBoardObserver o: observers)
      {
        o.onChange(this, x, y, old, Item.SPACE);
      }     
    }
  }
  

  /** layout of the default maze; 
   * Iwatu's maze is left/right symmetric, so define the left side, and generate the right
   (this is purely to save me typing!)
   */
  private final String boardLayout=
       "##############"
      +"#............#"
      +"#.####.#####.#"
      +"#O####.#####.#"
      +"#.####.#####.#"
      +"#............."
      +"#.####.##.####"
      +"#.####.##.####"
      +"#......##....#"
      +"######.##### #"
      +"     #.##### #"
      +"     #.##     "
      +"     #.## ###="
      +"######.## #///"
      +"      .   #///"
      +"######.## #///"
      +"     #.## ####"
      +"     #.##     "
      +"     #.## ####"
      +"######.## ####"
      +"#............#"
      +"#.####.#####.#"
      +"#.####.#####.#"
      +"#O..##....... "
      +"###.##.##.####"
      +"###.##.##.####"
      +"#......##....#"
      +"#.##########.#"
      +"#.##########.#"
      +"#............."
      +"##############"
       ;  
  
  public GameBoard()
  {
    observers= new HashSet<IGameBoardObserver>();
    cells=new GameBoard.Item[BOARD_HEIGHT][BOARD_WIDTH];
    resetSilently();
  }
  
  
  
  private void resetSilently()
  {
    int idx=0;
    for (int y=0; y<BOARD_HEIGHT; ++y)
    {
      for (int x=0; x<BOARD_WIDTH; ++x)
      {
        idx=y*(BOARD_WIDTH/2);
        idx+=(x>=BOARD_WIDTH/2)? (BOARD_WIDTH-x)-1: x;
        
        
        GameBoard.Item item=Item.SPACE;
        switch (boardLayout.charAt(idx))
        {
        case '#':
          item=Item.WALL;
          break;
          
        case '.':
          item=Item.PILL;
          break;
          
        case 'O':
          item=Item.POWERPILL;
          break;
          
        case '=':
          item=Item.GATE;
          break;
  
        case '/':
          item=Item.BOX;
          break;
          
        default:
           break;
        }
        cells[y][x]=item;
      }
    }
  }
  
  public void reset()
  {
    resetSilently();
    for (IGameBoardObserver o: observers)
    {
      o.onReset(this);
    }
  }
  
}

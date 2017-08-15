package pakku.controllers;

import java.util.HashSet;

import pakku.models.GameBoard;

public class BasicActor implements IActor
{
  protected int cellX;
  protected int cellY;
  protected double x;
  protected double y;
  protected int deltaX;
  protected int deltaY;
  private double speed;
  
  /*-- initial positions */
  protected double startX;
  protected double startY;
  protected int startDeltaX;
  protected int startDeltaY;

  
  protected double getSpeed()
  {
    return speed;
  }
  protected void setSpeed(double speed)
  {
    this.speed = speed;
  }
  

  protected int stateTimer=0;
  /** length of time actor has been in its current state */
  public int getStateTimer()
  {
    return stateTimer;
  }

  
  
  /** horizontal position of this actor on the playing surface (includes fractions) */
  public double getX() { return x; }
  /** vertical position of this actor on the game board */
  public double getY() { return y; }

  /** horizontal position of the board square actor rests on (an integer) */
  public int getCellX() { return cellX; }
  /** vertical position of the board square actor rests on (an integer) */
  public int getCellY() { return cellY; }

  /** direction of travel in X axis. Will be -1, 0, or +1 */
  public int getDeltaX() { return deltaX; };
  /** direction of travel in Y axis. Will be -1, 0, or +1 */
  public int getDeltaY() { return deltaY; };
  
  
  private HashSet<IActorObserver> observers;
  public void addObserver(IActorObserver observer)
  {
    observers.add(observer);
  }
  public void removeObserver(IActorObserver observer)
  {
    observers.remove(observer);
  }
  
  protected void signalMoved( double oldX, double oldY)
  {
    for (IActorObserver o: observers)
    {
      o.onMoved(this, oldX, oldY);
    }
  }
  
  protected void signalChanged( String whatChanged )
  {
    for (IActorObserver o: observers)
    {
      o.onChanged(this,whatChanged);
    }
  }
  

  @Override
  /** perform one tick of gameplay */
  public void tick(Game game)
  {
    GameBoard board = game.getBoard();

    double oldX=x; double oldY=y;
    x+=((double)deltaX)*getSpeed();
    y+=((double)deltaY)*getSpeed();

    signalMoved(oldX,oldY); // allow animation

    if (x==oldX&&y==oldY)
    {
      return;
    }

    
    // detect movement into a new square on the game board
   
    Boolean hasMovedToNewCell=(deltaX!=0 && cellX!=(int)Math.round(x) &&  (x-Math.floor(x)) < getSpeed() )
              || (deltaY!=0 && cellY!=(int)Math.round(y) && (y-Math.floor(y))<getSpeed() );

    //System.out.print("Moved - ("+oldX+","+oldY+") to ("+x+","+y+") newCell="+hasMovedToNewCell);
    
    if ( hasMovedToNewCell  )
    { // moved into a new square on the game board
      cellX = (int)x;
      cellY = (int)y;
      
      // Special case: moved off-board, reappear on other side.
      if (cellX<-1)
      {
        cellX=GameBoard.BOARD_WIDTH +2;
        x=cellX;
      }
      else if (cellX>GameBoard.BOARD_WIDTH+1)
      {
        cellX=-2;
        x=cellX;
      }
      // TODO: The game maze does not allow movement off top or bottom; if maze changes, add logic for top/bottom.
      GameBoard.Item item=board.getItemAt(cellX,cellY);
      this.onMovedToNewPosition(game, board, item);
    }
  }

  void onMovedToNewPosition(Game game,GameBoard board, GameBoard.Item item)
  {
    return; // intended to be overidden by derived classes.
  }


  public BasicActor(double x_, double y_, int deltaX_, int deltaY_, double speed_)
  {
    observers = new HashSet<IActorObserver>();
    startX=x_;
    startY=y_;
    startDeltaX=deltaX_;
    startDeltaY=deltaY_;
    speed=speed_;
    reset();
  }
  
  public void reset()
  {
    x=startX;
    y=startY;
    cellX=(int)startX;
    cellY=(int)startY;
    deltaX=startDeltaX;
    deltaY=startDeltaY;
  }
  
}

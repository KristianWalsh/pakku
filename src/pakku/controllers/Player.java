package pakku.controllers;

import pakku.controllers.Ghost.Personality;
import pakku.controllers.Ghost.State;
import pakku.models.GameBoard;

public class Player extends BasicActor
{
  public enum State
  {
    PLAYING,
    DYING,
    DEAD
  }
  
  
  private State state;
  public State getState()
  {
    return state;
  }


  void setState(State state_)
  {
    if (state_ != state)
    {
      state = state_;
        // currently, change of state implies movement stops.
      deltaX=0;
      deltaY=0;
      stateTimer=0;
      signalChanged("State");
    }
  }
  
  
  public Player(double x_, double y_, double speed )
  {
    super( x_, y_, 0,0, speed );
    setState(State.PLAYING);
  }

  @Override
  public void reset()
  {
    super.reset();
    setState(State.PLAYING);
  }

  
  
  @Override
  public void tick(Game g)
  {
    switch (state)
    {
    case PLAYING:
      super.tick(g);
      if (deltaX==0 && deltaY==0)
      { // input is checked whenever Pakku passes a turning opportunity, but
        // if he's standing still, it needs to be done explicitly.
        checkInput(g.getInput(),g.getBoard());
      }
      break;
     
    case DYING:
      ++stateTimer;
      if (stateTimer>120)
      {
        setState(State.DEAD);
      }
      signalChanged("stateTimer");
      break;
      
    case DEAD:  // no action needed.
      break;
    }
    
  } 

  @Override
  public void onMovedToNewPosition( Game game, GameBoard board, GameBoard.Item item )
  {
    if (state!=State.PLAYING)
    {
      return; 
    }

    if (item==GameBoard.Item.POWERPILL || item==GameBoard.Item.PILL)
    {
      board.RemoveItemAt(cellX, cellY);
      // board observer will handle the scoring and other implications of this.
    }

    checkInput(game.getInput(), board);

    int newDeltaX= deltaX;
    int newDeltaY= deltaY;
    
    if (deltaX!=0 || deltaY!=0)
    { // Pakku is moving; make sure he stops when he hits a wall!
      GameBoard.Item itemInFront=board.getItemAt(cellX + deltaX, cellY + deltaY);
      if (itemInFront.isSolidToPlayer())
      {
        newDeltaX=0;
        newDeltaY=0;
      }      
    }

    setDirection(newDeltaX, newDeltaY);
  }

  private void setDirection( int newDeltaX, int newDeltaY)
  {
    if (deltaX!=newDeltaX || deltaY!=newDeltaY)
    { // apply new direction
      deltaX=newDeltaX;
      deltaY=newDeltaY; 
      // clamp position to current cell before moving in new direction.
      x=(double)cellX;
      y=(double)cellY;

      if (deltaX!=0 && deltaY!=0)
      {
        signalChanged("Direction");
      }
    }
  }
  
  
  
  void checkInput(Game.IGameControlSource joystick, GameBoard board)
  {
    if (state==State.PLAYING)
    {
      int desiredDeltaX=deltaX;
      int desiredDeltaY=deltaY;
      switch (joystick.getJoystickPosition())
      {
      case UP:
        desiredDeltaX=0;
        desiredDeltaY=-1;
        break;

      case DOWN:
        desiredDeltaX=0;
        desiredDeltaY=1;
        break;
      case LEFT:
        desiredDeltaX=-1;
        desiredDeltaY=0;
        break;
      case RIGHT:
        desiredDeltaX=1;
        desiredDeltaY=0;
        break;

      default:
        break;
      }
      
      if (! board.getItemAt(cellX+desiredDeltaX, cellY+desiredDeltaY).isSolidToPlayer())
      {
        setDirection(desiredDeltaX,desiredDeltaY);
      }
      
    }
  }
  

  
  
  public void collide(IActor a)
  {
    if (Math.abs(a.getX() - getX())>1.2 || Math.abs(a.getY()-getY())>1.2 )
    { // no collision
      return;
    }
    
    if (a instanceof Ghost)
    {
      Ghost ghost = (Ghost)a;
      
      switch (ghost.getState())
      {
        case ACTIVE:
          setState(State.DYING);
          break;
          
        case SCARED:
          ghost.setState(Ghost.State.EATEN);
          break;
          
        case EATEN:
          break;
      }
    }

  }

  
}

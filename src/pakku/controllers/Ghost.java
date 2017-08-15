package pakku.controllers;

import pakku.models.GameBoard;

public class Ghost extends BasicActor
{
  int counter;
  
  /** Ghost behaviours depend on who they are! */
  public enum Identity
  {
    BLINKY,
    PINKY,
    INKY,
    CLYDE
  };
  private Identity id;
  public Identity getId() { return id; };
  
  enum Personality
  {
    CHASE,
    CHASE_FRONT,
    FLEE,
    WAVER_A,  // wavers between same as "CHASE" (WAVER_A) and "Go to bottom left"
    WAVER_B, //  the "Go-to-bottom-left" personality of "WAVER"
    RUN_TO_BOX // The personality for all eaten ghosts
  };
  protected Personality personality;
  
  public enum State
  {
    ACTIVE,
    SCARED,
    EATEN
  }
  private State state;
  public State getState()
  {
    return state;
  }
  
  public void setState(State state_)
  {
    if (state_ != state)
    {
      this.state = state_;
      signalChanged("State");
    }
  }

  
  private Player player;
  

  public Ghost(double positionX, double positionY, double speed,Identity which, Player p)
  {
    super(positionX, positionY, 1, 0, speed);
    id=which;
    counter=0;
    player = p;
    
    switch (id)
    {
    case INKY:
      personality=Personality.CHASE;
      break;
    case BLINKY:
      personality=Personality.CHASE_FRONT;
      break;
    case PINKY:
      personality=Personality.FLEE;
      break;
    case CLYDE:
      personality=Personality.WAVER_A;
      break;
    }
    deltaX=-1; deltaY=0;
    setState(State.ACTIVE);
  }
  
  public void reset()
  {
    super.reset();
    setState(State.ACTIVE);
  }
  
  @Override
  public void tick(Game game)
  {
    if (player.getState()!=Player.State.PLAYING)
    { // if player is dead, stop all ghosts.
      signalMoved(cellX,cellY);
      return;
    }
    else
    {
      super.tick(game);
    }
  }
  
  
  // Ghost
  public void onMovedToNewPosition( Game game, GameBoard board, GameBoard.Item item )
  {
    if (item==GameBoard.Item.BOX && state==State.EATEN)
    {
      setState(State.ACTIVE);
    }
    
    GameBoard.Item itemInFront=board.getItemAt(cellX + deltaX, cellY + deltaY);
    GameBoard.Item itemToLeft=board.getItemAt(cellX + deltaY, cellY - deltaX);
    GameBoard.Item itemToRight=board.getItemAt(cellX - deltaY, cellY + deltaX);

    Boolean canTurnLeft=!itemToLeft.isSolidToGhost( state!=Ghost.State.EATEN && deltaX==-1);
    Boolean canTurnRight=!itemToRight.isSolidToGhost((state!=Ghost.State.EATEN && deltaX==1));
    Boolean isBlockedAhead = itemInFront.isSolidToGhost((state!=Ghost.State.EATEN && deltaY==1) );
//    System.out.print("Ghost is at ("+cellX+","+cellY+") can move forward?"+!isBlockedAhead+"  left?"+canTurnLeft+"   right?"+canTurnRight+"\n" );

    
    // consider turning only if an opening is left or right, or have hit a wall
    if ( isBlockedAhead || canTurnLeft || canTurnRight  )
    {
//      System.out.print("\n" );
//     System.out.print("am at "+cellX+" "+cellY+" counter is "+counter+"\n" );
//     System.out.print("am going "+deltaX+" "+deltaY+"\n" );
      
        // if in the box, the overriding goal is to get out.
      int goalX= 14; 
      int goalY= 11; 
      if (state==State.EATEN || item!=GameBoard.Item.BOX && item!=GameBoard.Item.GATE)
      {
        Personality activePersonality = (state==State.EATEN)? Personality.RUN_TO_BOX 
                                      : (state==State.SCARED)? Personality.FLEE 
                                      : personality;
        goalX= this.player.getCellX();
        goalY= this.player.getCellY();
 //       System.out.print("Want to reach at "+goalX+" "+goalY+"\n" );
      
        
        switch (activePersonality)
        {
        case CHASE:
          break;
  
        case CHASE_FRONT:
          goalX += this.player.getDeltaX()*2;
          goalY += this.player.getDeltaY()*2;
          break;
  
        case FLEE:
          if (player.getCellX()<GameBoard.BOARD_WIDTH/2)
          {
            goalX=GameBoard.BOARD_WIDTH-2;
          }
          else
          {
            goalX=1;
          }
          if (player.getCellY()<GameBoard.BOARD_HEIGHT/2)
          {
            goalY=GameBoard.BOARD_HEIGHT-2;
          }
          else
          {
            goalY=1;
          }
          break;
  
        case WAVER_A:
          ++this.counter;
          if (this.counter>=10)
          {
            this.counter=0;
            personality=Personality.WAVER_B;
          }
          break;
  
        case WAVER_B:
          goalX=1;
          goalY=GameBoard.BOARD_HEIGHT-2;
          ++counter;
          if (counter>=5)
          {
            counter=0;
            personality=Personality.WAVER_A;
          }
          break;
          
        case RUN_TO_BOX:
          goalX=14;
          goalY=(cellY>14) ? 11 : 14;  // go above, then down, into box.
          //TODO= remove this hard-coding!
          break;
        }
  
 //       System.out.print("(post-personality) Want to reach "+goalX+" "+goalY+"\n" );
      }
 
      // All things being equal, keep going ahead.
      int newDeltaX= deltaX;
      int newDeltaY= deltaY;
      // default is to continue...
      int bestOption =getDistanceCost( goalX,goalY, cellX+deltaX, cellY+deltaY);
      if (isBlockedAhead)
      { // if can't go forward, consider the cost of going back as best option insteead.
        bestOption=Integer.MAX_VALUE;
//        System.out.print("reversing to ("+(cellX-deltaX)+","+(cellY-deltaY)+") has a distance of  "+bestOption+"\n" );
      }
//      else
//      {
//        System.out.print("carry on to ("+(cellX+deltaX)+","+(cellY+deltaY)+") has a distance of  "+bestOption+"\n" );
//      }
      
      Boolean decisionMade = false;
      
      if (canTurnLeft )
      { // turn left.
        int optionCost = getDistanceCost(goalX,goalY, cellX+deltaY, cellY-deltaX);
        //System.out.print("turning left to ("+(cellX+deltaY)+","+(cellY-deltaX)+") has a distance of  "+optionCost+"\n" );
        if ( optionCost<bestOption )
        {
          bestOption = optionCost;
          newDeltaX=+deltaY;
          newDeltaY=-deltaX;
          decisionMade=true;
        }
      }

      if (canTurnRight )
      { // turn right.
        int optionCost = getDistanceCost(goalX,goalY, cellX-deltaY, cellY+deltaX);
        //System.out.print("turning right to ("+(cellX-deltaY)+","+(cellY+deltaX)+") has a distance of  "+optionCost+"\n" );
        if ( optionCost<bestOption )
        {
          bestOption = optionCost;
          newDeltaX= -deltaY;
          newDeltaY= deltaX;
          decisionMade=true;
        }
      }
      
      if (isBlockedAhead && decisionMade==false)
      { // Have run into a wall, but neither left nor right was a better option, so reverse.
        //System.out.print("out of ideas. Reversing.\n" );
        newDeltaX= -deltaX;
        newDeltaY= -deltaY;
        
      }

      if (deltaX!=newDeltaX || deltaY!=newDeltaY)
      { // apply new direction
        deltaX=newDeltaX;
        deltaY=newDeltaY; 
        // clamp position to current cell before moving in new direction.
        x=(double)cellX;
        y=(double)cellY;

        signalChanged("Direction");
      }
    }
  }

  
  /**  Calculate the cost of a path between two points. The
   * heuristic chosen is the "Manhattan distance" (an approximation of
   * straight-line distance) plus a cost for long distances on either
   * x or y axis*/
  private int getDistanceCost(int x1,int y1,int x2,int y2)
  { 
    int vertical = Math.abs(y2-y1);
    int horizontal = Math.abs(x2-x1);
    return vertical+horizontal+Math.max(vertical, horizontal)/2;
  }
  
  
  
  
}

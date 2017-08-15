package pakku.views;

import pakku.controllers.IActor;
import pakku.controllers.IActorObserver;
import pakku.models.GameBoard;

public class BasicActorView implements IActorObserver
{
  GameBoardView background;
  protected IDisplay display;
  Boolean needsDraw;
  Boolean alreadyErased;
  protected IActor actor;
  
  public BasicActorView(IDisplay destination_, GameBoardView background_, IActor subject)
  {
    display=destination_;
    background=background_;
    needsDraw=true;
    alreadyErased=true;
    actor=subject;
    actor.addObserver(this);
  }

  public void erase(int left, int top)
  {
    if (alreadyErased==false)
    {
      // erase old positions
      for (int y=top-1; y<top+3; ++y)
      {
        for (int x=left-1; x<left+3; ++x)
        {
          background.drawTile(x,y);
        }
      }
      display.markDirty( Math.max(0, left-1), Math.max(0, top-1), Math.min(GameBoard.BOARD_WIDTH, left+3), Math.min(GameBoard.BOARD_HEIGHT, top+3) );
      alreadyErased=true;
    }
  }
  
  protected void drawToDisplay()
  {
     // Derived classes will handle this.
  }
  
  public void draw()
  {
    if (needsDraw)
    {
      drawToDisplay();
      alreadyErased=false;
      needsDraw=false;
    }
  }
  
  
  /*- IActorObserver -*/
  
  @Override
  public void onMoved(IActor what, double oldX, double oldY)
  { // erase old positions
    erase( (int)oldX,(int)oldY);
    needsDraw=true;
  }
  
  @Override
  public void onChanged(IActor what, String whatChanged)
  {
    erase( (int)what.getX(),(int)what.getY());
    needsDraw=true;
  }
  
  
  
}

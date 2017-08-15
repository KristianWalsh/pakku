package pakku.views;

import pakku.controllers.Ghost;
import pakku.controllers.IActor;

public class GhostActorView extends BasicActorView
{

  
  public GhostActorView(IDisplay destination_, GameBoardView background_, IActor subject) 
  {
    super(destination_, background_, subject);
  }
  
  @Override
  protected void drawToDisplay()
  {
    Ghost ghost = (Ghost)actor;
    Ghost.State state=ghost.getState();
    IDisplay.GameColour colour =  ((ghost.getCellX()+ghost.getCellY())%2==0) ? IDisplay.GameColour.GHOST_WORRIED_DIM : IDisplay.GameColour.GHOST_WORRIED_BRIGHT;
    if (state==Ghost.State.ACTIVE)
    {
      switch (ghost.getId())
      {
      case BLINKY:
        colour = IDisplay.GameColour.GHOST_RED;
        break;
      case CLYDE:
        colour = IDisplay.GameColour.GHOST_ORANGE;
        break;
      case INKY:
        colour = IDisplay.GameColour.GHOST_BLUE;
        break;
      case PINKY:
        colour = IDisplay.GameColour.GHOST_PINK;
        break;
      default:
        break;
      
      }
    }
    
    double size=1.5;
    double top=actor.getY()+0.5-(size/2);
    double left=actor.getX()+0.5-(size/2);
    
    if (ghost.getState()!=Ghost.State.EATEN)
    {
      display.pie(left,top,left+size,top+(size*0.8),0,180,colour);
      display.rectangle(left,top+(size*0.4),left+size,top+(size*0.85),colour);
      display.pie(left-(size/6),top+(size*0.7),left+(size/6),top+size,270,90,colour);
      display.pie(left+(size/6),top+(size*0.7),left+(size/2),top+size,180,180,colour);
      display.pie(left+(size/2),top+(size*0.7),left+(size-size/6),top+size,180,180,colour);
      display.pie(left+(size-size/6),top+(size*0.7),left+(size+size/6),top+size,180,90,colour);
    }
    double eyeballSize=(size*0.375);
    double eyeballSpace=(size*0.0375);
    double eyeballLeft=left+(size*0.5)-(eyeballSize+eyeballSpace/2);
    eyeballLeft+=size*(ghost.getDeltaX()*0.05);
    double eyeballTop=top+size*(0.15+ghost.getDeltaY()*0.05);
    double pupilSize=eyeballSize/4;
    double pupilLeft=eyeballLeft+(eyeballSize-pupilSize)/2 + (ghost.getDeltaX()*pupilSize);
    double pupilTop=eyeballTop+(eyeballSize-pupilSize)/2 + (ghost.getDeltaY()*pupilSize);

    if (ghost.getState()==Ghost.State.SCARED)
    {
      display.rectangle(left+size*0.25, top+(size*0.7), left+size*0.75, top+(size*0.72), IDisplay.GameColour.GHOST_EYEBALL);
    }
    
    for (int i=0; i<2; ++i)
    {
      display.ellipse(eyeballLeft, eyeballTop, eyeballLeft+eyeballSize, eyeballTop+eyeballSize, IDisplay.GameColour.GHOST_EYEBALL);
      display.ellipse(pupilLeft, pupilTop, pupilLeft+pupilSize, pupilTop+pupilSize, IDisplay.GameColour.GHOST_PUPIL);
      eyeballLeft+=eyeballSize+eyeballSpace;
      pupilLeft+=eyeballSize+eyeballSpace;
    }
  }


}

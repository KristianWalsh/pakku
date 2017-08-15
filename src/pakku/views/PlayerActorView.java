package pakku.views;
import pakku.controllers.Player;
import pakku.controllers.IActor;

  public class PlayerActorView extends BasicActorView
  {
    
    public PlayerActorView(IDisplay destination_, GameBoardView background_, IActor subject) 
    {
      super(destination_, background_, subject);
    }
    
    @Override
    protected void drawToDisplay()
    {
      Player p= (Player)actor;

      double size=1.5;
      double top=actor.getY()+0.5-(size/2);
      double left=actor.getX()+0.5-(size/2);
      double mouth=0;
      double angle=mouth;

      switch (p.getState())
      {
      case PLAYING:
        mouth=200.0*(p.getY()-Math.floor(p.getY())  + p.getX()-Math.floor(p.getX()));
        if (mouth>100) {mouth=200.0-mouth;}
        if (p.getDeltaY()==0)
        {
          angle=(p.getDeltaX()<0)? 180+mouth/2 : mouth/2;
        }
        else
        {
          angle=(p.getDeltaY()<0)? 90+mouth/2 : 270+mouth/2;
        }
        display.pie(left, top, left+size, top+size, angle, 360-mouth, IDisplay.GameColour.PAKKU);
        break;
       
      case DYING:
        double timeScale=p.getStateTimer()/120.0;
        size=size*(1-timeScale*0.4);
        left+=size*(0.2*timeScale);
        double offset=0.3*timeScale;

        if (timeScale<0.5)
        {
          display.ellipse(left,top+offset,left+size,top+size+offset, IDisplay.GameColour.PAKKU);
        }
        else
        {
          mouth=355*((timeScale-0.5)*2.0);
          angle = 90+mouth/2;
          display.pie(left, top+offset, left+size, top+size+offset, angle, 360-mouth, IDisplay.GameColour.PAKKU);
        }
        break;
        
      case DEAD:
        angle=0;
        mouth=0;
        break;        
      }

    }


  }



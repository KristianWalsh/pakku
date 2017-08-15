package pakku.views;

import pakku.models.GameBoard;
import pakku.views.IDisplay.GameColour;

public class Tile
{
  private GameBoard.Item tileType;
  
  public void drawAt( IDisplay d, int x,int y)
  {
    d.rectangle(x,y,x+1,y+1,IDisplay.GameColour.BACKGROUND);
    
    switch (tileType)
    { 
    case PILL:
      d.ellipse(x+0.4, y+0.4, x+0.6, y+0.6, GameColour.PILL);
      break;

    case POWERPILL :
      d.ellipse(x+0.2, y+0.2, x+0.8, y+0.8, GameColour.PILL);
      break;

    case GATE:
      d.rectangle(x, y+0.4, x+1, y+0.6, GameColour.GATE);
      
    default:
      break;
    }
  }
  
  public Tile( GameBoard.Item item)
  {
    tileType = item;
  }
  
};


package pakku.views;

import pakku.models.GameBoard;
import pakku.models.GameBoard.Item;
import pakku.views.IDisplay.GameColour;

public class WallTile extends Tile
{
  Boolean isInner;
  Boolean trimTop;
  Boolean trimBottom;
  Boolean trimLeft;
  Boolean trimRight;
  
  Boolean trimBottomLeft;
  Boolean trimBottomRight;
  Boolean trimTopLeft;
  Boolean trimTopRight;
  
  static final double trimming = 0.375;
  static final double outlineWidth = 0.0625;
  static final double curveRadius = 0.5;
  
  // Iwata-san used a character generator and hand-set tiles for the various curved bits; 
  // I'm going to determine the shape of the wall section and draw it manually.
  public void drawAt( IDisplay d, int x,int y)
  {
    if (isInner)
    {
      d.rectangle(x,y,x+1,y+1,IDisplay.GameColour.WALL_FILL);
      return;
    }
    
    double top= trimTop? y+trimming : y;
    double bottom= trimBottom?  y+1-trimming: y+1;
    double left= trimLeft? x+trimming : x;
    double right=trimRight? x+1-trimming:x+1;
    double topEdge=trimTop? outlineWidth:0;
    double leftEdge=trimLeft? outlineWidth:0;
    double rightEdge=trimRight? outlineWidth:0;
    double bottomEdge=trimBottom? outlineWidth:0;
    super.drawAt(d, x, y);

    
    
    if (trimTop && trimLeft)
    {
     d.pie(left, top, left+curveRadius*2, top+curveRadius*2, 90, 180, IDisplay.GameColour.WALL_EDGE); 
     d.pie(left+outlineWidth, top+outlineWidth, (left+curveRadius*2)-outlineWidth, (top+curveRadius*2)-outlineWidth, 90, 90, IDisplay.GameColour.WALL_FILL); 
     d.rectangle(left+curveRadius,top,right,top+curveRadius,IDisplay.GameColour.WALL_EDGE);
     d.rectangle(left+curveRadius,top+topEdge,right,top+curveRadius,IDisplay.GameColour.WALL_FILL);
     top+=curveRadius;
     topEdge=0;
    }
    else if (trimTop && trimRight)
    {
      d.rectangle(left,top,right-curveRadius,top+curveRadius,IDisplay.GameColour.WALL_EDGE);
      d.pie(right-curveRadius*2, top, right, top+curveRadius*2, 0, 90, IDisplay.GameColour.WALL_EDGE); 
      d.rectangle(left,top+topEdge,right-rightEdge-curveRadius,top+curveRadius,IDisplay.GameColour.WALL_FILL);
      d.pie( (right-curveRadius*2)-outlineWidth, top+outlineWidth, right-outlineWidth, top+(curveRadius*2)-outlineWidth, 0, 90, IDisplay.GameColour.WALL_FILL); 
      top+=curveRadius;
      topEdge=0;
    }
    else if (trimBottom && trimLeft)
    {
     d.pie(left, bottom-curveRadius*2, left+curveRadius*2, bottom, 180, 90, IDisplay.GameColour.WALL_EDGE); 
     d.pie(left+outlineWidth, (bottom-curveRadius*2)+outlineWidth, (left+curveRadius*2)-outlineWidth, bottom-outlineWidth, 180, 90, IDisplay.GameColour.WALL_FILL); 
     d.rectangle(left+curveRadius,bottom-(curveRadius),right,bottom,IDisplay.GameColour.WALL_EDGE);
     d.rectangle(left+curveRadius,bottom-(curveRadius),right,bottom-outlineWidth,IDisplay.GameColour.WALL_FILL);
     bottom-=curveRadius;
     bottomEdge=0;
    }
    else if (trimBottom && trimRight)
    {
      d.rectangle(left,bottom-curveRadius,right-curveRadius,bottom,IDisplay.GameColour.WALL_EDGE);
      d.pie(right-curveRadius*2, bottom-curveRadius*2, right, bottom, 270, 90, IDisplay.GameColour.WALL_EDGE); 
      d.rectangle(left,bottom-curveRadius,right-curveRadius,bottom-outlineWidth,IDisplay.GameColour.WALL_FILL);
      d.pie( (right-curveRadius*2)+outlineWidth, (bottom-curveRadius*2)+outlineWidth, right-outlineWidth, bottom-outlineWidth, 270, 90, IDisplay.GameColour.WALL_FILL); 
      bottom-=curveRadius;
      bottomEdge=0;
    }
    
    d.rectangle(left,top,right,bottom,IDisplay.GameColour.WALL_EDGE);
    d.rectangle(left+leftEdge,top+topEdge,right-rightEdge,bottom-bottomEdge,IDisplay.GameColour.WALL_FILL);

    left=x; 
    right=x+1;
    top=y;
    bottom=y+1;
    
    if (trimTopLeft)
    {
      d.pie((left-trimming)-outlineWidth, (top-trimming)-outlineWidth, (left+trimming)+outlineWidth, (top+trimming)+outlineWidth, 270, 90, GameColour.WALL_EDGE);
      d.pie((left-trimming), (top-trimming), (left+trimming), (top+trimming), 270, 90, GameColour.BACKGROUND);
    }
    else if (trimTopRight)
    {
      d.pie((right-trimming)-outlineWidth, (top-trimming)-outlineWidth, (right+trimming)+outlineWidth, (top+trimming)+outlineWidth, 180, 90, GameColour.WALL_EDGE);
      d.pie((right-trimming), (top-trimming), (right+trimming), (top+trimming), 180, 90, GameColour.BACKGROUND);
    }
    else if (trimBottomLeft)
    {
      d.pie((left-trimming)-outlineWidth, (bottom-trimming)-outlineWidth, (left+trimming)+outlineWidth, (bottom+trimming)+outlineWidth, 0, 90, GameColour.WALL_EDGE);
      d.pie((left-trimming), (bottom-trimming), (left+trimming), (bottom+trimming), 0, 90, GameColour.BACKGROUND);
    }
    else if (trimBottomRight)
    {
      d.pie((right-trimming)-outlineWidth, (bottom-trimming)-outlineWidth, (right+trimming)+outlineWidth, (bottom+trimming)+outlineWidth, 90, 90, GameColour.WALL_EDGE);
      d.pie((right-trimming), (bottom-trimming), (right+trimming), (bottom+trimming), 90, 90, GameColour.BACKGROUND);
    }

/*    
    if (trimTop)
    {
      top+=trimming;
      if (trimBottom)
      {
        bottom-=trimming;
        d.rectangle(x,y+trimming,x+1,y+1-trimming,IDisplay.GameColour.WALL_EDGE);
        d.rectangle(x,y+trimming+outlineWidth,x+1,y+1-trimming-outlineWidth,IDisplay.GameColour.WALL_FILL);
      }
      else if (trimLeft)
      {
        if (trimBottomRight)
        {
          d.rectangle(x+trimming,y+trimming,x+1,y+1-trimming,IDisplay.GameColour.WALL_EDGE);
          d.rectangle(x+trimming,y+1-trimming,x+1-trimming,y+1,IDisplay.GameColour.WALL_EDGE);
        }
        else
        {
          d.rectangle(x+trimming,y+trimming,x+1,y+1,IDisplay.GameColour.WALL_EDGE);
          d.rectangle(x+trimming+outlineWidth,y+trimming+outlineWidth,x+1,y+1,IDisplay.GameColour.WALL_FILL);
        }
      }
      else if (trimRight)
      {
        if (trimBottomLeft)
        {
          
        }
        else
        {
          d.rectangle(x,y+trimming,x+1-trimming,y+1,IDisplay.GameColour.WALL_EDGE);
          d.rectangle(x,y+trimming+outlineWidth,x+1-trimming-outlineWidth,y+1,IDisplay.GameColour.WALL_FILL);
        }
      }
      else
      {
        d.rectangle(x,y+trimming,x+1,y+1,IDisplay.GameColour.WALL_EDGE);
        d.rectangle(x,y+trimming+outlineWidth,x+1,y+1,IDisplay.GameColour.WALL_FILL);
      }
      
    }
    else if (trimLeft)
    {
      if (trimRight)
      {
        d.rectangle(x+trimming,y,x+1-trimming,y+1,IDisplay.GameColour.WALL_EDGE);
        d.rectangle(x+trimming+outlineWidth,y,x+1-trimming-outlineWidth,y+1,IDisplay.GameColour.WALL_FILL);
      }
      else if (trimBottomRight)
      {
        d.rectangle(x,y+trimming,x+1,y+1-trimming,IDisplay.GameColour.WALL_EDGE);
        d.rectangle(x,y+trimming+outlineWidth,x+1,y+1-trimming+outlineWidth,IDisplay.GameColour.WALL_FILL);
      }
      else
      {
        d.rectangle(x+trimming,y,x+1-trimming,y+1,IDisplay.GameColour.WALL_EDGE);
        d.rectangle(x+trimming+outlineWidth,y,x+1-trimming-outlineWidth,y+1,IDisplay.GameColour.WALL_FILL);
      }
    }
    else if (trimRight)
    {
      
    }
    else if (trimBottom)
    {
      
    }
    else
    {
      d.rectangle(x,y,x+1,y+1,IDisplay.GameColour.WALL_FILL);
    }*/
  }  
  
  public WallTile(GameBoard.Item item, GameBoard.Item aboveLeft,GameBoard.Item above,GameBoard.Item aboveRight,GameBoard.Item left,GameBoard.Item right,GameBoard.Item belowLeft, GameBoard.Item below,GameBoard.Item belowRight)
  {
    super(item);
    trimTop = above != Item.WALL;// && aboveRight!=item.WALL;
    trimBottom = below != Item.WALL;
    trimLeft = left != Item.WALL;
    trimRight =  right != Item.WALL;
    trimTopLeft = aboveLeft!=Item.WALL && above==Item.WALL && left==Item.WALL;
    trimTopRight = aboveRight!=Item.WALL && above==Item.WALL && right==Item.WALL;
    trimBottomLeft = belowLeft!=Item.WALL && below==Item.WALL && left==Item.WALL;
    trimBottomRight = belowRight!=Item.WALL && below==Item.WALL && right==Item.WALL;
    isInner = !(trimTop||trimBottom||trimLeft||trimRight||trimTopLeft||trimBottomLeft||trimTopRight||trimBottomRight);
    // special case for "off the edges" walls, and those around the gates
    if (trimTop&&trimBottom&&trimLeft)
    {
      trimLeft=false;
    }
    if (trimTop&&trimBottom&&trimRight)
    {
      trimRight=false;
    }
    
  }
}

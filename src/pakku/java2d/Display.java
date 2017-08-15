package pakku.java2d;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JPanel;

import pakku.views.IDisplay;

@SuppressWarnings("serial")
public class Display extends JPanel implements IDisplay
{ 
  private Image backingStore;

  public void update(Graphics g) {
    paint(g);
  }

  @Override
  public void paint(Graphics g) {
    // Show the offscreen image.
    g.drawImage(backingStore, 0, 0, null);
  }

  /*-- IDisplay ------------------------------------------*/
 
  
  protected class DirtyRegion
  {
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    
    DirtyRegion(int x1_,int y1_, int x2_, int y2_)
    {
      x1=x1_;
      y1=y1_;
      x2=x2_;
      y2=y2_;
    }
  }

  ArrayList<DirtyRegion> dirtyRegions;
  
  

  private Graphics getImageGraphics()
  {
    if (backingStore==null)
    {
      backingStore= createImage(getWidth(), getHeight());
    }
    return backingStore.getGraphics();
  }
  
  
  /*-- IDisplay ------------------------------------------*/
  
  @Override
  public void ellipse(double left, double top, double right, double bottom,  IDisplay.GameColour colour)
  {
    Graphics g = getImageGraphics();
    g.setColor(palette[colour.getValue()]);
    g.fillOval( (int)(left*tileWidth), (int)(top*tileHeight), (int)(Math.ceil((right-left)*tileWidth)), (int)(Math.ceil((bottom-top)*tileHeight)));
    
  }

  @Override
  public void pie(double left, double top, double right, double bottom, double startAtDegree, double sweep,  IDisplay.GameColour colour)
  {
    if (sweep==0) 
    {
      return;
    }
    if (sweep>=360)
    {
      ellipse(left,top,right,bottom,colour);
      return;
    }
    Graphics g = getImageGraphics();
    g.setColor(palette[colour.getValue()]);
    g.fillArc( (int)(left*tileWidth), (int)(top*tileHeight), (int)(Math.ceil((right-left)*tileWidth)), (int)(Math.ceil((bottom-top)*tileHeight)), (int)startAtDegree, (int)sweep);
  }

  @Override
  public void rectangle(double left, double top, double right, double bottom, IDisplay.GameColour colour)
  {
    Graphics g = getImageGraphics();
    g.setColor(palette[colour.getValue()]);
    g.fillRect( (int)(left*tileWidth), (int)(top*tileHeight), (int)(Math.ceil((right-left)*tileWidth)), (int)(Math.ceil((bottom-top)*tileHeight)));
    
    
  }
  
  public void clear()
  {
    Graphics g = getImageGraphics();
    g.clearRect(0, 0, getWidth(), getHeight());
  }
  
  @Override
  public void markDirty(int left, int top,  int beyondRight, int belowBottom)
  {
    dirtyRegions.add(new DirtyRegion(left,top,beyondRight,belowBottom));
  }
  
  /*-------------------------------------------- */
  
  double tileWidth;
  double tileHeight;
  Color palette[];
  
 
  public Display( double tileWidth_, double tileHeight_, int tilesAcross, int tilesDown)
  {
    tileWidth=tileWidth_;
    tileHeight=tileHeight_;
    setSize((int)(tileWidth*tilesAcross),(int)(tileHeight*tilesDown));
    
    dirtyRegions=new ArrayList<DirtyRegion>();
    
    
    
    palette = new Color[IDisplay.GameColour.NUMBER_OF_COLOURS.getValue()];
    
    palette[IDisplay.GameColour.BACKGROUND.getValue()]=new Color(0,0,0);
    palette[IDisplay.GameColour.WALL_EDGE.getValue()]=new Color( 0x20, 0x20, 0xD0);
    palette[IDisplay.GameColour.WALL_FILL.getValue()]=new Color( 0x10,0x10,0x30);
    palette[IDisplay.GameColour.PILL.getValue()]=new Color(0xF0,0xC0,0xA0);
    palette[IDisplay.GameColour.PAKKU.getValue()]=new Color(0xFE,0xD0,0);
    palette[IDisplay.GameColour.GATE.getValue()]=new Color(0x80,0x50,0xf0);
    palette[IDisplay.GameColour.GHOST_RED.getValue()]=new Color(0xE0,0,0);
    palette[IDisplay.GameColour.GHOST_PINK.getValue()]=new Color(0xF8,0x80,0x90);
    palette[IDisplay.GameColour.GHOST_BLUE.getValue()]=new Color(0, 0xC0, 0xF8);
    palette[IDisplay.GameColour.GHOST_ORANGE.getValue()]=new Color(0xF8,0x90,0);
    palette[IDisplay.GameColour.GHOST_WORRIED_DIM.getValue()]=new Color(0x0,0x0,0xa0);
    palette[IDisplay.GameColour.GHOST_WORRIED_BRIGHT.getValue()]=new Color(0x80,0x80,0xF0);
    palette[IDisplay.GameColour.GHOST_EYEBALL.getValue()]=new Color(0xf8,0xf8,0xf8);
    palette[IDisplay.GameColour.GHOST_PUPIL.getValue()]=new Color( 32, 32, 32);
    
  }
  
  public void flush()
  {
    Graphics g=this.getGraphics();
    for (DirtyRegion r: dirtyRegions)
    {
      int left = (int)(tileWidth*r.x1);
      int right =(int)(tileWidth*r.x2) -1;
      int top = (int)(tileHeight*r.y1);
      int bottom = (int)(tileHeight*r.y2) -1;
      
      g.drawImage(backingStore, left,top, right, bottom, left, top, right, bottom, null);
    }
    dirtyRegions.clear();
  }
  

}

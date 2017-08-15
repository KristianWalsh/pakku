package pakku;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PakkuWindow extends Frame
{
  JPanel display;
  
  public PakkuWindow(int tileWidth,int tileHeight, int tilesAcross, int tilesDown)
  {
    super("Pakkuman");

    setSize((int)(tileWidth*tilesAcross),(int)(tileHeight*tilesDown)+32);
    setResizable(false);
  }

  public void attachDisplay( JPanel display_)
  {
    display=display_;
    add(display);
    display.setVisible(true);
  }

  public void paint(Graphics g) 
  {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, WIDTH, HEIGHT);
    if (display!=null)
    {
      display.repaint();
    }
  }

  
  @Override
  public void paintAll(Graphics g)
  {
    super.paintAll(getGraphics());
  }
 
  
  
  
}

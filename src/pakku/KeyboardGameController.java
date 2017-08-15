package pakku;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import pakku.controllers.Game.IGameControlSource;

public class KeyboardGameController implements KeyListener, IGameControlSource
{
  /** bitmask for "pressed" keys: bit 4...0:  {FIRE}{UP}{DOWN}{LEFT}{RIGHT} */ 
  int bits;
  

  private int keyCodeToJoystickBit( int keycode )
  {
    int ret=0;
    switch (keycode)
    {
      case KeyEvent.VK_KP_RIGHT:
      case KeyEvent.VK_RIGHT:
      case KeyEvent.VK_D:
        ret=0x01;
        break;
        
      case KeyEvent.VK_KP_LEFT:
      case KeyEvent.VK_LEFT:
      case KeyEvent.VK_A:
        ret=0x02;
        break;

      case KeyEvent.VK_KP_DOWN:
      case KeyEvent.VK_DOWN:
      case KeyEvent.VK_X:
      case KeyEvent.VK_S:
        ret=0x04;
        break;
      
      case KeyEvent.VK_KP_UP:
      case KeyEvent.VK_UP:
      case KeyEvent.VK_W:
        ret=0x08;
        break;
      
      case KeyEvent.VK_SPACE:
      case KeyEvent.VK_ENTER:
        ret=0x10;
        break;
    }
    return ret;
  }
  
  
  @Override
  public void keyPressed(KeyEvent arg0)
  {
    bits |= keyCodeToJoystickBit(arg0.getKeyCode());
  }

  @Override
  public void keyReleased(KeyEvent arg0)
  {
    bits &= ~(keyCodeToJoystickBit(arg0.getKeyCode()));
  }

  @Override
  public void keyTyped(KeyEvent arg0)
  {
    return; // no interested in typing events.
    
  }

  /*- IGameControlSource -------------------------------------------*/

  @Override
  public JoystickPosition getJoystickPosition()
  {
    JoystickPosition pos=JoystickPosition.NONE;
    if ((bits&0x10) != 0)
    {
      pos=JoystickPosition.FIRE;
    }
    else if ((bits&0x08) != 0)
    {
      pos=JoystickPosition.UP;
    }
    else if ((bits&0x04) != 0)
    {
      pos=JoystickPosition.DOWN;
    }
    else if ((bits&0x02) != 0)
    {
      pos=JoystickPosition.LEFT;
    }
    else if ((bits&0x01) != 0)
    {
      pos=JoystickPosition.RIGHT;
    }
      
    return pos;
  }

  /*--------------------------------------------*/
  
  public void KeyboardGameController(Component eventSource)
  {
    bits=0;
    eventSource.addKeyListener(this);
  }
  
  
  
}

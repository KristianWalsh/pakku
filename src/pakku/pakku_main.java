package pakku;

import pakku.models.*;
import pakku.views.*;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import pakku.controllers.Game;
import pakku.controllers.Ghost;
import pakku.controllers.Player;
import pakku.java2d.*;

public class pakku_main
{
  public static class WindowCloser implements WindowListener
  {

    @Override
    public void windowActivated(WindowEvent e)
    {
      // TODO Auto-generated method stub

    }

    @Override
    public void windowClosed(WindowEvent e)
    {
      // TODO Auto-generated method stub

    }

    @Override
    public void windowClosing(WindowEvent e)
    {
      // TODO Auto-generated method stub
      System.exit(0);
    }

    @Override
    public void windowDeactivated(WindowEvent e)
    {
      // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent e)
    {
      // TODO Auto-generated method stub

    }

    @Override
    public void windowIconified(WindowEvent e)
    {
      // TODO Auto-generated method stub

    }

    @Override
    public void windowOpened(WindowEvent e)
    {
      // TODO Auto-generated method stub

    }

    public WindowCloser() {

    }
  }

  public static void main(String[] args)
  {
    PakkuWindow window = new PakkuWindow(24, 24, GameBoard.BOARD_WIDTH, GameBoard.BOARD_HEIGHT);
    window.addWindowListener(new WindowCloser());
    Display display = new Display(24, 24, GameBoard.BOARD_WIDTH, GameBoard.BOARD_HEIGHT);
    window.attachDisplay(display);
    window.setVisible(true);
    display.paint(window.getGraphics());
    
    KeyboardGameController joystick = new KeyboardGameController();
    window.addKeyListener(joystick);

    GameBoard board = new GameBoard();

    Player pakku = new Player(13.5, 17, 0.1);

    ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
     ghosts.add(new Ghost(13.5,11,0.09, Ghost.Identity.BLINKY, pakku));
     ghosts.add(new Ghost(11.5,13,0.11, Ghost.Identity.INKY, pakku));
     ghosts.add(new Ghost(13.5,13,0.095, Ghost.Identity.CLYDE,pakku));
    ghosts.add(new Ghost(15.5, 13, 0.12, Ghost.Identity.PINKY, pakku));

    ArrayList<BasicActorView> sprites = new ArrayList<BasicActorView>();

    GameBoardView boardView = new GameBoardView(display, board);
    for (Ghost g : ghosts) {
      sprites.add(new GhostActorView(display, boardView, g));
    }
    sprites.add(new PlayerActorView(display, boardView, pakku));

    /*
     * boardView.redrawAll(); for (BasicActorView s: sprites) { s.draw(); }
     */

    Game theGame = new Game(joystick, board, pakku, ghosts, 3);
    theGame.startGame();

    int interval = 16;  // ~60 ticks per second.
    while (true) {
      int now =(int)( System.currentTimeMillis() % 960 );
      int waitTime = interval-(now%interval); 
      try 
      {
        Thread.sleep(waitTime);
      }
      catch (InterruptedException e)
      {

      }

      theGame.tick();
      for (BasicActorView s : sprites) {
        s.draw();
      }
      display.flush();
    }

  }
}

package pakku.controllers;

import java.util.ArrayList;
import java.util.HashSet;

import pakku.models.GameBoard;
import pakku.models.GameBoard.IGameBoardObserver;
import pakku.models.GameBoard.Item;

public class Game implements IActorObserver, IGameBoardObserver
{
  int score;
  int level;
  int lives;
  int remainingPills;
  int powerPillTimer;
  
  public interface IGameObserver
  {
    void onScoreChanged(Game g,int score);
    void onLevelChanged(Game g,int level);
    void onPlayerLivesChanged(Game g,int lives);
  }
  private HashSet<IGameObserver> observers;
  public void addObserver(IGameObserver observer)
  {
    observers.add(observer);
  }
  public void removeObserver(IGameObserver observer)
  {
    observers.remove(observer);
  }
  
  
  /**
   * Defines the functions that the Game object needs from an external
   * control device.  Conceptually, this is a joystick, but the 
   * more likely implementation is a keyboard, mouse, or touch-device
   * @author Kristian
   *
   */
  public interface IGameControlSource
  {
    enum JoystickPosition
    {
      UP,
      DOWN,
      RIGHT,
      LEFT,
      FIRE,
      NONE
    }

    /** return the current position of the "joystick" controller */
    public JoystickPosition getJoystickPosition();
  }
  
  private IGameControlSource input;
  public IGameControlSource getInput()
  {
    return input;
  }
  
  private GameBoard board;
  public GameBoard getBoard()
  {
    return board;
  }
  
  /** the moving players/enemies in the game */
  private ArrayList<IActor> actors;
  private Player player;
  
  
  public Game( IGameControlSource input_, GameBoard board_, Player player_, ArrayList<Ghost> ghosts, int lives_)
  {
    observers = new HashSet<IGameObserver>();
    input = input_;
    board = board_;
    player = player_;
    score=0;
    lives=lives_;
    
    actors=new ArrayList<IActor>();
    actors.add(player);
    for (Ghost g: ghosts)
    {
      actors.add(g);
      g.addObserver(this);
    }
    player.addObserver(this);
    board.addObserver(this);
    
    level=-1;
  }

  public void startGame()
  {
    level=-1;
    changeScore(0);
    changeLives(0);
    startLevel();
  }
  
  
  /** the game loop */
  public void tick()
  {
    for (IActor a: actors)
    {
      a.tick(this);
      if (a==player) continue;
      player.collide(a);
    }

    if (powerPillTimer>0)
    {
      --powerPillTimer;
      if (powerPillTimer==0)
      {
        endPowerPill();
      }
    }
  }
  
  
  private void startLevel()
  {
    powerPillTimer=0;
    board.reset();
    changeScore(score);
    for (IActor a: actors)
    {
      a.reset();
    }
  }
  
  /*- provide information to observers ---------------------------------------*/

  void changeScore( int newScore )
  {
    score=newScore;
    for (IGameObserver o: observers)
    {
      o.onScoreChanged(this, score);
    }
  }
  
  void changeLevel( int newLevel )
  {
    level=newLevel;
    for (IGameObserver o: observers)
    {
      o.onScoreChanged(this, level);
    }
  }
  
  void changeLives( int newLives )
  {
    lives=newLives;
    for (IGameObserver o: observers)
    {
      o.onScoreChanged(this, lives);
    }
  }

  
  /*------------------------------------------------------------*/
 
  private void startPowerPill()
  {
    powerPillTimer=600;
    for (IActor a: actors)
    {
      if ( a instanceof Ghost)
      {
        Ghost ghost=(Ghost)a;
        if (ghost.getState()==Ghost.State.ACTIVE)
        {
          ghost.setState(Ghost.State.SCARED);
        }
      }
    }
  }
  
  private void endPowerPill()
  {
    powerPillTimer=0;
    for (IActor a: actors)
    {
      if ( a instanceof Ghost)
      {
        Ghost ghost=(Ghost)a;
        if (ghost.getState()==Ghost.State.SCARED)
        {
          ghost.setState(Ghost.State.ACTIVE);
        }
      }
    }
  }

  
  /*- IGameBoardObserver ---------------------------------------*/
  
  @Override
  public void onReset(GameBoard b)
  {
    remainingPills=0;
      // count the number of pills on the board.
    for (int y=0; y<GameBoard.BOARD_HEIGHT; ++y)
    {
      for (int x=0; x<GameBoard.BOARD_HEIGHT; ++x)
      {
        if (b.getItemAt(x, y)==GameBoard.Item.PILL)
        {
          ++remainingPills;
        }
      }
    } 
  }
  
  
  @Override
  public void onChange(GameBoard b, int x, int y, Item oldContent, Item newContent)
  {
    if (oldContent.equals(GameBoard.Item.POWERPILL))
    {
      changeScore(score+50);
      startPowerPill();
    }
    else
    if (oldContent.equals(GameBoard.Item.PILL))
    {
      --remainingPills;
      changeScore(score+10);
      if (remainingPills<=0)
      {
        changeLevel(level+1);
        startLevel();
      }
    }
    
  }

  /*- IActor observer ----------------------*/
  
  @Override
  public void onMoved(IActor what, double oldX, double oldY)
  {
    return; // don't care about player movement.
  }

  @Override
  public void onChanged(IActor actor, String whatChanged)
  {
    if (actor==player && whatChanged=="State" && player.getState()==Player.State.DEAD)
    {
      changeLives(lives-1);
      startLevel();
    }
  }
  
}

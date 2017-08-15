package pakku.views;

import pakku.controllers.Game;
import pakku.models.GameBoard;

public class GameView implements Game.IGameObserver
{
  private Game game;
  private IDisplay display;
  private GameBoardView boardView;
  
  public GameView(Game g, IDisplay drawOnThis)
  {
    game = g;
    display=drawOnThis;
    
    boardView = new GameBoardView(display,game.getBoard());
  }

  
  /*-- IGameObserver ---------------------------------------- */
  @Override
  public void onScoreChanged(Game g, int score)
  {
    // TODO Auto-generated method stub
  }

  @Override
  public void onLevelChanged(Game g, int level)
  {
    // TODO Auto-generated method stub
  }

  @Override
  public void onPlayerLivesChanged(Game g, int lives)
  {
    // TODO Auto-generated method stub
  }
  

}

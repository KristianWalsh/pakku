package pakku.views;

import pakku.models.*;
import pakku.models.GameBoard.IGameBoardObserver;
import pakku.models.GameBoard.Item;

public class GameBoardView implements IGameBoardObserver
{
  private GameBoard board;
  private IDisplay display;
  
  private Tile[][] tiles;
  
  public void drawTile( int x, int y)
  {
    if (x<0 || y<0 || x>=GameBoard.BOARD_WIDTH || y>=GameBoard.BOARD_HEIGHT)
    {
      return;
    }
 
    tiles[y][x].drawAt(display, x, y);
  }
  
  public void redrawAll()
  {
    for (int y=0; y<GameBoard.BOARD_HEIGHT;++y)
    {
      for (int x=0; x<GameBoard.BOARD_WIDTH; ++x)
      {
        drawTile(x,y);
      }
    }
    display.markDirty(0, 0, GameBoard.BOARD_WIDTH, GameBoard.BOARD_HEIGHT);
  }
  

  public GameBoardView( IDisplay display_, GameBoard board_ )
  {
    display=display_; 
    board=board_;
    board.addObserver(this);
    rebuildTiles();
  }

  
  private void rebuildTiles()
  {
    tiles=new Tile[GameBoard.BOARD_HEIGHT][GameBoard.BOARD_WIDTH];
    for (int y=0; y<GameBoard.BOARD_HEIGHT;++y)
    {
      for (int x=0; x<GameBoard.BOARD_WIDTH; ++x)
      {
        GameBoard.Item i = board.getItemAt(x, y);
        if (i==GameBoard.Item.WALL)
        { // "maze wall" tiles display differently depending on surrounding tiles, so provide that info
          tiles[y][x]= new WallTile(i, 
               board.getItemAt(x-1, y-1), board.getItemAt(x, y-1), board.getItemAt(x+1, y-1),
               board.getItemAt(x-1, y),                         board.getItemAt(x+1, y),
               board.getItemAt(x-1, y+1), board.getItemAt(x, y+1), board.getItemAt(x+1, y+1)
              );
        }
        else
        {
          tiles[y][x]=new Tile(i);
        }
     }
    }
  
  }
  
  
  
  @Override
  public void onReset(GameBoard b)
  {
    rebuildTiles();
    redrawAll();
  }

  @Override
  public void onChange(GameBoard b, int x, int y, Item oldContent, Item newContent)
  {
    tiles[y][x]=new Tile(newContent);
    drawTile( x, y); 
    display.markDirty(x, y, x+1, y+1);
  }
  
}

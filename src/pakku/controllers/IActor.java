package pakku.controllers;

/** Player or enemy - anything that can move around the game board */
public interface IActor
{
  /** horizontal position of this actor on the game board */
  double getX();
  /** vertical position of this actor on the game board */
  double getY();

  /** called once per game loop */
  public void tick(Game g);
  
  /** return to initial state */
  public void reset();

  /** attach observer */
  public void addObserver(IActorObserver observer);

  /** remove observer */
  public void removeObserver(IActorObserver observer);
}

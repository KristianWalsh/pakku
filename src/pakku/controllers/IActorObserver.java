package pakku.controllers;

/** expected functions from a class that observes an IActor object */
public interface IActorObserver
{
  void onMoved(IActor what, double oldX, double oldY);
  void onChanged(IActor what, String whatChanged);
}

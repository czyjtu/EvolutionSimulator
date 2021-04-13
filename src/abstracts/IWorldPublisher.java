package abstracts;

import world.MapElement;

public interface IWorldPublisher {
    public void newElementNotification(MapElement element);
    public void elementRemovalNotification(MapElement element);
    public void addWorldObserver(IWorldObserver observer);
    public void removeWorldObserver(IWorldObserver observer);
}

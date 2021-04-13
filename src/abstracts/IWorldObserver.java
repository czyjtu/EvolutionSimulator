package abstracts;

import world.MapElement;

public interface IWorldObserver {
    public void newElementUpdate(MapElement element);
    public void elementRemovalUpdate(MapElement element);

}

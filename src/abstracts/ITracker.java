package abstracts;

import world.MapElement;

public interface ITracker {
    public void track(MapElement element, int timeInterval);
    public void stopTracking();
    public boolean isTracking();

}

package abstracts;

import utils.Coordinates;

public interface IElementPositionPublisher {

    public void addPositionObserver( IElementPositionObserver observer);
    public void removePositionObserver( IElementPositionObserver observer);
    public void notifyPositionUpdate(Coordinates oldCoor);

}

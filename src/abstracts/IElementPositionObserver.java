package abstracts;

import utils.Coordinates;
import world.MapElement;

public interface IElementPositionObserver {
    public void updatePosition(Coordinates oldCoor, MapElement updatedElement);
}

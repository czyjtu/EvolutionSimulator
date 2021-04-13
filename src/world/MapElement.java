package world;

import abstracts.*;
import utils.Coordinates;

import java.util.ArrayList;

public abstract class MapElement implements IElementPositionPublisher {
    public Coordinates coordinates;
    protected int energy;
    protected final ArrayList<IElementEnergyObserver> energyObservers = new ArrayList<>();
    protected final ArrayList<IElementPositionObserver> positionObservers = new ArrayList<>();

    public MapElement(int x, int y, int energy){
        this.coordinates = new Coordinates(x, y);
        this.energy = energy;

    }

    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    public int getX() {
        return coordinates.getX();
    }
    public int getY(){
        return coordinates.getY();
    }

    public int getEnergy(){
        return this.energy;
    }

    @Override
    public void addPositionObserver(IElementPositionObserver observer) {
        this.positionObservers.add(observer);
    }

    @Override
    public void removePositionObserver(IElementPositionObserver observer) {
        this.energyObservers.remove(observer);
    }

    @Override
    public void notifyPositionUpdate(Coordinates oldCoor) {
        for (IElementPositionObserver observer : positionObservers)
            observer.updatePosition(oldCoor, this);
    }

    public boolean containsPositionObserver(IElementPositionObserver observer){
        return this.positionObservers.contains(observer);
    }

}

package abstracts;

public interface IElementEnergyPublisher {
    public void addEnergyObserver( IElementEnergyObserver observer);
    public void removeEnergyObserver( IElementEnergyObserver observer);
    public void notifyEnergyUpdate();
}

package abstracts;

public interface IReproductive {
    public void breedWith(IReproductive creature);
    public boolean accept();
    public boolean satisfyBreedingConditions();
}

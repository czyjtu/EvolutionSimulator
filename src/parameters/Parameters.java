package parameters;

public class Parameters {
    public final long width;
    public final long height;
    public final double jungleRatio;
    public final long startEnergy;
    public final long moveEnergy;
    public final long plantEnergy;
    public final long statsDay;
    public long numOfAnimals;

    public Parameters(long width, long height, double jungleRatio, long startEnergy, long moveEnergy,
                      long plantEnergy, long numOfAnimals, long days) {
        this.width = width;
        this.height = height;
        this.jungleRatio = jungleRatio;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.numOfAnimals = numOfAnimals;
        this.statsDay = days;
    }
}

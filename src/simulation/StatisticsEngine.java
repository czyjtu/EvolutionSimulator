package simulation;

import abstracts.ITracker;
import abstracts.IWorldObserver;
import abstracts.IWorldPublisher;
import parameters.ParametersParser;
import world.Animal;
import utils.Genotype;
import world.MapElement;
import world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class StatisticsEngine implements IWorldObserver, ITracker {

    private final Simulation simulation;
    private int numOfPlants;
    private int deathCounter;
    private float averageLifeLength;
    private float averageKidsNum;
    private final HashMap<Genotype, Integer> currentGenotypes = new HashMap<>();
    private final IWorldPublisher world;
    private HashSet<Animal> family;
    private int trackingKidsNum;
    private boolean isTracking;
    private int trackingDeathDay;
    private int trackingPeriod;
//  simulation average
    private double simulationAverageKidsNum;
    private double simulationAverageEnergyLevel;
    private double simulationAveragePlantsNum;
    private double simulationAverageAnimalsNum;
    private double simulationAverageLifeLength;
    private Animal trackedAnimal;


    public StatisticsEngine(IWorldPublisher world, Simulation simulation) {
        world.addWorldObserver(this);
        this.simulation = simulation;
        this.world = world;
        this.numOfPlants = 0;
        this.averageLifeLength = 0;
        this.averageKidsNum = 0;
        this.deathCounter = 0;
        this.isTracking = false;
        this.trackingKidsNum = -1;
        this.trackingDeathDay = -1;

        simulationAverageKidsNum = 0;
        simulationAverageEnergyLevel = 0;
        simulationAveragePlantsNum = 0;
        simulationAverageAnimalsNum = 0;
        simulationAverageLifeLength = 0;
    }

    @Override
    public void newElementUpdate(MapElement element) {
        if(element instanceof Animal) {
            Animal animal = (Animal) element;

//          Tracking stats update
            if(this.isTracking && animal.hasParents()){
                if(family.contains(animal.getParents().get(0)) || family.contains(animal.getParents().get(1)))
                    this.family.add(animal);

                if(animal.getParents().get(0).isTracked() || animal.getParents().get(1).isTracked())
                    this.trackingKidsNum++;
            }

//          Genotype stats update
            if(this.currentGenotypes.containsKey(animal.getGenotype())){
                Integer counter = this.currentGenotypes.get(animal.getGenotype());
                this.currentGenotypes.put(animal.getGenotype(), ++counter);
            }
            else
                this.currentGenotypes.put(animal.getGenotype(), 1);

//            if(animal.hasParents()) {
//                if (this.averageKidsNum != 0)
//                    this.averageKidsNum = (this.averageKidsNum * (this.getNumOfAnimals() - 1) + 2) / this.getAverageKidsNum();
//                else
//                    this.averageKidsNum = 1;
//            }
//            else if(this.averageKidsNum != 0)
//                this.averageKidsNum /= this.getNumOfAnimals();

        }else{
            this.numOfPlants ++;
        }
    }

    @Override
    public void elementRemovalUpdate(MapElement element) {
        if(element instanceof Animal) {
            Animal animal = (Animal) element;

            if(animal.isTracked()){
                this.trackingDeathDay = (int)simulation.getDay();
            }
            int counter = this.currentGenotypes.get(animal.getGenotype());
            if(counter == 1)
                this.currentGenotypes.remove(animal.getGenotype());
            else
                this.currentGenotypes.put(animal.getGenotype(), counter - 1);

            if(this.averageLifeLength == 0)
                this.averageLifeLength = animal.getAge();
            else
                this.averageLifeLength = (this.averageLifeLength*this.deathCounter + animal.getAge())/(this.deathCounter + 1);
            this.deathCounter ++;
        }
    }

    public Map.Entry<Genotype, Integer> getMostCommonGenotype(){
        int max = 0;
        Map.Entry<Genotype, Integer> mostCommon = null;
        for(Map.Entry<Genotype, Integer> set : this.currentGenotypes.entrySet())
            if(set.getValue() > max) {
                max = set.getValue();
                mostCommon = set;
            }

        return mostCommon;
    }

    public double getAverageEnergyLevel(){
        return ((World)this.world).getAnimals().stream()
                .mapToInt(Animal::getEnergy)
                .average()
                .orElse(Double.NaN);
    }

    public int getNumOfPlants() {
        return ((World)this.world).getNumOfPlants();
    }

    public float getAverageLifeLength() {
        return averageLifeLength;
    }

    public double getAverageKidsNum() {
        return ((World)this.world).getAnimals().stream()
                .mapToInt(Animal::numOfKids)
                .average()
                .orElse(Double.NaN);
    }

    public int getNumOfAnimals(){
        return ((World)this.world).getNumOfAnimals();
    }

    public int getNumOfDeaths(){
        return this.deathCounter;
    }

    public void updateAverageStats(){
        long prevDay = this.simulation.getDay() -1;
        long day = prevDay + 1;

        simulationAverageKidsNum = (double)(simulationAverageKidsNum*prevDay + getAverageKidsNum())/(double)day;
        simulationAverageEnergyLevel = (double)(simulationAverageEnergyLevel*prevDay + getAverageEnergyLevel())/(double)day;
        simulationAveragePlantsNum = (double)(simulationAveragePlantsNum*prevDay + getNumOfPlants())/(double)day;
        simulationAverageAnimalsNum = (double)(simulationAverageAnimalsNum*prevDay + getNumOfAnimals())/(double)day;
        simulationAverageLifeLength = (double)(simulationAverageLifeLength*prevDay + getAverageLifeLength())/(double)day;
    }

//Tracking methods

    public int getTrackedOffspringSize(){
        return this.family.size() - 1; // Tracked element is also part of the family
    }

    public int getTrackedNumOfKids(){
        return this.trackingKidsNum;
    }

    public int getTrackedDeathDay(){
        return this.trackingDeathDay;
    }

//    saving

    public void saveTrackingStats(String fileName){
        HashMap<String, Double> stats = new HashMap<String, Double>() {{
            put("Potomkowie", (double) getTrackedOffspringSize());
            put("Dzieci", (double) getTrackedNumOfKids());
            put("DeathDay",  (double)getTrackedDeathDay());
        }};
        ParametersParser.saveStats(stats, fileName);
    }

    public void saveSimulationAverageStats(String fileName){
        HashMap<String, Double> stats = new HashMap<String, Double>() {{
            put("NumberOfAnimals", (double) getSimulationAverageAnimalsNum());
            put("NumberOfPlants", (double) getSimulationAveragePlantsNum());
            put("AverageEnergLevel",  (double)getSimulationAverageEnergyLevel());
            put("AverageNumberOfKids",  (double)getSimulationAverageKidsNum());
            put("AverageLifeLength", (double) getSimulationAverageLifeLength());
        }};

        ParametersParser.saveStats(stats, fileName);
    }

    public void decreaseTrackingPeriod(){
        this.trackingPeriod --;
        if(trackingPeriod == 0){
            this.saveTrackingStats(this.trackedAnimal.hashCode() + "tracking");
            this.stopTracking();
        }
    }

    @Override
    public void track(MapElement element, int timeInterval) {
        this.trackedAnimal = (Animal) element;
        this.family = new HashSet<>();
        this.isTracking = true;
        this.family.add((Animal)element);
        this.trackingKidsNum = 0;
        this.trackingPeriod = timeInterval;
    }

    @Override
    public void stopTracking() {
        this.trackedAnimal = null;
        this.trackingPeriod = -1;
        this.isTracking = false;
        this.family.clear();
    }

    @Override
    public boolean isTracking() {
        return this.isTracking;
    }

    public double getSimulationAverageKidsNum() {
        return simulationAverageKidsNum;
    }

    public double getSimulationAverageEnergyLevel() {
        return simulationAverageEnergyLevel;
    }

    public double getSimulationAveragePlantsNum() {
        return simulationAveragePlantsNum;
    }

    public double getSimulationAverageAnimalsNum() {
        return simulationAverageAnimalsNum;
    }

    private double getSimulationAverageLifeLength() {
        return simulationAverageLifeLength;
    }
}

package simulation;

import parameters.Parameters;
import parameters.ParametersParser;
import world.World;

public class Simulation{
    private World world;
    private StatisticsEngine statistics;
    private Parameters parameters;
    private int delay;
    private long statsDay;
    private long day;
    private boolean isRunning = false;

    public Simulation(){
        this.parameters = ParametersParser.parse("parameters.json");
        this.world = new World(
                parameters.height,
                parameters.width,
                parameters.startEnergy,
                parameters.moveEnergy,
                parameters.plantEnergy,
                parameters.jungleRatio
                );
        this.statistics = new StatisticsEngine(world, this);
        world.initialize((int)parameters.numOfAnimals);

        this.statsDay = parameters.statsDay;

        this.day = 0;

    }

    public Parameters getParameters(){
        return this.parameters;
    }

    public void update(){
        long startTime;
        long endTime;
        long duration;
        startTime = System.currentTimeMillis();
        this.world.moveAnimals();
        this.world.letAnimalsEat();
        this.world.letAnimalsBreed();
        this.world.growPlants();
        this.isRunning = this.world.isRunning();
        this.day ++;
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        this.delay = (int)duration;
        this.statistics.updateAverageStats();
        this.statistics.decreaseTrackingPeriod();
    }

    public void save(String fileName){
        this.statistics.saveSimulationAverageStats(fileName);
    }

    public int getDelay(){
        return this.delay;
    }

    public long getDay() {
        return day;
    }

    public boolean isRunning(){
        return this.isRunning;
    }

    public void stop(){
        this.isRunning = false;
    }

    public void start(){
        this.isRunning = true;
    }

    public World getWorld(){
        return this.world;
    }

    public long getStatsDay(){
        return this.statsDay;
    }

    public StatisticsEngine getStatistics(){
        return this.statistics;
    }




}

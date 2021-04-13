package world;

import abstracts.IWorldObserver;
import abstracts.IWorldPublisher;
import utils.AnimalEnergyComperator;
import utils.AnimalsContainer;
import utils.Coordinates;
import utils.Direction;

import java.util.*;

public class World implements IWorldPublisher {
    private final ArrayList<IWorldObserver> observers = new ArrayList<IWorldObserver>();
    private final int width;
    private final int height;
    private final int startEnergy;
    private final int moveEnergy;
    private final int plantEnergy;
    private final Coordinates jungleUpLeft;
    private final Coordinates jungleDownRight;
    private final AnimalsContainer animals = new AnimalsContainer();
    private final Map<Coordinates, Plant> plants = new HashMap<Coordinates, Plant>();
    private int jungleCounter = 0;
    private int steppeCounter = 0;
    private int jungleCapacity;
    private int steppeCapacity;
//    private final List<Coordinates> freeJunglePos = new LinkedList<>();
//    private final List<Coordinates> freeSteppePos = new LinkedList<>();
    private boolean isRunning = false;

    public World(long height, long width, long startEnergy, long moveEnergy, long plantEnergy, double jungleRatio) {
        this.height = (int) height;
        this.width = (int) width;
        this.startEnergy = (int) startEnergy;
        this.moveEnergy = (int) moveEnergy;
        this.plantEnergy = (int) plantEnergy;
        this.jungleUpLeft = new Coordinates((int) (this.width * (1 - jungleRatio)) / 2,
                (int) (this.height * (1 - jungleRatio)) / 2);
        this.jungleDownRight = new Coordinates(this.jungleUpLeft.getX() + (int) (jungleRatio * this.width),
                this.jungleUpLeft.getY() + (int) (jungleRatio * this.height));


        int a = this.jungleDownRight.getX() - this.jungleUpLeft.getX() + 1;
        int b = this.jungleDownRight.getY() - this.jungleUpLeft.getY() + 1;
        this.jungleCapacity = a*b;
        this.steppeCapacity = (int) (width*height - this.jungleCapacity);
    }


    public void initialize(int numOfAnimals){
        this.isRunning = true;
        this.initializeRandomAnimals(numOfAnimals);
        this.growPlants();
    }

    private void initializeRandomAnimals(int numOfAnimals){
        Random rand = new Random();

        while(animals.size() < numOfAnimals){
            Animal animal = new Animal(rand.nextInt(width), rand.nextInt(height), startEnergy, this);
            if (animals.isFree(animal.getCoordinates())) {
                this.addAnimal(animal);
            }

        }
    }
    public Animal getAnimal(Coordinates coor){
        if(this.animals.isFree(coor))
            return null;
        else
            return this.animals.getStrongest(coor).get(0);
    }

    public void growPlants(){
        if( (this.jungleCapacity > this.jungleCounter) && (this.steppeCapacity > this.steppeCounter) ) {
            Coordinates coorJungle = Coordinates.randInSquare(this.jungleUpLeft, this.jungleDownRight);
            int attempts = 0;
            while (!this.isFree(coorJungle)  && attempts < 30) {
                coorJungle = Coordinates.randInSquare(this.jungleUpLeft, this.jungleDownRight);
                attempts ++;
            }

            if(!this.isFree(coorJungle) || !this.isInJungle(coorJungle)){

                LinkedList<Coordinates> freeJunglePos = new LinkedList<>();
                for (int x = this.jungleUpLeft().getX(); x < this.jungleDownRight().getX(); x++)
                    for (int y = this.jungleUpLeft().getY(); y < this.jungleDownRight().getY(); y++) {
                        Coordinates coor = new Coordinates(x, y);
                        freeJunglePos.add(coor);
                    }

                Random rand = new Random();
                coorJungle = freeJunglePos.get(rand.nextInt(freeJunglePos.size()));
            }

            Coordinates coorSteppe = Coordinates.randInSquare(new Coordinates(0, 0), new Coordinates(width, height));
            while (!this.isFree(coorSteppe) && this.isInJungle(coorSteppe))
                coorSteppe = Coordinates.randInSquare(new Coordinates(0, 0), new Coordinates(width, height));

            this.addPlant(new Plant(coorJungle.getX(), coorJungle.getY(), this.plantEnergy));
            this.addPlant(new Plant(coorSteppe.getX(), coorSteppe.getY(), this.plantEnergy));
        }
    }

    private void addPlant(Plant plant){
        this.newElementNotification(plant);
        this.plants.put(plant.getCoordinates(), plant);
        if(this.isInJungle(plant.getCoordinates()))
            this.jungleCounter ++;
        else
            this.steppeCounter ++;
    }

    public void addAnimal(Animal animal){
        this.animals.add(animal);
        this.newElementNotification(animal);
    }

    public void removeAnimal(Animal animal){
//        Coordinates toFree = animal.getCoordinates();
        this.animals.remove(animal);

        if(this.animals.isEmpty())
            this.isRunning = false;

        this.elementRemovalNotification(animal);
    }

    public boolean isInJungle(Coordinates coor){
        return (coor.getX() >= this.jungleUpLeft.getX() && coor.getX() <= this.jungleDownRight.getX()) &&
                (coor.getY() >= this.jungleUpLeft.getY() && coor.getY() <= this.jungleDownRight.getY());

    }

    public int getNumOfPlants(){
        return this.plants.size();
    }

    public int getNumOfAnimals(){
        return this.animals.size();
    }

    public void letAnimalsEat(){
        LinkedList<Plant> toBeRemoved = new LinkedList<>();
        for(Map.Entry<Coordinates, Plant> set : this.plants.entrySet()){
            if(!this.animals.isFree(set.getKey())){
                ArrayList<Animal> strongest = this.animals.getStrongest(set.getKey());
                for(Animal a: strongest){
                    a.eat(set.getValue().getEnergy()/strongest.size());
                    toBeRemoved.add(set.getValue());
                }
            }
        }
        for(Plant plant: toBeRemoved) {
            if(this.isInJungle(plant.getCoordinates()))
                this.jungleCounter --;
            else
                this.steppeCounter --;

            this.plants.remove(plant.getCoordinates());
        }
    }

    public void letAnimalsBreed(){
        HashMap<Coordinates, Animal[]> breedingMap = new HashMap<>();
        for(Map.Entry<Coordinates, ArrayList<Animal>> element: this.animals.entrySet()){
            if(element.getValue().size() > 1){
                ArrayList<Animal> breedCandidates = element.getValue();
                breedCandidates.sort(new AnimalEnergyComperator());
                breedingMap.put(element.getKey(), new Animal[]{breedCandidates.get(0),breedCandidates.get(1)});
            }
        }
        for(Map.Entry<Coordinates, Animal[]> toBreed: breedingMap.entrySet())
            toBreed.getValue()[0].breedWith(toBreed.getValue()[1]);
    }

    public void moveAnimals(){
        Set<Coordinates> toFree = new HashSet<>();
        for(Animal a: new ArrayList<Animal>(this.animals.asList())) {
            Coordinates oldCoor = a.getCoordinates();
            a.move(this.moveEnergy, this.getWidth(), this.getHeight());
            Coordinates newCoor = a.getCoordinates();
        }
    }

    public boolean isFree(Coordinates coor){
        return this.animals.isFree(coor) && !this.plants.containsKey(coor);
    }

    public Coordinates getPositionAround(Coordinates coor) {
        ArrayList<Coordinates> freeAround = new ArrayList<>();
        ArrayList<Coordinates> allAround = new ArrayList<>();
        //Direction[] around = Direction.values();
        for(Direction d: Direction.values()){
            Coordinates wrapped = new Coordinates(coor.add(Objects.requireNonNull(d.getVectorDirection())).getX()%width,
                                                  coor.add(d.getVectorDirection()).getY()%height);
            allAround.add(wrapped);
            if(this.isFree(wrapped))
                freeAround.add(wrapped);
        }
        Random rand = new Random();

        if(freeAround.isEmpty())
            return allAround.get(rand.nextInt(allAround.size()));
        else
            return freeAround.get(rand.nextInt(freeAround.size()));


    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public Collection<Animal> getAnimals(){
        //System.out.println(this.animals.asList());
        return this.animals.asList();
    }

    public Collection<Plant> getPlants(){
        return this.plants.values();
    }

    public boolean isRunning(){
        return this.isRunning;
    }


    public Coordinates jungleUpLeft(){
        return this.jungleUpLeft;
    }

    public Coordinates jungleDownRight(){
        return this.jungleDownRight;
    }

    @Override
    public void newElementNotification(MapElement element) {
        for(IWorldObserver o: this.observers)
            o.newElementUpdate(element);
    }

    @Override
    public void elementRemovalNotification(MapElement element) {
        for(IWorldObserver o: this.observers)
            o.elementRemovalUpdate(element);
    }

    @Override
    public void addWorldObserver(IWorldObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeWorldObserver(IWorldObserver observer) {
        this.observers.remove(observer);
    }
}

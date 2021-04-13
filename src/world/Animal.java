package world;

import abstracts.*;
import utils.Coordinates;
import utils.Direction;
import utils.Genotype;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class Animal extends MapElement implements IElementEnergyPublisher, IReproductive, ITrackable {

    private final Genotype genotype;
    private Direction direction;
    private World world;
    private int age;
    private final LinkedList<Animal> kids = new LinkedList<Animal>();
    private final ArrayList<Animal> parents= new ArrayList<Animal>();
    private boolean isTracked = false;


//    public Animal(int x, int y, int energy){
//        super(x, y, energy);
//        this.genotype = new Genotype();
//        this.direction = this.genotype.getDirection();
//        this.world = null;
//    }
    public Animal(int x, int y, int energy, World world){
        super(x, y, energy);
        this.genotype = new Genotype();
        this.direction = this.genotype.getDirection();
        this.world = world;
        //this.addPositionObserver(world);
        this.age = 0;
    }

    public Animal(Animal parent1, Animal parent2, Coordinates coor){
        super(coor.getX(), coor.getY(), (parent1.getEnergy() + parent2.getEnergy())/4);
        parent1.addChild(this);
        parent2.addChild(this);
        this.parents.add(parent1);
        this.parents.add(parent2);
        this.world = parent1.world;
        this.genotype = new Genotype(parent1.genotype, parent2.genotype);
        this.direction = this.genotype.getDirection();
        this.age = 0;
    }

    public void eat(int foodEnergy){
        this.setEnergy(this.getEnergy() + foodEnergy);
    }

    public void move(int moveEnergy, int xBound, int yBound){
        this.age ++;
        if(this.getEnergy() >= moveEnergy) {
            this.newDirection();
            this.setEnergy(this.getEnergy() - moveEnergy);
            Coordinates toMove = this.getCoordinates().add(Objects.requireNonNull(this.direction.getVectorDirection()));
            this.setCoordinates(new Coordinates((toMove.getX() + xBound )% xBound, (toMove.getY() + yBound )% yBound));
        }
        else
            this.die();
    }

    public void die(){
        this.world.removeAnimal(this);
    }

    public void setEnergy(int newEnergy){
        this.energy = newEnergy;
        this.notifyEnergyUpdate();
    }

    public void setCoordinates(Coordinates newCoor){
        Coordinates oldCoor = this.getCoordinates();
        this.coordinates = newCoor;
        this.notifyPositionUpdate(oldCoor);
    }

    public int getAge() {
        return age;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public int numOfKids(){
        return this.kids.size();
    }

    public boolean hasParents(){
        return !this.parents.isEmpty();
    }

    public LinkedList<Animal> getKids() {
        return kids;
    }

    public ArrayList<Animal> getParents() {
        return parents;
    }

    private void newDirection(){
        Random rand = new Random();
        Direction rotation = this.genotype.getDirection();
        this.direction = Direction.valueOf( (this.direction.ordinal() + rotation.ordinal())%8 );
    }

    public void addChild(Animal animal){
        this.kids.add(animal);
    }
    @Override
    public String toString(){
        return "coordinates: " + "(" + this.getX() + "," + this.getY() + ")\n" +
                "energy:" + this.getEnergy() + "\n";

    }

    @Override
    public void addEnergyObserver(IElementEnergyObserver observer) {
        this.energyObservers.add(observer);
    }

    @Override
    public void removeEnergyObserver(IElementEnergyObserver observer) {
        this.energyObservers.remove(observer);
    }

    @Override
    public void notifyEnergyUpdate() {
        for (IElementEnergyObserver observer : energyObservers)
            observer.updateEnergy(this);

    }

    public boolean containsEnergyObserver(IElementEnergyObserver observer){
        return this.energyObservers.contains(observer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                genotype,
                direction,
                energy,
                energyObservers,
                positionObservers,
                coordinates,
                world);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Animal)) return false;
        Animal animal = (Animal) o;
        return genotype.equals(animal.genotype) &&
                direction == animal.direction &&
                energy == animal.getEnergy() &&
                coordinates.equals(animal.coordinates) &&
                Objects.equals(energyObservers, animal.energyObservers) &&
                Objects.equals(positionObservers, animal.positionObservers) &&
                Objects.equals(world, animal.world);
    }

    @Override
    public void breedWith(IReproductive animal) {
        if (this.accept() && animal.accept() ){
            Coordinates babyCoor = this.world.getPositionAround(this.getCoordinates());
            Animal baby = new Animal(this, (Animal)animal, babyCoor);
            this.world.addAnimal(baby);
        }
    }

    @Override
    public boolean accept() {
        if( this.satisfyBreedingConditions() ){
            this.setEnergy(this.getEnergy()*3/4);
            return true;
        }
        return false;

    }

    @Override
    public boolean satisfyBreedingConditions() {
        return this.energy*2 >= this.world.getStartEnergy();
    }

    @Override
    public boolean isTracked() {
        return this.isTracked;
    }

    @Override
    public void setTracked(){
        this.isTracked = true;
    }
}

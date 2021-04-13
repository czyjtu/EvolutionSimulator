package utils;

import abstracts.IElementPositionObserver;
import world.Animal;
import world.MapElement;

import java.util.*;

//  klasa mogłaby dziedziczyć po HashMapie, lecz w przypadku tego projektu nie widziałem potrzeby by to robić,
//  ponadto dzięki takiej implementacji możemy lepiej kontrolować udostpępniane funkcjonalności.
public class AnimalsContainer implements IElementPositionObserver {
    private Map<Coordinates, ArrayList<Animal>> animals;
    private LinkedList<Animal> list;

    public AnimalsContainer(){
        this.animals = new HashMap<Coordinates, ArrayList<Animal>>();
        this.list = new LinkedList<Animal>();
    }

    public void add(Animal animal) {
        if (!animal.containsPositionObserver(this))
            animal.addPositionObserver(this);

        Coordinates key = animal.getCoordinates();
        this.list.add(animal);
        this.insert(animal);
    }

    public void remove(Animal animal){
        if( this.animals.containsKey(animal.getCoordinates())) {
            this.list.remove(animal);
            animal.removePositionObserver(this);
            this.delete(animal);
        }
    }

    private void insert(Animal animal){
        Coordinates key = animal.getCoordinates();
        if (this.animals.containsKey(key)) {
            this.animals.get(key).add(animal);
        }
        else {
            ArrayList<Animal> values = new ArrayList<>();
            values.add(animal);
            this.animals.put(key, values);
        }
    }

    private void delete(Animal animal) {
        ArrayList<Animal> values = this.animals.get(animal.getCoordinates());
        animal.removePositionObserver(this);
        if (values.size() > 1)
            values.remove(animal);
         else
            this.animals.remove(animal.getCoordinates());

    }

    private ArrayList<Animal> get(Coordinates key){
        if( this.animals.containsKey(key)){
            return this.animals.get(key);
        }
        else
            throw new IllegalStateException("No animals at this coordinates");
    }

    public ArrayList<Animal> getStrongest(Coordinates key){
        ArrayList<Animal> list = this.get(key);
        list.sort(new AnimalEnergyComperator());

        int maxEnergy = list.get(0).getEnergy();
        ArrayList<Animal> strongest = new ArrayList<>();
        for(Animal a: list){
            if(a.getEnergy() == maxEnergy)
                strongest.add(a);
            else
                return strongest;
        }
        return strongest;
    }

    public boolean contains(Animal animal){
        if (this.animals.containsKey(animal.getCoordinates())){
            return this.get(animal.getCoordinates()).contains(animal);
        }
        return false;
    }

    public boolean isFree(Coordinates key){
        return !this.animals.containsKey(key);
    }

    public int size(){
        return this.list.size();
    }

    public LinkedList<Animal> asList(){
        return this.list;
    }

    public Set<Map.Entry<Coordinates, ArrayList<Animal>>> entrySet(){
        return this.animals.entrySet();
    }

    @Override
    public void updatePosition(Coordinates oldCoor, MapElement updatedAnimal) {
        ArrayList<Animal> values = this.get(oldCoor);
        if (values.size() == 1){
            this.animals.remove(oldCoor);
        }
        else
            values.remove((Animal)updatedAnimal);

        this.insert((Animal)updatedAnimal);
    }

    public boolean isEmpty() {
        return this.animals.isEmpty();
    }
}

package utils;

import world.Animal;

import java.util.Comparator;

public class AnimalEnergyComperator implements Comparator<Animal> {
    @Override
    public int compare(Animal o1, Animal o2) {
        if (o1.getEnergy() >= o2.getEnergy())
            return -1;
        else
            return 1;

    }
}

package utils;

import java.util.*;

public class Genotype {
    private final ArrayList<Direction> genotype = new ArrayList<>(32);

    public Genotype(){
        TreeSet<Integer> indexes = split(0, 31, 7);
        indexes.add(31);
        int currentDirection = 0;
        int currentGenotypeIdx = 0;

        for(int splitIdx: indexes){
            while(currentGenotypeIdx <= splitIdx) {
                this.genotype.add(currentGenotypeIdx, Direction.valueOf(currentDirection));
                currentGenotypeIdx += 1;
            }
            currentDirection += 1;
        }
    }

    public Genotype(Genotype parent1, Genotype parent2){
        TreeSet<Integer> indexes = split(0, 31, 2);
        Random rand = new Random();
        ArrayList<ArrayList<Direction>> parents = new ArrayList<>();

        parents.add(parent1.getGenotype());
        parents.add(parent2.getGenotype());

        int first = rand.nextInt(2);
        int second = rand.nextInt(2);
        int third;

        if (first == second){
            third = 1 - first;
        }else{
            third = rand.nextInt(2);
        }

        int idx1 = indexes.pollFirst();
        int idx2 = indexes.pollFirst();

        for(int i=0; i<= idx1; i++){
            this.genotype.add(i, parents.get(first).get(i));
        }
        for(int i=idx1+1; i<= idx2; i++){
            this.genotype.add(i, parents.get(second).get(i));
        }
        for(int i=idx2+1; i<= 31; i++){
            this.genotype.add(i, parents.get(third).get(i));
        }

        Collections.sort(this.genotype);

        if(!isValid(this.genotype))
            this.fixGenotype();
    }

    public static boolean isValid(ArrayList<Direction> genotype){
        HashSet<Integer> occurrences = new HashSet<>();
        for(Direction d: genotype)
            occurrences.add(d.ordinal());

        return occurrences.size() == 8;

    }

    private void fixGenotype() {
        Collections.sort(this.genotype);
        Random rand = new Random();
        ArrayList<Integer> count = new ArrayList<>();
        ArrayList<Integer> atLeast2 = new ArrayList<>();//which direction occurred at least 2 times
        for(int i=0; i<8; i++)
            count.add(i, 0);

        int missingDirection;
        do {
            for (int i = 0; i < this.genotype.size(); i++) {
                int currentDirection = this.genotype.get(i).ordinal();
                int occurrence = count.get(currentDirection);
                occurrence += 1;
                if(occurrence >= 2 && !atLeast2.contains(currentDirection))
                    atLeast2.add(currentDirection);

                count.set(currentDirection, occurrence);
            }
            missingDirection = count.lastIndexOf(0);
            if(missingDirection != -1) {
                int directionToChange = atLeast2.remove(rand.nextInt(atLeast2.size()));
                int idx = this.genotype.indexOf(Direction.valueOf(directionToChange));
                this.genotype.set(idx, Direction.valueOf(missingDirection));
                Collections.sort(this.genotype);
            }
            for(int i=0; i<8; i++)
                count.set(i, 0);
            atLeast2.clear();
        }while(missingDirection != -1);
    }

    //  return indexes where the array will be split
    public TreeSet<Integer> split(int lowerbound, int upperbound, int size){
        TreeSet<Integer> set = new TreeSet<>();
        Random random = new Random();
        while(set.size() < size){
            int thisOne = random.nextInt(upperbound-lowerbound) + lowerbound;
            set.add(thisOne);
        }
        return set;
    }

    public ArrayList<Direction> getGenotype() {
        return genotype;
    }



    public Direction getDirection(){
        Random rand = new Random();
        return this.genotype.get(rand.nextInt(this.genotype.size()));
    }

    @Override
    public String toString() {
        String str = "";
        for(Direction d: this.genotype){
            str += Integer.toString(d.ordinal());
        }
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genotype)) return false;
        Genotype genotype1 = (Genotype) o;
        return this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}

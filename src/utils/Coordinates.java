package utils;

import java.util.Objects;
import java.util.Random;

public class Coordinates {
    private final int x;
    private final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Coordinates))
            return false;
        Coordinates that = (Coordinates) other;

        return this.hashCode() == that.hashCode();
    }

   static public Coordinates randInSquare(Coordinates upperLeftCorner, Coordinates lowerRightCorner){
        Random rand = new Random();

        int xBoundaries = lowerRightCorner.getX() - upperLeftCorner.getX();
        int yBoundaries = lowerRightCorner.getY() - upperLeftCorner.getY();

        int x = rand.nextInt(xBoundaries + 1) + upperLeftCorner.getX();
        int y = rand.nextInt(yBoundaries + 1) + upperLeftCorner.getY();

        return new Coordinates(x, y);
   }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String toString(){
        return "(" + this.getX() + "," + this.getY() + ")";
    }

    public Coordinates add(Coordinates coor){
        return new Coordinates(this.getX() + coor.getX(), this.getY() + coor.getY());
    }
}

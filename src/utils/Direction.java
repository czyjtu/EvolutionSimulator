package utils;

public enum Direction {
    UP(0), UP_RIGHT(1), RIGHT(2), DOWN_RIGHT(3), DOWN(4), DOWN_LEFT(5), LEFT(6), UP_LEFT(7);

    private int index;
    Direction(int i) {
        this.index = i;
    }

    public static Direction valueOf(int index){
        return Direction.values()[index];
    }

    public Coordinates getVectorDirection(){
        switch (this) {
            case UP:
                return new Coordinates(0, -1);
            case UP_RIGHT:
                return new Coordinates(1, -1);
            case RIGHT:
                return new Coordinates(1, 0);
            case DOWN_RIGHT:
                return new Coordinates(1, 1);
            case DOWN:
                return new Coordinates(0, 1);
            case DOWN_LEFT:
                return new Coordinates(-1, 1);
            case LEFT:
                return new Coordinates(-1, 0);
            case UP_LEFT:
                return new Coordinates(-1, -1);
        }
        return null;
    }

}

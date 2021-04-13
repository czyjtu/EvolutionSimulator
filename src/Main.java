import gui.MainFrame;
import utils.Coordinates;
import world.Plant;

public class Main {
    public static void update(Plant plant){
        plant.coordinates = new Coordinates(1,1);
    }

    public static void main(String[] args) {

        new MainFrame();
    }
}

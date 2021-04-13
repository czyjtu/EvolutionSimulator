package gui;

import simulation.Simulation;
import world.Animal;
import utils.Coordinates;
import world.MapElement;
import world.Plant;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {
    private int width = 800;
    private int height;
    private double squareSide;
    private Simulation simulation;
    private boolean showMostCommon = false;

    public MapPanel(Simulation simulation) {
        this.simulation = simulation;

        long mapWidth = this.simulation.getParameters().width;
        long mapHeight = this.simulation.getParameters().height;
        this.height = (int) (((double) mapHeight / (double) mapWidth * this.width));
        this.squareSide = (double)(this.width/(double)mapWidth);

        this.setSize(new Dimension(this.width, this.height));
        this.setVisible(true);
    }

    public Color elementsColor(MapElement element){
        if(element instanceof Animal){
            if(((Animal) element).isTracked())
                return new Color(139,49,225);

            if( ((Animal) element).getGenotype().equals(this.simulation.getStatistics().getMostCommonGenotype().getKey()) &&
            this.showMostCommon){
                System.out.println(this.showMostCommon);
                return new Color(0,0,0);
            }

//          skala kolorów mówi o ilosci energii dla zwierzaka, czerwony-dużo energii, żółty-mało
            long start = this.simulation.getParameters().startEnergy;
            long move = this.simulation.getParameters().moveEnergy;
            long plant = this.simulation.getParameters().plantEnergy;
            int current = element.getEnergy();
            double energyRatio = ((double)current*start/( move*(double)plant));
            int additionalGreen = energyRatio < 225 ? 225 - (int)energyRatio : 0;

            return new Color(230, additionalGreen, 15);
        }
        else if(element instanceof Plant){
            return new Color(60,105,48);
        }
        return null;
    }

    public Animal getObjectAt(int x, int y){
        return this.simulation.getWorld().getAnimal(new Coordinates((int)(x/squareSide), (int)(y/squareSide)));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        double jungleRatio = simulation.getParameters().jungleRatio;

        Coordinates JUL= this.simulation.getWorld().jungleUpLeft();
        Coordinates JDR = this.simulation.getWorld().jungleDownRight();
        g.setColor(new Color(90, 180, 90));
        g.fillRect((int)(JUL.getX()*squareSide)-3,
                   (int)(JUL.getY()*squareSide)-3,
                (int)((JDR.getX() - JUL.getX() + 1)*squareSide) + 10,
                (int)((JDR.getY() - JUL.getY() + 1)*squareSide) + 8);

        for(MapElement element: this.simulation.getWorld().getPlants()){
            g.setColor(this.elementsColor(element));
            g.fillRect((int)(element.getX()*squareSide ) + 3,
                    (int)(element.getY()*squareSide ) + 3,
                    (int)squareSide - 3, (int)squareSide - 3);
        }

        for(MapElement element: this.simulation.getWorld().getAnimals()){
            g.setColor(this.elementsColor(element));
            g.fillOval((int)(element.getX()*squareSide ) + 3,
                    (int)(element.getY()*squareSide ) + 3,
                    (int)squareSide - 3, (int)squareSide - 3);
        }
    }

    public void shiftMostCommon() {
        this.showMostCommon = !this.showMostCommon;
    }
}

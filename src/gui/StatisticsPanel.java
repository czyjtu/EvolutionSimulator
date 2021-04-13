package gui;

import abstracts.ITracker;
import simulation.Simulation;
import world.Animal;
import utils.Genotype;
import world.MapElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class StatisticsPanel extends JPanel implements ActionListener, ITracker {
    private final JLabel averageEnergy;
    private final JLabel mostCommonGEnotype;
    private final JLabel dayLabel;
    private final JLabel animalCountLabel;
    private final JLabel plantsCount;
    private final JLabel deathCounter;
    private final JLabel averageLifeLength;
    private final JLabel averageKidsNum;
    private final JLabel trackedKidsNum;
    private final JLabel trackedOffspring;

    private final JButton pauseButton;
    private final JButton startButton;
    private final JLabel currentGenotype;
    private final JLabel currentGenotypeTxt;
    private final JButton trackButton;
    private final JButton mostCommonButton;
    private final MapPanel mapPanel;
    private final JTextField trackTextField;
    private final Simulation simulation;
    private boolean isTracking;
    private MapElement currentlyPointed = null;

    private JSplitPane splitPane;

    static final Color LABEL_TEXT_COLOR = new Color(171,171,171);
    static final Color BUTTON_COLOR = new Color(127,0,255);

    public StatisticsPanel(Simulation simulation,  MapPanel panel) {
        this.simulation = simulation;
        this.mapPanel = panel;
//      current day
        this.dayLabel = new JLabel();
        this.dayLabel.setText("  Day: ");
        this.dayLabel.setForeground(LABEL_TEXT_COLOR);
        this.dayLabel.setFont(new Font("Bank Gothic",Font.BOLD, 20));

//      animals number
        this.animalCountLabel = new JLabel();
        this.animalCountLabel.setText("  Number of animals: ");
        this.animalCountLabel.setForeground(LABEL_TEXT_COLOR);
        this.animalCountLabel.setFont(new Font("Bank Gothic",Font.BOLD, 20));

//      plants number
        this.plantsCount = new JLabel();
        this.plantsCount.setText("  Number of plants: ");
        this.plantsCount.setForeground(LABEL_TEXT_COLOR);
        this.plantsCount.setFont(new Font("Bank Gothic",Font.BOLD, 20));

//      death counter
        this.deathCounter = new JLabel();
        this.deathCounter.setText("  Deaths: ");
        this.deathCounter.setForeground(LABEL_TEXT_COLOR);
        this.deathCounter.setFont(new Font("Bank Gothic",Font.BOLD, 20));

//      average energy
        this.averageEnergy = new JLabel();
        this.averageEnergy.setText("  AverageEnergy: ");
        this.averageEnergy.setForeground(LABEL_TEXT_COLOR);
        this.averageEnergy.setFont(new Font("Bank Gothic",Font.BOLD, 20));

//      average kids number
        this.averageKidsNum = new JLabel();
        this.averageKidsNum.setText("  Average number of kids: ");
        this.averageKidsNum.setForeground(LABEL_TEXT_COLOR);
        this.averageKidsNum.setFont(new Font("Bank Gothic",Font.BOLD, 20));

//      average life length
        this.averageLifeLength = new JLabel();
        this.averageLifeLength.setText("  Average life length: ");
        this.averageLifeLength.setForeground(LABEL_TEXT_COLOR);
        this.averageLifeLength.setFont(new Font("Bank Gothic",Font.BOLD, 20));

//      most common genotype
        this.mostCommonGEnotype = new JLabel();
        this.mostCommonGEnotype.setText("");
        this.mostCommonGEnotype.setForeground(LABEL_TEXT_COLOR);
        this.mostCommonGEnotype.setFont(new Font("Bank Gothic",Font.BOLD, 20));

        JLabel mostCommonGenotypeTxt = new JLabel();
        mostCommonGenotypeTxt.setText("  Most common genotype: ");
        mostCommonGenotypeTxt.setForeground(LABEL_TEXT_COLOR);
        mostCommonGenotypeTxt.setFont(new Font("Bank Gothic",Font.BOLD, 20));

//      Tracking statistics
        this.trackedKidsNum = new JLabel();
        this.trackedKidsNum.setText("");
        this.trackedKidsNum.setForeground(new Color(255,94,19));
        this.trackedKidsNum.setFont(new Font("Bank Gothic",Font.BOLD, 20));

        this.trackedOffspring = new JLabel();
        this.trackedOffspring.setText("");
        this.trackedOffspring.setForeground(new Color(255,94,19));
        this.trackedOffspring.setFont(new Font("Bank Gothic",Font.BOLD, 20));

        this.currentGenotype = new JLabel();
        this.currentGenotype.setText("");
        this.currentGenotype.setForeground(new Color(255,94,19));
        this.currentGenotype.setFont(new Font("Bank Gothic",Font.BOLD, 20));
        this.currentGenotypeTxt = new JLabel();
        this.currentGenotypeTxt.setText("");
        this.currentGenotypeTxt.setForeground(new Color(255,94,19));
        this.currentGenotypeTxt.setFont(new Font("Bank Gothic",Font.BOLD, 20));

//      Buttons
        this.pauseButton = new JButton();
        this.pauseButton.setVerticalTextPosition(JLabel.CENTER);
        this.pauseButton.setText("Stop");
        this.pauseButton.setForeground(LABEL_TEXT_COLOR);
        this.pauseButton.setBackground(new Color(40,40,40));
        this.pauseButton.setFont(new Font("Bank Gothic",Font.BOLD, 20));
        this.pauseButton.addActionListener(this);

        this.startButton = new JButton();
        this.startButton .setVerticalTextPosition(JLabel.CENTER);
        this.startButton .setText("Start");
        this.startButton .setForeground(LABEL_TEXT_COLOR);
        this.startButton .setBackground(new Color(40,40,40));
        this.startButton .setFont(new Font("Bank Gothic",Font.BOLD, 20));
        this.startButton.addActionListener(this);

        this.trackButton = new JButton();
        this.trackButton .setVerticalTextPosition(JLabel.CENTER);
        this.trackButton .setText("Track statistics");
        this.trackButton .setForeground(new Color(255,94,19));
        this.trackButton .setBackground(new Color(40,40,40));
        this.trackButton .setFont(new Font("Bank Gothic",Font.BOLD, 20));
        this.trackButton.addActionListener(this);
        this.trackButton.setEnabled(true);
        this.trackButton.setVisible(false);

        this.mostCommonButton= new JButton();
        this.mostCommonButton.setVerticalTextPosition(JLabel.CENTER);
        this.mostCommonButton.setText("Show all with most common genotype");
        this.mostCommonButton.setForeground(LABEL_TEXT_COLOR);
        this.mostCommonButton.setBackground(new Color(40,40,40));
        this.mostCommonButton.setFont(new Font("Bank Gothic",Font.BOLD, 20));
        this.mostCommonButton.addActionListener(this);
        this.mostCommonButton.setEnabled(true);
        this.mostCommonButton.setVisible(true);

//      Text Fields
        this.trackTextField = new JTextField("input number of days");
        this.trackTextField.setFont(new Font("Bank Gothic",Font.BOLD, 20));
        this.trackTextField.setEnabled(false);
        this.trackTextField.setVisible(false);

//      Panel settings
        this.setLayout(new GridLayout(18,1)); //BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(new Color(209, 206, 200));

//      adding components
        this.add(dayLabel);
        this.add(animalCountLabel);
        this.add(plantsCount);
        this.add(deathCounter);
        this.add(averageEnergy);
        this.add(averageLifeLength);
        this.add(averageKidsNum);
        this.add(mostCommonGenotypeTxt);
        this.add(mostCommonGEnotype);
        this.add(trackedKidsNum);
        this.add(trackedOffspring);
        this.add(currentGenotypeTxt);
        this.add(currentGenotype);
        this.add(trackButton);
        this.add(trackTextField);
        this.add(mostCommonButton);
        this.add(pauseButton);
        this.add(startButton);

    }

    public void updateStats(){
        if(!isTracking){
            this.trackedKidsNum.setText("");
            this.trackedOffspring.setText("");
        }else{
            this.trackedKidsNum.setText("  Number of kids: " + this.simulation.getStatistics().getTrackedNumOfKids());
            this.trackedOffspring.setText("  Offspring size: " + this.simulation.getStatistics().getTrackedOffspringSize());
        }
        this.currentGenotypeTxt.setText("");
        this.currentGenotype.setText("");
        this.dayLabel.setText("  Day: " + this.simulation.getDay());
        this.animalCountLabel.setText("  Number of animals: " + this.simulation.getStatistics().getNumOfAnimals());
        this.plantsCount.setText("  Number of plants: " + this.simulation.getStatistics().getNumOfPlants() );
        this.deathCounter.setText("  Deaths: " + this.simulation.getStatistics().getNumOfDeaths() );
        this.averageKidsNum.setText("  Average number of kids: " + String.format("%.2f", (float)this.simulation.getStatistics().getAverageKidsNum()) );
        this.averageLifeLength.setText("  Average life length: " + (int)this.simulation.getStatistics().getAverageLifeLength());
        Map.Entry<Genotype, Integer> mostCommon = this.simulation.getStatistics().getMostCommonGenotype();
        if( mostCommon != null)
            this.mostCommonGEnotype.setText("  " + mostCommon.getKey() + "  #" + mostCommon.getValue() );
        this.averageEnergy.setText("  Average energy: " + (int)this.simulation.getStatistics().getAverageEnergyLevel());
    }

    public void showPointedGenotype(){
        if(currentlyPointed != null) {
            this.currentGenotypeTxt.setText("Genotype");
            this.currentGenotype.setText(((Animal)currentlyPointed).getGenotype().toString());
        }
    }

    public int parseInput(){
        String input = this.trackTextField.getText();
        int days = -1;
        try
        {
            days = Integer.parseInt(input.trim());
            System.out.println("int days = " + days);
        }
        catch (NumberFormatException e)
        {
            System.out.println("NumberFormatException: " + e.getMessage());
        }
        return days;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.pauseButton){
            this.simulation.stop();
        }
        else if(e.getSource() == this.startButton){
            this.trackButton.setEnabled(false);
            this.trackButton.setVisible(false);
            this.trackTextField.setEnabled(false);
            this.trackTextField.setVisible(false);
            this.simulation.start();
        }
        else if(e.getSource() == this.trackButton){
            if(currentlyPointed != null) {
                int days = this.parseInput();
                if(days > 0) {
                    this.trackButton.setEnabled(false);
                    this.trackButton.setVisible(false);
                    this.trackTextField.setEnabled(false);
                    this.trackTextField.setVisible(false);
                    this.track(currentlyPointed, days);
                }
            }
        }else if(e.getSource() == this.mostCommonButton){
            this.mapPanel.shiftMostCommon();
            this.mapPanel.repaint();
            //this.mapPanel.getContentPane().paintComponent();
            //this.mapPanel.setMostCommon(false);
        }
    }


    @Override
    public void track(MapElement element, int timeInterval) {
        if(element != null && !this.isTracking()) {
            ((Animal) element).setTracked();
            this.simulation.getStatistics().track(element, timeInterval);
            this.isTracking = true;
        }
    }

    @Override
    public void stopTracking() {
        this.isTracking = false;
    }

    @Override
    public boolean isTracking() {
        return this.isTracking;
    }

    public void trackingAction(Animal pointed) {
        this.currentlyPointed = pointed;
        this.showPointedGenotype();
        if(!isTracking){
            this.trackButton.setEnabled(true);
            this.trackButton.setVisible(true);
            this.trackTextField.setEnabled(true);
            this.trackTextField.setVisible(true);
        }
    }
}

package gui;

import world.Animal;
import simulation.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainFrame extends JFrame implements ActionListener, MouseListener {
    private final Simulation simulation1;
    private final Simulation simulation2;
    private final JSplitPane splitPane;
    private final Timer timer;
    private final MapPanel mapPanel1;
    private final StatisticsPanel statsPanel1;
    private final MapPanel mapPanel2;
    private final StatisticsPanel statsPanel2;

    public MainFrame() {

//  Simulation 1
        this.simulation1 = new Simulation();

        this.mapPanel1 = new MapPanel(simulation1);
        this.mapPanel1.setBackground(new Color(228, 192, 122));

        this.statsPanel1 = new StatisticsPanel(simulation1, mapPanel1);
        this.statsPanel1.setBackground(new Color(68,68,68));

        JSplitPane splitLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.mapPanel1, this.statsPanel1);
        splitLeft.setDividerLocation(805);
        splitLeft.setDividerSize(0);
        splitLeft.setEnabled(false);

//  simulation 2
        this.simulation2 = new Simulation();

        this.mapPanel2 = new MapPanel(simulation2);
        this.mapPanel2.setBackground(new Color(228, 192, 122));

        this.statsPanel2 = new StatisticsPanel(simulation2, mapPanel2);
        this.statsPanel2.setBackground(new Color(68,68,68));

        JSplitPane splitRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.mapPanel2, this.statsPanel2);
        splitRight.setDividerLocation(805);
        splitRight.setDividerSize(0);
        splitRight.setEnabled(false);

//  main SplitPane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitLeft, splitRight);
        splitPane.setDividerLocation(1250);
        splitPane.setEnabled(false);
        this.setPreferredSize(new Dimension(
                2500 ,
                this.mapPanel1.getSize().height + 50
        ));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridLayout());
        this.getContentPane().add(splitPane);

        this.pack();
        this.setResizable(false);

        this.setVisible(true);

        mapPanel1.addMouseListener(this);
        mapPanel2.addMouseListener(this);

        timer = new Timer(100, this);
        timer.setCoalesce(false);
        timer.start();
    }
//niestety napotkałem na problemy ze współbieżnością których nie udało mi się rozwiązać
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.stop();
        if(this.simulation1.isRunning()) {
            this.mapPanel1.repaint();
            this.statsPanel1.repaint();
            this.statsPanel1.updateStats();
            this.simulation1.update();
            if(simulation1.getDay() == simulation1.getStatsDay())
                this.simulation1.save("World1_day" + this.simulation1.getDay());
        }
        if(this.simulation2.isRunning()){
            this.mapPanel2.repaint();
            this.statsPanel2.repaint();
            this.statsPanel2.updateStats();
            this.simulation2.update();
            if(simulation2.getDay() == simulation2.getStatsDay())
                this.simulation2.save("World1_day" + this.simulation2.getDay());
        }
        timer.setDelay(Math.max(this.simulation1.getDelay(), this.simulation2.getDelay()) + 10);
        timer.restart();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Animal pointed = ((MapPanel)e.getSource()).getObjectAt(e.getX(), e.getY());
        if(pointed != null) {
            if (e.getSource() == this.mapPanel1)
                this.statsPanel1.trackingAction(pointed);

            else if (e.getSource() == this.mapPanel2)
                this.statsPanel2.trackingAction(pointed);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

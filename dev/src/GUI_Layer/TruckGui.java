package GUI_Layer;

import ControllerLayer.TruckController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class TruckGui extends JFrame {
    public TruckGui() {
        setTitle("Truck Manager");
        setSize(400, 300);
        setLayout(new GridLayout(4, 1));

        JButton addTruckButton = new JButton("Add a new truck");
        JButton removeTruckButton = new JButton("Remove truck");
        JButton showAllTrucksButton = new JButton("Show all trucks");
        JButton backButton = new JButton("Back to Delivery System Menu");

        addTruckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle add truck action
            }
        });

        removeTruckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle remove truck action
            }
        });

        showAllTrucksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle show all trucks action
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                PresentationGui presentationGui = new PresentationGui();
                presentationGui.setVisible(true);
            }
        });

        add(addTruckButton);
        add(removeTruckButton);
        add(showAllTrucksButton);
        add(backButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
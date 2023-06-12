package GUI_Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TransitGui extends JFrame {
    public TransitGui() {
        setTitle("Update Transit");
        setSize(400, 400);
        setLayout(new GridLayout(8, 1));

        JButton printTransitDetailsButton = new JButton("Print transit details");
        JButton printPendingOrdersButton = new JButton("Print pending orders");
        JButton addOrderToTransitButton = new JButton("Add order to transit");
        JButton removeOrderFromTransitButton = new JButton("Remove order from transit");
        JButton replaceTruckButton = new JButton("Replace truck");
        JButton startTransitButton = new JButton("Start transit");
        JButton printStoreAvailabilityButton = new JButton("Print store availability");
        JButton backButton = new JButton("Back to Delivery System Menu");

        printTransitDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle print transit details action
            }
        });

        printPendingOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle print pending orders action
            }
        });

        addOrderToTransitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle add order to transit action
            }
        });

        removeOrderFromTransitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle remove order from transit action
            }
        });

        replaceTruckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle replace truck action
            }
        });

        startTransitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle start transit action
            }
        });

        printStoreAvailabilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle print store availability action
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

        add(printTransitDetailsButton);
        add(printPendingOrdersButton);
        add(addOrderToTransitButton);
        add(removeOrderFromTransitButton);
        add(replaceTruckButton);
        add(startTransitButton);
        add(printStoreAvailabilityButton);
        add(backButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

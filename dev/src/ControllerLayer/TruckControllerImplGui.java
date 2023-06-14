package ControllerLayer;

import DataAccessLayer.TruckDAO;
import DomainLayer.Truck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class TruckControllerImplGui implements TruckController, ActionListener {
    private TruckDAO truckDAO;
    private JFrame frame;
    private JButton createTruckButton;
    private JButton removeTruckButton;
    private JButton findTruckButton;
    private JButton showAllTrucksButton;
    private JButton showTruckByPlateButton;
    private JButton transferLoadButton;

    public TruckControllerImplGui(TruckDAO truckDAO) {
        // Initialize GUI components
        this.truckDAO = truckDAO;

        frame = new JFrame("Truck Controller");
        createTruckButton = new JButton("Create Truck");
        removeTruckButton = new JButton("Remove Truck");
        findTruckButton = new JButton("Find Truck");
        showAllTrucksButton = new JButton("Show All Trucks");
        showTruckByPlateButton = new JButton("Show Truck by Plate");
        transferLoadButton = new JButton("Transfer Load");

        // Set ActionListener for each button
        createTruckButton.addActionListener(this);
        removeTruckButton.addActionListener(this);
        findTruckButton.addActionListener(this);
        showAllTrucksButton.addActionListener(this);
        showTruckByPlateButton.addActionListener(this);
        transferLoadButton.addActionListener(this);

        // Add buttons to the frame
        frame.setLayout(new FlowLayout());
        frame.add(createTruckButton);
        frame.add(removeTruckButton);
        frame.add(findTruckButton);
        frame.add(showAllTrucksButton);
        frame.add(showTruckByPlateButton);
        frame.add(transferLoadButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        //frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createTruckButton) {
            // TODO: Implement createTruck functionality
        } else if (e.getSource() == removeTruckButton) {
            // TODO: Implement removeTruckByPlateNumber functionality
        } else if (e.getSource() == findTruckButton) {
            // TODO: Implement findTruckByPlate functionality
        } else if (e.getSource() == showAllTrucksButton) {
            // TODO: Implement showAllTrucks functionality
        } else if (e.getSource() == showTruckByPlateButton) {
            // TODO: Implement showTruckByPlate functionality
        } else if (e.getSource() == transferLoadButton) {
            // TODO: Implement transferLoadV2 functionality
        }
    }

    @Override
    public Truck createTruck(String plateNumber, int iModel, int[] iQArr, double truckWeight, double maxWeight) {
        // TODO: Implement createTruck method
        return null;
    }

    @Override
    public boolean removeTruckByPlateNumber(String tPlateNumber) {
        // TODO: Implement removeTruckByPlateNumber method
        return false;
    }

    @Override
    public Truck findTruckByPlate(String tPlateNumber) {
        // TODO: Implement findTruckByPlate method
        return null;
    }

    @Override
    public void showAllTrucks() {
        Set<Truck> trucks = truckDAO.getTrucksSet();

        if (trucks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No trucks found.", "Truck Manager", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder message = new StringBuilder();
            message.append("List of Trucks:\n\n");
            for (Truck truck : trucks) {
                message.append("Truck ID: ").append(truck.getPlateNumber()).append("\n");
                message.append("TruckWeight: ").append(truck.getTruckWeight()).append("\n");
                message.append("Model: ").append(truck.getModel()).append("\n");
                message.append("Capacity: ").append(truck.getMaxCarryWeight()).append("\n\n");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);

            JOptionPane.showMessageDialog(null, scrollPane, "Truck Manager", JOptionPane.PLAIN_MESSAGE);
        }
    }


    @Override
    public boolean showTruckByPlate(String tPlateNumber) {
        // TODO: Implement showTruckByPlate method
        return false;
    }

    @Override
    public boolean transferLoadV2(Truck smallerTruck, Truck biggerTruck) {
        // TODO: Implement transferLoadV2 method
        return false;
    }

//    public static void main(String[] args) {
//        new TruckControllerImplGui();
//    }
}

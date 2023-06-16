package ControllerLayer;

import DataAccessLayer.TruckDAO;
import DomainLayer.Truck;
import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class TruckControllerImplGui extends TruckControllerImpl {
    public TruckControllerImplGui(TruckDAO truckDAO) {
        super(truckDAO);
    }

    @Override
    public void showAllTrucks() {
        Set<Truck> trucks = truckDAO.getTrucksSet();

        if (trucks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No trucks found.", "Truck Manager", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder message = new StringBuilder();
            message.append("All Trucks:\n\n");
            for (Truck truck : trucks) {
                message.append("Truck ID: ").append(truck.getPlateNumber()).append("\n");
                message.append("TruckWeight: ").append(truck.getTruckWeight()).append("\n");
                message.append("Model: ").append(truck.getModel()).append("\n");
                message.append("Capacity: ").append(truck.getMaxCarryWeight()).append("\n\n");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            JOptionPane.showMessageDialog(null, scrollPane, "Truck Manager", JOptionPane.PLAIN_MESSAGE);
        }
    }

}

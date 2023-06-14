package GUI_Layer;

import ControllerLayer.TransitController;
import DomainLayer.Transit;
import ExceptionsPackage.UiException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TransitGui extends JFrame {

    private TransitController transitControllerGui;
    public TransitGui(TransitController transitControllerGui) {
        this.transitControllerGui = transitControllerGui;

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
        backButton.setForeground(Color.red);

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


    public void createNewTransit() {
        JFrame createTransitFrame = new JFrame("Create New Transit");
        createTransitFrame.setSize(400, 300);
        createTransitFrame.setLayout(new GridLayout(5, 2));

        JLabel transitDateLabel = new JLabel("Transit Date (yyyy-MM-dd):");
        JTextField transitDateField = new JTextField();
        JLabel plateNumberLabel = new JLabel("Truck's Plate Number:");
        JTextField plateNumberField = new JTextField();
        JLabel driverIdLabel = new JLabel("Driver's ID:");
        JTextField driverIdField = new JTextField();
        JButton createButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String transitDate = transitDateField.getText();
                String plateNumber = plateNumberField.getText();
                String driverId = driverIdField.getText();

                // Input validation
                if (transitDate.isEmpty() || plateNumber.isEmpty() || driverId.isEmpty()) {
                    JOptionPane.showMessageDialog(createTransitFrame, "Please fill in all fields.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Transit newTransit = transitControllerGui.createTransit(transitDate, plateNumber, driverId);
                    newTransit.printTransit();
                    JOptionPane.showMessageDialog(createTransitFrame, "Transit added successfully!");
                } catch (UiException ex) {
                    JOptionPane.showMessageDialog(createTransitFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                createTransitFrame.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTransitFrame.dispose();
            }
        });

        createTransitFrame.add(transitDateLabel);
        createTransitFrame.add(transitDateField);
        createTransitFrame.add(plateNumberLabel);
        createTransitFrame.add(plateNumberField);
        createTransitFrame.add(driverIdLabel);
        createTransitFrame.add(driverIdField);
        createTransitFrame.add(new JLabel()); // Empty label for alignment
        createTransitFrame.add(new JLabel()); // Empty label for alignment
        createTransitFrame.add(createButton);
        createTransitFrame.add(cancelButton);
        createTransitFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createTransitFrame.setLocationRelativeTo(null);
        createTransitFrame.setVisible(true);
    }

}

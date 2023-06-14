package GUI_Layer;

import ControllerLayer.TruckController;
import DomainLayer.TruckModel;
import BussinesLogic.License;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class TruckGui extends JFrame {
    private TruckController truckControllerGUI;


    public TruckGui(TruckController truckControllerGUI) {

        this.truckControllerGUI = truckControllerGUI;

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
                JFrame createTruckFrame = new JFrame("Add New Truck");
                createTruckFrame.setSize(400, 300);
                createTruckFrame.setLayout(new GridLayout(9, 2));

                JLabel plateNumberLabel = new JLabel("Truck Plate Number:");
                JTextField plateNumberField = new JTextField();
                JLabel truckWeightLabel = new JLabel("Truck Weight (kg):");
                JTextField truckWeightField = new JTextField();
                JLabel maxWeightLabel = new JLabel("Max Carry Weight (kg):");
                JTextField maxWeightField = new JTextField();
                JLabel truckModelLabel = new JLabel("Truck Model:");
                JComboBox<TruckModel> truckModelComboBox = new JComboBox<>(TruckModel.values());
                JLabel qualificationsLabel = new JLabel("Truck Qualifications:");
                JLabel licenseLabel = new JLabel("License:");
                JComboBox<License> licenseComboBox = new JComboBox<>(new License[]{License.C1, License.C});
                JCheckBox coolerCheckBox = new JCheckBox("Cooler");
                JButton addButton = new JButton("Add");
                JButton escapeButton = new JButton("Exit");
                escapeButton.setForeground(Color.red);

                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String plateNumber = plateNumberField.getText();
                        String truckWeightStr = truckWeightField.getText();
                        String maxWeightStr = maxWeightField.getText();

                        // Input validation
                        if (plateNumber.isEmpty() || truckWeightStr.isEmpty() || maxWeightStr.isEmpty()) {
                            JOptionPane.showMessageDialog(createTruckFrame, "Please fill in all fields.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (!plateNumber.matches("\\d+")) {
                            JOptionPane.showMessageDialog(createTruckFrame, "Plate number should contain only numbers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        double truckWeight = 0.0;
                        double maxWeight = 0.0;

                        try {
                            truckWeight = Double.parseDouble(truckWeightStr);
                            maxWeight = Double.parseDouble(maxWeightStr);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(createTruckFrame, "Invalid weight input.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        TruckModel selectedModel = (TruckModel) truckModelComboBox.getSelectedItem();
                        License selectedLicense = (License) licenseComboBox.getSelectedItem();
                        boolean hasCooler = coolerCheckBox.isSelected();

                        int[] qualifications = new int[2];
                        qualifications[0] = selectedLicense.ordinal();
                        qualifications[1] = hasCooler ? 1 : 0;

                        truckControllerGUI.createTruck(plateNumber, selectedModel.ordinal(), qualifications, truckWeight, maxWeight);
                        JOptionPane.showMessageDialog(createTruckFrame, "Truck added successfully!");
                        createTruckFrame.dispose();
                    }
                });

                escapeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        createTruckFrame.dispose();
                    }
                });

                createTruckFrame.add(plateNumberLabel);
                createTruckFrame.add(plateNumberField);
                createTruckFrame.add(truckWeightLabel);
                createTruckFrame.add(truckWeightField);
                createTruckFrame.add(maxWeightLabel);
                createTruckFrame.add(maxWeightField);
                createTruckFrame.add(truckModelLabel);
                createTruckFrame.add(truckModelComboBox);
                createTruckFrame.add(qualificationsLabel);
                createTruckFrame.add(new JLabel()); // Empty label for alignment
                createTruckFrame.add(licenseLabel);
                createTruckFrame.add(licenseComboBox);
                createTruckFrame.add(new JLabel()); // Empty label for alignment
                createTruckFrame.add(coolerCheckBox);
                createTruckFrame.add(new JLabel()); // Empty label for alignment
                createTruckFrame.add(new JLabel()); // Empty label for alignment
                createTruckFrame.add(escapeButton);
                createTruckFrame.add(addButton);
                createTruckFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                createTruckFrame.setLocationRelativeTo(null);
                createTruckFrame.setVisible(true);
            }
        });


        removeTruckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String plateNumberInput = JOptionPane.showInputDialog(null, "Enter the plate number of the truck to remove:", "Remove Truck", JOptionPane.PLAIN_MESSAGE);
                if (plateNumberInput != null && !plateNumberInput.isEmpty()) {
                    try {
                        int plateNumber = Integer.parseInt(plateNumberInput);
                        boolean removed = truckControllerGUI.removeTruckByPlateNumber(plateNumberInput);
                        if (removed) {
                            JOptionPane.showMessageDialog(null, "Truck removed successfully!", "Remove Truck", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Truck with plate number " + plateNumber + " does not exist!", "Remove Truck", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid plate number! Please enter a valid integer.", "Remove Truck", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        showAllTrucksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                truckControllerGUI.showAllTrucks();
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


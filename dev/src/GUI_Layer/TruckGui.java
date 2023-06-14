package GUI_Layer;

import ControllerLayer.TruckController;
import DomainLayer.Truck;
import DomainLayer.TruckModel;
import BussinesLogic.License;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class TruckGui extends JFrame {
    private TruckController truckControllerGui;


    public TruckGui(TruckController truckControllerGui) {

        this.truckControllerGui = truckControllerGui;

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
                createNewTruck();
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
                truckControllerGui.showAllTrucks();
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


    public void createNewTruck() {
        final String[] plateNumber = new String[1];
        final double[] truckWeight = new double[1];
        final double[] maxWeight = new double[1];
        final int[] iModel = new int[1];
        final int[] iQualifications = new int[2];

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

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plateNumber[0] = plateNumberField.getText();
                truckWeight[0] = Double.parseDouble(truckWeightField.getText());
                maxWeight[0] = Double.parseDouble(maxWeightField.getText());
                TruckModel selectedModel = (TruckModel) truckModelComboBox.getSelectedItem();
                iModel[0] = selectedModel.ordinal();
                iQualifications[0] = ((License) licenseComboBox.getSelectedItem()).ordinal();
                if (coolerCheckBox.isSelected()) {
                    iQualifications[1] |= 1;
                }
//                License selectedLicense = (License) licenseComboBox.getSelectedItem();
//                iQualifications[0] |= selectedLicense.ordinal() << 1;

//                int[] iQArr = {iQualifications[0]};
                truckControllerGui.createTruck(plateNumber[0], iModel[0], iQualifications, truckWeight[0], maxWeight[0]);

                JOptionPane.showMessageDialog(createTruckFrame, "Truck added successfully!");
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
        createTruckFrame.add(new JLabel()); // Empty label for alignment
        createTruckFrame.add(addButton);

        createTruckFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createTruckFrame.setLocationRelativeTo(null);
        createTruckFrame.setVisible(true);
    }

}


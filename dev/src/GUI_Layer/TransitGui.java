package GUI_Layer;

import BussinesLogic.TransitCoordinator;
import ControllerLayer.OrderDocumentController;
import ControllerLayer.TransitController;
import ControllerLayer.TransitRecordController;
import ControllerLayer.TruckController;
import DomainLayer.OrderDocument;
import DomainLayer.Product;
import DomainLayer.Transit;
import ExceptionsPackage.UiException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Map;

class TransitGui extends JFrame {

    private final TransitController transitControllerGui;
    private TruckController truckControllerGui;
    private TransitCoordinator transitCoordinator;
    //private OrderDocumentController orderDocumentControllerGui;
    private TransitRecordController transitRecordControllerGui;

    public TransitGui(TransitController transitControllerGui, TruckController truckControllerGui, TransitCoordinator transitCoordinator,
                      OrderDocumentController orderDocumentControllerGui, TransitRecordController transitRecordControllerGui) {
        this.transitControllerGui = transitControllerGui;
        this.truckControllerGui = truckControllerGui;
        this.transitCoordinator  = transitCoordinator;
        //this.orderDocumentControllerGui = orderDocumentControllerGui;
        this.transitRecordControllerGui = transitRecordControllerGui;

        setTitle("Update Transit");
        setSize(400, 400);
        setLayout(new GridLayout(8, 1));

        JButton ShowTransitDetailsButton = new JButton("Show transit details");
        JButton ShowPendingOrdersButton = new JButton("Show pending orders");
        JButton addOrderToTransitButton = new JButton("Add order to transit");
        JButton removeOrderFromTransitButton = new JButton("Remove order from transit");
        JButton replaceTruckButton = new JButton("Replace truck");
        JButton startTransitButton = new JButton("Start transit");
        JButton ShowStoreAvailabilityButton = new JButton("Show store availability for receiving transit");
        JButton backButton = new JButton("Back to Delivery System Menu");
        backButton.setForeground(Color.red);

        ShowTransitDetailsButton.addActionListener(e -> {
            JFrame transitIdFrame = new JFrame("Enter Transit ID");
            transitIdFrame.setSize(300, 150);
            transitIdFrame.setLayout(new FlowLayout());

            JLabel transitIdLabel = new JLabel("Transit ID:");
            JTextField transitIdField = new JTextField(10);
            JButton submitButton = new JButton("Submit");

            submitButton.addActionListener(e1 -> {
                String transitIdText = transitIdField.getText();

                // Input validation
                if (transitIdText.isEmpty()) {
                    JOptionPane.showMessageDialog(transitIdFrame, "Please enter a Transit ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int transitId = Integer.parseInt(transitIdText);
                    showTransitDetails(transitId);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(transitIdFrame, "Invalid Transit ID. Please enter a valid integer value.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }

                transitIdFrame.dispose();
            });

            transitIdFrame.add(transitIdLabel);
            transitIdFrame.add(transitIdField);
            transitIdFrame.add(submitButton);
            transitIdFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            transitIdFrame.setLocationRelativeTo(null);
            transitIdFrame.setVisible(true);
        });





        ShowPendingOrdersButton.addActionListener(e -> orderDocumentControllerGui.showPendingOrderDocs());

        addOrderToTransitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame addOrderFrame = new JFrame("Add Order to Transit");
                addOrderFrame.setSize(400, 200);
                addOrderFrame.setLayout(new GridLayout(3, 2));

                JLabel transitIdLabel = new JLabel("Transit ID:");
                JTextField transitIdField = new JTextField();
                JLabel orderIdLabel = new JLabel("Order ID:");
                JTextField orderIdField = new JTextField();
                JButton addButton = new JButton("Add");
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setForeground(Color.red);

                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int transitId;
                        int orderId;

                        try {
                            transitId = Integer.parseInt(transitIdField.getText());
                            orderId = Integer.parseInt(orderIdField.getText());
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(addOrderFrame, "Invalid ID input.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        Transit currentTransit = transitControllerGui.findTransitByID(transitId);
                        if (currentTransit == null) {
                            JOptionPane.showMessageDialog(addOrderFrame, "Transit not found.", "Transit Not Found", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        OrderDocument orderDocument = orderDocumentControllerGui.findOrderDocById(orderId);
                        if (orderDocument == null) {
                            JOptionPane.showMessageDialog(addOrderFrame, "Order document not found.", "Order Document Not Found", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        transitCoordinator.addTransitInDate(currentTransit.getTransitDate(), orderDocument.getDestination().getBranchID());
                        transitControllerGui.updateOrderDocumentOfTransit(currentTransit, orderDocument, "+1");

                        JOptionPane.showMessageDialog(addOrderFrame, "Order document added successfully!", "Add Order to Transit", JOptionPane.INFORMATION_MESSAGE);
                        addOrderFrame.dispose();
                    }
                });

                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addOrderFrame.dispose();
                    }
                });

                addOrderFrame.add(transitIdLabel);
                addOrderFrame.add(transitIdField);
                addOrderFrame.add(orderIdLabel);
                addOrderFrame.add(orderIdField);
                addOrderFrame.add(addButton);
                addOrderFrame.add(cancelButton);
                addOrderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                addOrderFrame.setLocationRelativeTo(null);
                addOrderFrame.setVisible(true);
            }
        });

        removeOrderFromTransitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame removeOrderFrame = new JFrame("Remove Order from Transit");
                removeOrderFrame.setSize(400, 200);
                removeOrderFrame.setLayout(new GridLayout(3, 2));

                JLabel transitIdLabel = new JLabel("Transit ID:");
                JTextField transitIdField = new JTextField();
                JLabel orderIdLabel = new JLabel("Order ID:");
                JTextField orderIdField = new JTextField();
                JButton removeButton = new JButton("Remove");
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setForeground(Color.red);

                removeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int transitId;
                        int orderId;

                        try {
                            transitId = Integer.parseInt(transitIdField.getText());
                            orderId = Integer.parseInt(orderIdField.getText());
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(removeOrderFrame, "Invalid ID input.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        Transit currentTransit = transitControllerGui.findTransitByID(transitId);
                        if (currentTransit == null) {
                            JOptionPane.showMessageDialog(removeOrderFrame, "Transit not found.", "Transit Not Found", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        OrderDocument orderDocument = orderDocumentControllerGui.findOrderDocById(orderId);
                        if (orderDocument == null) {
                            JOptionPane.showMessageDialog(removeOrderFrame, "Order document not found.", "Order Document Not Found", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        // TODO verify this not happening - with ita y
                        //transitCoordinator.removeTransitInDate(currentTransit.getTransitDate(), orderDocument.getDestination().getBranchID());
                        transitControllerGui.updateOrderDocumentOfTransit(currentTransit, orderDocument, "-1");

                        JOptionPane.showMessageDialog(removeOrderFrame, "Order document removed successfully!", "Remove Order from Transit", JOptionPane.INFORMATION_MESSAGE);
                        removeOrderFrame.dispose();
                    }
                });

                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        removeOrderFrame.dispose();
                    }
                });

                removeOrderFrame.add(transitIdLabel);
                removeOrderFrame.add(transitIdField);
                removeOrderFrame.add(orderIdLabel);
                removeOrderFrame.add(orderIdField);
                removeOrderFrame.add(removeButton);
                removeOrderFrame.add(cancelButton);
                removeOrderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                removeOrderFrame.setLocationRelativeTo(null);
                removeOrderFrame.setVisible(true);
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

        ShowStoreAvailabilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame storeAvailabilityFrame = new JFrame("Show Store Availability");
                storeAvailabilityFrame.setSize(400, 200);
                storeAvailabilityFrame.setLayout(new FlowLayout());

                JLabel storeIdLabel = new JLabel("Store ID:");
                JTextField storeIdField = new JTextField(10);
                JButton printButton = new JButton("Show");
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setForeground(Color.red);

                printButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int storeId;

                        try {
                            storeId = Integer.parseInt(storeIdField.getText());
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(storeAvailabilityFrame, "Invalid ID input.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        Map<LocalDate, Boolean> map = transitCoordinator.getTransitsInBranch(storeId);
                        StringBuilder message = new StringBuilder();
                        for (Map.Entry<LocalDate, Boolean> entry : map.entrySet()) {
                            message.append("Date: ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                        }

                        JTextArea textArea = new JTextArea(message.toString());
                        textArea.setEditable(false);
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        scrollPane.setPreferredSize(new Dimension(500, 400));

                        JOptionPane.showMessageDialog(null, scrollPane, "Store Availability", JOptionPane.PLAIN_MESSAGE);
                        storeAvailabilityFrame.dispose();
                    }
                });

                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        storeAvailabilityFrame.dispose();
                    }
                });

                storeAvailabilityFrame.add(storeIdLabel);
                storeAvailabilityFrame.add(storeIdField);
                storeAvailabilityFrame.add(printButton);
                storeAvailabilityFrame.add(cancelButton);
                storeAvailabilityFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                storeAvailabilityFrame.setLocationRelativeTo(null);
                storeAvailabilityFrame.setVisible(true);
            }
        });



        backButton.addActionListener(e -> {
            dispose();
            PresentationGui presentationGui = new PresentationGui();
            presentationGui.setVisible(true);
        });

        add(ShowTransitDetailsButton);
        add(ShowPendingOrdersButton);
        add(addOrderToTransitButton);
        add(removeOrderFromTransitButton);
        add(replaceTruckButton);
        add(startTransitButton);
        add(ShowStoreAvailabilityButton);
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

        createButton.addActionListener(e -> {
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
                showTransitDetails(newTransit.getTransitId());
                JOptionPane.showMessageDialog(createTransitFrame, "Transit added successfully!");
            } catch (UiException ex) {
                JOptionPane.showMessageDialog(createTransitFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            createTransitFrame.dispose();
        });

        cancelButton.addActionListener(e -> createTransitFrame.dispose());

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

    public void showTransitDetails(int transitId) {
        Transit transitToShow = transitControllerGui.showTransitByID(transitId);

        if (transitToShow == null) {
            JOptionPane.showMessageDialog(null, String.format("Transit's ID: %d not found!", transitId), "Transit Not Found", JOptionPane.ERROR_MESSAGE);
        } else {
            StringBuilder message = new StringBuilder();
            message.append("Transit Details:\n\n");
            message.append("Transit ID: ").append(transitToShow.getTransitId()).append("\n");
            message.append("Transit Date: ").append(transitToShow.getTransitDate()).append("\n");
            message.append("Truck Details: ");
            message.append(transitToShow.getTruck().getPlateNumber()).append("\n");
            message.append("Driver Details: ");
            message.append(transitToShow.getDriver().getId()).append("\n");
            message.append("Orders Documents:\n");
            for (OrderDocument od : transitToShow.getOrdersDocs()) {
                message.append("    Document ID: ").append(od.getOrderDocumentId()).append("\n");
                message.append("    Source: ").append(od.getSource().getAddress()).append("\n");
                message.append("    Destination: ").append(od.getDestination().getAddress()).append("\n");
                message.append("    Total Weight: ").append(od.getTotalWeight()).append("\n");
                message.append("Products in Order:\n");
                for (Map.Entry<Product, Double> entry : od.getProductsList().entrySet()) {
                    message.append("        Product Name: ").append(entry.getKey().getProductName()).append("\n");
                    message.append("        Product Amount: ").append(entry.getValue()).append("\n");
                }
                message.append("\n");
            }
            message.append("ETA in minutes for the transit: ").append(transitToShow.getETA()).append("\n");

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            textArea.setCaretPosition(0);

            JOptionPane.showMessageDialog(null, scrollPane, "Transit Details", JOptionPane.PLAIN_MESSAGE);
        }
    }
}

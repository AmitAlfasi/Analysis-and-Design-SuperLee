package GUI_Layer;

import BussinesLogic.BranchStore;
import BussinesLogic.TransitCoordinator;
import ControllerLayer.OrderDocumentController;
import ControllerLayer.TransitController;
import ControllerLayer.TransitRecordController;
import ControllerLayer.TruckController;
import DomainLayer.*;
import ExceptionsPackage.UiException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;

import BussinesLogic.Driver;
import BussinesLogic.License;

class TransitGui extends JFrame {

    private final TransitController transitControllerGui;
    private TruckController truckControllerGui;
    private TransitCoordinator transitCoordinator;
    private OrderDocumentController orderDocumentControllerGui;
    private TransitRecordController transitRecordControllerGui;

    public TransitGui(TransitController transitControllerGui, TruckController truckControllerGui, TransitCoordinator transitCoordinator,
                      OrderDocumentController orderDocumentControllerGui, TransitRecordController transitRecordControllerGui) {
        this.transitControllerGui = transitControllerGui;
        this.truckControllerGui = truckControllerGui;
        this.transitCoordinator  = transitCoordinator;
        this.orderDocumentControllerGui = orderDocumentControllerGui;
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
        JButton backToMainMenuButton = new JButton("Back to Delivery System Menu");
        backToMainMenuButton.setForeground(Color.red);

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
                addOrderFrame.setLayout(new GridLayout(5, 2));

                JLabel transitIdLabel = new JLabel("Transit ID:");
                JTextField transitIdField = new JTextField();
                transitIdField.setHorizontalAlignment(JTextField.CENTER);
                JLabel orderIdLabel = new JLabel("Order ID:");
                JTextField orderIdField = new JTextField();
                orderIdField.setHorizontalAlignment(JTextField.CENTER);
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
                addOrderFrame.add(new JLabel()); // Empty label for alignment
                addOrderFrame.add(new JLabel()); // Empty label for alignment
                addOrderFrame.add(new JLabel()); // Empty label for alignment
                addOrderFrame.add(new JLabel()); // Empty label for alignment
                addOrderFrame.add(cancelButton);
                addOrderFrame.add(addButton);
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
                removeOrderFrame.setLayout(new GridLayout(5, 2));

                JLabel transitIdLabel = new JLabel("Transit ID:");
                JTextField transitIdField = new JTextField();
                transitIdField.setHorizontalAlignment(JTextField.CENTER);
                JLabel orderIdLabel = new JLabel("Order ID:");
                JTextField orderIdField = new JTextField();
                orderIdField.setHorizontalAlignment(JTextField.CENTER);
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
                removeOrderFrame.add(new JLabel()); // Empty label for alignment
                removeOrderFrame.add(new JLabel()); // Empty label for alignment
                removeOrderFrame.add(new JLabel()); // Empty label for alignment
                removeOrderFrame.add(new JLabel()); // Empty label for alignment
                removeOrderFrame.add(cancelButton);
                removeOrderFrame.add(removeButton);
                removeOrderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                removeOrderFrame.setLocationRelativeTo(null);
                removeOrderFrame.setVisible(true);
            }
        });


        replaceTruckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt the user for transit ID
                String transitIdInput = JOptionPane.showInputDialog(null, "Enter transit ID:");
                if (transitIdInput == null) {
                    return; // User clicked cancel or closed the window
                }

                int transitId;
                try {
                    transitId = Integer.parseInt(transitIdInput);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid transit ID. Please enter a numeric value.");
                    return; // Exit the method
                }

                // Prompt the user for new truck plate number
                String newTruckPlate = JOptionPane.showInputDialog(null, "Enter new truck plate number:");
                if (newTruckPlate == null) {
                    return; // User clicked cancel or closed the window
                }

                int flag = transitControllerGui.replaceTransitTruck(transitId, newTruckPlate);

                switch (flag) {
                    case -2:
                        JOptionPane.showMessageDialog(null, "Transit id " + transitId + " not found!");
                        break;
                    case -1:
                        JOptionPane.showMessageDialog(null, "Truck's plate number " + newTruckPlate + " not found!");
                        break;
                    case 0:
                        JOptionPane.showMessageDialog(null, "Current driver is not qualified to drive the chosen truck");
                        lookForQualifiedDriver(transitId, newTruckPlate);
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Transit's truck updated successfully");
                        break;
                }
            }
        });





        startTransitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt the user to enter the transit ID
                UIManager.put("OptionPane.okButtonText", "OK");
                UIManager.put("OptionPane.cancelButtonText", "<html><font color='red'>Cancel</font></html>");
                String transitIdInput = JOptionPane.showInputDialog("Enter Transit ID:");
                if (transitIdInput != null) {
                    int transitId;
                    try {
                        transitId = Integer.parseInt(transitIdInput);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid transit ID.");
                        return;
                    }

                    Transit selectedTransit = transitControllerGui.findTransitByID(transitId);

                    if (selectedTransit != null) {
                        // Verify the transit date is today
                        LocalDate today = LocalDate.now();
                        if (!selectedTransit.getTransitDate().isEqual(today)) {
                            JOptionPane.showMessageDialog(null, "The selected transit is not scheduled for today.");
                            return;
                        }
//                        if (!transitCoordinator.StorageWorkersExist(selectedTransit.getDestinationStores(), selectedTransit.getTransitDate()))
//                        {
//                            JOptionPane.showMessageDialog(null, "One or more of branchStore on route does not have storage workers.");
//                            return;
//                        }
                        startTransit(selectedTransit);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid transit ID.");
                    }
                }
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



        backToMainMenuButton.addActionListener(e -> {
            navigateToMainMenu();
        });

        add(ShowTransitDetailsButton);
        add(ShowPendingOrdersButton);
        add(addOrderToTransitButton);
        add(removeOrderFromTransitButton);
        add(replaceTruckButton);
        add(startTransitButton);
        add(ShowStoreAvailabilityButton);
        add(backToMainMenuButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public void createNewTransit() {
        JFrame createTransitFrame = new JFrame("Create New Transit");
        createTransitFrame.setSize(400, 300);
        createTransitFrame.setLayout(new GridLayout(9, 2));

        JLabel transitDateLabel = new JLabel("Transit Date (yyyy-MM-dd):");
        JTextField transitDateField = new JTextField();
        transitDateField.setHorizontalAlignment(JTextField.CENTER); // Aligns cursor to the center
        JLabel plateNumberLabel = new JLabel("Truck's Plate Number:");
        JTextField plateNumberField = new JTextField();
        plateNumberField.setHorizontalAlignment(JTextField.CENTER); // Aligns cursor to the center
        JLabel driverIdLabel = new JLabel("Driver's ID:");
        JTextField driverIdField = new JTextField();
        driverIdField.setHorizontalAlignment(JTextField.CENTER); // Aligns cursor to the center
        JButton createButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setForeground(Color.red);

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
        createTransitFrame.add(new JLabel()); // Empty label for alignment
        createTransitFrame.add(new JLabel()); // Empty label for alignment
        createTransitFrame.add(new JLabel()); // Empty label for alignment
        createTransitFrame.add(new JLabel()); // Empty label for alignment
        createTransitFrame.add(new JLabel()); // Empty label for alignment
        createTransitFrame.add(new JLabel()); // Empty label for alignment
        createTransitFrame.add(new JLabel()); // Empty label for alignment
        createTransitFrame.add(new JLabel()); // Empty label for alignment
        createTransitFrame.add(cancelButton);
        createTransitFrame.add(createButton);
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
    public void navigateToMainMenu() {
        dispose();
        PresentationGui presentationGui = new PresentationGui();
        presentationGui.setVisible(true);
    }

    private void lookForQualifiedDriver(int transitIdToReplace, String newTruckPlate) {
        String driverId = JOptionPane.showInputDialog("Enter qualified driver ID:");

        if (driverId == null) {
            return; // User clicked cancel or closed the window
        }

        int iFlag = transitControllerGui.replaceTransitDriver(transitIdToReplace, driverId, newTruckPlate, "notOnTheFly");

        if (iFlag == -1) {
            JOptionPane.showMessageDialog(null, "Driver ID: " + driverId + " not found");
        } else if (iFlag == 0) {
            JOptionPane.showMessageDialog(null, "Chosen driver: " + driverId + " is not qualified to drive the chosen truck");
        } else if (iFlag == 1) {
            JOptionPane.showMessageDialog(null, "Transit's driver updated successfully");
        }
    }


    private void startTransit(Transit transit) {
        LocalDateTime presentDate = LocalDate.now().atStartOfDay();

        if (!transit.getTransitDate().isEqual(presentDate.toLocalDate())) {
            JOptionPane.showMessageDialog(null, "Back to transit menu."); // TODO: Update with appropriate message
            return;
        }

        JTextArea progressTextArea = new JTextArea();
        progressTextArea.setEditable(false);

        JFrame progressFrame = new JFrame("Transit Progress");
        progressFrame.add(progressTextArea);
        progressFrame.setSize(400, 300);
        progressFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        progressFrame.setVisible(true);

        appendToProgressTextArea(progressTextArea, "Starting transit...");
        transit.setDepartureTime(LocalTime.now());
        appendToProgressTextArea(progressTextArea, "ETA in minutes for the transit: " + transit.getETA());

        TransitRecord transitRecord = transitRecordControllerGui.createTransitRecord(transit);

        for (Supplier supplier : transit.getDestinationSuppliers()) {
            appendToProgressTextArea(progressTextArea, "Arrived at supplier: " + supplier.getSupplierId());

            for (OrderDocument orderDoc : transit.getOrdersDocs()) {
                if (supplier.getSupplierId() == orderDoc.getSource().getSupplierId()) {
                    appendToProgressTextArea(progressTextArea, "Loading order number: " + orderDoc.getOrderDocumentId());
                    transit.getTruck().loadTruck(orderDoc.getTotalWeight());

                    if (transit.getTruck().getMaxCarryWeight() < transit.getTruck().getCurrentWeight()) {
                        transitRecord.updateTransitProblem();
                        appendToProgressTextArea(progressTextArea, "------");
                        appendToProgressTextArea(progressTextArea, "OverWeight Problem");
                        overweight(transit, orderDoc);
                        appendToProgressTextArea(progressTextArea, "OverWeight Solved");
                        appendToProgressTextArea(progressTextArea, "------");
                    }
                }
            }
            transitRecord.addSupWeightExit(supplier, transit.getTruck().getCurrentWeight());
        }

        for (BranchStore branchStore : transit.getDestinationStores()) {
            appendToProgressTextArea(progressTextArea, "Arrived at branchStore: " + branchStore.getBranchID());

            for (OrderDocument orderDoc : transit.getOrdersDocs()) {
                if (branchStore.getBranchID() == orderDoc.getDestination().getBranchID()) {
                    appendToProgressTextArea(progressTextArea, "Unloading order number: " + orderDoc.getOrderDocumentId());
                    transit.getTruck().unloadTruck(orderDoc.getTotalWeight());
                    this.orderDocumentControllerGui.moveOrderToFinishDB(orderDoc);
                }
            }
        }

        transitRecordControllerGui.saveTransitRecordDB(transitRecord);
        appendToProgressTextArea(progressTextArea, "Finished transit.");

        JOptionPane.showMessageDialog(null, "Transit completed.", "Transit Progress", JOptionPane.INFORMATION_MESSAGE);
        transitControllerGui.moveTransitToFinishedDB(transit);
        progressFrame.dispose();
    }

    private void appendToProgressTextArea(JTextArea textArea, String message) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(message + "\n");
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }


    private void overweight(Transit transit, OrderDocument currentOrder) {
        String[] options = {"Bring bigger truck", "Remove products from this order", "Delete this order from transit"};

        boolean verifiedFlag = false;

        do {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.weightx = 1.0;
            constraints.weighty = 1.0;

            JLabel messageLabel = new JLabel("----------------Overweight-----------------", SwingConstants.CENTER);
            messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(messageLabel, constraints);

            int choice = JOptionPane.showOptionDialog(
                    null,
                    panel,
                    "Overweight",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            switch (choice) {
                case 0: // Bring bigger truck
                    verifiedFlag = bringBiggerTruck(transit);
                    break;
                case 1: // Remove products from order
                    if (currentOrder.getProductsList().size() > 1) {
                        verifiedFlag = removeProductFromOrder(transit, currentOrder);
                    } else {
                        JOptionPane.showMessageDialog(null, "Order id: " + currentOrder.getOrderDocumentId() + " contains only one product, removing order");
                        // Update weight
                        // double updatedLoadWeight = transit.getTruck().getCurrentWeight() - currentOrder.getTotalWeight();
                        // transit.getTruck().setCurrentLoadWeight(updatedLoadWeight);
                        // transit.getOrdersDocs().remove(currentOrder);
                        // transitControllerGui.updateOrderDocumentOfTransit(transit, currentOrder, "-1");
                        //verifiedFlag = true;
                    }
                    //break;
                case 2: // Delete order
                    // Update weight
                    double updatedLoadWeight = transit.getTruck().getCurrentWeight() - currentOrder.getTotalWeight();
                    transit.getTruck().setCurrentLoadWeight(updatedLoadWeight);
                    transit.getOrdersDocs().remove(currentOrder);
                    transitControllerGui.updateOrderDocumentOfTransit(transit, currentOrder, "-1");
                    verifiedFlag = true;
                    break;
                default:
                    // Invalid input
                    break;
            }
        } while (!verifiedFlag);
    }

    private boolean removeProductFromOrder(Transit transit, OrderDocument orderDocument) {
        double truckCurrentWeight = transit.getTruck().getCurrentWeight();
        double overWeightAmount = truckCurrentWeight - transit.getTruck().getMaxCarryWeight();
        double originalOrderWeight = orderDocument.getTotalWeight();
        JOptionPane.showMessageDialog(null, "Reduce at least: " + overWeightAmount + " kg");

        showOrder(orderDocument);


        // Show input dialog for product name
        String sProductName = JOptionPane.showInputDialog("Enter product name: ");
        orderDocument = orderDocumentControllerGui.removeProductFromOrderDocDBdP(orderDocument.getOrderDocumentId(), sProductName);
        double updatedOrderWeight = orderDocument.getTotalWeight();
        double newCurrentWeight = truckCurrentWeight - (originalOrderWeight - updatedOrderWeight);
        transit.getTruck().setCurrentLoadWeight(newCurrentWeight);

        return !(newCurrentWeight > transit.getTruck().getMaxCarryWeight());
    }


    private Truck findNewTruck(String plateNumber) {
        return truckControllerGui.findTruckByPlate(plateNumber);
    }

    private Driver findNewDriver(LocalDate transitDate, String driverId, Set<License> licenses) {
        // Show input dialog for driver's id
        return transitCoordinator.SwitchDriverInTransit(transitDate, driverId, licenses);
    }

    private boolean bringBiggerTruck(Transit transit) {
        // TODO: Verify truck and driver availability

        // Show input dialog for truck's plate number
        String sPlateNumber = JOptionPane.showInputDialog("Enter truck's plate number: ");
        Truck newTruck = findNewTruck(sPlateNumber);

        if (newTruck == null) {
            JOptionPane.showMessageDialog(null, "Truck's plate number: " + sPlateNumber + " not found");
            return false;
        }

        // Show input dialog for driver's id
        String newDriverId = JOptionPane.showInputDialog("Enter driver's id: ");
        Driver newDriver = findNewDriver(transit.getTransitDate(), newDriverId, newTruck.getTruckLicenses());

        if (newDriver == null) {
            JOptionPane.showMessageDialog(null, "Driver's id: " + newDriverId + " not found");
            return false;
        }

        if (!transitControllerGui.isDriverAllowToDriveTruck(newTruck, newDriver)) {
            JOptionPane.showMessageDialog(null, "Chosen driver: " + newDriver.getId() + " is not qualified to drive the chosen truck");
            return false;
        }

        Truck smallTruck = transit.getTruck();

        if (transitControllerGui.transferLoad(smallTruck, newTruck)) {
            transitControllerGui.replaceTransitDriver(transit.getTransitId(), newDriver.getId(), newTruck.getPlateNumber(), "onTheFly");
            return true;
        }

        return false;
    }

    private void showOrder(OrderDocument orderDocument) {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        StringBuilder message = new StringBuilder();
        message.append("Order document ID: ").append(orderDocument.getOrderDocumentId()).append("\n");
        message.append("Source: ").append(orderDocument.getSource().getSupplierId()).append("\n");
        message.append("Destination: ").append(orderDocument.getDestination().getBranchID()).append("\n");
        message.append("Total weight: ").append(orderDocument.getTotalWeight()).append("\n\n");

        message.append("Products in this Order:\n");
        Map<Product, Double> productsList = orderDocument.getProductsList();
        for (Map.Entry<Product, Double> entry : productsList.entrySet()) {
            Product product = entry.getKey();
            Double quantity = entry.getValue();
            message.append("Product Name: ").append(product.getProductName()).append("\n");
            message.append("Amount: ").append(quantity).append("\n\n");
        }
        message.append("\n");

        textArea.setText(message.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        textArea.setCaretPosition(0);
        panel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(null, panel, "Order Document Details", JOptionPane.PLAIN_MESSAGE);
    }



}

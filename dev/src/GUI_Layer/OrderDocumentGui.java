package GUI_Layer;


import BussinesLogic.TransitCoordinator;
import ControllerLayer.*;
import DomainLayer.OrderDocument;
import DomainLayer.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class OrderDocumentGui extends JFrame {
    private final OrderDocumentController orderDocumentControllerImplGui;
    private final ProductController productController;
    private final SupplierController supplierController;
    private final TransitCoordinator transitCoordinator;
    public OrderDocumentGui(OrderDocumentController orderDocumentControllerImplGui,ProductController productController,
                            SupplierController supplierController,TransitCoordinator transitCoordinator) {
        this.orderDocumentControllerImplGui = orderDocumentControllerImplGui;
        this.productController = productController;
        this.supplierController = supplierController;
        this.transitCoordinator = transitCoordinator;


        setTitle("Manage Orders");
        setSize(400, 300);
        setLayout(new GridLayout(4, 1));

        JButton createOrderButton = new JButton("Create a new order");
        JButton showPendingOrdersButton = new JButton("Show pending orders");
        JButton editOrderButton = new JButton("Edit order");
        JButton backButton = new JButton("Back to Delivery System Menu");
        backButton.setForeground(Color.red);
        createOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel inputPanel = new JPanel();
                inputPanel.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(20, 15, 10, 15);

                JTextField supplierIdField = new JTextField(10);
                JTextField branchStoreIdField = new JTextField(10);
                JComboBox<String>[] productComboBoxes = new JComboBox[3];
                JTextField[] amountFields = new JTextField[3];

                gbc.gridx = 0;
                gbc.gridy = 0;
                inputPanel.add(new JLabel("Supplier ID:"), gbc);

                gbc.gridx = 1;
                gbc.gridy = 0;
                inputPanel.add(supplierIdField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                inputPanel.add(new JLabel("Branch Store ID:"), gbc);

                gbc.gridx = 1;
                gbc.gridy = 1;
                inputPanel.add(branchStoreIdField, gbc);

                Set<Product> products = productController.getProductSet();

                String[] productOptions = new String[products.size() + 1]; // Increase the size by 1

                // Add "null" as the first option
                productOptions[0] = "null";

                int index = 1; // Start from index 1
                for (Product product : products) {
                    productOptions[index] = product.getProductName();
                    index++;
                }

                for (int i = 0; i < 3; i++) {
                    gbc.gridx = 0;
                    gbc.gridy = i * 3 + 2;
                    productComboBoxes[i] = new JComboBox<>(productOptions);
                    inputPanel.add(new JLabel("Product " + (i + 1) + ":"), gbc);

                    gbc.gridx = 1;
                    gbc.gridy = i * 3 + 2;
                    inputPanel.add(productComboBoxes[i], gbc);

                    gbc.gridx = 0;
                    gbc.gridy = i * 3 + 3;
                    inputPanel.add(new JLabel("Amount (kg):"), gbc);

                    gbc.gridx = 1;
                    gbc.gridy = i * 3 + 3;
                    amountFields[i] = new JTextField(10);
                    inputPanel.add(amountFields[i], gbc);
                }

                inputPanel.setPreferredSize(new Dimension(300, 550));

                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Create Order", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    int supplierId;
                    try {
                        supplierId = Integer.parseInt(supplierIdField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid integer for Supplier ID.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int branchStoreId;
                    try {
                        branchStoreId = Integer.parseInt(branchStoreIdField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid integer for Branch Store ID.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Check if at least one product is selected
                    boolean productSelected = false;

                    for (int i = 0; i < 3; i++) {
                        String selectedProduct = (String) productComboBoxes[i].getSelectedItem();
                        if (!selectedProduct.equals("null")) {
                            productSelected = true;
                            break;
                        }
                    }

                    if (!productSelected) {
                        JOptionPane.showMessageDialog(null, "Please select at least one product.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int lineCount = 0;
                    for (int i = 0; i < 3; i++) {
                        String selectedProduct = (String) productComboBoxes[i].getSelectedItem();
                        if (!selectedProduct.equals("null")) {
                            lineCount++;
                        }
                    }

                    String[] selectedProducts = new String[lineCount];
                    double[] amounts = new double[lineCount];

                    int lineIndex = 0;

                    for (int i = 0; i < 3; i++) {
                        String selectedProduct = (String) productComboBoxes[i].getSelectedItem();
                        double amount;

                        if (!selectedProduct.equals("null")) {
                            try {
                                amount = Double.parseDouble(amountFields[i].getText());
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null, "Invalid input! Please enter valid doubles for Amount.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            selectedProducts[lineIndex] = selectedProduct;
                            amounts[lineIndex] = amount;

                            lineIndex++;
                        } else {
                            // Show an error if amount is entered for a "null" product
                            String amountText = amountFields[i].getText();
                            if (!amountText.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Please enter the amount only for selected products.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    }

                    OrderDocument orderDoc = orderDocumentControllerImplGui.createOrderDocDBD(supplierId, branchStoreId);
                    if (orderDoc == null) {
                        JOptionPane.showMessageDialog(null, "Store or Supplier ID has not been found. Failed to create the order document.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int weight = 0;
                        for (int i = 0; i < lineCount; i++) {
                            Product newProduct = productController.findProductByName(selectedProducts[i]);
                            int productId = newProduct.getProductId();
                            double productAmount = amounts[i];
                            weight += amounts[i];
                            orderDocumentControllerImplGui.addProductToOrderDocDB(orderDoc.getOrderDocumentId(), productId, productAmount);
                        }
                        orderDocumentControllerImplGui.updateWeightDB(orderDoc, weight);
                        StringBuilder message = new StringBuilder();
                        message.append("Order Document created successfully!\n");
                        message.append("Order Document ID: ").append(orderDoc.getOrderDocumentId()).append("\n");
                        message.append("Branch Store ID: ").append(branchStoreId).append("\n");
                        message.append("Supplier ID: ").append(supplierId).append("\n");

                        for (int i = 0; i < lineCount; i++) {
                            String product = selectedProducts[i];
                            double amount = amounts[i];
                            message.append("Product ").append(i + 1).append(": ").append(product).append(", Amount: ").append(amount).append("\n");
                        }

                        JOptionPane.showMessageDialog(null, message.toString(), "Order Document Created", JOptionPane.INFORMATION_MESSAGE);
                        // Perform any additional actions if needed
                    }
                }
            }
        });






        showPendingOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderDocumentControllerImplGui.showPendingOrderDocs();
                // Code to handle show pending orders action
            }
        });

        editOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                EditOrderGui editOrderGui = new EditOrderGui();
                editOrderGui.setVisible(true);
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

        add(createOrderButton);
        add(showPendingOrdersButton);
        add(editOrderButton);
        add(backButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    class EditOrderGui extends JFrame {


        public EditOrderGui() {
            setTitle("Edit Order");
            setSize(400, 300);
            setLayout(new GridLayout(4, 1));

            JButton addProductsButton = new JButton("Add products to an order");
            JButton changeProductAmountButton = new JButton("Change the amount of a product");
            JButton removeProductsButton = new JButton("Remove products");
            JButton backButton = new JButton("Back to Manage Orders");
            backButton.setForeground(Color.red);

            addProductsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JPanel inputPanel = new JPanel();
                    inputPanel.setLayout(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.fill = GridBagConstraints.HORIZONTAL;
                    gbc.insets = new Insets(20, 15, 10, 15);

                    JTextField orderDocumentIdField = new JTextField(10);
                    JComboBox<String>[] productComboBoxes = new JComboBox[3];
                    JTextField[] amountFields = new JTextField[3];

                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    gbc.gridwidth = 1;
                    inputPanel.add(new JLabel("Enter order document id:"), gbc);

                    gbc.gridx = 1;
                    gbc.gridy = 0;
                    gbc.gridwidth = 1;
                    inputPanel.add(orderDocumentIdField, gbc);

                    Set<Product> products = productController.getProductSet();

                    String[] productOptions = new String[products.size() + 2];
                    productOptions[0] = "null";

                    int index = 1;
                    for (Product product : products) {
                        productOptions[index] = product.getProductName();
                        index++;
                    }

                    for (int i = 0; i < 3; i++) {
                        gbc.gridx = 0;
                        gbc.gridy = i * 3 + 2;
                        gbc.gridwidth = 1;
                        productComboBoxes[i] = new JComboBox<>(productOptions);
                        inputPanel.add(new JLabel("Product " + (i + 1) + ":"), gbc);

                        gbc.gridx = 1;
                        gbc.gridy = i * 3 + 2;
                        gbc.gridwidth = 2;
                        inputPanel.add(productComboBoxes[i], gbc);

                        gbc.gridx = 0;
                        gbc.gridy = i * 3 + 3;
                        gbc.gridwidth = 1;
                        inputPanel.add(new JLabel("Amount:"), gbc);

                        gbc.gridx = 1;
                        gbc.gridy = i * 3 + 3;
                        gbc.gridwidth = 2;
                        amountFields[i] = new JTextField(10);
                        inputPanel.add(amountFields[i], gbc);
                    }

                    // Adjust the preferred size of the inputPanel
                    inputPanel.setPreferredSize(new Dimension(300, 250));

                    JScrollPane scrollPane = new JScrollPane();
                    scrollPane.setViewportView(inputPanel);
                    scrollPane.setPreferredSize(new Dimension(400, 300));

                    // Add the MouseWheelListener to the scrollPane
                    scrollPane.addMouseWheelListener(new MouseWheelListener() {
                        @Override
                        public void mouseWheelMoved(MouseWheelEvent e) {
                            JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
                            verticalScrollBar.setValue(verticalScrollBar.getValue() - e.getWheelRotation() * verticalScrollBar.getUnitIncrement());
                        }
                    });

                    int result = JOptionPane.showConfirmDialog(null, scrollPane, "Add Products", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        int orderDocumentId;
                        try {
                            orderDocumentId = Integer.parseInt(orderDocumentIdField.getText());
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid integer for Order Document ID.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        List<String> selectedProducts = new ArrayList<>();
                        List<Double> amounts = new ArrayList<>();
                        boolean hasEmptyAmount = false;  // Flag to track empty amounts

                        for (int i = 0; i < 3; i++) {
                            String selectedProduct = (String) productComboBoxes[i].getSelectedItem();
                            double amount;

                            if (!selectedProduct.equals("null")) {
                                try {
                                    amount = Double.parseDouble(amountFields[i].getText());
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(null, "Invalid input! Please enter valid doubles for Amount.", "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }

                                selectedProducts.add(selectedProduct);
                                amounts.add(amount);
                            } else {
                                // Check if amount is empty when product is null
                                if (amountFields[i].getText().isEmpty()) {
                                    hasEmptyAmount = true;
                                }
                            }
                        }

                        // Display error message if at least one product has an empty amount
                        if (hasEmptyAmount) {
                            JOptionPane.showMessageDialog(null, "Please enter the amount for all selected products.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        StringBuilder message = new StringBuilder();
                        message.append("Order Document ID: ").append(orderDocumentId).append("\n");

                        for (int i = 0; i < selectedProducts.size(); i++) {
                            String product = selectedProducts.get(i);
                            double amount = amounts.get(i);
                            message.append("Product ").append(i + 1).append(": ").append(product).append(", Amount: ").append(amount).append("\n");
                        }

                        JOptionPane.showMessageDialog(null, message.toString(), "Products Added", JOptionPane.INFORMATION_MESSAGE);
                        // Perform any additional actions if needed
                    }
                }
            });










            changeProductAmountButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Code to handle change product amount action
                }
            });

            removeProductsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Code to handle remove products action
                }
            });

            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    OrderDocumentGui orderDocumentGui = null;
                    try {
                        orderDocumentGui = new OrderDocumentGui(ControllerGen.getOrderDocumentControllerGui(),
                                ControllerGen.getProductController(),
                                ControllerGen.getSupplierController(),
                                ControllerGen.getTransitCoordinator());
                    } catch (SQLException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    orderDocumentGui.setVisible(true);
                }
            });

            add(addProductsButton);
            add(changeProductAmountButton);
            add(removeProductsButton);
            add(backButton);

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }
}
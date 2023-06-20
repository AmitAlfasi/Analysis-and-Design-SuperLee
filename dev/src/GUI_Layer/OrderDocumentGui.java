package GUI_Layer;


import BussinesLogic.TransitCoordinator;
import ControllerLayer.*;
import DomainLayer.OrderDocument;
import DomainLayer.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

class OrderDocumentGui extends JFrame {
    private final OrderDocumentController orderDocumentControllerGui;
    private final ProductController productController;
//    private final SupplierController supplierController;
//    private final TransitCoordinator transitCoordinator;
    public OrderDocumentGui(OrderDocumentController orderDocumentControllerGui,ProductController productController,
                            SupplierController supplierController,TransitCoordinator transitCoordinator) {
        this.orderDocumentControllerGui = orderDocumentControllerGui;
        this.productController = productController;
//        this.supplierController = supplierController;
//        this.transitCoordinator = transitCoordinator;


        setTitle("Manage Orders");
        setSize(400, 300);
        setLayout(new GridLayout(4, 1));

        JButton createOrderButton = new JButton("Create a new order");
        JButton showPendingOrdersButton = new JButton("Show pending orders");
        JButton editOrderButton = new JButton("Edit order");
        JButton backButton = new JButton("Back to Delivery System Menu");
        backButton.setForeground(Color.red);
        createOrderButton.addActionListener(e -> {
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
            List <String> pOptions = new ArrayList<>();
            // Add defaultString as the first option
            String defaultString = "Choose Product: ";
            pOptions.add(defaultString);
            for (Product product : products) {
                pOptions.add(product.getProductName());
            }

            for (int i = 0; i < pOptions.size()-1; i++) {
                gbc.gridx = 0;
                gbc.gridy = i * 3 + 2;
                productComboBoxes[i] = new JComboBox<>(pOptions.toArray(new String[0]));
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

            inputPanel.setPreferredSize(new Dimension(300, 430));

            UIManager.put("OptionPane.cancelButtonText", "Cancel");
            UIManager.put("OptionPane.okButtonText", "Ok");
            UIManager.put("OptionPane.cancelButtonText", "<html><font color='red'>Cancel</font></html>");

            int result = JOptionPane.showConfirmDialog(null, inputPanel, "Create Order", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                int supplierId;
                try {
                    supplierId = Integer.parseInt(supplierIdField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Supplier ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int branchStoreId;
                try {
                    branchStoreId = Integer.parseInt(branchStoreIdField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Branch Store ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if at least one product is selected
                boolean productSelected = false;

                for (int i = 0; i < 3; i++) {
                    String selectedProduct = (String) productComboBoxes[i].getSelectedItem();
                    if (!selectedProduct.equals(defaultString)) {
                        productSelected = true;
                        break;
                    }
                }

                if (!productSelected) {
                    JOptionPane.showMessageDialog(null, "Please select at least one product.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int lineCount = 0;
                for (int i = 0; i < productComboBoxes.length; i++) {
                    String selectedProduct = (String) productComboBoxes[i].getSelectedItem();
                    if (!selectedProduct.equals(defaultString)) {
                        lineCount++;
                    }
                }

                String[] selectedProducts = new String[lineCount];
                double[] amounts = new double[lineCount];
                int lineIndex = 0;

                for (int i = 0; i < productComboBoxes.length; i++) {
                    String selectedProduct = (String) productComboBoxes[i].getSelectedItem();
                    double amount;

                    if (!selectedProduct.equals(defaultString)) {
                        try {
                            amount = Double.parseDouble(amountFields[i].getText());
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid Amount.", "Error", JOptionPane.ERROR_MESSAGE);
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
                Set<String> helperSet = new HashSet<>(Arrays.asList(selectedProducts));
                if (helperSet.size() < selectedProducts.length) {
                    JOptionPane.showMessageDialog(null, "Error: Duplicate products selected.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                OrderDocument orderDocument = orderDocumentControllerGui.createOrderDocDBD(supplierId, branchStoreId);
                if (orderDocument == null) {
                    JOptionPane.showMessageDialog(null, "Store ID or Supplier ID has not been found. Failed to create the order document.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    double weight = 0;
                    for (int i = 0; i < lineCount; i++) {
                        Product newProduct = productController.findProductByName(selectedProducts[i]);
                        int productId = newProduct.getProductId();
                        double productAmount = amounts[i];
                        weight += amounts[i];
                        orderDocument.getProductsList().put(newProduct, productAmount);
                        orderDocumentControllerGui.addProductToOrderDocDB(orderDocument.getOrderDocumentId(), productId, productAmount);
                    }
                    orderDocumentControllerGui.updateWeightDB(orderDocument, weight);
                    StringBuilder message = new StringBuilder();
                    message.append("Order Document created successfully!\n");
                    message.append("Order Document ID: ").append(orderDocument.getOrderDocumentId()).append("\n");
                    message.append("Branch Store ID: ").append(branchStoreId).append("\n");
                    message.append("Supplier ID: ").append(supplierId).append("\n");

                    for (int i = 0; i < lineCount; i++) {
                        String product = selectedProducts[i];
                        double amount = amounts[i];
                        message.append("Product ").append(i + 1).append(": ").append(product).append(", Amount: ").append(amount).append("\n");
                    }

                    JOptionPane.showMessageDialog(null, message.toString(), "Order Document Created", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });







        showPendingOrdersButton.addActionListener(e -> orderDocumentControllerGui.showPendingOrderDocs());

        editOrderButton.addActionListener(e -> {
            dispose();
            EditOrderGui editOrderGui = new EditOrderGui();
            editOrderGui.setVisible(true);
        });

        backButton.addActionListener(e -> {
            dispose();
            PresentationGui presentationGui = new PresentationGui();
            presentationGui.setVisible(true);
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
            JButton updateProductAmountButton = new JButton("Update product amount");
            JButton removeProductsButton = new JButton("Remove products");
            JButton backButton = new JButton("Back to Manage Orders");
            backButton.setForeground(Color.red);



            addProductsButton.addActionListener(e -> {
                JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

                JTextField orderDocumentIdField = new JTextField(10);
                JComboBox<String> productComboBox = new JComboBox<>();
                JTextField amountField = new JTextField(10);

                inputPanel.add(new JLabel("Enter order document id:"));
                inputPanel.add(orderDocumentIdField);

                Set<Product> products = productController.getProductSet();

                List<String> pOptions = new ArrayList<>();
                String defaultString = "Choose Product: ";
                pOptions.add(defaultString);
                for (Product product : products) {
                    pOptions.add(product.getProductName());
                }

                // Populate the productComboBox with product options
                DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(pOptions.toArray(new String[0]));
                productComboBox.setModel(comboBoxModel);

                inputPanel.add(new JLabel("Product:"));
                inputPanel.add(productComboBox);
                inputPanel.add(new Label());
                inputPanel.add(new Label());
                inputPanel.add(new JLabel("Amount:"));
                inputPanel.add(amountField);

                inputPanel.setPreferredSize(new Dimension(300, 150));

                UIManager.put("OptionPane.okButtonText", "Ok");
                UIManager.put("OptionPane.cancelButtonText", "Cancel");
                UIManager.put("OptionPane.cancelButtonText", "<html><font color='red'>Cancel</font></html>");

                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Add Products", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    int orderDocumentId;
                    try {
                        orderDocumentId = Integer.parseInt(orderDocumentIdField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid Order Document ID.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    OrderDocument orderDocument = orderDocumentControllerGui.findOrderDocById(orderDocumentId);
                    if (orderDocument == null) {
                        JOptionPane.showMessageDialog(null, "Order document number does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String selectedProduct = (String) productComboBox.getSelectedItem();
                    double amount;
                    try {
                        amount = Double.parseDouble(amountField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (selectedProduct.equals(defaultString)) {
                        JOptionPane.showMessageDialog(null, "Please select a product.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Product product = productController.findProductByName(selectedProduct);
                    int productId = product.getProductId();

                    if (orderDocument.getProductsList().containsKey(product)) {
                        JOptionPane.showMessageDialog(null, "Product is already in the order document - go to 'Update product amount' ", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    orderDocument.getProductsList().put(product, amount);
                    orderDocumentControllerGui.addProductToOrderDocDB(orderDocumentId, productId, amount);

                    StringBuilder message = new StringBuilder();
                    message.append("Order Document ID: ").append(orderDocumentId).append("\n");
                    message.append("Product: ").append(selectedProduct).append(", Amount: ").append(amount).append("\n");

                    double currentWeight = orderDocument.getTotalWeight();
                    currentWeight += amount;
                    orderDocumentControllerGui.updateWeightDB(orderDocument, currentWeight);

                    JOptionPane.showMessageDialog(null, message.toString(), "Product Added", JOptionPane.INFORMATION_MESSAGE);
                }
            });


            updateProductAmountButton.addActionListener(e -> {
                // Create the input panel
                JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

                // Create the text field for order document ID
                JTextField orderDocumentField = new JTextField(10);
                inputPanel.add(new JLabel("Order Document ID:"));
                inputPanel.add(orderDocumentField);

                // Populate the comboBox with product options
                Set<Product> products = productController.getProductSet();
                List<String> pOptions = new ArrayList<>();
                String defaultString = "Choose Product: ";
                pOptions.add(defaultString);
                for (Product product : products) {
                    pOptions.add(product.getProductName());
                }

                // Create the comboBox for product selection
                JComboBox<String> comboBox = new JComboBox<>(pOptions.toArray(new String[0]));
                inputPanel.add(new JLabel("Select a product:"));
                inputPanel.add(comboBox);

                // Create the text field for the amount
                JTextField amountField = new JTextField(10); // Increase the size to accommodate the input
                inputPanel.add(new JLabel("Amount:"));
                inputPanel.add(amountField);

                UIManager.put("OptionPane.okButtonText", "OK");
                UIManager.put("OptionPane.cancelButtonText", "<html><font color='red'>Cancel</font></html>");

                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Update Product Amount", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    int orderDocumentId;
                    try {
                        orderDocumentId = Integer.parseInt(orderDocumentField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid Order Document ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!orderDocumentControllerGui.orderDocumentChooser(orderDocumentId)) {
                        JOptionPane.showMessageDialog(null, "Order Document ID: " + orderDocumentId + " does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    OrderDocument orderDocument = orderDocumentControllerGui.findOrderDocById(orderDocumentId);
                    String selectedProductName = (String) comboBox.getSelectedItem();
                    if (selectedProductName.equals(defaultString)) {
                        JOptionPane.showMessageDialog(null, "Please select a product.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Product productToUpdate = productController.findProductByName(selectedProductName);
                    if (!orderDocument.getProductsList().containsKey(productToUpdate)) {
                        JOptionPane.showMessageDialog(null, "The chosen product is not in Order Document ID: " + orderDocumentId, "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double amount;
                    try {
                        amount = Double.parseDouble(amountField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid Amount. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Update the product amount in the order document
                    orderDocumentControllerGui.updateAmountDBD(orderDocumentId, selectedProductName, amount);
                    JOptionPane.showMessageDialog(null, "Product amount updated successfully in Order Document ID: " + orderDocumentId, "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            });



            removeProductsButton.addActionListener(e -> {
                JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

                JTextField orderIdField = new JTextField(10);
                JComboBox<String> productComboBox = new JComboBox<>();

                inputPanel.add(new JLabel("Order Document ID:"));
                inputPanel.add(orderIdField);

                Set<Product> products = productController.getProductSet();

                List<String> pOptions = new ArrayList<>();
                String defaultString = "Choose Product: ";
                pOptions.add(defaultString);
                for (Product product : products) {
                    pOptions.add(product.getProductName());
                }

                // Populate the productComboBox with product options
                DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(pOptions.toArray(new String[0]));
                productComboBox.setModel(comboBoxModel);
                inputPanel.add(new JLabel("Select a product:"));
                inputPanel.add(productComboBox);

                UIManager.put("OptionPane.okButtonText", "OK");
                UIManager.put("OptionPane.cancelButtonText", "<html><font color='red'>Cancel</font></html>");

                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Remove Products", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    int orderId;
                    try {
                        orderId = Integer.parseInt(orderIdField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid Order Document ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!orderDocumentControllerGui.orderDocumentChooser(orderId)) {
                        JOptionPane.showMessageDialog(null, "Order Document ID: " + orderId + " does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    OrderDocument orderDocument = orderDocumentControllerGui.findOrderDocById(orderId);
                    String selectedProduct = (String) productComboBox.getSelectedItem();
                    if (selectedProduct.equals(defaultString)) {
                        JOptionPane.showMessageDialog(null, "Please select a product.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Product productToRemove = productController.findProductByName(selectedProduct);
                    if (!orderDocument.getProductsList().containsKey(productToRemove)) {
                        JOptionPane.showMessageDialog(null, "The chosen product is not in Order Document ID: " + orderId, "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    orderDocumentControllerGui.removeProductFromOrderDocDBdP(orderId, selectedProduct);
                    JOptionPane.showMessageDialog(null, "Product removed successfully from Order Document ID: " + orderId, "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            backButton.addActionListener(e -> {
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
            });

            add(addProductsButton);
            add(updateProductAmountButton);
            add(removeProductsButton);
            add(backButton);

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }
}
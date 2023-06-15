package GUI_Layer;


import BussinesLogic.TransitCoordinator;
import ControllerLayer.*;
import DomainLayer.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
                final int[] lineCount = {1};

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

                String[] productOptions = { "null", "Apple", "Banana", "Orange" };

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

                inputPanel.setPreferredSize(new Dimension(300, 430));

                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Create Order", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int supplierId = Integer.parseInt(supplierIdField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid integer for Supplier ID.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        int branchStoreId = Integer.parseInt(branchStoreIdField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid integer for Branch Store ID.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String[] selectedProducts = new String[lineCount[0]];
                    double[] amounts = new double[lineCount[0]];

                    for (int i = 0; i < lineCount[0]; i++) {
                        try {
                            String selectedProduct = (String) productComboBoxes[i].getSelectedItem();
                            double amount = Double.parseDouble(amountFields[i].getText());

                            if (selectedProduct.equals("null")) {
                                JOptionPane.showMessageDialog(null, "Invalid input! Please select a valid product for Product " + (i + 1) + ".", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            selectedProducts[i] = selectedProduct;
                            amounts[i] = amount;
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid input! Please enter valid doubles for Amount.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    HashMap<Product, Double> productSet = new HashMap<>();
                    for (int i = 0; i < lineCount[0]; i++) {
                        //productSet.put(new Product(selectedProducts[i],), amounts[i]);
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
                // Code to handle add products action
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
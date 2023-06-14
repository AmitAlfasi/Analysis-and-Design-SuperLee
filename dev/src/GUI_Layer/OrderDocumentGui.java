package GUI_Layer;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class OrderDocumentGui extends JFrame {
    public OrderDocumentGui() {
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
                // Code to handle create order action
            }
        });

        showPendingOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                OrderDocumentGui orderDocumentGui = new OrderDocumentGui();
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
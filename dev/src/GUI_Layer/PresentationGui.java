package GUI_Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PresentationGui extends JFrame {
    public PresentationGui() {
        setTitle("Delivery System Menu");
        setSize(400, 300);
        setLayout(new GridLayout(6, 1));

        JButton createTransitButton = new JButton("Create new transit");
        JButton updateTransitButton = new JButton("Update transit");
        JButton manageTrucksButton = new JButton("Manage trucks");
        JButton manageDocumentsButton = new JButton("Manage documents");
        JButton manageOrdersButton = new JButton("Manage orders");
        JButton escapeMenuButton = new JButton("Exit");

        createTransitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle create new transit action
            }
        });

        updateTransitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                TransitGui transitGui = new TransitGui();
                transitGui.setVisible(true);
            }
        });

        manageTrucksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                TruckGui truckGui = new TruckGui();
                truckGui.setVisible(true);
            }
        });

        manageDocumentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayDocumentManagerMenu();
            }
        });

        manageOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                OrderDocumentGui orderDocumentGui = new OrderDocumentGui();
                orderDocumentGui.setVisible(true);
            }
        });

        escapeMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        add(createTransitButton);
        add(updateTransitButton);
        add(manageTrucksButton);
        add(manageDocumentsButton);
        add(manageOrdersButton);
        add(escapeMenuButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PresentationGui presentationGui = new PresentationGui();
            }
        });
    }




    private void displayDocumentManagerMenu() {
        // Set the title of the window to "Manage Documents"
        setTitle("Manage Documents");

        // Remove all components from the content pane of the window
        getContentPane().removeAll();

        // Set the size of the window to 400 pixels wide and 300 pixels high
        setSize(400, 300);

        // Set the layout manager for the content pane to a GridLayout with 4 rows and 1 column
        setLayout(new GridLayout(4, 1));

        // Create and initialize four JButton objects with their respective labels
        JButton showPendingOrdersButton = new JButton("Show pending orders (by area)");
        JButton showCompletedOrdersButton = new JButton("Show completed orders");
        JButton showTransitRecordsButton = new JButton("Show Transit records");
        JButton backButton = new JButton("Back to Delivery System Menu");

        // Add action listeners to the showPendingOrdersButton
        showPendingOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle show pending orders action
            }
        });

        // Add action listeners to the showCompletedOrdersButton
        showCompletedOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle show completed orders action
            }
        });

        // Add action listeners to the showTransitRecordsButton
        showTransitRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle show transit records action
            }
        });

        // Add action listeners to the backButton
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Dispose the current window
                dispose();

                // Create a new instance of PresentationGui and make it visible
                PresentationGui presentationGui = new PresentationGui();
                presentationGui.setVisible(true);
            }
        });

        // Add the buttons to the content pane of the window
        add(showPendingOrdersButton);
        add(showCompletedOrdersButton);
        add(showTransitRecordsButton);
        add(backButton);

        // Revalidate the window to reflect the changes
        revalidate();

        // Repaint the window to update its appearance
        repaint();
    }

}




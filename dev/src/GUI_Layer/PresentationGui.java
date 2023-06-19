package GUI_Layer;

import ControllerLayer.ControllerGen;
import ControllerLayer.OrderDocumentController;
import ControllerLayer.OrderDocumentControllerImplGui;
import ControllerLayer.TransitRecordController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class PresentationGui extends JFrame {
    OrderDocumentController orderDocumentControllerGui;
    TransitRecordController transitRecordControllerGui;
    public PresentationGui() {
        try {
            this.orderDocumentControllerGui = ControllerGen.getOrderDocumentControllerGui();
            this.transitRecordControllerGui = ControllerGen.getTransitRecordControllerGui();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        setTitle("Delivery System Menu");
        setSize(400, 300);
        setLayout(new GridLayout(6, 1));

        JButton createTransitButton = new JButton("Create new transit");
        JButton updateTransitButton = new JButton("Update transit");
        JButton manageTrucksButton = new JButton("Manage trucks");
        JButton manageDocumentsButton = new JButton("Manage documents");
        JButton manageOrdersButton = new JButton("Manage orders");
        JButton escapeMenuButton = new JButton("Exit");
        escapeMenuButton.setForeground(Color.red);

        createTransitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransitGui transitGui = null;
                try {
                    transitGui = new TransitGui(ControllerGen.getTransitControllerGUI(), ControllerGen.getTruckControllerGUI(),
                            ControllerGen.getTransitCoordinator(), ControllerGen.getOrderDocumentControllerGui(), ControllerGen.getTransitRecordController());
                } catch (SQLException | ClassNotFoundException ex)
                {
                    ex.printStackTrace();
                }
                dispose();
                transitGui.setAlwaysOnTop(true);
                transitGui.navigateToMainMenu();
                transitGui.createNewTransit();
            }
        });


        updateTransitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                TransitGui transitGui = null;
                try {
                    transitGui = new TransitGui(ControllerGen.getTransitControllerGUI(), ControllerGen.getTruckControllerGUI(),
                            ControllerGen.getTransitCoordinator(), ControllerGen.getOrderDocumentControllerGui(), ControllerGen.getTransitRecordController());
                } catch (SQLException | ClassNotFoundException ex)
                {
                    ex.printStackTrace();
                }
                transitGui.setVisible(true);
            }
        });

        manageTrucksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                TruckGui truckGui = null;
                try {
                    truckGui = new TruckGui(ControllerGen.getTruckControllerGUI());
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
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

//                OrderDocumentControllerImplGui
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
        backButton.setForeground(Color.red);


        // Add action listeners to the showPendingOrdersButton
        showPendingOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderDocumentControllerGui.showPendingOrderDocs();
            }
        });

        // Add action listeners to the showCompletedOrdersButton
        showCompletedOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderDocumentControllerGui.showCompletedOrderDocs();
                // Code to handle show completed orders action
            }
        });

        // Add action listeners to the showTransitRecordsButton
        showTransitRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transitRecordControllerGui.showTransitRecords();
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




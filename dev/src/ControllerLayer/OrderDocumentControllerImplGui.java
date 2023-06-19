package ControllerLayer;

import BussinesLogic.TransitCoordinator;
import DataAccessLayer.OrderDocumentDAO;
import DomainLayer.OrderDocument;
import DomainLayer.Product;
import DomainLayer.Truck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.*;
import java.util.List;

public class OrderDocumentControllerImplGui extends OrderDocumentControllerImpl {

    public OrderDocumentControllerImplGui(OrderDocumentDAO orderDocDAO,
                                          SupplierController supplierController,
                                          TransitCoordinator transitCoordinator,
                                          ProductController productController) {
        super(orderDocDAO, supplierController, transitCoordinator, productController);
    }
    @Override
    public void showPendingOrderDocs() {
        Set<OrderDocument> pendingOrders = orderDocumentDAO.getOrderDocsSet(false);
        List<OrderDocument> sortedOrders = new ArrayList<>(pendingOrders);
        Comparator<OrderDocument> bySupplierArea = Comparator.comparing(
                order -> order.getSource().getAreaCode().name());
        sortedOrders.sort(bySupplierArea);

        if (pendingOrders.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Orders Documents found.", "Order Document Manager", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JPanel panel = new JPanel(new BorderLayout());
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);

            StringBuilder message = new StringBuilder();
            message.append("All OrderDocuments By Area:\n\n");
            for (OrderDocument orderDocument : sortedOrders) {
                message.append("Order Area: ").append(orderDocument.getSource().getAreaCode()).append("\n");
                message.append("Order document ID: ").append(orderDocument.getOrderDocumentId()).append("\n");
                message.append("supplier ID: ").append(orderDocument.getSource().getSupplierId()).append("\n");
                message.append("BranchStore ID: ").append(orderDocument.getDestination().getBranchID()).append("\n");
                message.append("Total weight in this order: ").append(orderDocument.getTotalWeight()).append("\n\n");

                message.append("Products in this Order:\n");
                Map<Product, Double> productsList = orderDocument.getProductsList();
                for (Map.Entry<Product, Double> entry : productsList.entrySet()) {
                    Product product = entry.getKey();
                    Double quantity = entry.getValue();
                    message.append("Product Name: ").append(product.getProductName()).append("\n");
                    message.append("Quantity: ").append(quantity).append("\n\n");
                }
                message.append("\n");
            }

            textArea.setText(message.toString());

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            textArea.setCaretPosition(0);
            panel.add(scrollPane, BorderLayout.CENTER);

            JOptionPane.showMessageDialog(null, panel, "Order Document Manager", JOptionPane.PLAIN_MESSAGE);
        }
    }
    @Override
    public void showCompletedOrderDocs(){
        Set<OrderDocument> completedOrders = orderDocumentDAO.getOrderDocsSet(true);
        List<OrderDocument> sortedOrders = new ArrayList<>(completedOrders);
        Comparator<OrderDocument> bySupplierArea = Comparator.comparing(
                order -> order.getSource().getAreaCode().name());
        sortedOrders.sort(bySupplierArea);

        if (completedOrders.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Orders Documents found.", "Order Document Manager", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JPanel panel = new JPanel(new BorderLayout());
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);

            StringBuilder message = new StringBuilder();
            message.append("All OrderDocuments By Area:\n\n");
            for (OrderDocument orderDocument : sortedOrders) {
                message.append("Order Area: ").append(orderDocument.getSource().getAreaCode()).append("\n");
                message.append("Order document ID: ").append(orderDocument.getOrderDocumentId()).append("\n");
                message.append("supplier ID: ").append(orderDocument.getSource().getSupplierId()).append("\n");
                message.append("BranchStore ID: ").append(orderDocument.getDestination().getBranchID()).append("\n");
                message.append("Total weight in this order: ").append(orderDocument.getTotalWeight()).append("\n\n");

                message.append("Products in this Order:\n");
                Map<Product, Double> productsList = orderDocument.getProductsList();
                for (Map.Entry<Product, Double> entry : productsList.entrySet()) {
                    Product product = entry.getKey();
                    Double quantity = entry.getValue();
                    message.append("Product Name: ").append(product.getProductName()).append("\n");
                    message.append("Quantity: ").append(quantity).append("\n\n");
                }
                message.append("\n");
            }

            textArea.setText(message.toString());

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            textArea.setCaretPosition(0);
            panel.add(scrollPane, BorderLayout.CENTER);

            JOptionPane.showMessageDialog(null, panel, "Order Document Manager", JOptionPane.PLAIN_MESSAGE);
        }
    }



}

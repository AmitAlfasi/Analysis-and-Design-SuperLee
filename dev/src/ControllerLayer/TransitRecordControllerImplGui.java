package ControllerLayer;

import DataAccessLayer.TransitRecordDAO;
import DomainLayer.OrderDocument;
import DomainLayer.Product;
import DomainLayer.Supplier;
import DomainLayer.TransitRecord;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TransitRecordControllerImplGui extends TransitRecordControllerImpl{
    public TransitRecordControllerImplGui(TransitRecordDAO transitRecordDAO) {
        super(transitRecordDAO);
    }
    @Override
    public void showTransitRecords(){

        Set<TransitRecord> transitRecords = transitRecordDAO.getTransitRecordsSet();
        List<TransitRecord> transitRecordList = new ArrayList<>(transitRecords);

        if (transitRecords.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No transit records found.", "Transit record Manager", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JPanel panel = new JPanel(new BorderLayout());
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);

            StringBuilder message = new StringBuilder();
            message.append("Transit Records:\n\n");
            for (TransitRecord transitRecord : transitRecordList) {
                message.append("Record id: ").append(transitRecord.getTransitRecordId()).append("\n");
                message.append("Transit id: ").append(transitRecord.getTransit().getTransitId()).append("\n");
                message.append("Overweight in record: ").append(transitRecord.isTransitProblem()).append("\n");

                message.append("Weight at exit from each supplier:\n");
                Map<Supplier, Double> transitWeightAtExit = transitRecord.getWeightsAtExits();
                transitWeightAtExit.forEach((supplier, weight) -> {
                    message.append("Supplier id: ").append(supplier.getSupplierId()).append("\n");
                    message.append("Truck weight at exit: ").append(weight).append("\n");
                });
                message.append("\n");
            }

            textArea.setText(message.toString());

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            textArea.setCaretPosition(0);
            panel.add(scrollPane, BorderLayout.CENTER);

            JOptionPane.showMessageDialog(null, panel, "Transit record manager", JOptionPane.PLAIN_MESSAGE);
        }
    }
}

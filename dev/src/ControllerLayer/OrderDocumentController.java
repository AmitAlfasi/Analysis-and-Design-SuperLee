package ControllerLayer;

import DomainLayer.OrderDocument;


public interface OrderDocumentController {
    OrderDocument createOrderDocDBD(int sourceId, int destinationId);
    void updateWeightDB(OrderDocument orderDocument, double weight);
    void addProductToOrderDocDB(int orderDocumentId, int productId, double productAmount);
    void showAllProductsInDoc(int orderId);
    void updateAmountDBD(int orderId, String productName, double amount);
    OrderDocument removeProductFromOrderDocDBdP(int orderDocumentId, String productName);
    boolean orderDocumentChooser(int orderId);
    void moveOrderToFinishDB(OrderDocument completedOrder);
    void showCompletedOrderDocs();
    void showPendingOrderDocs();
    OrderDocument findOrderDocById(int orderId);
    void removeOrderCompletely(OrderDocument orderDocument);

    //    Set<OrderDocument> getOrderDocumentsSet();
}

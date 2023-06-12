package DataAccessLayer;

import DomainLayer.OrderDocument;

import java.util.Set;

public interface OrderDocumentDAO {
    void saveOrderDocument(OrderDocument orderDocument);
    void addProductToOrderDocument(int orderDocumentId, int productId, double productAmount);
    void removeProductFromOrder(int orderDocumentId, int productId);
    void updateOrderDocumentWeight(int orderDocumentId, double weight);
    void updateProductAmount(int orderDocumentId, int productId, double weight);
    OrderDocument findOrderDocumentById(int OrderDocId);
    void moveToCompleted(OrderDocument completedOrder);
    Set<OrderDocument> getOrderDocsSet(boolean isCompleted);

    void removeOrder(OrderDocument orderDocument);
}
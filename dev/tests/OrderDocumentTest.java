import BussinesLogic.BranchStore;
import DataAccess.DAO_BranchStore;
import DataAccess.DAO_Generator;
import DataAccessLayer.*;
import DomainLayer.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class OrderDocumentTest {
    private SupplierDAO supplierDAO;
    private DAO_BranchStore daoBranchStore;
    private OrderDocumentDAO orderDocumentDAO;
    private ProductDAO productDAO;
    private OrderDocument orderDocument;
    @Before
    public void setUp() throws Exception {
        supplierDAO = DAO_Generator.getSupplierDAO();
        daoBranchStore = DAO_Generator.getBranchStoreDAO();
        orderDocumentDAO = DAO_Generator.getOrderDocumentDAO();
        productDAO = DAO_Generator.getProductDAO();
        // Generate a random plate number for the test truck
        BranchStore b1 = (BranchStore) daoBranchStore.findByID(1);
        Supplier s1 = supplierDAO.findSupplierById(1);
        orderDocument = new OrderDocument(s1, b1);
        orderDocument.getProductsList().put(productDAO.findProductById(1), 1000.0);
        orderDocument.getProductsList().put(productDAO.findProductById(2), 5000.0);
        orderDocument.setTotalWeight(6000.0);
    }

    @After
    public void tearDown() throws Exception {
        orderDocumentDAO.removeOrder(orderDocument);
    }

    @Test
    public void testSaveOrderDoc() {
        orderDocumentDAO.saveOrderDocument(orderDocument);
        orderDocumentDAO.addProductToOrderDocument(orderDocument.getOrderDocumentId(),1,1000.0 );
        orderDocumentDAO.addProductToOrderDocument(orderDocument.getOrderDocumentId(),2,5000.0 );
        OrderDocument savedOrderDoc = orderDocumentDAO.findOrderDocumentById(orderDocument.getOrderDocumentId());
        savedOrderDoc.printOrder();
        assertNotNull(savedOrderDoc);
        assertEquals(savedOrderDoc.getSource().getSupplierId(), orderDocument.getSource().getSupplierId());
        assertEquals(savedOrderDoc.getDestination().getBranchID(), orderDocument.getDestination().getBranchID());
        assertEquals(savedOrderDoc.getTotalWeight(), orderDocument.getTotalWeight(), 0.01);
        assertTrue(savedOrderDoc.getProductsList().containsKey(productDAO.findProductById(1)));
        assertTrue(savedOrderDoc.getProductsList().containsValue(1000.0));
        assertTrue(savedOrderDoc.getProductsList().containsKey(productDAO.findProductById(2)));
        assertTrue(savedOrderDoc.getProductsList().containsValue(5000.0));
    }
    @Test
    public void testRemoveProductFromOrderDocDAO() {
        orderDocumentDAO.saveOrderDocument(orderDocument);
        orderDocumentDAO.removeProductFromOrder(orderDocument.getOrderDocumentId(), 1);
        OrderDocument savedOrderDoc = orderDocumentDAO.findOrderDocumentById(orderDocument.getOrderDocumentId());
        assertFalse(savedOrderDoc.getProductsList().containsKey(productDAO.findProductById(1)));
    }

    @Test
    public void testOrderDocumentTotalQuantity() {
        // Calculate the expected total quantity
        double expectedTotalQuantity = 1000.0 + 5000.0;

        // Get the actual total quantity from the order document
        double actualTotalWeight = orderDocument.getTotalWeight();

        // Assert that the expected total quantity matches the actual total quantity
        Assert.assertEquals(expectedTotalQuantity, actualTotalWeight, 0.001);
    }

    @Test
    public void testOrderDocumentContainsProduct() {
        // Get a product from the product DAO for testing
        Product product1 = productDAO.findProductById(1);
        Product product2 = productDAO.findProductById(2);

        // Check if the order document contains the product
        boolean containsProduct1 = orderDocument.getProductsList().containsKey(product1);
        boolean containsProduct2 = orderDocument.getProductsList().containsKey(product2);

        // Assert that the order document contains the product
        assertTrue(containsProduct1);
        assertTrue(containsProduct2);
    }
    @Test
    public void testRemoveOrderDocument() {
        orderDocumentDAO.saveOrderDocument(orderDocument);
        orderDocumentDAO.removeOrder(orderDocument);
        OrderDocument removerOrder = orderDocumentDAO.findOrderDocumentById(orderDocument.getOrderDocumentId());
        assertNull(removerOrder);
    }

}

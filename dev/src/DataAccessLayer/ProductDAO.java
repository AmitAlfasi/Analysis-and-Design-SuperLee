package DataAccessLayer;

import DomainLayer.Product;

import java.util.Set;

public interface ProductDAO {
    void saveProduct(Product product);
    void removeProduct(Product product);
    Set<Product> getProductsSet();
    Product findProductById(int productID);
}

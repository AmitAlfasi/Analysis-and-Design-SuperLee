package ControllerLayer;

import DomainLayer.Product;

import java.util.Set;

public interface ProductController {
    Product findProductByName(String productName);
    void showAllProducts();
    public Set<Product> getProductSet();
}

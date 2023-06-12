package ControllerLayer;

import DomainLayer.Product;

public interface ProductController {
    Product findProductByName(String productName);
    void showAllProducts();
}

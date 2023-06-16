package ControllerLayer;

import DataAccessLayer.ProductDAO;
import DomainLayer.Product;

import java.util.Set;

public class ProductControllerImpl implements ProductController {
    private final ProductDAO productDAO;


    public ProductControllerImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }
    @Override
    public Product findProductByName(String productName) {
        for (Product product : productDAO.getProductsSet()) {
            if (product.getProductName().equalsIgnoreCase(productName)) {
                return product;
            }
        }
        return null;
    }
    @Override
    public void showAllProducts() {
        for (Product product: productDAO.getProductsSet())
        {
            product.printProduct();
        }
    }
    @Override
    public Set<Product> getProductSet()
    {
        return productDAO.getProductsSet();
    }

}
package DataAccessLayer;

import DomainLayer.Product;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

public class ProductDAOImpl implements ProductDAO {
    private final Set<Product> productsSet = new HashSet<>();

    public ProductDAOImpl() {
        Product p1 = new Product(1,"Banana");
        Product p2 = new Product(2,"Apple");
        Product p3 = new Product(3,"Orange");
        productsSet.add(p1);
        productsSet.add(p2);
        productsSet.add(p3);
    }

    @Override
    public void saveProduct(Product product) {
        productsSet.add(product);
    }
    @Override
    public void removeProduct(Product product) {
        productsSet.remove(product);
    }
    @Override
    public Set<Product> getProductsSet() {
        return productsSet;
    }
    @Override
    public Product findProductById(int productID) {
        for (Product product: productsSet){
            if (product.getProductId() == productID)
                return product;
        }
        return null;
    }
}

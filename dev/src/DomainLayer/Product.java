package DomainLayer;

public class Product {
    private final int productId;
    private final String productName;


    public Product(int productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }
    public int getProductId() {
        return productId;
    }
    public String getProductName() {
        return productName;
    }
    public void printProduct()
    {
        System.out.println(productName + ": " + productId );
    }
}

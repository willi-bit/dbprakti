public class ProductCatalog {
    String storeId;
    String productId;
    Float price;
    boolean isAvailable;
    String condition;

    public ProductCatalog(String storeId, String productId, Float price, boolean isAvailable, String condition) {
        this.storeId = storeId;
        this.productId = productId;
        this.price = price;
        this.isAvailable = isAvailable;
        this.condition = condition;
    }
}

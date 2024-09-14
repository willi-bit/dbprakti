package Entities;

import jakarta.persistence.*;

@Entity
@Table(name="ProductCatalog")
public class ProductCatalog {

    @EmbeddedId
    public ProductCatalogId id;
    @ManyToOne
    @MapsId("storeId")
    @JoinColumn(name = "Store", referencedColumnName = "StoreID", foreignKey = @ForeignKey(name = "fk_product_catalog_store"))
    public Store store;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "Product", referencedColumnName = "ProductID", foreignKey = @ForeignKey(name = "fk_product_catalog_product"))
    public Product product;

    @Column(name="price")
    public Float price;

    @Column(name="Available")
    public boolean isAvailable;

    @Column(name="condition")
    public String condition;

    public ProductCatalog() {}

    public ProductCatalog(String storeId, String productId, Float price, boolean isAvailable, String condition) {
        this.price = price;
        this.isAvailable = isAvailable;
        this.condition = condition;
    }
}

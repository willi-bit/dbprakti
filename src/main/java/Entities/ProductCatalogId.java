package Entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductCatalogId implements Serializable {

    @Column(name = "Store")
    public String storeId;

    @Column(name = "Product")
    public String productId;

    @Column(name = "Condition")
    public String condition;

    public ProductCatalogId() {}

    public ProductCatalogId(String storeId, String productId, String condition) {
        this.storeId = storeId;
        this.productId = productId;
        this.condition = condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCatalogId that = (ProductCatalogId) o;
        return Objects.equals(storeId, that.storeId) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, productId, condition);
    }

}

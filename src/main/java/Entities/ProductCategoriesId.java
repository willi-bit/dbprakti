package Entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductCategoriesId implements Serializable {

    @Column(name = "Product")
    public String productId;

    @Column(name = "Category")
    public String categoryId;

    public ProductCategoriesId() {}

    public ProductCategoriesId(String productId, String categoryId) {
        this.productId = productId;
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategoriesId that = (ProductCategoriesId) o;
        return Objects.equals(productId, that.productId) &&
                Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, categoryId);
    }

}

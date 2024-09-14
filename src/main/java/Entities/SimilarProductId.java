package Entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SimilarProductId implements Serializable {

    @Column(name = "Product1", length = 255)
    private String product1Id;

    @Column(name = "Product2", length = 255)
    private String product2Id;

    public SimilarProductId() {}

    public SimilarProductId(String product1Id, String product2Id) {
        this.product1Id = product1Id;
        this.product2Id = product2Id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimilarProductId that = (SimilarProductId) o;
        return Objects.equals(product1Id, that.product1Id) &&
                Objects.equals(product2Id, that.product2Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product1Id, product2Id);
    }

    public String getProduct1Id() {
        return product1Id;
    }

    public void setProduct1Id(String product1Id) {
        this.product1Id = product1Id;
    }

    public String getProduct2Id() {
        return product2Id;
    }

    public void setProduct2Id(String product2Id) {
        this.product2Id = product2Id;
    }
}

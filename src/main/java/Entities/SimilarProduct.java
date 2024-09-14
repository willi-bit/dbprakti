package Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "SimilarProduct")
public class SimilarProduct {

    @EmbeddedId
    private SimilarProductId id;

    @ManyToOne
    @MapsId("product1Id")
    @JoinColumn(name = "Product1", referencedColumnName = "ProductID", foreignKey = @ForeignKey(name = "fk_similar_product1"))
    private Product product1;

    @ManyToOne
    @MapsId("product2Id")
    @JoinColumn(name = "Product2", referencedColumnName = "ProductID", foreignKey = @ForeignKey(name = "fk_similar_product2"))
    private Product product2;

    // Getters and Setters
    public SimilarProductId getId() {
        return id;
    }

    public void setId(SimilarProductId id) {
        this.id = id;
    }

    public Product getProduct1() {
        return product1;
    }

    public void setProduct1(Product product1) {
        this.product1 = product1;
    }

    public Product getProduct2() {
        return product2;
    }

    public void setProduct2(Product product2) {
        this.product2 = product2;
    }
}

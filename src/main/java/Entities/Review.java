package Entities;

import jakarta.persistence.*;

@Entity
@Table(name="Review")
public class Review {

    @Id
    @Column(name = "ReviewID", length = 255, nullable = false)
    public String reviewId;

    @ManyToOne
    @JoinColumn(name = "Customer", referencedColumnName = "CustomerID", foreignKey = @ForeignKey(name = "fk_review_customer"), nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "Product", referencedColumnName = "ProductID", foreignKey = @ForeignKey(name = "fk_review_product"), nullable = false)
    public Product product;

    @Column(name = "Stars", nullable = false)
    public int stars;

    @Column(name = "Summary", columnDefinition = "TEXT", nullable = false)
    public String summary;

    @Column(name = "Review", columnDefinition = "TEXT", nullable = false)
    public String review;

    @Column(name = "Helpful", nullable = false)
    public int helpful;

    @Column(name = "Username", length = 255, nullable = false)
    public String username;

    public String productId;

    public Review() {}

    public Review(String id, String product, int stars, String summary, String review, int helpful, String username) {
        this.reviewId = id;
        this.stars = stars;
        this.summary = summary;
        this.review = review;
        this.helpful = helpful;
        this.username = username;
        this.productId = product;
    }
}

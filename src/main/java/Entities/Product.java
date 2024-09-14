package Entities;

import jakarta.persistence.*;

@Entity
@Table(name="Product")
public class Product implements Comparable<Product> {

    @Id
    @Column(name="ProductID")
    public String productID;

    @Column(name = "Title", nullable = false)
    public String title;

    @Column(name="Rating")
    public float rating;

    @Column(name="Rank", nullable = false)
    public Integer rank;

    @Column(name="Picture")
    public String picture;

    public Product() {}

    public Product(String id, String title, float rating, Integer rank, String picture) {
        this.productID = id;
        this.title = title;
        this.rating = rating;
        this.rank = rank;
        this.picture = picture;
    }
    @Override
    public int compareTo(Product o) {
        return this.productID.compareTo(o.productID);
    }
}
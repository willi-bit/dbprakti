package Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "ProductID")
    public String productId;

    @Column(name="Publisher", nullable = false)
    public String publisher;

    @Column(name="pages", nullable = false)
    public Integer pages;

    @Column(name = "ReleaseDate", nullable = false)
    @Temporal(TemporalType.DATE)
    public java.sql.Date releaseDate;

    @Column(name="ISBN", nullable = false, unique = true)
    public String isbn;

    @OneToOne
    @JoinColumn(name = "ProductID", referencedColumnName = "ProductID", foreignKey = @ForeignKey(name = "fk_book_product"))
    public Product product;

    public String authorId;

    public Book() {}

    public Book(String id, String author, String publisher, Integer pages, java.sql.Date releaseDate, String isbn) {
        this.productId = id;
        this.publisher = publisher;
        this.pages = pages;
        this.releaseDate = releaseDate;
        this.isbn = isbn;
        this.authorId = author;
    }

    public String toString(){
        return productId + " " + " " + publisher + " " + pages + " " + isbn;
    }
}

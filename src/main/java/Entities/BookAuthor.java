package Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Book_Author")
public class BookAuthor {

    @EmbeddedId
    public BookAuthorId id;

    @ManyToOne
    @MapsId("bookProductId")
    @JoinColumn(name = "Book_ProductID", referencedColumnName = "ProductID", foreignKey = @ForeignKey(name = "fk_book_author_book"))
    public Book book;

    @ManyToOne
    @MapsId("authorId")
    @JoinColumn(name = "Author_ID", referencedColumnName = "PersonID", foreignKey = @ForeignKey(name = "fk_book_author_person"))
    public Person author;

}

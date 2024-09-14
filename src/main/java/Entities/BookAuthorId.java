package Entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BookAuthorId implements Serializable {
    @Column(name = "Book_ProductID")
    public String bookProductId;

    @Column(name = "Author_ID")
    public int authorId;

    public BookAuthorId() {}

    public BookAuthorId(String bookProductId, int authorId) {
        this.bookProductId = bookProductId;
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookAuthorId that = (BookAuthorId) o;
        return authorId == that.authorId &&
                Objects.equals(bookProductId, that.bookProductId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookProductId, authorId);
    }

}
public class Book {
    public String id;
    public String author;
    public String publisher;
    public Integer pages;
    public java.sql.Date releaseDate;
    public String ISBN;

    public Book(String id, String author, String publisher, int pages, java.sql.Date releaseDate) {
        this.id = id;
        this.author = author;
        this.publisher = publisher;
        this.pages = pages;
        this.releaseDate = releaseDate;
    }
}

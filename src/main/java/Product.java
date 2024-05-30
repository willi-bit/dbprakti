public class Product {
    String id;
    String title;
    float rating;
    int rank;
    String nr;
    String picture;
    String category;

    public Product(String id, String title, float rating, int rank, String nr, String picture, String category) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.rank = rank;
        this.nr = nr;
        this.picture = picture;
        this.category = category;
    }
}
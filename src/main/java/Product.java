public class Product implements Comparable<Product> {
    String id;
    String title;
    float rating;
    Integer rank;
    String nr;
    String picture;

    public Product(String id, String title, float rating, Integer rank, String nr, String picture) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.rank = rank;
        this.nr = nr;
        this.picture = picture;
    }
    @Override
    public int compareTo(Product o) {
        return this.id.compareTo(o.id);
    }
}
import java.util.UUID;

public class Review {
    String id;
    String product;
    int stars;
    String summary;
    String content;
    int helpful;
    String username;

    public Review(String id, String product, int stars, String summary, String content, int helpful, String username) {
        this.id = id;
        this.product = product;
        this.stars = stars;
        this.summary = summary;
        this.content = content;
        this.helpful = helpful;
        this.username = username;
    }
}

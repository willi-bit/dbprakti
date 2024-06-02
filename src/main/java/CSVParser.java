import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CSVParser {
    public static void run() {
        try (CSVReader reader = new CSVReader(new FileReader("data/reviews.csv"))) {
            String[] lineInArray;
            List<Review> reviews = new ArrayList<>();
            while ((lineInArray = reader.readNext()) != null) {
                Review review = new Review();
                String productId = lineInArray[0];
                if (productId.equals("product")) {
                    continue;
                }
                String rating = lineInArray[1];
                String helpfulRating = lineInArray[2];
                String userName = lineInArray[4];
                String summary = lineInArray[5];
                String content = lineInArray[6];

                review.id = UUID.randomUUID().toString();
                review.product = productId;
                review.username = userName;
                review.helpful = helpfulRating.isEmpty() ? -1 : Integer.parseInt(helpfulRating);
                review.rating = Integer.parseInt(rating);
                review.summary = summary;
                review.content = content;
                reviews.add(review);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

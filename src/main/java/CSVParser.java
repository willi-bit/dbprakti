import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CSVParser {

    /**
     * reads reviews from csv
     * @return list of reviews
     */
    public List<Review> ParseCSV() {
        List<Review> reviews = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("data/reviews.csv"))) {
            String[] lineInArray;

            while ((lineInArray = reader.readNext()) != null) {

                String productId = lineInArray[0];
                if (productId.equals("product")) {
                    continue;
                }
                String rating = lineInArray[1];
                String helpfulRating = lineInArray[2];
                String userName = lineInArray[4];
                String summary = lineInArray[5];
                String content = lineInArray[6];

                int helpful = helpfulRating.isEmpty() ? -1 : Integer.parseInt(helpfulRating);
                Review review = new Review(UUID.randomUUID().toString(), productId, Integer.parseInt(rating), summary, content, helpful, userName);
                reviews.add(review);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reviews;
    }
}

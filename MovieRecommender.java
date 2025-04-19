package MovieRecommender;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.io.*;
import java.util.*;

public class MovieRecommender {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your user ID: ");
            int userID = scanner.nextInt();

            // Load movie titles into a map
            Map<Long, String> movieTitles = loadMovieTitles("src/movie_titles.csv");

            // Load ratings data
            File file = new File("src/movie_ratings.csv");
            DataModel model = new FileDataModel(file);

            // User similarity
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(5, similarity, model);
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            // Get recommendations
            List<RecommendedItem> recommendations = recommender.recommend(userID, 3);

            System.out.println("\nRecommendations for user " + userID + ":");
            if (recommendations.isEmpty()) {
                System.out.println("No recommendations available. Try a different user ID.");
            } else {
                for (RecommendedItem recommendation : recommendations) {
                    String movieName = movieTitles.get(recommendation.getItemID());
                    System.out.println("Movie: " + movieName +
                            " (ID: " + recommendation.getItemID() +
                            "), Estimated Rating: " + recommendation.getValue());
                }
            }

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to load titles from CSV
    private static Map<Long, String> loadMovieTitles(String filePath) throws IOException {
        Map<Long, String> titles = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",", 2);
            if (parts.length == 2) {
                long id = Long.parseLong(parts[0].trim());
                String name = parts[1].trim();
                titles.put(id, name);
            }
        }
        br.close();
        return titles;
    }
}

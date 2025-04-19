package MovieRecommender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;


import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.*;

public class MovieRecommenderMenu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Map<Long, String> movieTitles = loadMovieTitles("src/movie_titles.csv");
            File file = new File("src/movie_ratings.csv");
            DataModel model = new FileDataModel(file);

            while (true) {
                System.out.println("\n===== Movie Recommendation System =====");
                System.out.println("1. User-Based Recommendations");
                System.out.println("2. Item-Based Recommendations");
                System.out.println("3. GUI Version");
                System.out.println("4. Exit");
                System.out.print("Choose an option (1–4): ");
                int choice = scanner.nextInt();

                if (choice == 4) {
                    System.out.println("Exiting... Goodbye!");
                    break;
                }

                if (choice == 3) {
                    SwingUtilities.invokeLater(() -> {
                        new MovieRecommenderGUI(movieTitles, model).setVisible(true);
                    });
                    continue;
                }

                System.out.print("Enter your user ID: ");
                int userID = scanner.nextInt();

                List<RecommendedItem> recommendations;
                String type;

                if (choice == 1) {
                    UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
                    UserNeighborhood neighborhood = new NearestNUserNeighborhood(5, similarity, model);
                    Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
                    recommendations = recommender.recommend(userID, 5);
                    type = "User-Based";
                } else if (choice == 2) {
                    ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
                    Recommender recommender = new GenericItemBasedRecommender(model, similarity);
                    recommendations = recommender.recommend(userID, 5);
                    type = "Item-Based";
                } else {
                    System.out.println("Invalid option.");
                    continue;
                }

                displayAndSaveRecommendations(recommendations, userID, movieTitles, type);
            }

        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void displayAndSaveRecommendations(List<RecommendedItem> recommendations, int userID,
                                                      Map<Long, String> movieTitles, String type) {
        StringBuilder output = new StringBuilder();
        output.append("\n").append(type).append(" Recommendations for User ").append(userID).append(":\n");

        if (recommendations.isEmpty()) {
            output.append("No recommendations available.\n");
        } else {
            for (RecommendedItem recommendation : recommendations) {
                String movieName = movieTitles.getOrDefault(recommendation.getItemID(), "Unknown Movie");
                output.append(String.format("Movie: %-30s (ID: %d), Estimated Rating: %.2f\n",
                        movieName, recommendation.getItemID(), recommendation.getValue()));
            }
        }

        // Display on console
        System.out.println(output);

        // Export to file
        try (FileWriter writer = new FileWriter("recommendations_user" + userID + ".txt")) {
            writer.write(output.toString());
            System.out.println("Recommendations saved to: recommendations_user" + userID + ".txt");
        } catch (IOException e) {
            System.out.println("Failed to save recommendations.");
        }
    }

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

    // ✅ Embedded GUI Class
    public static class MovieRecommenderGUI extends JFrame {

        private JTextField userIdField;
        private JTextArea resultArea;
        private Map<Long, String> movieTitles;
        private DataModel model;

        public MovieRecommenderGUI(Map<Long, String> titles, DataModel dataModel) {
            this.movieTitles = titles;
            this.model = dataModel;

            setTitle("Movie Recommender GUI");
            setSize(500, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());

            // Top Panel
            JPanel topPanel = new JPanel();
            topPanel.add(new JLabel("Enter User ID: "));
            userIdField = new JTextField(10);
            topPanel.add(userIdField);
            JButton recommendButton = new JButton("Get Recommendations");
            topPanel.add(recommendButton);
            add(topPanel, BorderLayout.NORTH);

            // Result Area
            resultArea = new JTextArea();
            resultArea.setEditable(false);
            add(new JScrollPane(resultArea), BorderLayout.CENTER);

            // Button Action
            recommendButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String userIdText = userIdField.getText();
                    try {
                        int userId = Integer.parseInt(userIdText);
                        showRecommendations(userId);
                    } catch (NumberFormatException ex) {
                        resultArea.setText("Invalid user ID.");
                    }
                }
            });
        }

        private void showRecommendations(int userId) {
            resultArea.setText("");
            try {
                ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
                Recommender recommender = new GenericItemBasedRecommender(model, similarity);
                List<RecommendedItem> recommendations = recommender.recommend(userId, 5);

                if (recommendations.isEmpty()) {
                    resultArea.append("No recommendations found for user ID: " + userId);
                } else {
                    resultArea.append("Item-Based Recommendations for user ID " + userId + ":\n");
                    for (RecommendedItem recommendation : recommendations) {
                        String title = movieTitles.get(recommendation.getItemID());
                        resultArea.append(String.format("Movie: %-30s (ID: %d), Estimated Rating: %.2f\n",
                                title, recommendation.getItemID(), recommendation.getValue()));
                    }
                }
            } catch (Exception e) {
                resultArea.setText("Error generating recommendations.");
                e.printStackTrace();
            }
        }
    }
}

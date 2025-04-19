package MovieRecommender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

public class MovieRecommenderGUI extends JFrame {

    private JTextField userIdField;
    private JTextArea resultArea;
    private Map<Long, String> movieTitles;

    public MovieRecommenderGUI() {
        setTitle("Movie Recommender");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Enter User ID: "));
        userIdField = new JTextField(10);
        topPanel.add(userIdField);
        JButton recommendButton = new JButton("Get Recommendations");
        topPanel.add(recommendButton);
        add(topPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        try {
            movieTitles = loadMovieTitles("src/movie_titles.csv");
        } catch (IOException e) {
            resultArea.setText("Error loading movie titles.");
        }

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
            File file = new File("src/movie_ratings.csv");
            DataModel model = new FileDataModel(file);

            ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(model);
            GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, itemSimilarity);

            List<RecommendedItem> recommendations = recommender.recommend(userId, 5);

            if (recommendations.isEmpty()) {
                resultArea.append("No recommendations found for user ID: " + userId);
            } else {
                resultArea.append("Item-based Recommendations for user ID " + userId + ":\n");
                for (RecommendedItem recommendation : recommendations) {
                    String title = movieTitles.get(recommendation.getItemID());
                    resultArea.append("Movie: " + title + " (ID: " + recommendation.getItemID()
                            + "), Estimated Rating: " + recommendation.getValue() + "\n");
                }
            }

        } catch (Exception e) {
            resultArea.setText("Error generating recommendations.");
            e.printStackTrace();
        }
    }

    private Map<Long, String> loadMovieTitles(String filePath) throws IOException {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MovieRecommenderGUI().setVisible(true);
        });
    }
}

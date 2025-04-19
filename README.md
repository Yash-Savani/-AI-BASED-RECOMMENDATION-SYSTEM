# -AI-BASED-RECOMMENDATION-SYSTEM

*COMPANY*: CODTECH IT SOLUTIONS

*NAME*: YASH SAVANI

*INTERN ID*: CT12WOND

*DOMAIN*: JAVA PROGRAMMING

*DURATION*: 12 WEEKS

*MENTOR*: NEELA SANTHOSH

## DESCRIPTON :
  Task 4: Movie Recommendation System using Apache Mahout

### **Purpose of the Task**

The goal of Task 4 was to create a **Movie Recommendation System** that leverages **Apache Mahout**'s machine learning libraries to generate personalized movie recommendations. The system supports both **user-based** and **item-based** recommendation techniques, allowing users to receive movie suggestions based on their preferences or similar items.

The task was designed to demonstrate the practical application of **collaborative filtering** and how it can be used to predict user preferences based on their past behavior (e.g., movie ratings). In addition to the console-based recommendations, a **Graphical User Interface (GUI)** was implemented to provide an interactive user experience, making the system accessible and user-friendly.

---

### **Tools & Technologies Used**

- **Java 17+** – Core language used for development.
- **Apache Mahout** – A library for machine learning algorithms, particularly used for collaborative filtering.
  - **DataModel** – Represents the dataset used for recommendation.
  - **UserSimilarity** – Calculates the similarity between users.
  - **ItemSimilarity** – Computes the similarity between items (movies).
  - **Recommender** – The core interface for generating recommendations.
- **Swing (javax.swing)** – Used to create the **Graphical User Interface (GUI)**.
- **CSV files** – Used for storing movie titles and user ratings.

---

### **Features Implemented**

1. **User-Based Recommendations**  
   The system generates **user-based recommendations** by finding users who are similar to the input user and suggesting movies that those similar users have liked. This is achieved using **Pearson Correlation Similarity** and a **UserNeighborhood** of 5 nearest users.

2. **Item-Based Recommendations**  
   In addition to user-based recommendations, the system also supports **item-based recommendations**, where movies similar to those the user has rated highly are recommended. This is also achieved using **Pearson Correlation Similarity** but focuses on item similarity rather than user similarity.

3. **Movie Title Mapping**  
   The system loads movie titles from a **CSV file**, creating a mapping between movie IDs and their titles. This mapping is used to display human-readable movie names along with their respective ratings in the recommendations.

4. **Command-Line Interface (CLI)**  
   A **CLI interface** allows users to select between **user-based** or **item-based** recommendations, input their user ID, and view or save the results in a text file. This version of the application is useful for quick testing or batch processing.

5. **GUI for Easy Access**  
   A **Graphical User Interface (GUI)** allows users to input their user ID and get movie recommendations in a more interactive manner. The GUI provides a text area to display recommendations and a button to initiate the recommendation process.

6. **Recommendations Display and Save**  
   After generating the recommendations, the system displays the movie suggestions on the console or GUI. Additionally, the recommendations are saved to a text file for later reference, making it easy for users to access their recommendations offline.

7. **Error Handling**  
   The application handles errors such as invalid user IDs and issues with generating recommendations, providing appropriate feedback to users.

---

### **Real-World Applications**

This Movie Recommendation System has several potential real-world applications:

- **Streaming Platforms** like Netflix, Amazon Prime, or Hulu can use this type of system to suggest movies or TV shows to users based on their viewing history or similar users’ preferences.
- **E-commerce Platforms** can recommend products based on users’ purchase history or similar users’ buying patterns.
- **Social Media Apps** can use collaborative filtering to suggest content or connections based on user activity and similarities with others.

---

### **Conclusion**

Task 4 was an essential step in applying machine learning techniques, specifically **collaborative filtering**, to real-world recommendation systems. The combination of **Apache Mahout** and a user-friendly **GUI** makes this movie recommendation system an effective tool for predicting user preferences. The task not only demonstrates the power of machine learning in recommendation systems but also showcases how to integrate **data models**, **similarity metrics**, and **recommendation algorithms** into a functional application.


![Image](https://github.com/user-attachments/assets/bc7486e8-1f3e-4bd6-80c9-52932cc839ab)
![Image](https://github.com/user-attachments/assets/d58e5e45-5d22-4f0b-8f07-6fe79c1dca4f)

package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a competitor in a competition.
 * Stores the competitor's ID, name, level, country, and scores.
 */
public class Competitor {

    /**
     * The unique ID of the competitor.
     */
    private int competitorID;

    /**
     * The Name object representing the competitor's first and last names.
     */
    private Name name;

    /**
     * The competition level of the competitor (e.g., Beginner, Intermediate, Advanced).
     */
    private String level;

    /**
     * The country of the competitor.
     */
    private String country;

    /**
     * An array containing the scores of the competitor.
     * Scores that are not attempted are stored as -1.
     */
    private int[] scores;

    /**
     * Constructs a Competitor object with all details.
     * 
     * @param competitorID the unique ID of the competitor
     * @param name the Name object containing first and last names
     * @param level the competition level of the competitor
     * @param country the country of the competitor
     * @param scores an array of integer scores
     */
    public Competitor(int competitorID, Name name, String level, String country, int[] scores) {
        this.competitorID = competitorID;
        this.name = name;
        this.level = level;
        this.country = country;
        this.scores = scores;
    }

    /**
     * Returns the competitor's ID.
     * 
     * @return the competitor ID
     */
    public int getCompetitorID() {
        return competitorID;
    }

    /**
     * Returns the Name object of the competitor.
     * 
     * @return the competitor's name
     */
    public Name getName() {
        return name;
    }

    /**
     * Returns the competition level of the competitor.
     * 
     * @return the level
     */
    public String getLevel() {
        return level;
    }

    /**
     * Returns the country of the competitor.
     * 
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns an array of the competitor's scores.
     * 
     * @return the scores array
     */
    public int[] getScoreArray() {
        return scores;
    }

    /**
     * Calculates and returns the overall score.
     * Only scores >= 0 are considered; -1 indicates not attempted.
     * 
     * @return the average of valid scores as a double, or 0.0 if none
     */
    public double getOverallScore() {
        int total = 0;
        int count = 0;
        for (int s : scores) {
            if (s >= 0) {
                total += s;
                count++;
            }
        }
        return count > 0 ? (double) total / count : 0.0;
    }

    /**
     * Returns a string containing full details of the competitor,
     * including ID, name, country, level, scores, and overall score.
     * Scores not attempted are shown as "NA".
     * 
     * @return a detailed string of all competitor information
     */
    public String getFullDetails() {
        StringBuilder scoreList = new StringBuilder();
        for (int i = 0; i < scores.length; i++) {
            scoreList.append(scores[i] == -1 ? "NA" : scores[i]);
            if (i < scores.length - 1) scoreList.append(", ");
        }

        return "Competitor number " + competitorID +
               ", name " + name +
               ", country " + country + ".\n" +
               name.getFirstName() + " is a " + level +
               " and received these scores: " + scoreList + ".\n" +
               "Overall score: " + getOverallScore();
    }

    /**
     * Returns a short string describing the competitor.
     * Format includes competitor number, initials, and overall score.
     * 
     * @return a concise string with competitor number, initials, and overall score
     */
    public String getShortDetails() {
        return "CN " + competitorID +
               " (" + name.getInitials() + ") has overall score " +
               getOverallScore();
    }


    /**
     * Loads all competitors from the database.
     * 
     * @return a List of all Competitor objects; empty list if none
     */
    public static List<Competitor> loadAllFromDB() {
        List<Competitor> list = new ArrayList<>();
        String sql = """
                SELECT c.competitorID, c.firstName, c.lastName, c.country,
                       s.level, s.score1, s.score2, s.score3, s.score4, s.score5
                FROM Competitors c
                LEFT JOIN competitorscores s ON c.competitorID = s.competitorID
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int[] scores = new int[5];
                for (int i = 1; i <= 5; i++) {
                    int val = rs.getInt("score" + i);
                    scores[i - 1] = rs.wasNull() ? -1 : val;
                }

                Name name = new Name(
                        rs.getString("firstName"),
                        rs.getString("lastName")
                );

                String level = rs.getString("level");
                if (level == null) level = "-";

                list.add(new Competitor(
                        rs.getInt("competitorID"),
                        name,
                        level,
                        rs.getString("country"),
                        scores
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}

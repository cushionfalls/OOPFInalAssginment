package main;

import java.sql.*;

/**
 * Provides database operations for Competitor objects.
 * Allows adding competitors, retrieving competitor IDs,
 * and adding quiz scores for a specific level.
 */
public class CompetitorDAO {

    /**
     * Adds a new competitor to the database.
     *
     * @param userID    the ID of the user
     * @param firstName the competitor's first name
     * @param lastName  the competitor's last name
     * @param country   the competitor's country
     */
    public static void addCompetitor(int userID, String firstName, String lastName, String country) {
        String sql = "INSERT INTO competitors (userID, firstName, lastName, country) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userID);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, country);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the competitor ID associated with a given user ID.
     *
     * @param userID the ID of the user
     * @return the competitor ID, or -1 if not found
     */
    public static int getCompetitorID(int userID) {
        String sql = "SELECT competitorID FROM competitors WHERE userID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("competitorID");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Adds a quiz score for the given competitor and level.
     * If a row for the level exists, the score fills the first empty slot (score1–score5).
     * Otherwise, a new row is inserted with the score in score1.
     *
     * @param competitorID the competitor's ID
     * @param level        the competition level (e.g., Beginner, Intermediate, Advanced)
     * @param score        the score to add
     * @return true if the score was successfully added, false otherwise
     */
    public static boolean addQuizScore(int competitorID, String level, int score) {
        if (competitorID <= 0) return false;

        String selectSql = "SELECT score1, score2, score3, score4, score5 " +
                           "FROM competitorscores WHERE competitorID = ? AND level = ?";

        String insertSql = "INSERT INTO competitorscores (competitorID, level, score1) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            selectStmt.setInt(1, competitorID);
            selectStmt.setString(2, level);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, competitorID);
                    insertStmt.setString(2, level);
                    insertStmt.setInt(3, score);
                    insertStmt.executeUpdate();
                    return true;
                }
            }

            for (int i = 1; i <= 5; i++) {
                int val = rs.getInt("score" + i);
                if (rs.wasNull()) {
                    String updateSql = "UPDATE competitorscores SET score" + i +
                            " = ? WHERE competitorID = ? AND level = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, score);
                        updateStmt.setInt(2, competitorID);
                        updateStmt.setString(3, level);
                        updateStmt.executeUpdate();
                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}

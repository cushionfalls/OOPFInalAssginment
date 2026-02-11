package main;

import java.sql.*;

/**
 * Handles user registration, login, and user-related database operations.
 */
public class UserManager {

    /**
     * Registers a new player account in the database.
     *
     * @param username the userName of the new player
     * @param password the password for the account
     * @return true if registration succeeds; false if the userName already exists or an error occurs
     */
    public static boolean registerPlayer(String username, String password) {
        String checkSql = "SELECT userID FROM Users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String sql = "INSERT INTO Users (username, password, role) VALUES (?, ?, 'player')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticates a user and retrieves their role.
     *
     * @param username the userName of the user
     * @param password the password for the account
     * @return the role of the user if login succeeds; null if login fails or an error occurs
     */
    public static String login(String username, String password) {
        String sql = "SELECT role, userID FROM Users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves the user ID for a given userName
     *
     * @param username the userName to search for
     * @return the userID if found; -1 if not found or an error occurs
     */
    public static int getUserID(String username) {
        String sql = "SELECT userID FROM Users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("userID");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return -1;
    }
}

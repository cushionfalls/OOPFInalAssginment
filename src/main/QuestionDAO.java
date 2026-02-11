package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) for managing Question records in the database.
 */
public class QuestionDAO {

    /**
     * Adds a new question to the database.
     *
     * @param questionText the question text
     * @param optionA option A
     * @param optionB option B
     * @param optionC option C
     * @param optionD option D
     * @param correctOption the correct option
     * @param difficulty difficulty level of the question
     * @return true if the question was added successfully, false otherwise
     */
    public static boolean addQuestion(
            String questionText,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String correctOption,
            String difficulty) {

        String sql = "INSERT INTO Questions "
                + "(questionText, optionA, optionB, optionC, optionD, correctOption, difficulty) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, questionText);
            pstmt.setString(2, optionA);
            pstmt.setString(3, optionB);
            pstmt.setString(4, optionC);
            pstmt.setString(5, optionD);
            pstmt.setString(6, correctOption);
            pstmt.setString(7, difficulty);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Add Question Error: " + e.getMessage());
            return false;
        }
    }
}

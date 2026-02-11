package main;

import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;

public class QuizFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblQuestion;
    private JButton[] optionButtons;
    private int currentQuestionIndex = 0;
    private List<Question> questions;
    private int score = 0;

    private int playerID;
    private int competitorID;
    private String difficulty;

    public QuizFrame(int playerID, int competitorID, String difficulty) {
        this.playerID = playerID;
        this.competitorID = competitorID;
        this.difficulty = difficulty;

        setTitle("Quiz - " + difficulty);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null); // Center on screen

        contentPane = new JPanel(new BorderLayout(15, 15));
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        lblQuestion = new JLabel();
        lblQuestion.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblQuestion, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        contentPane.add(optionsPanel, BorderLayout.CENTER);

        optionButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            JButton btn = new JButton();
            final int index = i;
            btn.addActionListener(e -> checkAnswer(index));
            optionsPanel.add(btn);
            optionButtons[i] = btn;
        }

        loadQuestions();
        displayQuestion();
    }

    private void loadQuestions() {
        questions = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE difficulty = ? LIMIT 5";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, difficulty);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                questions.add(new Question(
                        rs.getInt("questionID"),
                        rs.getString("questionText"),
                        rs.getString("optionA"),
                        rs.getString("optionB"),
                        rs.getString("optionC"),
                        rs.getString("optionD"),
                        rs.getString("correctOption")));
            }

            java.util.Collections.shuffle(questions);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            finishQuiz();
            return;
        }

        Question q = questions.get(currentQuestionIndex);
        lblQuestion.setText("<html>Q" + (currentQuestionIndex + 1) + ": " + q.getQuestionText() + "</html>");
        optionButtons[0].setText(q.getOptionA());
        optionButtons[1].setText(q.getOptionB());
        optionButtons[2].setText(q.getOptionC());
        optionButtons[3].setText(q.getOptionD());
    }

    private void checkAnswer(int index) {
        Question q = questions.get(currentQuestionIndex);
        String chosen = "ABCD".charAt(index) + "";
        if (chosen.equalsIgnoreCase(q.getCorrectOption()))
            score++;
        currentQuestionIndex++;
        displayQuestion();
    }

    private void finishQuiz() {
        boolean saved = CompetitorDAO.addQuizScore(competitorID, difficulty, score);

        if (!saved) {
            JOptionPane.showMessageDialog(this, "You already used 5 attempts.");
        }

        new QuizResultFrame(playerID, competitorID, difficulty, score, questions.size()).setVisible(true);
        dispose();
    }
}

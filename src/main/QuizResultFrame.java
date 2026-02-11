package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class QuizResultFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private int playerID;
    private int competitorID;

    public QuizResultFrame(int playerID, int competitorID, String difficulty, int correct, int total) {
        this.playerID = playerID;
        this.competitorID = competitorID;

        setTitle("Quiz Result");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null); // Center on screen

        contentPane = new JPanel(new BorderLayout(15, 15));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Quiz Result", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 22));
        contentPane.add(lblTitle, BorderLayout.NORTH);

        double percentage = (correct * 100.0) / total;

        JPanel detailsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        contentPane.add(detailsPanel, BorderLayout.CENTER);

        detailsPanel.add(new JLabel("Difficulty Level: " + difficulty));
        detailsPanel.add(new JLabel("Total Questions: " + total));
        detailsPanel.add(new JLabel("Correct Answers: " + correct));

        JLabel lblScore = new JLabel(String.format("Score: %.2f%%", percentage));
        lblScore.setFont(new Font("Tahoma", Font.BOLD, 16));
        detailsPanel.add(lblScore);

        JLabel lblMessage = new JLabel(getMessage(percentage));
        lblMessage.setFont(new Font("Tahoma", Font.ITALIC, 14));
        detailsPanel.add(lblMessage);

        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.addActionListener(e -> {
            new PlayerDashboard(playerID, competitorID).setVisible(true);
            dispose();
        });
        contentPane.add(btnBack, BorderLayout.SOUTH);
    }

    private String getMessage(double score) {
        if (score >= 80)
            return "Excellent performance!";
        if (score >= 60)
            return "Good job!";
        if (score >= 40)
            return "You can do better. Keep practicing!";
        return "Needs improvement. Try again!";
    }
}

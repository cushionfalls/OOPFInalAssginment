package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PlayerDashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private int userID;
    private int competitorID;

    public static void main(String[] args) {
        MainApp mainapp = new MainApp();
        mainapp.main(null);
    }

    public PlayerDashboard(int playerID, int competitorID) {
        this.userID = playerID;
        this.competitorID = competitorID;

        setTitle("Player Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null); // Center on screen

        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Welcome Player", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20)); // Keeping a slightly bold title
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        contentPane.add(buttonPanel, BorderLayout.CENTER);

        JButton btnStartQuiz = new JButton("Start Quiz");
        btnStartQuiz.addActionListener(e -> {
            String[] options = { "Beginner", "Intermediate", "Advanced" };
            String difficulty = (String) JOptionPane.showInputDialog(
                    PlayerDashboard.this,
                    "Select Difficulty Level:",
                    "Quiz Difficulty",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (difficulty != null) {
                new QuizFrame(userID, competitorID, difficulty).setVisible(true);
            }
        });
        buttonPanel.add(btnStartQuiz);

        JButton btnViewReports = new JButton("Reports");
        btnViewReports.addActionListener(e -> new PlayerReportsFrame(competitorID).setVisible(true));
        buttonPanel.add(btnViewReports);

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            new LoginRegisterFrame().setVisible(true);
            dispose();
        });
        buttonPanel.add(btnLogout);
    }
}

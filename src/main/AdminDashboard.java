package main;

import java.awt.*;
import javax.swing.*;

/**
 * The main dashboard for administrator users.
 * Provides access to various administrative tasks such as managing questions
 * and viewing reports.
 */
public class AdminDashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        MainApp mainapp = new MainApp();
        mainapp.main(null);
    }

    /**
     * Constructs the AdminDashboard and initializes the navigation buttons.
     */
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null); // Center on screen

        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20)); // Keeping a slightly bold title
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        contentPane.add(buttonPanel, BorderLayout.CENTER);

        JButton btnAddQuestion = new JButton("Add Question");
        btnAddQuestion.addActionListener(e -> new AddQuestionFrame().setVisible(true));
        buttonPanel.add(btnAddQuestion);

        JButton btnViewQuestions = new JButton("View Questions");
        btnViewQuestions.addActionListener(e -> new ViewQuestionsFrame().setVisible(true));
        buttonPanel.add(btnViewQuestions);

        JButton btnViewReports = new JButton("View Reports");
        btnViewReports.addActionListener(e -> new AdminReportsFrame().setVisible(true));
        buttonPanel.add(btnViewReports);

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            new LoginRegisterFrame().setVisible(true);
            dispose();
        });
        buttonPanel.add(btnLogout);
    }
}

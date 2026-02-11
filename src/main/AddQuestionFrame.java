package main;

import java.awt.*;
import javax.swing.*;

/**
 * A graphical interface frame for adding new quiz questions to the database.
 * Provides fields for question text, options, correct answer, and difficulty
 * level.
 */
public class AddQuestionFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AddQuestionFrame frame = new AddQuestionFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Constructs the AddQuestionFrame and initializes the UI components.
     */
    public AddQuestionFrame() {
        setTitle("Add Question");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 500);
        setLocationRelativeTo(null); // Center on screen

        contentPane = new JPanel(new BorderLayout(15, 15));
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Add Quiz Question", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        contentPane.add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Question:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField txtQuestion = new JTextField(20);
        formPanel.add(txtQuestion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Option A:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField txtA = new JTextField(20);
        formPanel.add(txtA, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Option B:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        JTextField txtB = new JTextField(20);
        formPanel.add(txtB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Option C:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        JTextField txtC = new JTextField(20);
        formPanel.add(txtC, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Option D:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        JTextField txtD = new JTextField(20);
        formPanel.add(txtD, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Correct:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        JTextField txtCorrect = new JTextField(5);
        formPanel.add(txtCorrect, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Difficulty:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 6;
        JComboBox<String> cmbLevel = new JComboBox<>();
        cmbLevel.addItem("Beginner");
        cmbLevel.addItem("Intermediate");
        cmbLevel.addItem("Advanced");
        formPanel.add(cmbLevel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> {
            String question = txtQuestion.getText();
            String a = txtA.getText();
            String b = txtB.getText();
            String c = txtC.getText();
            String d = txtD.getText();
            String correct = txtCorrect.getText().toUpperCase();
            String difficulty = cmbLevel.getSelectedItem().toString();

            if (question.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill all question and option fields.",
                        "Missing Information",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!correct.matches("[A-D]")) {
                JOptionPane.showMessageDialog(this,
                        "Correct option must be A, B, C, or D!",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = QuestionDAO.addQuestion(question, a, b, c, d, correct, difficulty);

            if (success) {
                JOptionPane.showMessageDialog(this, "Question Added Successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error Adding Question!");
            }
        });
        buttonPanel.add(btnSave);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> dispose());
        buttonPanel.add(btnBack);
    }
}

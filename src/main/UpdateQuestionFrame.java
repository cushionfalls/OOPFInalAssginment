package main;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class UpdateQuestionFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtQuestion, txtA, txtB, txtC, txtD, txtCorrect;
    private JComboBox<String> cmbLevel;
    private int questionID;
    private ViewQuestionsFrame parentFrame;

    public UpdateQuestionFrame(int questionID, ViewQuestionsFrame parentFrame) {
        this(questionID);
        this.parentFrame = parentFrame;
    }

    public UpdateQuestionFrame(int questionID) {
        this.questionID = questionID;

        setTitle("Update Question");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 500);
        setLocationRelativeTo(null); // Center on screen

        contentPane = new JPanel(new BorderLayout(15, 15));
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Update Quiz Question", SwingConstants.CENTER);
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
        txtQuestion = new JTextField(20);
        formPanel.add(txtQuestion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Option A:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        txtA = new JTextField(20);
        formPanel.add(txtA, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Option B:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        txtB = new JTextField(20);
        formPanel.add(txtB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Option C:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        txtC = new JTextField(20);
        formPanel.add(txtC, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Option D:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        txtD = new JTextField(20);
        formPanel.add(txtD, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Correct:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        txtCorrect = new JTextField(5);
        formPanel.add(txtCorrect, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Difficulty:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 6;
        cmbLevel = new JComboBox<>();
        cmbLevel.addItem("Beginner");
        cmbLevel.addItem("Intermediate");
        cmbLevel.addItem("Advanced");
        formPanel.add(cmbLevel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        JButton btnSave = new JButton("Update");
        btnSave.addActionListener(e -> updateQuestion());
        buttonPanel.add(btnSave);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> dispose());
        buttonPanel.add(btnBack);

        loadQuestionDetails();
    }

    private void loadQuestionDetails() {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Questions WHERE questionID = ?")) {
            pstmt.setInt(1, questionID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                txtQuestion.setText(rs.getString("questionText"));
                txtA.setText(rs.getString("optionA"));
                txtB.setText(rs.getString("optionB"));
                txtC.setText(rs.getString("optionC"));
                txtD.setText(rs.getString("optionD"));
                txtCorrect.setText(rs.getString("correctOption"));
                cmbLevel.setSelectedItem(rs.getString("difficulty"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading question: " + e.getMessage());
        }
    }

    private void updateQuestion() {
        if (txtQuestion.getText().isEmpty() || txtA.getText().isEmpty() || txtB.getText().isEmpty() ||
                txtC.getText().isEmpty() || txtD.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Missing Information",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String correct = txtCorrect.getText().toUpperCase();
        if (!correct.matches("[A-D]")) {
            JOptionPane.showMessageDialog(this, "Correct option must be A, B, C, or D!", "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE Questions SET questionText=?, optionA=?, optionB=?, optionC=?, optionD=?, correctOption=?, difficulty=? WHERE questionID=?")) {

            pstmt.setString(1, txtQuestion.getText());
            pstmt.setString(2, txtA.getText());
            pstmt.setString(3, txtB.getText());
            pstmt.setString(4, txtC.getText());
            pstmt.setString(5, txtD.getText());
            pstmt.setString(6, correct);
            pstmt.setString(7, cmbLevel.getSelectedItem().toString());
            pstmt.setInt(8, questionID);

            int updated = pstmt.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Question updated successfully!");
                if (parentFrame != null) {
                    parentFrame.loadQuestions();
                }
                dispose();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating question: " + e.getMessage());
        }
    }
}

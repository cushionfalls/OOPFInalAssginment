package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewQuestionsFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ViewQuestionsFrame frame = new ViewQuestionsFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void loadQuestions() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String sql = "SELECT * FROM Questions";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("questionID"),
                        rs.getString("questionText"),
                        rs.getString("optionA"),
                        rs.getString("optionB"),
                        rs.getString("optionC"),
                        rs.getString("optionD"),
                        rs.getString("correctOption"),
                        rs.getString("difficulty")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading questions: " + e.getMessage());
        }
    }

    public ViewQuestionsFrame() {
        setTitle("View Questions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); // Center on screen

        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("All Quiz Questions", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        table = new JTable();
        table.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID", "Question", "Option A", "Option B", "Option C", "Option D", "Correct",
                        "Difficulty" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        table.getColumnModel().getColumn(0).setPreferredWidth(30); // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Question
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Option A
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Option B
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Option C
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Option D
        table.getColumnModel().getColumn(6).setPreferredWidth(55); // Correct
        table.getColumnModel().getColumn(7).setPreferredWidth(100); // Difficulty

        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(table);

        loadQuestions();

        JPanel sidePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        contentPane.add(sidePanel, BorderLayout.SOUTH);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadQuestions());
        sidePanel.add(btnRefresh);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(ViewQuestionsFrame.this, "Select a question to update!");
                return;
            }
            int questionID = (int) table.getValueAt(selectedRow, 0);
            new UpdateQuestionFrame(questionID, ViewQuestionsFrame.this).setVisible(true);
        });
        sidePanel.add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(ViewQuestionsFrame.this, "Select a question to delete!");
                return;
            }
            int questionID = (int) table.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(ViewQuestionsFrame.this,
                    "Are you sure you want to delete this question?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DBConnection.getConnection();
                        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Questions WHERE questionID = ?")) {
                    pstmt.setInt(1, questionID);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(ViewQuestionsFrame.this, "Question deleted!");
                    loadQuestions();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ViewQuestionsFrame.this,
                            "Error deleting question: " + ex.getMessage());
                }
            }
        });
        sidePanel.add(btnDelete);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> dispose());
        sidePanel.add(btnBack);
    }
}

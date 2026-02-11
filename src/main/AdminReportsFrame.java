package main;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminReportsFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public AdminReportsFrame() {
        setTitle("Admin - Reports & Statistics");
        setSize(960, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Player Reports", createPlayerReportsPanel());

        getContentPane().add(tabs);
    }

    private JPanel createPlayerReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = { "ID", "Name", "Country", "Level", "Score 1", "Score 2", "Score 3", "Score 4", "Score 5",
                "Overall Score" };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(25);

        try {
            String sqlCompetitors = "SELECT competitorID, firstName, lastName, country FROM competitors ORDER BY firstName, lastName";
            try (Connection conn = DBConnection.getConnection();
                    PreparedStatement psComp = conn.prepareStatement(sqlCompetitors);
                    ResultSet rsComp = psComp.executeQuery()) {

                while (rsComp.next()) {
                    int competitorID = rsComp.getInt("competitorID");
                    String name = rsComp.getString("firstName") + " " + rsComp.getString("lastName");
                    String country = rsComp.getString("country");

                    String sqlScores = "SELECT level, score1, score2, score3, score4, score5 FROM competitorscores WHERE competitorID = ?";
                    try (PreparedStatement psScore = conn.prepareStatement(sqlScores)) {
                        psScore.setInt(1, competitorID);
                        ResultSet rsScore = psScore.executeQuery();

                        boolean hasScores = false;
                        while (rsScore.next()) {
                            hasScores = true;
                            String level = rsScore.getString("level");
                            Object[] scores = new Object[5];
                            double total = 0;
                            int count = 0;

                            for (int i = 0; i < 5; i++) {
                                int s = rsScore.getInt("score" + (i + 1));
                                if (rsScore.wasNull()) {
                                    scores[i] = "Not Attempted";
                                } else {
                                    scores[i] = s;
                                    total += s;
                                    count++;
                                }
                            }

                            double overall = count == 0 ? 0 : total / count;

                            model.addRow(new Object[] {
                                    competitorID,
                                    name,
                                    country,
                                    level,
                                    scores[0], scores[1], scores[2], scores[3], scores[4],
                                    count == 0 ? "-" : String.format("%.2f", overall)
                            });
                        }

                        if (!hasScores) {
                            model.addRow(new Object[] {
                                    competitorID,
                                    name,
                                    country,
                                    "-",
                                    "Not Attempted", "Not Attempted", "Not Attempted", "Not Attempted", "Not Attempted",
                                    "-"
                            });
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading player reports: " + e.getMessage());
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

}

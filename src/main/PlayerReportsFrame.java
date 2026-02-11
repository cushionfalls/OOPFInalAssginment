package main;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PlayerReportsFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private int competitorID;

    public PlayerReportsFrame(int competitorID) {
        this.competitorID = competitorID;

        setTitle("Player Reports");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null); // Center on screen

        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);

        // Title
        JLabel title = new JLabel("Reports & Statistics", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 22));
        contentPane.add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        contentPane.add(tabs, BorderLayout.CENTER);

        tabs.addTab("Leaderboard", leaderboardPanel());
        tabs.addTab("Statistics", statisticsPanel());
        tabs.addTab("My History", historyPanel());
    }

    // ===================== LEADERBOARD =====================
    private JPanel leaderboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Level selection
        JComboBox<String> cmbLevel = new JComboBox<>(new String[] { "Beginner", "Intermediate", "Advanced" });
        panel.add(cmbLevel, BorderLayout.NORTH);

        // Leaderboard area
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        cmbLevel.addActionListener(e -> area.setText(loadLeaderboard(cmbLevel.getSelectedItem().toString())));

        cmbLevel.setSelectedIndex(0);
        return panel;
    }

    private String loadLeaderboard(String level) {
        StringBuilder sb = new StringBuilder("Leaderboard - " + level + "\n\n");

        class Row {
            String name;
            double avg;
        }

        ArrayList<Row> rows = new ArrayList<>();

        String sql = "SELECT c.firstName, c.lastName, " +
                "cs.score1, cs.score2, cs.score3, cs.score4, cs.score5 " +
                "FROM competitors c " +
                "JOIN competitorscores cs ON c.competitorID = cs.competitorID " +
                "WHERE cs.level = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, level);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int total = 0, count = 0;
                for (int i = 1; i <= 5; i++) {
                    int s = rs.getInt("score" + i);
                    if (!rs.wasNull()) {
                        total += s;
                        count++;
                    }
                }

                if (count > 0) {
                    Row r = new Row();
                    r.name = rs.getString("firstName") + " " + rs.getString("lastName");
                    r.avg = (double) total / count;
                    rows.add(r);
                }
            }

        } catch (SQLException e) {
            return "Error loading leaderboard.";
        }

        // sort by highest avg
        for (int i = 0; i < rows.size(); i++) {
            for (int j = i + 1; j < rows.size(); j++) {
                if (rows.get(j).avg > rows.get(i).avg) {
                    Row temp = rows.get(i);
                    rows.set(i, rows.get(j));
                    rows.set(j, temp);
                }
            }
        }

        if (rows.isEmpty()) {
            sb.append("No scores yet.");
        } else {
            int rank = 1;
            for (Row r : rows) {
                sb.append(rank++).append(". ")
                        .append(r.name)
                        .append(" | Avg: ")
                        .append(String.format("%.2f", r.avg))
                        .append("\n");
            }
        }

        return sb.toString();
    }

    // ===================== STATISTICS =====================
    private JPanel statisticsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 5, 5));

        // Total players
        JLabel lblTotalPlayers = new JLabel("Total Players: " + countPlayers());
        lblTotalPlayers.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(lblTotalPlayers);

        panel.add(new JLabel(" "));

        // Highest averages
        JLabel lblHighestTitle = new JLabel("Highest Average Score Per Level");
        lblHighestTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(lblHighestTitle);

        JLabel lblBeginner = new JLabel("Beginner: " + highestAvg("Beginner"));
        panel.add(lblBeginner);

        JLabel lblIntermediate = new JLabel("Intermediate: " + highestAvg("Intermediate"));
        panel.add(lblIntermediate);

        JLabel lblAdvanced = new JLabel("Advanced: " + highestAvg("Advanced"));
        panel.add(lblAdvanced);

        return panel;
    }

    private int countPlayers() {
        try (Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM competitors")) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            return 0;
        }
    }

    private String highestAvg(String level) {
        double max = 0;

        String sql = "SELECT score1,score2,score3,score4,score5 " +
                "FROM competitorscores WHERE level = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, level);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int total = 0, count = 0;
                for (int i = 1; i <= 5; i++) {
                    int s = rs.getInt("score" + i);
                    if (!rs.wasNull()) {
                        total += s;
                        count++;
                    }
                }
                if (count > 0) {
                    double avg = (double) total / count;
                    if (avg > max)
                        max = avg;
                }
            }

        } catch (SQLException ignored) {
        }

        return String.format("%.2f", max);
    }

    // ===================== HISTORY =====================
    private JPanel historyPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Level selection
        JComboBox<String> cmbLevel = new JComboBox<>(new String[] { "Beginner", "Intermediate", "Advanced" });
        panel.add(cmbLevel, BorderLayout.NORTH);

        // History area
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        cmbLevel.addActionListener(e -> area.setText(loadHistory(cmbLevel.getSelectedItem().toString())));

        cmbLevel.setSelectedIndex(0);
        return panel;
    }

    private String loadHistory(String level) {
        StringBuilder sb = new StringBuilder("My Quiz History\n\n");
        sb.append("Level: ").append(level).append("\n\n");

        String sql = "SELECT score1,score2,score3,score4,score5 " +
                "FROM competitorscores WHERE competitorID = ? AND level = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, competitorID);
            ps.setString(2, level);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return sb.append("No attempts for this level.").toString();
            }

            for (int i = 1; i <= 5; i++) {
                int s = rs.getInt("score" + i);
                sb.append("Attempt ").append(i).append(": ");
                sb.append(rs.wasNull() ? "Not Attempted" : s);
                sb.append("\n");
            }

        } catch (SQLException e) {
            return "Error loading history.";
        }

        return sb.toString();
    }
}

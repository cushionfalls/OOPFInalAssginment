package main;

import java.awt.*;
import javax.swing.*;

/**
 * The initial frame of the application providing login and registration
 * functionality.
 * Allows users to authenticate or create a new account.
 */
public class LoginRegisterFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtUsernameLogin, txtUsernameReg;
    private JPasswordField txtPasswordLogin, txtPasswordReg;
    private JTextField txtFirstName, txtLastName, txtCountry;
    private JLabel lblLoginMessage, lblRegMessage;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginRegisterFrame frame = new LoginRegisterFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Constructs the LoginRegisterFrame and sets up the login/registration forms.
     */
    public LoginRegisterFrame() {
        setTitle("Quiz Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null); // Center on screen
        getContentPane().setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        // Login Panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        tabbedPane.addTab("Login", loginPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtUsernameLogin = new JTextField(15);
        loginPanel.add(txtUsernameLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtPasswordLogin = new JPasswordField(15);
        loginPanel.add(txtPasswordLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        lblLoginMessage = new JLabel("");
        lblLoginMessage.setForeground(Color.RED);
        loginPanel.add(lblLoginMessage, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> performLogin());
        loginPanel.add(btnLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JLabel lblGoRegisterInfo = new JLabel("Not registered? Click the Register tab.", SwingConstants.CENTER);
        loginPanel.add(lblGoRegisterInfo, gbc);

        // Register Panel
        JPanel registerPanel = new JPanel(new GridBagLayout());
        tabbedPane.addTab("Register", registerPanel);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        registerPanel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtFirstName = new JTextField(15);
        registerPanel.add(txtFirstName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        registerPanel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtLastName = new JTextField(15);
        registerPanel.add(txtLastName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        registerPanel.add(new JLabel("Country:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        txtCountry = new JTextField(15);
        registerPanel.add(txtCountry, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        registerPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        txtUsernameReg = new JTextField(15);
        registerPanel.add(txtUsernameReg, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        registerPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        txtPasswordReg = new JPasswordField(15);
        registerPanel.add(txtPasswordReg, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        lblRegMessage = new JLabel("");
        lblRegMessage.setForeground(Color.RED);
        registerPanel.add(lblRegMessage, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JButton btnRegister = new JButton("Register");
        btnRegister.addActionListener(e -> performRegister());
        registerPanel.add(btnRegister, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        JLabel lblGoLoginInfo = new JLabel("Already registered? Click the Login tab.", SwingConstants.CENTER);
        registerPanel.add(lblGoLoginInfo, gbc);
    }

    private void performLogin() {
        String username = txtUsernameLogin.getText();
        String password = new String(txtPasswordLogin.getPassword());
        String role = UserManager.login(username, password);

        if (role == null) {
            lblLoginMessage.setText("Invalid credentials!");
        } else if (role.equals("admin")) {
            new AdminDashboard().setVisible(true);
            dispose();
        } else {
            int userID = UserManager.getUserID(username);
            int competitorID = CompetitorDAO.getCompetitorID(userID);
            new PlayerDashboard(userID, competitorID).setVisible(true);
            dispose();
        }
    }

    private void performRegister() {
        String username = txtUsernameReg.getText().trim();
        String password = new String(txtPasswordReg.getPassword()).trim();
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String country = txtCountry.getText().trim();

        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()
                || country.isEmpty()) {
            lblRegMessage.setText("Please fill in all fields!");
            return;
        }

        boolean success = UserManager.registerPlayer(username, password);

        if (success) {
            int userID = UserManager.getUserID(username);
            CompetitorDAO.addCompetitor(userID, firstName, lastName, country);

            txtUsernameReg.setText("");
            txtPasswordReg.setText("");
            txtFirstName.setText("");
            txtLastName.setText("");
            txtCountry.setText("");

            lblRegMessage.setForeground(Color.GREEN);
            lblRegMessage.setText("Registration successful! You can login now.");
        } else {
            lblRegMessage.setForeground(Color.RED);
            lblRegMessage.setText("Username already exists!");
        }
    }
}

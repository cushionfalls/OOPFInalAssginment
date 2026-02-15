package main;

import java.awt.EventQueue;

/**
 * The main entry point for the Competitor Management System application.
 */
public class MainApp {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginRegisterFrame loginFrame = new LoginRegisterFrame();
                loginFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    	CompetitorManager manager = new CompetitorManager();
        manager.start();
    }
}

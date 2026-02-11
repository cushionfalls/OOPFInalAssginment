package main;

/**
 * The main entry point for the Competitor Management System application.
 * Initializes the {@link CompetitorManager} and starts the application.
 */
public class MainApp {

    /**
     * Main method which starts the execution of the application.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        CompetitorManager manager = new CompetitorManager();
        manager.start();
    }
}

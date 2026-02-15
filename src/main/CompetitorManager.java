package main;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Manages the console-based Competitor Management System.
 * Provides menu options for reports, statistics, and competitor search.
 */
public class CompetitorManager {

    private CompetitorList competitorList;
    private Scanner scanner;

    /**
     * Constructs a CompetitorManager and initializes required objects.
     */
    public CompetitorManager() {
        competitorList = new CompetitorList();
        scanner = new Scanner(System.in);
    }
    
    /**
     * Starts the main menu loop for the management system.
     */
    public void start() {
        int choice = 0;

        do {
            System.out.println("\nCompetitor Management System ");
            System.out.println("1. Generate Full Report");
            System.out.println("2. Display Top Performer");
            System.out.println("3. Generate Statistics");
            System.out.println("4. Search Competitor by ID");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input!");
                continue;
            }

            switch (choice) {
                case 1 -> generateFullReport();
                case 2 -> displayTopPerformer();
                case 3 -> generateStatistics();
                case 4 -> searchByID();
                case 5 -> System.out.println("See You Soon!");
                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 5);
    }

    /**
     * Generates and displays a full report of all competitors.
     */
    private void generateFullReport() {
        System.out.println("\nFull Report \n");

        System.out.printf("%-5s %-20s %-12s %-20s %-8s\n",
                "ID", "Name", "Level", "Scores", "Overall");

        System.out.println("-------------------------------------");

        for (Competitor c : competitorList.getAll()) {
            int[] scores = c.getScoreArray();
            String[] displayScores = new String[scores.length];
            for (int i = 0; i < scores.length; i++) {
                displayScores[i] = scores[i] == -1 ? "NA" : String.valueOf(scores[i]);
            }

            System.out.printf("%-5d %-20s %-12s %-20s %-8.2f\n",
                    c.getCompetitorID(),
                    c.getName(),
                    c.getLevel() == null ? "-" : c.getLevel(),
                    Arrays.toString(displayScores),
                    c.getOverallScore());
        }


        System.out.println("-------------------");
    }

    /**
     * Displays the competitor with the highest overall score.
     */
    private void displayTopPerformer() {
        Competitor top = competitorList.getTopPerformer();
        if (top == null) {
            System.out.println("No competitors found.");
            return;
        }
        System.out.println("\n op Performer\n");
        System.out.println(top.getFullDetails());
    }
    
    /**
     * Generates and displays score statistics.
     */
    private void generateStatistics() {
        int[] freq = competitorList.getScoreFrequency();
        System.out.println("\n Statistics \n");
        System.out.println("\nTotal competitors: " + competitorList.size());
        System.out.println("Score Frequency:");

        for (int i = 1; i <= 5; i++) {
            System.out.println("Score " + i + ": " + freq[i]);
        }
    }

    /**
     * Searches for a competitor by ID and displays short details.
     */
    private void searchByID() {
        System.out.println("\n Competitor ID \n");
        System.out.print("Enter Competitor ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        Competitor best = null;
        for (Competitor c : competitorList.getAll()) {
            if (c.getCompetitorID() == id) {
                if (best == null || c.getOverallScore() > best.getOverallScore()) {
                    best = c;
                }
            }
        }

        if (best == null) {
            System.out.println("Competitor not found.");
        } else {
            System.out.println(best.getShortDetails());
        }
    }

}
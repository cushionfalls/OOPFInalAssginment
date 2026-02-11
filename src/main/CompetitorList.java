package main;

import java.util.List;

/**
 * Represents a collection of Competitor objects.
 * Provides methods to access competitors, determine the top performer,
 * and calculate score statistics across all competitors in the collection.
 */
public class CompetitorList {

    /** List of all competitors loaded from the database */
    private List<Competitor> competitors;

    /**
     * Constructs a CompetitorList and loads all competitors from the database.
     */
    public CompetitorList() {
        competitors = Competitor.loadAllFromDB();
    }

    /**
     * Returns the list of all competitors.
     *
     * @return a List of {@link Competitor} objects
     */
    public List<Competitor> getAll() {
        return competitors;
    }

    /**
     * Finds and returns the competitor with the highest overall score.
     *
     * @return the top-performing Competitor, or null if the list is empty
     */
    public Competitor getTopPerformer() {
        if (competitors.isEmpty())
            return null;

        Competitor top = competitors.get(0);
        for (Competitor c : competitors) {
            if (c.getOverallScore() > top.getOverallScore()) {
                top = c;
            }
        }
        return top;
    }

    /**
     * Calculates the frequency of scores from 1 to 5 across all competitors.
     *
     * @return an int array where the index represents the score (1-5)
     *         and the value at that index represents its frequency
     */
    public int[] getScoreFrequency() {
        int[] freq = new int[6];

        for (Competitor c : competitors) {
            for (int s : c.getScoreArray()) {
                if (s >= 1 && s <= 5) {
                    freq[s]++;
                }
            }
        }
        return freq;
    }

    /**
     * Returns the total number of competitors in the list.
     *
     * @return the number of competitors
     */
    public int size() {
        return competitors.size();
    }
}

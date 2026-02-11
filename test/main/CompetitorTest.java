package main;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Competitor class.
 */
public class CompetitorTest {

    @Test
    void testOverallScoreCalculation() {
        int[] scores = {4, 3, 5, 2, 4};
        Name name = new Name("Ram", "Adhikari");

        Competitor c = new Competitor(2, name, "Beginner", "Nepal", scores);

        assertEquals(3.6, c.getOverallScore(), 0.01);
    }

    @Test
    void testShortDetailsFormat() {
        int[] scores = {5, 5, 5, 5, 5};
        Name name = new Name("Shyam", "Shrestha");

        Competitor c = new Competitor(202, name, "Advanced", "Nepal", scores);

        String expected = "CN 202 (SS) has overall score 5.0";
        assertEquals(expected, c.getShortDetails());
    }
}

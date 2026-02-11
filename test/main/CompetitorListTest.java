package main;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for CompetitorList.
 */
public class CompetitorListTest {

    @Test
    void testTopPerformerNotNull() {
        CompetitorList list = new CompetitorList();
        Competitor top = list.getTopPerformer();
        assertNotNull(top);
    }

    @Test
    void testScoreFrequencyArrayLength() {
        CompetitorList list = new CompetitorList();
        int[] freq = list.getScoreFrequency();
        assertEquals(6, freq.length);
    }
}

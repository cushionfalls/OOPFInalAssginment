package main;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Name class.
 */
public class NameTest {

    @Test
    void testGetInitials() {
        Name name = new Name("Ram", "Adhikari");
        assertEquals("RA", name.getInitials());
    }

    @Test
    void testToString() {
        Name name = new Name("Shyam", "Shrestha");
        assertEquals("Shyam Shrestha", name.toString());
    }
}

package main;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for UserManager.
 */
public class UserManagerTest {

    @Test
    void testRegisterDuplicateUsername() {
        boolean result = UserManager.registerPlayer("testuser1", "testuser1");
        boolean resultAgain = UserManager.registerPlayer("testuser1", "testuser1");

        assertFalse(resultAgain);
    }

    @Test
    void testLoginInvalidCredentials() {
        String role = UserManager.login("wronguser", "wrongpass");
        assertNull(role);
        
        String newRole = UserManager.login("testuser1", "testuser1");
        assertNotNull(newRole);
    }
}

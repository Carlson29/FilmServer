package business;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {

    /**
     * Test of addUser() method, of class UserManager.
     */
    @Test
    void addUser() {
        UserManager uM = new UserManager();
//        User user1= new User("John","passworD$123",0);
        boolean actual = uM.addUser("John", "passworD$123");
        assertTrue(actual);
    }

    /**
     * Test of searchByUsername() method, of class UserManager.
     */
    @Test
    void searchByUsername() {
        {
            UserManager uM = new UserManager();
            User expected = new User("Mandy", "passworD$123");
            User actual = uM.searchByUsername("Mandy");
            assertEquals(actual, expected);
        }
    }

    /**
     * Test of searchByUsername_NotFound() method, of class UserManager.
     */
        @Test
        void searchByUsername_NotFound() {
            {
                UserManager uM = new UserManager();
                User expected = null;
                User actual = uM.searchByUsername("Lyss");
                assertEquals(actual, expected);
            }
        }

}
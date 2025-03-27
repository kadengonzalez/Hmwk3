package databasePart1;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for User class.
 * These test some of the main methods like setting roles and getting user info.
 *
 * Made by Kaden Gonzalez
 */
public class UserTest {

    /**
     * Tests if creating a User using the default constructor sets everything appropriately.
     * Tests the getters return exactly what we input.
     */
    @Test
    public void testConstructorAndGetters() {
        // creating a user with correct arguments
        User user = new User("kaden123", "kingJames", "Kaden Gonzalez", "kaden@email.com", "student");

        // checking that the values were set right
        assertEquals("kaden123", user.getUserName());
        assertEquals("kingJames", user.getPassword());
        assertEquals("Kaden Gonzalez", user.getName());
        assertEquals("kaden@email.com", user.getEmail());
        assertEquals("student", user.getRole());
    }

    /**
     * Tests changing a user's role using setRole and checking if it updates.
     */
    @Test
    public void testSetRole() {
        // make a new user
        User user = new User("oskar636", "ilia123", "Oskar Lofek", "oskar@email.com", "user");

        // change their role to admin
        user.setRole("admin");

        // make sure role has changed
        assertEquals("admin", user.getRole());
    }

    /**
     * Tests setting isAdmin to true works and stays.
     */
    @Test
    public void testSetAndGetAdmin() {
        User user = new User("kaden123", "kingJames", "Kaden Gonzalez", "kaden@email.com", "student");

        // flip admin to true
        user.setIsAdmin(true);

        // check that it stuck
        assertTrue(user.getIsAdmin());
    }

    /**
     * Tests if the instructor role flag can be set and read back correctly.
     */
    @Test
    public void testSetAndGetInstructor() {
        User user = new User("oskar636", "ilia123", "Oskar Lofek", "oskar@email.com", "student");

        // setting instructor flag
        user.setIsInstructor(true);

        // should be true now
        assertTrue(user.getIsInstructor());
    }

    /**
     * Uses the extended constructor to instantiate a user with role flags.
     * Then tests each flag and value is correctly stored.
     */
    @Test
    public void testRebuildUser() {
        // this uses the constructor that sets roles manually
        User user = new User("Oskar Lofek", "oskar636", "oskar@email.com", true, true, false, false, true, false);

        // just making sure everything matches what we passed in
        assertEquals("Oskar Lofek", user.getName());
        assertEquals("oskar636", user.getUserName());
        assertEquals("oskar@email.com", user.getEmail());

        assertTrue(user.getIsAdmin());
        assertTrue(user.getIsUser());
        assertFalse(user.getIsInstructor());
        assertFalse(user.getIsStaff());
        assertTrue(user.getIsReviewer());
        assertFalse(user.getIsStudent());
    }
}

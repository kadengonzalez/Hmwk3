package databasePart1;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Just a basic test class for the User class.
 * These test some of the main methods like setting roles and getting user info.
 * 
 * Made by Kaden Gonzalez
 */
public class UserTest {

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

    @Test
    public void testSetRole() {
        // make a new user
        User user = new User("oskar636", "ilia123", "Oskar Lofek", "oskar@email.com", "user");

        // change their role to admin
        user.setRole("admin");

        // make sure role has changed
        assertEquals("admin", user.getRole());
    }

    @Test
    public void testSetAndGetAdmin() {
        User user = new User("kaden123", "kingJames", "Kaden Gonzalez", "kaden@email.com", "student");

        // flip admin to true
        user.setIsAdmin(true);

        // check that it stuck
        assertTrue(user.getIsAdmin());
    }

    @Test
    public void testSetAndGetInstructor() {
        User user = new User("oskar636", "ilia123", "Oskar Lofek", "oskar@email.com", "student");

        // setting instructor flag
        user.setIsInstructor(true);

        // should be true now
        assertTrue(user.getIsInstructor());
    }

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

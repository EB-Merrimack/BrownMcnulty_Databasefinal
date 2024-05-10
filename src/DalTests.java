import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class DalTests {
    private Dal dal;

    @Before
    public void setUp() {
        dal = new Dal(); // Initialize Dal
    }

    @Test
    public void testSearchInventory() {
        List<String> inventory = dal.searchInventory("testDB", "Park", "testUser", "testPassword");
        assertNotNull(inventory); 
        assertFalse(inventory.isEmpty()); 
    }

    @Test
    public void testSearchRestaurantsByPark() {
        List<String> restaurants = dal.searchRestaurantsByPark("ParkName");
        assertNotNull(restaurants); 
        assertFalse(restaurants.isEmpty()); 
        assertTrue(restaurants.contains("RestaurantName")); 
    }

    @Test
    public void testGetAllRestaurantNames() {
        List<String> restaurantNames = Dal.getAllRestaurantNames();
        assertNotNull(restaurantNames); 
        assertFalse(restaurantNames.isEmpty()); 
        assertTrue(restaurantNames.contains("RestaurantName")); 
    }

    @Test
    public void testAddFavorite() {
        List<String> items = List.of("Restaurant1", "Restaurant2", "Restaurant3");
        Dal.addFavorite(items);
        // Add assertions to verify that the favorites are added correctly
    }

    @Test
    public void testRemoveFavorite() {
        String removedFavorite = "RestaurantToRemove";
        String username = "testUser";
        Dal.removeFavorite(removedFavorite, username);
        // Add assertions to verify that the favorite is removed correctly
    }

    @Test
    public void testAddRestaurantDetails() {
        // Prepare test data
        List<String> itemsToAdd = Arrays.asList("Test Restaurant");

        // Mock user input
        String simulatedUserInput = "Test Description\nfalse\n08:00:00\n20:00:00\nfalse\nTest Park\nTest Food\n$10-$20\n";
        InputStream savedStandardInputStream = System.in;
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        // Redirect console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the method
        dal.addRestaurantDetails(itemsToAdd, "testUser", "testPassword", new Scanner(System.in));

        // Restore standard input and output
        System.setIn(savedStandardInputStream);
        System.setOut(System.out);

        // Assert the output
        assertTrue(outContent.toString().contains("Test Restaurant inserted successfully with additional details."));
        // Add more assertions based on the expected behavior of the method
    }
}

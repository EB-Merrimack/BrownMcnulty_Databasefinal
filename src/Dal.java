import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Dal {
    public Dal() {
    }

    public List<String> searchInventory(String dbName, String parksearch, String username, String password) {
        try (Connection connection = DriverManager.getConnection(DataMGR.DB_URL + dbName, username, password)) {
            CallableStatement statement = connection.prepareCall("{CALL SearchByPark(?, ?)}");
            statement.setString(1, parksearch);
            statement.registerOutParameter(2, Types.REF_CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet) statement.getObject(2);
            List<String> results = new ArrayList<>();
            while (resultSet.next()) {
                String result = resultSet.getString(1);
                results.add(result);
            }
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
 }

	public List<String> Servicesearch(String dbName, String username, String password, String searchServiceType) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'Servicesearch'");
	}

    public static void addItemstoInventory(String dbName, List<String> itemsToAdd, String username, String password, Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(DataMGR.DB_URL + dbName, username, password)) {
            for (String item : itemsToAdd) {
                CallableStatement statement = connection.prepareCall("{CALL InsertNewRestaurants(?, ?)}");
                statement.setString(1, item);
                statement.registerOutParameter(2, java.sql.Types.BOOLEAN);
                statement.execute();
                boolean needAdditionalDetails = statement.getBoolean(2);
                if (needAdditionalDetails) {
                    // Prompt the user for additional details
                    System.out.println("Additional details required for the restaurant " + item + ". Please provide:");
                    System.out.print("Description: ");
                    String description = scanner.nextLine();
                    System.out.print("Is Character Dining? (true/false): ");
                    boolean isCharacterDining = scanner.nextBoolean();
                    scanner.nextLine(); // Consume newline
                    System.out.print("opening hours (HH:mm:ss): ");
                    String openhours = scanner.nextLine();
                    System.out.print("clossing hours (HH:mm:ss): ");
                    String closehours = scanner.nextLine();
                    System.out.print("Is All You Can Eat? (true/false): ");
                    boolean isAllYouCanEat = scanner.nextBoolean();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Park: ");
                    String park = scanner.nextLine();
                    System.out.print("Type of Food: ");
                    String typeOfFood = scanner.nextLine();
                    // Call InsertNewRestaurantsfull with additional details
                    insertNewRestaurantFull(connection, item, description, isCharacterDining, openhours,closehours, isAllYouCanEat, park, typeOfFood);
                } else {
                    System.out.println("Restaurant " + item + " inserted successfully.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertNewRestaurantFull(Connection connection, String restaurantName, String description, boolean isCharacterDining, String openhours, String closehours, boolean isAllYouCanEat, String park, String typeOfFood) throws SQLException {
        CallableStatement statement = connection.prepareCall("{CALL InsertNewRestaurantsfull(?, ?, ?,?, ?, ?, ?, ?)}");
        statement.setString(1, restaurantName);
        statement.setString(2, description);
        statement.setBoolean(3, isCharacterDining);
        statement.setString(4, openhours);
        statement.setString(5, closehours);
        statement.setBoolean(6, isAllYouCanEat);
        statement.setString(7, park);
        statement.setString(8, typeOfFood);
        statement.execute();
        System.out.println("Restaurant " + restaurantName + " inserted successfully with additional details.");
    }

    public List<String> searchWholeInventory(String restaurantString) {
    
        List<String> searchResults = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DataMGR.DB_URL, DataMGR.username, DataMGR.password)) {
            // Define the SQL query to search for the restaurant
            String sql = "SELECT * FROM restaurants WHERE restaurant_name LIKE ?";
            
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // Set the parameter for the restaurant name using a wildcard '%' to match any part of the name
                statement.setString(1, "%" + restaurantString + "%");
                
                // Execute the query
                try (ResultSet resultSet = statement.executeQuery()) {
                    // Process the result set
                    if (!resultSet.isBeforeFirst()) {
                        // No restaurants found with the provided name
                        searchResults.add("No restaurant exists with the name: " + restaurantString);
                    } else {
                        while (resultSet.next()) {
                            // Retrieve restaurant details from the result set
                            String name = resultSet.getString("restaurant_name");
                            String description = resultSet.getString("description");
                            boolean isCharacterDining = resultSet.getBoolean("ischaracterdining");
                            // Retrieve other restaurant details as needed
                            Time openingHours = resultSet.getTime("opening_hours");
                            Time closingHours = resultSet.getTime("closing_hours");
                            String priceRange = resultSet.getString("pricerange");
                            boolean isAllYouCanEat = resultSet.getBoolean("isallyoucaneat");
                            String park = resultSet.getString("Park");
                            String typeOfFood = resultSet.getString("typeoffood");
                            
                            // Build a string representation of the restaurant details
                            StringBuilder result = new StringBuilder();
                            result.append("Restaurant Name: ").append(name).append("\n");
                            result.append("Description: ").append(description).append("\n");
                            result.append("Character Dining: ").append(isCharacterDining).append("\n");
                            // Append other restaurant details
                            result.append("Opening Hours: ").append(openingHours).append("\n");
                            result.append("Closing Hours: ").append(closingHours).append("\n");
                            result.append("Price Range: ").append(priceRange).append("\n");
                            result.append("All You Can Eat: ").append(isAllYouCanEat).append("\n");
                            result.append("Park: ").append(park).append("\n");
                            result.append("Type of Food: ").append(typeOfFood).append("\n");
                            
                            // Add the string representation to the search results list
                            searchResults.add(result.toString());
                            
                            // Print out the restaurant details as they are retrieved
                            System.out.println(result);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Pause before going back to the menu
        System.out.println("Press Enter to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); // Wait for user to press Enter
        
        return searchResults;
    }
}    
    




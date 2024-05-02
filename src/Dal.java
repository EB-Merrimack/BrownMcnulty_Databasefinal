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

@SuppressWarnings("unused")
public class Dal {
    public Dal() {
    }

    public List<String> searchInventory(String dbName, String parksearch, String username, String password) {
        try (Connection connection = DriverManager.getConnection(DataMGR.DB_URL + dbName, username, password)) {
            CallableStatement statement = connection.prepareCall("{CALL SearchByPark(?, ?)}");
            statement.setString(1, parksearch);
            statement.registerOutParameter(2, Types.REF_CURSOR);
            statement.execute();
            @SuppressWarnings("resource")
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



    public static void addItemstoInventory( List<String> itemsToAdd, String username, String password, Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(DataMGR.DB_URL , username, password)) {
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
                    String restaurantDescription = scanner.nextLine();
                    System.out.print("Is Character Dining? (true/false): ");
                    boolean isCharacterDining = scanner.nextBoolean();
                    scanner.nextLine(); // Consume newline
                    System.out.print("opening hours (HH:mm:ss): ");
                    String openingHours = scanner.nextLine();
                    System.out.print("clossing hours (HH:mm:ss): ");
                    String closingHours = scanner.nextLine();
                    System.out.print("Is All You Can Eat? (true/false): ");
                    boolean isAllYouCanEat = scanner.nextBoolean();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Park: ");
                    String park = scanner.nextLine();
                    System.out.print("Type of Food: ");
                    String typeOfFood = scanner.nextLine();
                    System.out.print("Price Range ($): ");
                    String priceRange = scanner.nextLine();
                    // Call InsertNewRestaurantsfull with additional details
                    insertNewRestaurantFull(connection, item, restaurantDescription, isCharacterDining, openingHours,closingHours, isAllYouCanEat, park, typeOfFood, priceRange);
                } else {
                    System.out.println("Restaurant " + item + " inserted successfully.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertNewRestaurantFull(Connection connection, String restaurantName, String description, boolean isCharacterDining, String openhours, String closehours, boolean isAllYouCanEat, String park, String typeOfFood, String priceRange) throws SQLException {
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

    @SuppressWarnings("resource")
    public void searchWholeInventory(String restaurantString) {
    
        List<String> searchResults = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DataMGR.DB_URL, DataMGR.username, DataMGR.password)) {
            // Define the SQL query to search for the restaurant
            String sql = "SELECT * FROM restaurants WHERE restaurantName LIKE ?";
            
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
                            String name = resultSet.getString("restaurantName");
                            String description = resultSet.getString("restaurantDescription");
                            boolean isCharacterDining = resultSet.getBoolean("isCharacterDining");
                            // Retrieve other restaurant details as needed
                            Time openingHours = resultSet.getTime("openingHours");
                            Time closingHours = resultSet.getTime("closingHours");
                            String priceRange = resultSet.getString("pricerange");
                            boolean isAllYouCanEat = resultSet.getBoolean("isAllYouCanEat");
                            String park = resultSet.getString("Park");
                            String typeOfFood = resultSet.getString("typeOfFood");
                            
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
        scanner.nextLine(); 
        IntroToPresentationLayer.choices();
        
    }


    public List<String> searchRestaurantsByPark(String parkName) {
        List<String> restaurants = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DataMGR.DB_URL, DataMGR.username, DataMGR.password)) {
            CallableStatement statement = connection.prepareCall("{CALL FindRestaurantsByParkName(?)}");
            statement.setString(1, parkName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String restaurantName = resultSet.getString("restaurantName");
                restaurants.add(restaurantName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Here are the restaurants in " + parkName + ": " + restaurants);
        // Pause before going back to the menu
        System.out.println("Press Enter to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); 
        addFavorite(restaurants);
        return restaurants;
    }

    public List<String> findRestaurantsByServiceType( String username, String password, String serviceType) {
        List<String> restaurants = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DataMGR.DB_URL, username, password)) {
            String procedureCall = "{CALL FindRestaurantsByServiceType(?)}";
            try (CallableStatement statement = connection.prepareCall(procedureCall)) {
                statement.setString(1, serviceType);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String restaurantName = resultSet.getString("restaurantName");
                        restaurants.add(restaurantName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Here are the restaurants in " + serviceType + ": " + restaurants);
        System.out.println("Press Enter to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); 
        addFavorite(restaurants);
        return restaurants;
    }

    static void addFavorite(List<String> items) {
        try (Scanner scanner = new Scanner(System.in)) {
           System.out.print("Do you want to add any of these restaurants to your favorites? (yes/no): ");
           String response = scanner.nextLine();
           if (response.equalsIgnoreCase("yes")) {
               System.out.println("Enter the names of restaurants you want to add (comma-separated): ");
               String namesString = scanner.nextLine();
               String[] names = namesString.split(",");
               for (String name : names) {
                   String restaurant = name.trim();
                   if (items.contains(restaurant)) {
                       favoritesList.addFavorite(restaurant,DataMGR.username);
                       System.out.println(restaurant + " added to favorites.");
                   } else {
                       System.out.println("Restaurant '" + restaurant + "' not found.");
                   }
               }
           }
           IntroToPresentationLayer.choices();
       } catch (Exception e) {
           e.printStackTrace();
       }
        
   }

   public static List<String> getAllRestaurantNames() {
    List<String> restaurantNames = new ArrayList<>();
    String sql = "SELECT restaurantName FROM restaurants";

    try (Connection connection = DriverManager.getConnection(DataMGR.DB_URL, DataMGR.username, DataMGR.password);
         PreparedStatement pstmt = connection.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            // Retrieve the restaurant name from the result set
            String name = rs.getString("restaurantName");
            restaurantNames.add(name);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return restaurantNames;
}
}
    




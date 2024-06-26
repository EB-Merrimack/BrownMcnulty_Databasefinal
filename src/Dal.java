import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            ResultSet resultSet = (ResultSet) statement.getObject(1);
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



 public static void addRestaurantDetails(List<String> itemsToAdd, String username, String password, Scanner scanner) {
    try (Connection connection = DriverManager.getConnection(DataMGR.DB_URL, username, password)) {
        for (String restaurant_name : itemsToAdd) {
            // Check if the restaurant already exists
            boolean restaurantExists = checkRestaurantExists(connection, restaurant_name);
            if (restaurantExists) {
                System.out.println("Restaurant " + restaurant_name + " already exists in the database.");
                // If the restaurant exists, retrieve its details
                retrieveRestaurantDetails(connection, restaurant_name);
            } else {
                Scanner input = new Scanner(System.in); // Create a new scanner
                // If the restaurant doesn't exist, prompt for additional details
                System.out.println("Additional details required for the restaurant " + restaurant_name + ". Please provide:");
                System.out.print("Description: ");
                String Description = input.nextLine();
                System.out.print("Is Character Dining? (true/false): ");
                boolean isCharacterDining = input.nextBoolean();
                System.out.print("Opening hours (HH:mm:ss): ");
                String openingHours = input.nextLine();
                input.nextLine(); // Consume newline
                System.out.print("Closing hours (HH:mm:ss): ");
                String closingHours = input.nextLine();
                System.out.print("Is All You Can Eat? (true/false): ");
                boolean isAllYouCanEat = input.nextBoolean();
                input.nextLine(); // Consume newline
                System.out.print("Park: ");
                String userInput = input.nextLine().toLowerCase(); // Convert input to lowercase
                
                String park = null;
                
                if ("magic kingdom".equalsIgnoreCase(userInput) || "epcot".equalsIgnoreCase(userInput) ||
                    "animal kingdom".equalsIgnoreCase(userInput) || "hollywood studios".equalsIgnoreCase(userInput)) {
                    
                    // Capitalize the first letter of the park name
                    park = userInput.substring(0, 1).toUpperCase() + userInput.substring(1);
                    
                    // Do something if park is one of the main four
                    System.out.println("You've selected: " + park);
                } else {
                    // If park is not one of the main four, set park to null for restaurant
                    System.out.println("These parks are not the main four official parks.");
                }
                System.out.print("Type of Food: ");
                String typeOfFood = input.nextLine();
                System.out.print("Price Range ($ to $$$$$): ");


            String priceRange = input.nextLine();
        boolean validInput = false;

            // Check if the input contains only 1 to 5 dollar signs
            if (priceRange.matches("^\\$?\\$?\\$?\\$?\\$?$")) {
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter 1 to 5 dollar signs.");
                priceRange="null";
            }
                
                // Insert the new restaurant with additional details
                insertNewRestaurantFull(connection, restaurant_name, Description, isCharacterDining, openingHours, closingHours, isAllYouCanEat, park, typeOfFood, priceRange);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        IntroToPresentationLayer.choices();

    }
}

private static boolean checkRestaurantExists(Connection connection, String restaurantName) throws SQLException {
    String sql = "SELECT COUNT(*) AS count FROM restaurants WHERE restaurantName = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, restaurantName);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }
        }
    }
    return false;
}

static String retrieveRestaurantDetails(Connection connection, String restaurantName) throws SQLException {
    String sql = "SELECT * FROM restaurants WHERE restaurantName = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, restaurantName);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                // Retrieve restaurant details
                String name = rs.getString("restaurantName");
                String description = rs.getString("restaurantdescription");
                boolean isCharacterDining = rs.getBoolean("isCharacterDining");
                String openingHours = rs.getString("openingHours");
                String closingHours = rs.getString("closingHours");
                String priceRange = rs.getString("priceRange");
                boolean isAllYouCanEat = rs.getBoolean("isAllYouCanEat");
                String park = rs.getString("park");
                String typeOfFood = rs.getString("typeOfFood");
                
                // Construct the details into a string
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

                // Print the details
                System.out.println(result.toString());
            }
        }
    }
    return restaurantName;
}

    private static void insertNewRestaurantFull(Connection connection, String restaurantName, String description, boolean isCharacterDining, String openhours, String closehours, boolean isAllYouCanEat, String park, String typeOfFood, String priceRange) throws SQLException {
        CallableStatement statement = connection.prepareCall("{CALL InsertNewRestaurantsfull(?, ?, ?,?, ?, ?, ?, ?,?)}");
        statement.setString(1, restaurantName);
        statement.setString(2, description);
        statement.setBoolean(3, isCharacterDining);
        statement.setString(4, openhours);
        statement.setString(5, closehours);
        statement.setBoolean(6, isAllYouCanEat);
        statement.setString(7, park);
        statement.setString(8, typeOfFood);
        statement.setString(9, priceRange);
        statement.execute();
        System.out.println("Restaurant " + restaurantName + " inserted successfully with additional details.");
    }

    @SuppressWarnings("resource")
    public void searchAllRestaurants(String restaurantString) {
    
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
                            IntroToPresentationLayer.choices();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            IntroToPresentationLayer.choices();
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
            IntroToPresentationLayer.choices();
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
            IntroToPresentationLayer.choices();
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
            List<String> newFavorites = new ArrayList<>();

            // Check if favorites file exists, if not, create an empty one
            Path favoritesFile = Paths.get("userdata", DataMGR.username, "favorites.txt");
            if (!Files.exists(favoritesFile)) {
                Files.createDirectories(favoritesFile.getParent());
                Files.createFile(favoritesFile);
            } else {
                // Read existing favorites
                try (BufferedReader reader = Files.newBufferedReader(favoritesFile)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        newFavorites.add(line);
                    }
                }
            }

            // Add new favorites
            for (String name : names) {
                String restaurant = name.trim();
                if (items.contains(restaurant) && !newFavorites.contains(restaurant)) {
                    newFavorites.add(restaurant);
                    System.out.println(restaurant + " added to favorites.");
                } else if (!items.contains(restaurant)) {
                    System.out.println("Restaurant '" + restaurant + "' not found.");
                } else {
                    System.out.println("Restaurant '" + restaurant + "' is already in favorites.");
                }
            }

            // Write new favorites to file
            try (BufferedWriter writer = Files.newBufferedWriter(favoritesFile)) {
                for (String favorite : newFavorites) {
                    writer.write(favorite);
                    writer.newLine();
                }
            }
        }
        IntroToPresentationLayer.choices();
    } catch (Exception e) {
        e.printStackTrace();
        IntroToPresentationLayer.choices();
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
        IntroToPresentationLayer.choices();
    }

    return restaurantNames;
}

public static void removeFavorite(String removedFavorite, String username) {
    // Load the user's favorites list
    List<String> favorites = loadFavorites(username);

    // Remove the specified favorite restaurant
    if (favorites.remove(removedFavorite)) {
        // If the favorite was removed, save the updated favorites list
        saveFavorites(favorites, username);
        System.out.println(removedFavorite + " removed from favorites.");
    } else {
        System.out.println("Favorite not found.");
    }
}

private static List<String> loadFavorites(String username) {
    List<String> favorites = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader("userdata\\" + username + "\\favorites.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            favorites.add(line);
        }
    } catch (IOException e) {
        System.err.println("Error reading favorites: " + e.getMessage());
    }
    return favorites;
}

private static void saveFavorites(List<String> favorites, String username) {
    try (PrintWriter writer = new PrintWriter(new FileWriter("userdata\\" + username + "\\favorites.txt"))) {
        for (String favorite : favorites) {
            writer.println(favorite);
        }
    } catch (IOException e) {
        System.err.println("Error saving favorites: " + e.getMessage());
    }
}

}
    




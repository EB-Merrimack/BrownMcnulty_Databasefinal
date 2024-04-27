import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    private static Connection getMySQLConnection(String databaseName, String user, String password) {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, user, password);
        } catch (SQLException var5) {
            System.out.println("Failed to connect to the database" + var5.getMessage());
            return null;
        }
    }

    private static final String SELECT_ITEM_QUERY = "SELECT * FROM shop_items WHERE ItemName = ?";

    public static boolean addItemstoInventory(String databaseName, List<String> itemsToAdd, String user, String password, String string) {
        Connection myConnection = getMySQLConnection(databaseName, user, password);
    
        try {
            @SuppressWarnings("resource")
            Scanner scanner = new Scanner(System.in);
    
            // Check if the item already exists in the inventory
            for (String item : itemsToAdd) {
                if (!itemExists(myConnection, item)) {
                    // If the item does not exist, prompt the user for additional information
                    promptForItemDetails(myConnection, item);
                    System.out.println("Item created.");
                } else {
                    // If the item exists, print its existing values and ask the user if they want to update them
                    System.out.println("Item '" + item + "' already exists in the inventory.");
                    System.out.println("Existing details:");
                    printItemDetails(myConnection, item);
                    System.out.println("Do you want to update its values? (yes/no)");
                    String updateChoice = scanner.nextLine().toLowerCase();
                    if (updateChoice.equals("yes")) {
                        System.out.println("Enter the new category:");
                        String newCategory = scanner.nextLine();
                        // Update the category in the database
                        updateItemCategory(myConnection, item, newCategory);
                    }
                }
            }
            return true;
        } catch (SQLException var10) {
            System.out.println("Failed to execute stored procedure:" + var10.getMessage());
            return false;
        }
    }
    
    private static boolean itemExists(Connection connection, String itemName) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ITEM_QUERY)) {
            statement.setString(1, itemName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // If resultSet.next() is true, the item exists
            }
        }
    }

    private static void insertNewShopItem(Connection connection, String itemName, int quantity, String description, int dangerLevel, double pricePaid, double salePrice, String magic, String item) throws SQLException {
        // Prepare the SQL statement
        String sql = "{CALL InsertNewShopItem(?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement statement = connection.prepareCall(sql)) {
            // Set the parameters
            statement.setString(1, itemName);
            statement.setInt(2, quantity);
            statement.setString(3, description);
            statement.setInt(4, dangerLevel);
            statement.setDouble(5, pricePaid);
            statement.setDouble(6, salePrice);
            statement.setString(7, magic);

            // Execute the stored procedure
            statement.execute();
        }
    }

    private static void promptForItemDetails(Connection connection, String item) throws SQLException {
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("Enter details for item '" + item + "':");
    
        System.out.print("Quantity: ");
        int quantity = scanner.nextInt();
    
        // Consume newline character
        scanner.nextLine();
    
        System.out.print("Description: ");
        String description = scanner.nextLine();
    
        int dangerLevel;
        do {
            System.out.print("Danger Level (1-5): ");
            dangerLevel = scanner.nextInt();
        } while (dangerLevel < 1 || dangerLevel > 5);
    
        double pricePaid;
        do {
            System.out.print("Price Paid (in dollars): ");
            pricePaid = readPrice(scanner);
        } while (pricePaid <= 0);
    
        double salePrice;
        do {
            System.out.print("Sale Price (in dollars): ");
            salePrice = readPrice(scanner);
        } while (salePrice <= 0);
        System.out.print("Magic: ");
        String magic = scanner.nextLine();
    
        // Call the method to insert the new shop item into the database
        insertNewShopItem(connection, item, quantity, description, dangerLevel, pricePaid, salePrice, magic, item);
    }
    
    private static double readPrice(Scanner scanner) {
        double price = 0;
        String input = scanner.nextLine();
        try {
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
            Number number = format.parse(input);
            price = number.doubleValue();
        } catch (ParseException e) {
            System.out.println("Invalid price format. Please enter a valid dollar amount.");
        }
        return price;
    }
    private static void printItemDetails(Connection connection, String itemName) throws SQLException {
        // SQL query to retrieve item details based on the item name
        String query = "SELECT * FROM shop_items WHERE ItemName = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, itemName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve item details from the result set
                    int quantity = resultSet.getInt("quantity");
                    String description = resultSet.getString("description");
                    int dangerLevel = resultSet.getInt("danger_level");
                    int pricePaid = resultSet.getInt("price_paid");
                    int salePrice = resultSet.getInt("sale_price");
                    String magicType = resultSet.getString("magic_type");
                    
                    // Print item details
                    System.out.println("Item Name: " + itemName);
                    System.out.println("Quantity: " + quantity);
                    System.out.println("Description: " + description);
                    System.out.println("Danger Level: " + dangerLevel);
                    System.out.println("Price Paid: " + pricePaid);
                    System.out.println("Sale Price: " + salePrice);
                    System.out.println("Magic Type: " + magicType);
                } else {
                    System.out.println("Item '" + itemName + "' not found in the inventory.");
                }
            }
        }
    }
    

    private static final String[] CATEGORIES = {"Item Name", "Quantity", "Description", "Danger Level", "Price Paid", "Sale Price", "Magic Type"};

    private static void updateItemCategory(Connection connection, String itemName, String newCategory) throws SQLException {
        // Check if the new category is valid
        boolean validCategory = false;
        for (String category : CATEGORIES) {
            if (category.equalsIgnoreCase(newCategory)) {
                validCategory = true;
                break;
            }
        }
    
        if (!validCategory) {
            System.out.println("Invalid category. Please choose from the following categories:");
            for (String category : CATEGORIES) {
                System.out.println(category);
            }
            return;
        }
    
        // SQL update statement to set the new category for the item
        String updateQuery = "UPDATE shop_items SET category = ? WHERE ItemName = ?";
    
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, newCategory);
            statement.setString(2, itemName);
    
            int rowsAffected = statement.executeUpdate();
    
            if (rowsAffected > 0) {
                System.out.println("Category for item '" + itemName + "' updated successfully.");
            } else {
                System.out.println("Failed to update category for item '" + itemName + "'. Item not found.");
            }
        }
    }
    private void saveSearch(int dangerLevel, String magicType, Double price, List<String> searchResults, String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath, true); // Append to existing file or create new file if not exists
            writer.write("Search Parameters:\n");
            writer.write("Danger Level: " + dangerLevel + ", Magic Type: " + magicType + ", Price: " + (price != null ? price.toString() : "Any") + "\n");
            writer.write("Search Results:\n");
            for (String result : searchResults) {
                writer.write(result + "\n");
            }
            writer.write("\n"); // Add a separator between search entries
            writer.close();
            System.out.println("Search saved successfully at: " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to save search parameters and results to file: " + e.getMessage());
        }
    }
    
    public List<String> searchInventory(String dbName, String magicType, String username, String password) {
        List<String> searchResult = new ArrayList<>();
    
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM shop_items WHERE (? IS NULL OR DangerLevel = ?) AND (? IS NULL OR magic = ?) AND (? IS NULL OR SalePrice = ?)")) {
    
            // Set parameters

    
            // Execute query
            ResultSet resultSet = statement.executeQuery();
    
            // Process results
            while (resultSet.next()) {
                // Assuming you have columns named item_name, description, danger_level, sale_price, purchase_price, magic_type in your inventory table
                String itemName = resultSet.getString("itemName");
                String description = resultSet.getString("description");
                int dangerLevelResult = resultSet.getInt("dangerLevel");
                double salePrice = resultSet.getDouble("salePrice");
                double purchasePrice = resultSet.getDouble("Pricepaid");
                String magicTypeResult = resultSet.getString("magic");
    
                // Construct result string
                String result = itemName + ": " + description + ", Danger Level: " + dangerLevelResult + ", Sale Price: " + salePrice + ", Purchase Price: " + purchasePrice + ", Magic Type: " + magicTypeResult;
                searchResult.add(result);
            }
    
            // Prompt user if they wish to save the search results
            Scanner scanner = new Scanner(System.in);
            System.out.println("Do you want to save the search results? (yes/no)");
            String saveChoice = scanner.nextLine().toLowerCase();
            if (saveChoice.equals("yes")) {
                // Prompt user for file path to save search results
                System.out.println("Enter the file path to save the search results:");
                String filePath = scanner.nextLine();

                //restaurant to be initialized to show on click
    
                Object restaurantname = null;
                // Save search results to specified file path
                saveFavorites(dbName, username, password, restaurantname);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return searchResult;
    }

    private void saveFavorites(String dbName, String username, String password, Object restaurantname) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveFavorites'");
    }

    public void salesReport(String dbName, String username, String password, int month) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
    
        try {
            // Establish connection
            connection = getMySQLConnection(dbName, username, password);
    
            // SQL query to retrieve sales data for the specified month
            String query = "SELECT itemName, CustomerName, saleprice, purchasedate FROM sales WHERE MONTH(purchasedate) = ?";
            System.out.println("Executing query: " + query); // Debug statement
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, month);
    
            // Execute the query
            resultSet = preparedStatement.executeQuery();
    
            // Print report header
            System.out.println("Sales Report for Month " + month + ":");
            System.out.println("---------------------------------------------------");
            System.out.printf("%-20s %-20s %-20s %-15s%n", "Item Name", "Customer", "Sale Date", "Price");
            System.out.println("---------------------------------------------------");
    
            // Write sales data to the console
            while (resultSet.next()) {
                String itemName = resultSet.getString("itemName");
                String purchaser = resultSet.getString("CustomerName");
                Timestamp saleDate = resultSet.getTimestamp("purchasedate");
                double price = resultSet.getDouble("saleprice");
    
                // Format sale date as string
                String saleDateString = saleDate.toString();
    
                // Print sales data
                System.out.printf("%-20s %-20s %-20s %-15.2f%n", itemName, purchaser, saleDateString, price);
            }
    
            System.out.println("Sales report generated successfully."); // Debug statement
        } catch (SQLException e) {
            System.out.println("Failed to generate sales report: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Failed to close resources: " + e.getMessage());
            }
        }
    }

	public void Servicesearch(String dbName, String username, String password, String searchservicetype) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'Servicesearch'");
	}
      }


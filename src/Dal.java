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

    private static final String SELECT_ITEM_QUERY = "SELECT * FROM restaurants WHERE RestaurantName= ?";

    public List<String> searchInventory(String dbName, String parksearch, String username, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchInventory'");
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
                    System.out.print("Hours (yyyy-MM-dd HH:mm:ss): ");
                    String hours = scanner.nextLine();
                    System.out.print("Is All You Can Eat? (true/false): ");
                    boolean isAllYouCanEat = scanner.nextBoolean();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Park: ");
                    String park = scanner.nextLine();
                    System.out.print("Type of Food: ");
                    String typeOfFood = scanner.nextLine();
                    // Call InsertNewRestaurantsfull with additional details
                    insertNewRestaurantFull(connection, item, description, isCharacterDining, hours, isAllYouCanEat, park, typeOfFood);
                } else {
                    System.out.println("Restaurant " + item + " inserted successfully.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertNewRestaurantFull(Connection connection, String restaurantName, String description, boolean isCharacterDining, String hours, boolean isAllYouCanEat, String park, String typeOfFood) throws SQLException {
        CallableStatement statement = connection.prepareCall("{CALL InsertNewRestaurantsfull(?, ?, ?, ?, ?, ?, ?)}");
        statement.setString(1, restaurantName);
        statement.setString(2, description);
        statement.setBoolean(3, isCharacterDining);
        statement.setString(4, hours);
        statement.setBoolean(5, isAllYouCanEat);
        statement.setString(6, park);
        statement.setString(7, typeOfFood);
        statement.execute();
        System.out.println("Restaurant " + restaurantName + " inserted successfully with additional details.");
    }

    public void searchwholeinventory(String restaurantString) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchwholeinventory'");
    }
}



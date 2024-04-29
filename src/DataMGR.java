import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataMGR {
    // JDBC URLs for the databases
    static final String DB_URL = "jdbc:mysql://localhost:3306/FindYourDisneyRestaurant";

    // Map to cache database connections
    private static final Map<String, Connection> connectionCache = new HashMap<>();
    
    // Cached username and password
    static String username;
    static String password;

    // Method to establish a connection to the specified Database with the given username and password
    private static Connection connectToDatabase(String dbUrl, String username, String password) throws SQLException {
        return DriverManager.getConnection(dbUrl, username, password);
    }

    // Method to get a cached connection or create a new one if not present
    private static Connection getCachedConnection(String dbUrl, String username, String password) throws SQLException {
        if (!connectionCache.containsKey(dbUrl)) {
            Connection connection = connectToDatabase(dbUrl, username, password);
            connectionCache.put(dbUrl, connection);
        }
        return connectionCache.get(dbUrl);
    }
    
    // Method to cache username and password
    public static void setCredentials(String user, String pass) {
        username = user;
        password = pass;
    }
    
    // Method to retrieve cached username
    public static String getUsername() {
        return username;
    }
    
    // Method to retrieve cached password
    public static String getPassword() {
        return password;
    }

   
    
    // Method to close all cached connections
    public static void closeConnections() {
        for (Connection connection : connectionCache.values()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        connectionCache.clear();
        System.out.println("All connections closed.");
    }

    // Example usage
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        String user = scanner.nextLine();
        System.out.println("Enter password:");
        String pass = scanner.nextLine();
        
        // Cache credentials
        setCredentials(user, pass);

        try {
            // Example usage to connect to the MealPlanning Database
            Connection dbConnection = getCachedConnection(DB_URL, user, pass);
            System.out.println("Connected to Disney Restaurant Database successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

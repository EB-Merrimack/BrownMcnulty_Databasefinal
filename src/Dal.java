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

    public static void addItemstoInventory(String dbName, List<String> itemsToAdd, String username, String password,
            Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addItemstoInventory'");
    }

    public List<String> searchInventory(String dbName, String parksearch, String username, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchInventory'");
    }

	public List<String> Servicesearch(String dbName, String username, String password, String searchServiceType) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'Servicesearch'");
	}

}

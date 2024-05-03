import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;

import javax.swing.JFileChooser;
import javax.swing.JSpinner.DateEditor;
import javax.swing.filechooser.FileNameExtensionFilter;

public class IntroToPresentationLayer {
    private static String dbName = "FindYourDisneyRestaurant";
    private static List<String> favoritesList = new ArrayList<>();
   

public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    DataMGR.connection(args);
    choices();
}



    public static void choices() {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);

    while (running) {
        try {
            System.out.println("Welcome to Disney restaurant database. Your options are as follows:");
            System.out.println("1- Add restaurant to database");
            System.out.println("2- Search by park");
            System.out.println("3- Search by service type");
            System.out.println("4- Export favorites");
            System.out.println("5- view favorites Dashboard");
            System.out.println("6- Close database connections");
            System.out.println("7- Search restaurant details");
            System.out.println("8- Exit");

            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add items into the inventory
                    addRestaurantToDatabase();
                    break;

                case 2:
                    // Search through the inventory by park and add to favorites
                    searchByPark();
                    break;

                case 3:
                    // Search through the inventory by service type and add to favorites
                    searchByServiceType();
                    break;

                case 4:
                    // Export favorites
                    exportFavorites();
                    break;

                case 5:
                    // Change favorites
                    viewFavoritesdashboard();
                    break;

                case 6:
                    // Close connections
                    closeConnections();
                    break;

                case 7:
                    searchRestaurantDetails();
                    break;

                case 8:
                    running = false;
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

    private static void searchRestaurantDetails() {
        try (Scanner scanner2 = new Scanner(System.in)) {
            Dal dal = new Dal();
            System.out.println("Enter the restaurant name:");
            String restaurantString = scanner2.nextLine();
            dal.searchAllRestaurants( restaurantString); // Use the correct database name
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    

private static void addRestaurantToDatabase() {
    try (Scanner scanner = new Scanner(System.in)) {
        // Add restaurants into the database and allow the user to add favorites
        Dal dal = new Dal();
        System.out.println("Enter the restaurants to add into the database with each restaurant name on a new line (type 'done' when finished):");

        List<String> restaurantsToAdd = new ArrayList<>();
        while (true) {
            String restaurantName = scanner.nextLine();
            if (restaurantName.equalsIgnoreCase("done")) {
                break;
            }
            restaurantsToAdd.add(restaurantName);
        }

        if (!restaurantsToAdd.isEmpty()) {
            // Push all the restaurants to the database
            for (String restaurant : restaurantsToAdd) {
                Dal.addRestaurantDetails( Collections.singletonList(restaurant), DataMGR.getUsername(), DataMGR.getPassword(), null);
            }
            System.out.println("Restaurants successfully added to the database.");
            Dal.addFavorite(restaurantsToAdd); // Allow user to add favorites
        } else {
            System.out.println("No restaurants were added to the database.");
        }
    }
}


    private static void searchByPark() {
        try (Scanner scanner = new Scanner(System.in)) {
            // Search through the inventory by park and add to favorites
            Dal dal = new Dal();
            System.out.println("Enter the park you want to search :");
            String parksearch = scanner.nextLine();

            // Perform the search with the provided filters
            List<String> searchResult = dal.searchRestaurantsByPark(parksearch);
        }
    }

    private static void searchByServiceType() {
        try (Scanner scanner = new Scanner(System.in)) {
            // Search through the inventory by service type and add to favorites
            System.out.print("Enter the service type: ");
            String searchServiceType = scanner.nextLine();
            Dal dal = new Dal();
            List<String> searchResult = dal.findRestaurantsByServiceType(DataMGR.getUsername(), DataMGR.getPassword(), searchServiceType);
            }
    }

    private static void exportFavorites() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the file name to save the favorites (without extension): ");
        String fileName = scanner.nextLine();

        // Construct the file path in the current directory
        String filePath = fileName + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the favorites to the file
            for (String favorite : favoritesList ){
                writer.write(favorite);
                writer.newLine();
            }
            System.out.println("Favorites exported successfully.");
        } catch (IOException e) {
            System.err.println("Error exporting favorites: " + e.getMessage());
        }
    }   
    private static void viewFavoritesdashboard() {
        Scanner scanner = new Scanner(System.in);
    
        List<String> readfavoritesList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("userdata\\" + DataMGR.getUsername() + "\\favorites.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                readfavoritesList.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading favorites: " + e.getMessage());
        }
    
        System.out.println("Your current favorites:");
        for (int i = 0; i < readfavoritesList.size(); i++) {
            System.out.println((i + 1) + ". " + readfavoritesList.get(i));
        }
    
        System.out.println("Do you want to (1) add a new favorite, (2) remove an existing favorite, or (3) exit? (1/2/3)");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        switch (choice) {
            case 1:
                // Add a new favorite
                List<String> restaurantNames = Dal.getAllRestaurantNames();
                System.out.println("the current restaurants in the database are:"+restaurantNames);
                Dal.addFavorite(restaurantNames); // Add favorites to Dal
                System.out.println("New favorites added successfully.");
                saveFavoritesToFile(readfavoritesList, DataMGR.getUsername()); // Save updated favorites to file
                IntroToPresentationLayer.choices();
                break;
            case 2:
                // Remove an existing favorite
                System.out.println("Enter the number of the favorite you want to remove:");
                int favoriteIndex = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (favoriteIndex > 0 && favoriteIndex <= readfavoritesList.size()) {
                    String removedFavorite = readfavoritesList.remove(favoriteIndex - 1);
                    Dal.removeFavorite(removedFavorite, DataMGR.getUsername()); // Remove favorite from Dal
                    System.out.println(removedFavorite + " removed from favorites.");
                    saveFavoritesToFile(readfavoritesList, DataMGR.getUsername()); // Save updated favorites to file
                } else {
                    System.out.println("Invalid favorite number.");
                }
                IntroToPresentationLayer.choices();
                break;
            case 3:
                // Exit
                IntroToPresentationLayer.choices();
                break;
            default:
                System.out.println("Invalid choice.");
                IntroToPresentationLayer.choices();
                break;
        }
    
        scanner.close();
    }
    

    private static void saveFavoritesToFile(List<String> favorites, String username) {
        try {
            Path userDir = Paths.get("userdata", username);
            if (!Files.exists(userDir)) {
                Files.createDirectories(userDir);
            }
            Path filePath = userDir.resolve("favorites.txt");
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
                for (String favorite : favorites) {
                    writer.println(favorite);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  

    // Method to close all cached connections
    private static void closeConnections() {
        DataMGR.closeConnections();
    }
}

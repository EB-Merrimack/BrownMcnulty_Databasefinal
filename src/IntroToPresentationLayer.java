import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class IntroToPresentationLayer {
    private static String dbName = "FindYourDisneyRestaurant";
    private static List<String> favoritesList = new ArrayList<>();
   

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        DataMGR.main(args);

        while (running) {
            System.out.println("Welcome to Disney restaurant database. Your options are as follows:");
            System.out.println("1- Add restaurant to database");
            System.out.println("2- Search by park");
            System.out.println("3- Search by service type");
            System.out.println("4- Export favorites");
            System.out.println("5- Change favorites");
            System.out.println("6- Close database connections");
            System.out.println("7- Exit");

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
                    changeFavorites();
                    break;

                case 6:
                    // Close connections
                    closeConnections();
                    break;

                case 7:
                searchrestaurantdetails();
                break;

                case 8:
                    running = false;
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
        scanner.close();
    }

 private static void searchrestaurantdetails() {
    try (Scanner scanner = new Scanner(System.in)) {
        Dal dal = new Dal();
        System.out.println("Enter the restaurant name:");
        String restaurantString = scanner.nextLine();
        dal.searchwholeinventory(restaurantString);
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
                Dal.addItemstoInventory(dbName, Collections.singletonList(restaurant), DataMGR.getUsername(), DataMGR.getPassword(), null);
            }
            System.out.println("Restaurants successfully added to the database.");
            addFavorite(restaurantsToAdd); // Allow user to add favorites
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
            List<String> searchResult = dal.searchInventory(dbName, parksearch, DataMGR.getUsername(), DataMGR.getPassword());

            // Display search result
            if (searchResult.isEmpty()) {
                System.out.println("No restaurants found matching the search criteria.");
            } else {
                System.out.println("Search Result:");
                for (String item : searchResult) {
                    System.out.println(item); // Assuming Item class has overridden toString() method
                }
                addFavorite(searchResult); // Allow user to add favorites
            }
        }
    }

    private static void searchByServiceType() {
        try (Scanner scanner = new Scanner(System.in)) {
            // Search through the inventory by service type and add to favorites
            System.out.print("Enter the service type: ");
            String searchServiceType = scanner.nextLine();
            Dal dal = new Dal();
            List<String> searchResult = dal.Servicesearch(dbName, DataMGR.getUsername(), DataMGR.getPassword(), searchServiceType);

            if (searchResult.isEmpty()) {
                System.out.println("No restaurants found matching the service type.");
            } else {
                System.out.println("Search Result:");
                for (String item : searchResult) {
                    System.out.println(item); // Assuming Item class has overridden toString() method
                }
                addFavorite(searchResult); // Allow user to add favorites
            }
        }
    }

    private static void exportFavorites() {
        // Export favorites
        System.out.println("Functionality to export favorites is not implemented yet.");
    }

    private static void changeFavorites() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Your current favorites:");
    
        for (int i = 0; i < favoritesList.size(); i++) {
            System.out.println((i + 1) + ". " + favoritesList.get(i));
        }
    
        System.out.println("Do you want to (1) add a new favorite or (2) remove an existing favorite? (1/2)");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        switch (choice) {
            case 1:
                // Add a new favorite
                System.out.println("Enter the name of the restaurant you want to add to favorites:");
                String newFavorite = scanner.nextLine();
                favoritesList.add(newFavorite);
                System.out.println(newFavorite + " added to favorites.");
                break;
            case 2:
                // Remove an existing favorite
                System.out.println("Enter the number of the favorite you want to remove:");
                int favoriteIndex = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (favoriteIndex > 0 && favoriteIndex <= favoritesList.size()) {
                    String removedFavorite = favoritesList.remove(favoriteIndex - 1);
                    System.out.println(removedFavorite + " removed from favorites.");
                } else {
                    System.out.println("Invalid favorite number.");
                }
                break;
            default:
                System.out.println("Invalid choice.");
        }
        scanner.close();
    }
    

    // Method to add restaurants to favorites list
    private static void addFavorite(List<String> items) {
         try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Do you want to add any of these restaurants to your favorites? (yes/no): ");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                System.out.println("Enter the numbers of restaurants you want to add (comma-separated): ");
                String numbers = scanner.nextLine();
                String[] nums = numbers.split(",");
                for (String num : nums) {
                    int index = Integer.parseInt(num.trim()) - 1;
                    if (index >= 0 && index < items.size()) {
                        String restaurant = items.get(index);
                        favoritesList.add(restaurant);
                        System.out.println(restaurant + " added to favorites.");
                    } else {
                        System.out.println("Invalid restaurant number.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Method to close all cached connections
    private static void closeConnections() {
        DataMGR.closeConnections();
    }
}
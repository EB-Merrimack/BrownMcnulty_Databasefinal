import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IntroToPresentationLayer {
    private static String dbName = "FindYourDisneyRestaurant";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        DataMGR.main(args);

        while (running) {
            System.out.println("Welcome to disney restaurant database. Your options are as follows:");
            System.out.println("1- add restaurant to database");
            System.out.println("2- search by park");
            System.out.println("3-  search by service type");
            System.out.println("4- export favorites");
            System.out.println("5- change favorites");
            System.out.println("6- close database connections");
            System.out.println("7- Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add items into the inventory
                    Dal dal1 = new Dal();
                    System.out.println("Enter the items to add into William's Inventory with each item on a new line (type 'done' when finished):");

                    List<String> itemsToAdd = new ArrayList<>();
                    while (true) {
                        String query = scanner.nextLine();
                        if (query.equalsIgnoreCase("done")) {
                            break;
                        }
                        itemsToAdd.add(query);
                    }

                    if (!itemsToAdd.isEmpty()) {
                        // Push all the items to the database
                        Dal.addItemstoInventory(dbName, itemsToAdd, DataMGR.getUsername(), DataMGR.getPassword(), null);
                        ;
                        System.out.println("Items successfully added to the inventory.");
                    } else {
                        System.out.println("No items added to the inventory.");
                    }
                    break;

                case 2:
                    // Search through the inventory
                   // Search through the inventory
Dal dal2 = new Dal();
System.out.println("Enter the search filters for William's Inventory :");
System.out.print("Danger Level: ");
String dangerLevelInput = scanner.nextLine();
Integer dangerLevel = null;
if (!dangerLevelInput.equalsIgnoreCase("0")) {
    try {
        dangerLevel = Integer.parseInt(dangerLevelInput);
    } catch (NumberFormatException e) {
        System.out.println("Invalid danger level input. Searching without danger level filter.");
    }
}

                    System.out.print("Magic Type: ");
                    String magicType = scanner.nextLine();
                    if (magicType.equalsIgnoreCase("skip")) {
                        magicType = null;
                    }
                    System.out.print("Price: ");
                    String priceInput = scanner.nextLine();
                    Double price = null;
                    if (!priceInput.equalsIgnoreCase("skip")) {
                        try {
                            price = Double.parseDouble(priceInput);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid price input. Searching without price filter.");
                        }
                    }

                    // Perform the search with the provided filters
                    List<String> searchResult = dal2.searchInventory(dbName, dangerLevel, magicType, price, DataMGR.getUsername(), DataMGR.getPassword());

                    // Display search result
                    if (searchResult.isEmpty()) {
                        System.out.println("No items found matching the search criteria.");
                    } else {
                        System.out.println("Search Result:");
                        for (String item : searchResult) {
                            System.out.println(item); // Assuming Item class has overridden toString() method
                        }
                    }
                    break;

                case 3:
                    // Print sales report for a specific month
                    System.out.print("Enter the month for which you would like to print out a sales report (1-12): ");
                    int month = scanner.nextInt();
                    Dal dal3 = new Dal();
                    dal3.salesReport(dbName, DataMGR.getUsername(), DataMGR.getPassword(), month);
                    break;

                case 4:
                    // Close connections
                    closeConnections();
                    break;

                case 5:
                    running = false;
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
        scanner.close();
    }

  

    public void promptForItemDetails(String itemname){
        Scanner Scanner2 = new Scanner(System.in);

        System.out.println(itemname+"does not exist please enter new item details");
        System.out.println("Enter the quanity for "+itemname);
       String quanity= Scanner2.nextLine();
        System.out.println("Enter the description for "+itemname);
        System.out.println("Enter the danger level for "+itemname);
        System.out.println("Enter the Sale price for "+itemname);
        System.out.println("Enter the purchase price for "+itemname);
        System.out.println("Enter the magic type for "+itemname);

    }
  // Method to close all cached connections
  private static void closeConnections() {
    DataMGR.closeConnections();
}

}
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class favoritesList {
    private static List<String> favorites;
    private static final String FILENAME = "favorites.txt";

    // Private constructor to prevent instantiation from outside
    private favoritesList() {
        // Empty constructor
    }

    // Initialize favorites list and load from file
    static {
        favorites = loadFavoritesFromFile();
    }

    // Add a favorite restaurant
    public static void addFavorite(String favorite, String username) {
        favorites.add(favorite);
        saveFavoritesToFile(username);
    }

    // Remove a favorite restaurant
    public static void removeFavorite(String favorite, String username) {
        favorites.remove(favorite);
        saveFavoritesToFile(username);
    }

    // Get the list of favorite restaurants
    public static List<String> getFavorites() {
        return new ArrayList<>(favorites);
    }

    // Save favorites to a file
    private static void saveFavoritesToFile(String username) {
        try {
            Path userDir = Paths.get("userdata", username);
            if (!Files.exists(userDir)) {
                Files.createDirectories(userDir);
            }
            Path filePath = userDir.resolve(FILENAME);
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
                for (String favorite : favorites) {
                    writer.println(favorite);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load favorites from a file
    private static List<String> loadFavoritesFromFile() {
        List<String> loadedFavorites = new ArrayList<>();
        try {
            Path filePath = Paths.get(FILENAME);
            if (Files.exists(filePath)) {
                List<String> lines = Files.readAllLines(filePath);
                loadedFavorites.addAll(lines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedFavorites;
    }
}

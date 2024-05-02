import java.util.ArrayList;
import java.util.List;

public class favoritesList {
    private static List<String> favorites = new ArrayList<>();

    // Private constructor to prevent instantiation from outside
    private favoritesList() {
        // Empty constructor
    }

    // Add a favorite restaurant
    public static void addFavorite(String favorite) {
        favorites.add(favorite);
    }

    // Remove a favorite restaurant
    public static void removeFavorite(String favorite) {
        favorites.remove(favorite);
    }

    // Get the list of favorite restaurants
    public static List<String> getFavorites() {
        return favorites;
    }
}

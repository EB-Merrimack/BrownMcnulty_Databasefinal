import java.util.ArrayList;
import java.util.List;

public class favoritesList {
    private static List<String> favorites;

    public favoritesList() {
        this.favorites = new ArrayList<>();
    }

    public void addFavorite(String favorite) {
        favorites.add(favorite);
    }

    public void removeFavorite(String favorite) {
        favorites.remove(favorite);
    }

    // Other methods for manipulating the favorites list...

    public List<String> getFavorites() {
        return favorites;
    }

    // Implement the add method
    public static void add(String restaurant) {
        favorites.add(restaurant);
    }
}

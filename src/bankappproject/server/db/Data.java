package bankappproject.server.db;

import bankappproject.models.user.User;
import java.util.TreeMap;

public class Data {

    private static Data instance;

    private final TreeMap<String, User> database;

    private Data() {
        this.database = new TreeMap<>();
    }

    public static synchronized Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    public TreeMap<String, User> getDatabase() {
        return database;
    }
}

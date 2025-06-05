package bankappproject.server.db;

import bankappproject.models.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Data {

    private static Data instance;

    private final TreeMap<String, User> storage;

    private Data() {
        this.storage = new TreeMap<>();
    }

    public static synchronized Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    // Devuelve acceso directo al mapa si se necesita externamente (uso avanzado)
    public TreeMap<String, User> getDatabase() {
        return storage;
    }

    // === CRUD de Usuario ===

    public void guardarUsuario(User user) {
        storage.put(user.getId(), user);
    }

    public void actualizarUsuario(String id, User user) {
        if (storage.containsKey(id)) {
            storage.put(id, user);
        }
    }

    public void eliminarUsuario(String id) {
        storage.remove(id);
    }

    public User buscarUsuarioPorId(String id) {
        return storage.get(id);
    }

    public List<User> listarUsuarios() {
        return new ArrayList<>(storage.values());
    }

    public boolean existeUsuario(String id) {
        return storage.containsKey(id);
    }
}

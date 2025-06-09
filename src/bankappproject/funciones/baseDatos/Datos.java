package bankappproject.funciones.baseDatos;

import bankappproject.modelos.usuario.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Datos {

    private static Datos instance;

    private final TreeMap<String, Usuario> storage;

    private Datos() {
        this.storage = new TreeMap<>();
    }

    public static synchronized Datos getInstance() {
        if (instance == null) {
            instance = new Datos();
        }
        return instance;
    }

    // Devuelve acceso directo al mapa si se necesita externamente (uso avanzado)
    public TreeMap<String, Usuario> getDatabase() {
        return storage;
    }

    // === CRUD de Usuario ===

    public void guardarUsuario(Usuario user) {
        storage.put(user.getId(), user);
    }

    public void actualizarUsuario(String id, Usuario user) {
        if (storage.containsKey(id)) {
            storage.put(id, user);
        }
    }

    public void eliminarUsuario(String id) {
        storage.remove(id);
    }

    public Usuario buscarUsuarioPorId(String id) {
        return storage.get(id);
    }

    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(storage.values());
    }

    public boolean existeUsuario(String id) {
        return storage.containsKey(id);
    }
}

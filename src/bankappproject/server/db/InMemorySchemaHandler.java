package bankappproject.server.db;

import java.util.*;
import java.util.stream.Collectors;

public abstract class InMemorySchemaHandler<T> {

    protected final Map<String, T> storage = new TreeMap<>();
    protected final Class<T> clazz;

    public InMemorySchemaHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void save(T item) {
        String id = getId(item);
        storage.put(id, item);
    }

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public T findById(String id) {
        return storage.get(id);
    }

    public void update(String id, T newItem) {
        if (storage.containsKey(id)) {
            storage.put(id, newItem);
        }
    }

    public void delete(String id) {
        storage.remove(id);
    }

    protected abstract String getId(T item);
}

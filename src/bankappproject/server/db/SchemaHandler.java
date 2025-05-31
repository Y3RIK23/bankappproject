package bankappproject.server.db;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public abstract class SchemaHandler<T> {

    protected final Class<T> clazz;
    protected final JsonTransformer transformer;
    protected List<T> data;

    public SchemaHandler(Class<T> clazz, String filePath) {
        this.clazz = clazz;
        this.transformer = new JsonTransformer(filePath);
        this.data = new ArrayList<>();

        try {
            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            List<T> loaded = transformer.read(listType);
            if (loaded != null) {
                this.data = loaded;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Guardar o actualizar un objeto por su ID
    public void save(T item) {
        String id = getId(item);
        boolean found = false;

        for (int i = 0; i < data.size(); i++) {
            if (getId(data.get(i)).equals(id)) {
                data.set(i, item); // Update
                found = true;
                break;
            }
        }

        if (!found) {
            data.add(item); // Insert
        }

        persist();
    }

    public List<T> findAll() {
        return new ArrayList<>(data);
    }

    public T findById(String id) {
        return data.stream()
                .filter(item -> getId(item).equals(id))
                .findFirst()
                .orElse(null);
    }

    public void update(String id, T newItem) {
        for (int i = 0; i < data.size(); i++) {
            if (getId(data.get(i)).equals(id)) {
                data.set(i, newItem);
                persist();
                return;
            }
        }
    }

    public void delete(String id) {
        data = data.stream()
                .filter(item -> !getId(item).equals(id))
                .collect(Collectors.toList());
        persist();
    }

    // Renombrado para evitar confusión con save(T item)
    protected void persist() {
        try {
            transformer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Este método debe ser implementado para obtener el ID del objeto
    protected abstract String getId(T item);
}

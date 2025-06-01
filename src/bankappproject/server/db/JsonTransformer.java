package bankappproject.server.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;

public class JsonTransformer {

    private final File file;
    private final Gson gson;

    public JsonTransformer(String filePath) {
        this.file = new File(filePath);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    // Método para escribir un objeto genérico a archivo JSON
    public <T> void write(T object) throws IOException {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(object, writer);
        }
    }

    // Método para leer un objeto genérico desde archivo JSON
    public <T> T read(Type typeOfT) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        }
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, typeOfT);
        }
    }
}

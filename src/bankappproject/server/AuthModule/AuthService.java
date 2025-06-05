package bankappproject.server.AuthModule;

import bankappproject.models.user.User;
import bankappproject.server.db.Data;

public class AuthService {

    private final Data data = Data.getInstance();

    public String login(LoginDTO loginData) throws IllegalArgumentException {
        User user = data.buscarUsuarioPorId(loginData.cedula);

        if (user == null) {
            throw new IllegalArgumentException(String.format("Usuario %s no existe", loginData.cedula));
        }

        if (!user.getPassword().equals(loginData.contraseña)) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        if (user.isAlreadyActive()) {
            throw new IllegalArgumentException("Este usuario ya tiene una sesión activa");
        }

        user.setAlreadyActive(true);
        data.guardarUsuario(user);

        return String.format("Bienvenido %s", user.getId());
    }

    public String logout(String cedula) throws IllegalArgumentException {
        User user = data.buscarUsuarioPorId(cedula);

        if (user == null) {
            throw new IllegalArgumentException(String.format("Usuario %s no existe", cedula));
        }

        if (!user.isAlreadyActive()) {
            throw new IllegalArgumentException("Este usuario no tiene una sesión activa");
        }

        user.setAlreadyActive(false);
        data.guardarUsuario(user);

        return String.format("Cerrando sesión de %s", user.getId());
    }

    public String register(User user) throws IllegalArgumentException {
        data.guardarUsuario(user);
        return String.format("Usuario %s registrado correctamente", user.getId());
    }
}

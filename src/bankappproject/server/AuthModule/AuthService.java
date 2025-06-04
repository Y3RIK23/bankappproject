package bankappproject.server.AuthModule;

import bankappproject.models.user.User;

public class AuthService {

    private final UserSchema userHandler = new UserSchema();

    public String login(LoginDTO loginData) throws IllegalArgumentException {
        User user = userHandler.findById(loginData.cedula);

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
        userHandler.save(user);

        return String.format("Bienvenido %s", user.getId());
    }

    public String logout(String cedula) throws IllegalArgumentException {
        User user = userHandler.findById(cedula);

        if (user == null) {
            throw new IllegalArgumentException(String.format("Usuario %s no existe", cedula));
        }

        if (!user.isAlreadyActive()) {
            throw new IllegalArgumentException("Este usuario no tiene una sesión activa");
        }

        user.setAlreadyActive(false);
        userHandler.save(user);

        return String.format("Cerrando sesión de %s", user.getId());
    }

    public String register(User user) throws IllegalArgumentException {
        if (userHandler.findById(user.getId()) != null) {
            throw new IllegalArgumentException(String.format("Usuario %s ya existe", user.getId()));
        }

        user.setAlreadyActive(false);
        userHandler.save(user);

        return String.format("Usuario %s registrado correctamente", user.getId());
    }
}

package bankappproject.funciones.autenticacion;

import bankappproject.modelos.usuario.Usuario;
import bankappproject.funciones.baseDatos.Datos;

public class Autenticacion {

    private final Datos datos = Datos.getInstance();

    public String inicioSesion(InicioSesionDTO loginData) throws IllegalArgumentException {
        Usuario user = datos.buscarUsuarioPorId(loginData.cedula);

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
        datos.guardarUsuario(user);

        return String.format("Bienvenido %s", user.getId());
    }

    public String logout(String cedula) throws IllegalArgumentException {
        Usuario user = datos.buscarUsuarioPorId(cedula);

        if (user == null) {
            throw new IllegalArgumentException(String.format("Usuario %s no existe", cedula));
        }

        if (!user.isAlreadyActive()) {
            throw new IllegalArgumentException("Este usuario no tiene una sesión activa");
        }

        user.setAlreadyActive(false);
        datos.guardarUsuario(user);

        return String.format("Cerrando sesión de %s", user.getId());
    }

    public String register(Usuario user) throws IllegalArgumentException {
        datos.guardarUsuario(user);
        return String.format("Usuario %s registrado correctamente", user.getId());
    }
}

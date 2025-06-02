/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.server.AuthModule;

import bankappproject.models.user.User;

/**
 *
 * @author Gigabyte
 */
public class AuthService {

    UserSchema userSchema = new UserSchema("./users.json");

    public String login(LoginDTO loginData) throws IllegalArgumentException {

        
        User existUser = userSchema.findById(loginData.cedula);

        if (existUser == null) {
            throw new IllegalArgumentException(String.format("Usuario %s no existe", "nombredeusuario"));
        }

        if (!existUser.getPassword().equals(loginData.contraseña)) {
            throw new IllegalArgumentException(String.format("Usuario %s no existe", "nombredeusuario"));
        }

        if (existUser.isAlreadyActive()) {
            throw new IllegalArgumentException("Este usuario ya tiene una sesión activa");
        } else {

            existUser.setAlreadyActive(true);
            
            userSchema.save(existUser);
        }

        return String.format("Bienvenido %s", existUser.getId());

    }

    public String logout(String cedula) throws IllegalArgumentException {

        User existUser = userSchema.findById(cedula);

        if (existUser == null) {
            throw new IllegalArgumentException(String.format("Usuario %s no existe", "nombredeusuario"));
        }

        if (!existUser.isAlreadyActive()) {
            throw new IllegalArgumentException("Este usuario no tiene una sesión activa");
        } else {

            existUser.setAlreadyActive(false);
            
            userSchema.save(existUser);
        }

        return String.format("Cerrando sesión de %s", existUser.getId());

    }

    public String register(User user) throws IllegalArgumentException {

        if (userSchema.findById(user.getId()) != null) {
            throw new IllegalArgumentException(String.format("Usuario %s ya existe", user.getId()));
        }

        user.setAlreadyActive(false);
        userSchema.save(user);

        return String.format("Usuario %s registrado correctamente", user.getId());
    }

}

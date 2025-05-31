/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.server.AuthModule;

/**
 *
 * @author Gigabyte
 */
public class AuthService {

    public void login(LoginDTO loginData) throws IllegalArgumentException {

        // TODO: consultar si existe el usuario por cédula
        // Mockeado a true, resultado de la funcion de buscar por cédula
        boolean existUser = true;

        // Mockeado a false, comparador de contraseñas
        boolean passwordMatch = false;
        
        
        // Mockeado a false, usar checkear si el usuario tiene la 
        boolean isAlreadyActive = false;

        if (!existUser) {
            throw new IllegalArgumentException(String.format("Usuario %s no existe", "nombredeusuario"));
        }
        if (!passwordMatch) {
            throw new IllegalArgumentException(String.format("Usuario %s no existe", "nombredeusuario"));
        }
        
        if(isAlreadyActive) {
            throw new IllegalArgumentException("Este usuario ya tiene una sesión activa");
        }
        
        else {
            
            // cambiar sesión de usuario a true
            
            // escribir el array en el JSON correspondiente
            
        }

    }

}

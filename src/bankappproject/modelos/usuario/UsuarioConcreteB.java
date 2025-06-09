/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.modelos.usuario;
import bankappproject.modelos.usuario.Usuario;
import bankappproject.funciones.baseDatos.Datos;

import java.util.ArrayList;

/**
 *
 * @author Gigabyte
 */
public class UsuarioConcreteB implements UsuarioAbstractB{

    Usuario user;
    Datos database = Datos.getInstance();
    
    public UsuarioConcreteB() {
        
        user = new Usuario();
        
    }

    @Override
    public void buildID(String ID) throws ExcepcionUsuario{
        
        if(database.existeUsuario(ID)) {
            throw new ExcepcionUsuario("Este usuario ya existe");
        }
        
        if (!ID.matches("\\d+")) throw new ExcepcionUsuario (
                "El ID solo puede contener numeros");
        
        if (ID.length() < 9) throw new ExcepcionUsuario(
                "El ID debe estar compuesto por 9 caracteres");
        
        user.setId(ID);

    }

    @Override
    public void buildPassword(String password) throws ExcepcionUsuario{

        if ((password.length() < 4 || password.length() > 10))
            throw new ExcepcionUsuario(
                    "La contraseña debe contar con almenos: "
                            + "\n.4 caracteres y maximo 10");
        
        if (!password.matches("[a-zA-Z0-9_-]+")) throw new  ExcepcionUsuario(
                "La contraseña solo puede contener: "
                        + "\n.Números, "
                        + "\n.Letras, "
                        + "\n.Guiones simples o bajos");
        
        user.setPassword(password);
        
    }

    @Override
    public Usuario getUser() {
        
        user.setBankAccounts(new ArrayList<>());
        return user;
        
    }

    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.models.user;
import bankappproject.models.bankAccount.BankAccount;
import bankappproject.models.user.User;

import java.util.ArrayList;

/**
 *
 * @author Gigabyte
 */
public class UserConcreteBuilder implements UserAbstractBuilder{

    User user;
    
    public UserConcreteBuilder() {
        
        user = new User();
        
    }

    @Override
    public void buildID(String ID) throws UserException{

        // FALTA VALIDAR QUE NO EXISTA UN USUARIO CON EL ID RECIBIDO // 
        
        if (!ID.matches("\\d+")) throw new UserException (
                "El ID solo puede contener numeros");
        
        if (ID.length() < 9) throw new UserException(
                "El ID debe estar compuesto por 9 caracteres");
        
        user.setId(ID);

    }

    @Override
    public void buildPassword(String password) throws UserException{

        if (!(password.length() >= 4 && password.length() >= 10))
            throw new UserException(
                    "La contraseña debe contar con almenos: "
                            + "\n.4 caracteres y maximo 10");
        
        if (!password.matches("[a-zA-Z0-9_-]+")) throw new  UserException(
                "La contraseña solo puede contener: "
                        + "\n.Números, "
                        + "\n.Letras, "
                        + "\n.Guiones simples o bajos");
        
        user.setPassword(password);
        
    }

    @Override
    public User getUser() {
        
        user.setBankAccounts(new ArrayList<>());
        // FALTA AGREGAR USER A LA LISTA DE USUARIOS //
        return user;
        
    }
    
}

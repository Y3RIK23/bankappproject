/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.models.user;

/**
 *
 * @author mejia
 */
public class UserDirector {
    
    public User construirAlerta (UserAbstractBuilder builder, 
            String ID, String password) 
            throws UserException{
                
        builder.buildID(ID);
        builder.buildPassword(password);
        
        return builder.getUser();
    
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package bankappproject.models.user;

/**
 *
 * @author mejia
 */
public interface UserAbstractBuilder {
    
    void buildID (String ID) throws UserException;
    
    void buildPassword (String password) throws UserException;
    
    User getUser ();
    
}

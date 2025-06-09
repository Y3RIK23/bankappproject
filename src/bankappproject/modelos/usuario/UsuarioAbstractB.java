/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package bankappproject.modelos.usuario;

/**
 *
 * @author mejia
 */
public interface UsuarioAbstractB {
    
    void buildID (String ID) throws ExcepcionUsuario;
    
    void buildPassword (String password) throws ExcepcionUsuario;
    
    Usuario getUser ();
    
}

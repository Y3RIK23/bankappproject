/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.modelos.usuario;

/**
 *
 * @author mejia
 */
public class UsuarioDirector {
    
    public Usuario construirAlerta (UsuarioAbstractB builder, 
            String ID, String password) 
            throws ExcepcionUsuario{
                
        builder.buildID(ID);
        builder.buildPassword(password);
        
        return builder.getUser();
    
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.server.AuthModule;

import bankappproject.models.user.User;
import bankappproject.server.db.SchemaHandler;

/**
 *
 * @author Gigabyte
 */
public class UserSchema extends SchemaHandler<User> {

    public UserSchema(String filePath) {
        super(User.class, filePath);
    }

    @Override
    protected String getId(User user) {
        return user.getId();
    }
    
}

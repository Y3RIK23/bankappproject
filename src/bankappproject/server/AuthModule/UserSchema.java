package bankappproject.server.AuthModule;

import bankappproject.models.user.User;
import bankappproject.server.db.InMemorySchemaHandler;

public class UserSchema extends InMemorySchemaHandler<User> {

    public UserSchema() {
        super(User.class);
    }

    @Override
    protected String getId(User item) {
        return item.getId(); // O el método que corresponda
    }
}

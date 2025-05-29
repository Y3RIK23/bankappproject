/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.models.user;
import bankappproject.models.bankaccount.BankAccount;
import bankappproject.models.user.User;

import java.util.ArrayList;

/**
 *
 * @author Gigabyte
 */
public class UserBuilder {

    String name;
    String id;
    ArrayList<BankAccount> cuentasBancarias = new ArrayList<>();
    boolean alreadyActive;

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public UserBuilder setCuentasBancarias(ArrayList<BankAccount> cuentasBancarias) {
        this.cuentasBancarias = cuentasBancarias;
        return this;
    }

    public UserBuilder setAlreadyActive(boolean alreadyActive) {
        this.alreadyActive = alreadyActive;
        return this;
    }

    public User build() {
        return new User(this);
    }
}

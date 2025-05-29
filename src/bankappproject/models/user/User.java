/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.models.user;

import bankappproject.models.bankaccount.BankAccount;
import bankappproject.models.user.UserBuilder;

import java.util.ArrayList;

/**
 *
 * @author Gigabyte
 */
public class User {

    private String name;
    private String id;

    //Referencia a cuentas bancarias en base de datos
    private ArrayList<BankAccount> cuentasBancarias;

    private boolean alreadyActive;

    User(UserBuilder builder) {
        this.name = builder.name;
        this.id = builder.id;
        this.cuentasBancarias = builder.cuentasBancarias;
        this.alreadyActive = builder.alreadyActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<BankAccount> getCuentasBancarias() {
        return cuentasBancarias;
    }

    public void setCuentasBancarias(ArrayList<BankAccount> cuentasBancarias) {
        this.cuentasBancarias = cuentasBancarias;
    }

    public boolean isAlreadyActive() {
        return alreadyActive;
    }

    public void setAlreadyActive(boolean alreadyActive) {
        this.alreadyActive = alreadyActive;
    }

}

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
    private String password;
    //Referencia a cuentas bancarias en base de datos
    private ArrayList<BankAccount> bankAccounts;

    private boolean alreadyActive;

    User(UserBuilder builder) {
        this.name = builder.name;
        this.id = builder.id;
        this.bankAccounts = builder.cuentasBancarias;
        this.alreadyActive = builder.alreadyActive;
        this.password = builder.password;
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

    public ArrayList<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(ArrayList<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public boolean isAlreadyActive() {
        return alreadyActive;
    }

    public void setAlreadyActive(boolean alreadyActive) {
        this.alreadyActive = alreadyActive;
    }
    
     public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" + "name=" + name + ", id=" + id + ", password=" + password + ", bankAccounts=" + bankAccounts + ", alreadyActive=" + alreadyActive + '}';
    }        
    
}

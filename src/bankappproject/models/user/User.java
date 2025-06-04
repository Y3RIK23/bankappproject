/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.models.user;

import bankappproject.models.bankAccount.BankAccount;

import java.util.ArrayList;

/**
 *
 * @author Gigabyte
 */
public class User {    
    private String id;
    private String password;
    private ArrayList<BankAccount> bankAccounts;

    private boolean alreadyActive;

    User() {
        
    }

    public ArrayList<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

   
    public void setBankAccounts(ArrayList<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public boolean isAlreadyActive() {
        return alreadyActive;
    }

    public String getPassword() {
        return password;
    }

    public void setAlreadyActive(boolean alreadyActive) {
        this.alreadyActive = alreadyActive;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", password=" + password + ", bankAccounts=" + bankAccounts + ", alreadyActive=" + alreadyActive + '}';
    }        
    
}

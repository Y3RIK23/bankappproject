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

    //Referencia a cuentas bancarias en base de datos
    private ArrayList<BankAccount> bankAccounts;
    
    private String id;
    private String password;
    
    private boolean alreadyActive; //Cambia su estado con cada inicio o cierre de sesion

    public User() {                
        
    }

    public ArrayList<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAlreadyActive() {
        return alreadyActive;
    }

    public void setBankAccounts(ArrayList<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAlreadyActive(boolean alreadyActive) {
        this.alreadyActive = alreadyActive;
    }       
    
    @Override
    public String toString() {
        return "User{" + 
                "bankAccounts=" + bankAccounts + 
                ", id=" + id + 
                ", password=" + password + 
                ", alreadyActive=" + alreadyActive + '}';
    }    
    
}

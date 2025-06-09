/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.modelos.usuario;

import bankappproject.modelos.CuentaBancaria;

import java.util.ArrayList;

/**
 *
 * @author Gigabyte
 */
public class Usuario {   
    
    private String id;
    private String password;
    private ArrayList<CuentaBancaria> bankAccounts;

    private boolean alreadyActive;

    Usuario() {
        
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<CuentaBancaria> getBankAccounts() {
        return bankAccounts;
    }

    public boolean isAlreadyActive() {
        return alreadyActive;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBankAccounts(ArrayList<CuentaBancaria> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public void setAlreadyActive(boolean alreadyActive) {
        this.alreadyActive = alreadyActive;
    }
    
    @Override
    public String toString() {
        return "User{id=" + id + ", password=" + password + ", bankAccounts=" + bankAccounts + ", alreadyActive=" + alreadyActive + '}';
    }        
    
}

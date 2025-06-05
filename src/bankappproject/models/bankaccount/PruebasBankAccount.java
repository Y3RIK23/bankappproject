/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.models.bankaccount;

import bankappproject.models.bankAccount.Transaction;
import java.util.ArrayList;
import java.util.Date;
import bankappproject.models.bankAccount.BankAccount;

/**
 *
 * @author Usuario
 */
public class PruebasBankAccount {
    public static void main (String[] args){
     
        Date date1 = new Date();
        Transaction transaction1 = new Transaction(date1, Transaction.TransaccionType.INVERSION, 1000);
        Transaction transaction2 = new Transaction(date1, Transaction.TransaccionType.DEPOSITO, 2000);
        Transaction transaction3 = new Transaction(date1, Transaction.TransaccionType.RETIRO, 3000);
        
        ArrayList<Transaction> cuenta1 = new ArrayList<>();
        
        cuenta1.add(transaction3);
        cuenta1.add(transaction2);
        cuenta1.add(transaction1);
        
        BankAccount cuenta = new BankAccount();
        cuenta.addSaldoDisponible(10000);
        cuenta.setEstadoCuenta(cuenta1);
        
        System.out.println(cuenta.estadoCuenta());

    }
}

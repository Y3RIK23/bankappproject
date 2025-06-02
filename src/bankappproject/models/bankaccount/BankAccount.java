package bankappproject.models.bankAccount;

import java.util.ArrayList;

public class BankAccount {
    
    private static int numCuentas = 1;
    
    private ArrayList<Transaction> estadoCuenta;
    private String numeroCuenta;
    private double saldoDisponible;

    public BankAccount() {
        
        this.saldoDisponible = 0.0;
        this.numeroCuenta = numCuentas++ + "";
        
        for (int i = numeroCuenta.length(); i < 10; i++) {
            
            this.numeroCuenta += "0";
            
        }                
        
    }

    public ArrayList<Transaction> getEstadoCuenta() {
        return estadoCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public double getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setEstadoCuenta(ArrayList<Transaction> estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void setSaldoDisponible(double saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }
    
    // Se puede modificar para que funcione con el estado de cuenta //
    @Override
    public String toString() {
        return "BankAccount{" + 
                "numeroCuenta=" + numeroCuenta + 
                ", saldoDisponible=" + saldoDisponible + '}';
    }   

}

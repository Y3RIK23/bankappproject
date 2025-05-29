package bankappproject.models.bankaccount;

import bankappproject.models.transactions.Transaction;
import java.util.ArrayList;
import java.util.UUID;

public class BankAccount {

    private String userCedula;
    private String numeroCuenta;
    private int saldoDisponible;
    private ArrayList<String> transacciones;

    // Constructor privado: solo accesible a través del Builder
    private BankAccount(Builder builder) {
        this.userCedula = builder.userCedula;
        this.numeroCuenta = builder.numeroCuenta != null ? builder.numeroCuenta : UUID.randomUUID().toString();
        this.saldoDisponible = builder.saldoDisponible;
        this.transacciones = builder.transacciones != null ? builder.transacciones : new ArrayList<>();
    }

    // Getters
    public String getUserCedula() {
        return userCedula;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public int getSaldoDisponible() {
        return saldoDisponible;
    }

    public ArrayList<String> getTransacciones() {
        // TODO: Poblar desde la base de datos si es necesario
        return transacciones;
    }

    // Métodos para modificar estado
    public void agregarTransaccion(Transaction transaction) {
        transacciones.add(transaction.getId());
    }

    public void actualizarSaldo(int nuevoSaldo) {
        this.saldoDisponible = nuevoSaldo;
    }

    // Builder estático
    public static class Builder {
        private String userCedula;
        private String numeroCuenta; // opcional
        private int saldoDisponible;
        private ArrayList<String> transacciones;

        public Builder setUserCedula(String userCedula) {
            this.userCedula = userCedula;
            return this;
        }

        public Builder setNumeroCuenta(String numeroCuenta) {
            this.numeroCuenta = numeroCuenta;
            return this;
        }

        public Builder setSaldoDisponible(int saldoDisponible) {
            this.saldoDisponible = saldoDisponible;
            return this;
        }

        public Builder setTransacciones(ArrayList<String> transacciones) {
            this.transacciones = transacciones;
            return this;
        }

        public BankAccount build() {
            return new BankAccount(this);
        }
    }
}

package bankappproject.models.bankaccount;

import bankappproject.models.transactions.Transaction;
import java.util.ArrayList;
import java.util.UUID;

public class BankAccount {

    private String userID;
    private String accountNumber;
    private int balance;
    private ArrayList<String> transactions;

    // Constructor privado: solo accesible a través del Builder
    private BankAccount(Builder builder) {
        
        this.userID = 
                builder.userID;
        this.accountNumber = 
                builder.accountNumber != null ? builder.accountNumber : UUID.randomUUID().toString();
        this.balance = 
                builder.balance;
        this.transactions = 
                builder.transactions != null ? builder.transactions : new ArrayList<>();
    
    }

    // Getters
    public String getUserID() {
        return userID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getBalance() {
        return balance;
    }

    public ArrayList<String> getTransactions() {
        // TODO: Poblar desde la base de datos si es necesario
        return transactions;
    }

    // Métodos para modificar estado
    public void agregarTransaccion(Transaction transaction) {
        transactions.add(transaction.getId());
    }

    public void actualizarSaldo(int newBalance) {
        this.balance = newBalance;
    }

    // Builder estático
    public static class Builder {
        
        private String userID;
        private String accountNumber; // opcional
        private int balance;
        private ArrayList<String> transactions;

        public Builder setUserID(String userID) {
            this.userID = userID;
            return this;
        }

        public Builder setNumberAccount(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder setBalance(int balance) {
            this.balance = balance;
            return this;
        }

        public Builder setTransactions(ArrayList<String> transactions) {
            this.transactions = transactions;
            return this;
        }

        public BankAccount build() {
            return new BankAccount(this);
        }
    }
}

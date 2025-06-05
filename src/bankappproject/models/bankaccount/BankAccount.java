package bankappproject.models.bankAccount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BankAccount {

    private static int numCuentas = 1;

    private ArrayList<Transaction> estadoCuenta;
    private String numeroCuenta;
    private double saldoDisponible;

    public BankAccount() {

        this.saldoDisponible = 0.0;
        this.numeroCuenta = numCuentas++ + "";
        this.estadoCuenta = new ArrayList<>();
        
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

    public void addTransaccion(Transaction transaction) {
        this.estadoCuenta.add(transaction);
    }

    public void addSaldoDisponible(double saldoDisponible) {
        this.saldoDisponible += saldoDisponible;
    }

    public String estadoCuenta() {

        StringBuilder sb = new StringBuilder();

        sb.append("\nCuenta: " + getNumeroCuenta() + " - Saldo: " + getSaldoDisponible());
        sb.append(String.format("\n\n%-15s %-15s %15s %15s%n", "Fecha", "Transaccion", "Debito", "Credito"));

        double debitos = 0;
        double creditos = 0;

        for (Transaction transaction : estadoCuenta) {

            if (transaction.getTipoTransaccion() == Transaction.TransaccionType.DEPOSITO) {

                sb.append(String.format(
                        "%-15s %-15s %-15s %15.2f %n",
                        new SimpleDateFormat("dd/mm/yy").format(transaction.getFecha()),
                        transaction.getTransactionType(),
                        "",
                        transaction.getMonto()));

                creditos += transaction.getMonto();

            } else {
                sb.append(String.format(
                        "%-15s %-15s %15.2f %-15s %n",
                        new SimpleDateFormat("dd/mm/yy").format(transaction.getFecha()),
                        transaction.getTransactionType(),
                        transaction.getMonto(),
                        ""));

                debitos += transaction.getMonto();

            }

        }

        sb.append(String.format("\n%-15s %-15s %15.2f %-15s %n", "Total Debitos:", "", debitos, ""));
        sb.append(String.format("%-15s %-15s %-15s %15.2f %n", "Total Creditos:", "", "", creditos));

        return sb.toString();

    }

    // Se puede modificar para que funcione con el estado de cuenta //
    @Override
    public String toString() {
        return "BankAccount{"
                + "numeroCuenta=" + numeroCuenta
                + ", saldoDisponible=" + saldoDisponible + '}';
    }

}

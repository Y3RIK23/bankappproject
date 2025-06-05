package bankappproject.models.bankAccount;

import java.util.Date;

public class Transaction {

    public enum TransaccionType {
        RETIRO,
        DEPOSITO,
        INVERSION

    }

    private static int numCredito = 1;
    private static int numDebito = 1;
    private static int numCertificado = 1;

    private Date fecha;
    private String transactionType;
    private double monto;
    private TransaccionType tipoTransaccion;

    public Transaction(
            Date fecha, TransaccionType transaccionType, double monto) {

        if (transaccionType == TransaccionType.DEPOSITO) {
            this.transactionType = "Credito " + numCredito++;

        } else if (transaccionType == TransaccionType.RETIRO) {
            this.transactionType = "Debito " + numDebito++;

        } else {
            this.transactionType = "Certificado " + numCertificado++;

        }

        this.fecha = fecha;
        this.monto = monto;
        this.tipoTransaccion = transaccionType;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public double getMonto() {
        return monto;
    }

    public TransaccionType getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "Transaction{"
                + "fecha=" + fecha
                + ", transaccionType=" + transactionType
                + ", monto=" + monto + '}';
    }

}

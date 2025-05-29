package bankappproject.models.transactions;

import java.util.Date;
import java.util.UUID;

public class Transaction {

    public enum TransaccionType {
        RETIRO,
        DEPOSITO,
        INVERSION
    }

    private String id;
    private String cuenta;
    private Date fecha;
    private TransaccionType transaccionType;
    private int debito;
    private int credito;

    // Constructor privado para el patrón Builder
    private Transaction(Builder builder) {
        this.id = UUID.randomUUID().toString();
        this.cuenta = builder.cuenta;
        this.fecha = builder.fecha;
        this.transaccionType = builder.transaccionType;
        this.debito = builder.debito;
        this.credito = builder.credito;
    }

    // Getters
    public String getCuenta() {
        return cuenta;
    }

    public Date getFecha() {
        return fecha;
    }

    public TransaccionType getTransaccionType() {
        return transaccionType;
    }

    public int getDebito() {
        return debito;
    }

    public int getCredito() {
        return credito;
    }

    public String getId() {
        return id;
    }

    // Builder estático
    public static class Builder {

        private String cuenta;
        private Date fecha;
        private TransaccionType transaccionType;
        private int debito;
        private int credito;

        public Builder setCuenta(String cuenta) {
            this.cuenta = cuenta;
            return this;
        }

        public Builder setFecha(Date fecha) {
            this.fecha = fecha;
            return this;
        }

        public Builder setTransaccionType(TransaccionType transaccionType) {
            this.transaccionType = transaccionType;
            return this;
        }

        public Builder setDebito(int debito) {
            this.debito = debito;
            return this;
        }

        public Builder setCredito(int credito) {
            this.credito = credito;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}

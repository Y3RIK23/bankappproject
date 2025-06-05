package bankappproject.server.TransactionsService;

import bankappproject.models.bankAccount.BankAccount;
import bankappproject.models.bankAccount.Transaction;
import bankappproject.models.user.User;
import bankappproject.server.db.Data;

import java.util.ArrayList;
import java.util.Date;

public class TransactionsService {

    private final Data data = Data.getInstance();

    public String deposit(TransactionDTO dto) throws IllegalArgumentException {
        if (dto.amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        User user = data.buscarUsuarioPorId(dto.userId);
        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        BankAccount cuenta = buscarCuentaPorNumero(user, dto.accountNumber);
        if (cuenta == null) {
            throw new IllegalArgumentException("Cuenta no encontrada para el usuario");
        }

        double nuevoSaldo = cuenta.getSaldoDisponible() + dto.amount;
        cuenta.setSaldoDisponible(nuevoSaldo);

        if (cuenta.getEstadoCuenta() == null) {
            cuenta.setEstadoCuenta(new ArrayList<>());
        }

        Transaction transaccion = new Transaction(new Date(), Transaction.TransaccionType.DEPOSITO, dto.amount);
        cuenta.getEstadoCuenta().add(transaccion);

        data.guardarUsuario(user);

        return String.format("Depósito exitoso. Nuevo saldo: %.2f en la cuenta %s", nuevoSaldo, cuenta.getNumeroCuenta());
    }

    public String withdraw(TransactionDTO dto) throws IllegalArgumentException {
        if (dto.amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        User user = data.buscarUsuarioPorId(dto.userId);
        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        BankAccount cuenta = buscarCuentaPorNumero(user, dto.accountNumber);
        if (cuenta == null) {
            throw new IllegalArgumentException("Cuenta no encontrada para el usuario");
        }

        if (cuenta.getSaldoDisponible() < dto.amount) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        double nuevoSaldo = cuenta.getSaldoDisponible() - dto.amount;
        cuenta.setSaldoDisponible(nuevoSaldo);

        if (cuenta.getEstadoCuenta() == null) {
            cuenta.setEstadoCuenta(new ArrayList<>());
        }

        Transaction transaccion = new Transaction(new Date(), Transaction.TransaccionType.RETIRO, dto.amount);
        cuenta.getEstadoCuenta().add(transaccion);

        data.guardarUsuario(user);

        return String.format("Retiro exitoso. Nuevo saldo: %.2f en la cuenta %s", nuevoSaldo, cuenta.getNumeroCuenta());
    }

    // Método auxiliar para encontrar una cuenta por número sin usar Optional ni Stream
    private BankAccount buscarCuentaPorNumero(User user, String numeroCuenta) {
        for (BankAccount cuenta : user.getBankAccounts()) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                return cuenta;
            }
        }
        return null;
    }
}

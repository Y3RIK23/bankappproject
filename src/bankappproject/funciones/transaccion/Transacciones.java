package bankappproject.funciones.transaccion;

import bankappproject.modelos.CuentaBancaria;
import bankappproject.modelos.usuario.Usuario;
import bankappproject.funciones.baseDatos.Datos;

import java.util.ArrayList;
import java.util.Date;

public class Transacciones {

    private final Datos data = Datos.getInstance();

    public String deposit(TransaccionDTO dto) throws IllegalArgumentException {
        if (dto.amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        Usuario user = data.buscarUsuarioPorId(dto.userId);
        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        CuentaBancaria cuenta = buscarCuentaPorNumero(user, dto.accountNumber);
        if (cuenta == null) {
            throw new IllegalArgumentException("Cuenta no encontrada para el usuario");
        }

        double nuevoSaldo = cuenta.getSaldoDisponible() + dto.amount;
        cuenta.setSaldoDisponible(nuevoSaldo);

        if (cuenta.getEstadoCuenta() == null) {
            cuenta.setEstadoCuenta(new ArrayList<>());
        }

        Transaccion transaccion = new Transaccion(new Date(), Transaccion.TransaccionType.DEPOSITO, dto.amount);
        cuenta.getEstadoCuenta().add(transaccion);

        data.guardarUsuario(user);

        return String.format("Depósito exitoso. Nuevo saldo: %.2f en la cuenta %s", nuevoSaldo, cuenta.getNumeroCuenta());
    }

    public String withdraw(TransaccionDTO dto) throws IllegalArgumentException {
        if (dto.amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        Usuario user = data.buscarUsuarioPorId(dto.userId);
        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        CuentaBancaria cuenta = buscarCuentaPorNumero(user, dto.accountNumber);
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

        Transaccion transaccion = new Transaccion(new Date(), Transaccion.TransaccionType.RETIRO, dto.amount);
        cuenta.getEstadoCuenta().add(transaccion);

        data.guardarUsuario(user);

        return String.format("Retiro exitoso. Nuevo saldo: %.2f en la cuenta %s", nuevoSaldo, cuenta.getNumeroCuenta());
    }

    public synchronized String invertir(TransaccionDTO dto) throws IllegalArgumentException, InterruptedException {
        if (dto.amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        Usuario user = data.buscarUsuarioPorId(dto.userId);
        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        CuentaBancaria cuenta = buscarCuentaPorNumero(user, dto.accountNumber);
        if (cuenta == null) {
            throw new IllegalArgumentException("Cuenta no encontrada para el usuario");
        }

        if (cuenta.getSaldoDisponible() < dto.amount) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        double nuevoSaldo = cuenta.getSaldoDisponible() - dto.amount;
        cuenta.setSaldoDisponible(nuevoSaldo);

        Thread.sleep(60000);

        dto.amount += dto.amount * 0.10;

        nuevoSaldo = cuenta.getSaldoDisponible() + dto.amount;
        cuenta.setSaldoDisponible(nuevoSaldo);

        Transaccion transaccion = new Transaccion(new Date(), Transaccion.TransaccionType.INVERSION, dto.amount);
        cuenta.getEstadoCuenta().add(transaccion);

        data.guardarUsuario(user);

        return String.format("Certificado de Inversion exitoso. Nuevo saldo: %.2f en la cuenta %s", dto.amount, cuenta.getNumeroCuenta());

    }

    // Método auxiliar para encontrar una cuenta por número sin usar Optional ni Stream
    private CuentaBancaria buscarCuentaPorNumero(Usuario user, String numeroCuenta) {
        for (CuentaBancaria cuenta : user.getBankAccounts()) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                return cuenta;
            }
        }
        return null;
    }

    public String accountStatus(String userId, String accountNumber) {
        Usuario user = Datos.getInstance().buscarUsuarioPorId(userId);
        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        for (CuentaBancaria cuenta : user.getBankAccounts()) {
            if (cuenta.getNumeroCuenta().equals(accountNumber)) {
                return cuenta.estadoCuenta();
            }
        }

        throw new IllegalArgumentException("No se encontró la cuenta bancaria.");
    }
}

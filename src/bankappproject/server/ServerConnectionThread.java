/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.server;

import bankappproject.models.bankAccount.BankAccount;
import bankappproject.models.bankAccount.Transaction;
import bankappproject.models.user.User;
import bankappproject.models.user.UserConcreteBuilder;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Usuario
 */
public class ServerConnectionThread extends Thread {

    private DataOutputStream output;
    private DataInputStream input;
    private Socket connection;


    public ServerConnectionThread(Socket connection) {
        this.connection = connection;
    }

    private void getStreams() throws IOException {
        output = new DataOutputStream(connection.getOutputStream());
        output.flush();
        input = new DataInputStream(connection.getInputStream());
    }

    private void processClient() throws IOException {
        // Leer ID usuario (se deberia utilizar para autenticar pero falta esa picha(clase))
        output.writeUTF("Ingrese su ID de usuario:");
        String userId = input.readUTF();

        ///////////// Usuario ficticio "quemado"///////////////////////
        BankAccount cuenta1 = new BankAccount.Builder()
                .setUserID("12345678")
                .setNumberAccount("1234567890")
                .setBalance(1500)
                .setTransactions(new ArrayList<>())
                .build();

        BankAccount cuenta2 = new BankAccount.Builder()
                .setUserID("12345678")
                .setNumberAccount("9876543210")
                .setBalance(3000)
                .build();

        ArrayList<BankAccount> cuentas = new ArrayList<>();
        cuentas.add(cuenta1);
        cuentas.add(cuenta2);

        User user = new UserBuilder()
                .setName("Juan Perez")
                .setId("12345678")
                .setCuentasBancarias(cuentas)
                .setAlreadyActive(true)
                .build();
        ///////////// Usuario ficticio "quemado"///////////////////////


        
        output.writeUTF("Bienvenido " + user.getName());
        boolean running = true;

        // Ejemplo simple de operaciones
        while (running) {
            output.writeUTF("Opciones:\n1-Ver cuentas\n2-Hacer deposito\n3-Hacer retiro\n4-Salir");
            String opcion = input.readUTF();

            switch (opcion) {
                case "1": //Ver cuentas
                    String cuentasInfo = "Tus cuentas:\n";
                    for (BankAccount cuenta : user.getBankAccounts()) {
                        cuentasInfo += "Cuenta: " + cuenta.getAccountNumber()
                                + ", Saldo: " + cuenta.getBalance() + "\n";
                    }
                    output.writeUTF(cuentasInfo.toString());
                    break;

                case "2": //Deposito simple
                    output.writeUTF("Ingrese monto a depositar:");
                    try {
                        int montoDeposito = Integer.parseInt(input.readUTF());
                        BankAccount cuenta = user.getBankAccounts().get(0);
                        cuenta.actualizarSaldo(cuenta.getBalance() + montoDeposito);

                        //Crear transacción
                        Transaction deposito = new Transaction.Builder()
                                .setCuenta(cuenta.getAccountNumber())
                                .setFecha(new Date())
                                .setTransaccionType(Transaction.TransaccionType.DEPOSITO)
                                .setCredito(montoDeposito)
                                .setDebito(0)
                                .build();

                        cuenta.agregarTransaccion(deposito);
                        output.writeUTF("Deposito realizado. Nuevo saldo: " + cuenta.getBalance());
                    } catch (IOException e) {
                        output.writeUTF("Error: Ingrese un número válido.");
                    }
                    break;

                case "3": //Retiro simple
                    output.writeUTF("Ingrese monto a retirar:");
                    try {
                        int montoRetiro = Integer.parseInt(input.readUTF());
                        BankAccount cuenta = user.getBankAccounts().get(0);
                        if (montoRetiro > cuenta.getBalance()) {
                            output.writeUTF("Saldo insuficiente.");
                        } else {
                            cuenta.actualizarSaldo(cuenta.getBalance() - montoRetiro);
                            Transaction retiro = new Transaction.Builder()
                                    .setCuenta(cuenta.getAccountNumber())
                                    .setFecha(new Date())
                                    .setTransaccionType(Transaction.TransaccionType.RETIRO)
                                    .setCredito(0)
                                    .setDebito(montoRetiro)
                                    .build();
                            cuenta.agregarTransaccion(retiro);
                            output.writeUTF("Retiro realizado. Saldo restante: " + cuenta.getBalance());
                        }
                    } catch (IOException e) {
                        output.writeUTF("Error: Ingrese un numero válido.");
                    }
                    break;

                case "4": //Salir
                    output.writeUTF("Gracias por usar el servicio. Adios!");
                    running = false;
                    break;

                default:
                    output.writeUTF("Opcion invalida. Intente de nuevo.");
            }
        }
    }

    public void run() {
        try {
            getStreams();
            processClient();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        System.out.println("\nTerminating connection\n\n");
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

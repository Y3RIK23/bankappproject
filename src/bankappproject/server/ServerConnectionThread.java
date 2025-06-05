/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.server;

import bankappproject.models.user.*;
import bankappproject.models.bankAccount.*;
import bankappproject.server.AuthModule.*;
import bankappproject.server.TransactionsService.*;
import bankappproject.server.db.Data;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnectionThread extends Thread {

    private DataOutputStream output;
    private DataInputStream input;
    private Socket connection;

    private final AuthService authService = new AuthService();
    private final TransactionsService transactionsService = new TransactionsService();

    public ServerConnectionThread(Socket connection) {
        this.connection = connection;
    }

    private void getStreams() throws IOException {
        output = new DataOutputStream(connection.getOutputStream());
        input = new DataInputStream(connection.getInputStream());
    }

    private void processClient() throws IOException {
        User user = null;
        boolean authenticated = false;

        while (!authenticated) {
            output.writeUTF("Seleccione una opción:\n1 - Iniciar sesión\n2 - Registrar\n3 - Salir");
            String option = input.readUTF();

            switch (option) {
                case "1":
                    output.writeUTF("Ingrese cédula:");
                    String cedula = input.readUTF();
                    output.writeUTF("Ingrese contraseña:");
                    String pass = input.readUTF();

                    try {
                        LoginDTO dto = new LoginDTO();
                        dto.cedula = cedula;
                        dto.contraseña = pass;
                        output.writeUTF(authService.login(dto));
                        user = Data.getInstance().buscarUsuarioPorId(cedula);
                        authenticated = true;
                    } catch (IllegalArgumentException e) {
                        output.writeUTF("Error: " + e.getMessage());
                    }
                    break;

                case "2":
                    output.writeUTF("Ingrese cédula:");
                    String newID = input.readUTF();
                    output.writeUTF("Ingrese contraseña:");
                    String newPass = input.readUTF();

                    try {
                        UserConcreteBuilder builder = new UserConcreteBuilder();
                        UserDirector director = new UserDirector();
                        User nuevo = director.construirAlerta(builder, newID, newPass);
                        nuevo.setAlreadyActive(false);
                        authService.register(nuevo);
                        output.writeUTF("Registro exitoso. Ahora puede iniciar sesión.");
                    } catch (UserException | IllegalArgumentException e) {
                        output.writeUTF("Error: " + e.getMessage());
                    }
                    break;

                case "3":
                    output.writeUTF("Saliendo...");
                    return;

                default:
                    output.writeUTF("Opción inválida.");
            }
        }

        boolean running = true;

        while (running) {
            output.writeUTF("Menú:\n1 - Ver cuentas\n2 - Crear cuenta\n3 - Depositar\n4 - Retirar\n5 - Inversión\n6 - Estado de cuenta\n7 - Cerrar sesión");
            String opcion = input.readUTF();

            try {
                switch (opcion) {
                    case "1":
                        StringBuilder banckAccounts = new StringBuilder("Cuentas:\n");
                        for (BankAccount cuenta : user.getBankAccounts()) {
                            banckAccounts.append("Cuenta: ").append(cuenta.getNumeroCuenta())
                                    .append(" - Saldo: ").append(cuenta.getSaldoDisponible()).append("\n");
                        }
                        output.writeUTF(banckAccounts.toString());
                        break;

                    case "2":
                        BankAccount nueva = new BankAccount();
                        if (user.getBankAccounts() == null) {
                            user.setBankAccounts(new ArrayList<>());
                        }
                        user.getBankAccounts().add(nueva);
                        Data.getInstance().guardarUsuario(user);
                        output.writeUTF("Cuenta creada con éxito: " + nueva.getNumeroCuenta());
                        break;

                    case "3":
                        output.writeUTF("Ingrese número de cuenta:");
                        String depCuenta = input.readUTF();
                        output.writeUTF("Ingrese monto:");
                        double monto = Double.parseDouble(input.readUTF());

                        TransactionDTO dtoDep = new TransactionDTO();
                        dtoDep.userId = user.getId();
                        dtoDep.accountNumber = depCuenta;
                        dtoDep.amount = monto;

                        output.writeUTF(transactionsService.deposit(dtoDep));
                        break;

                    case "4":
                        output.writeUTF("Ingrese número de cuenta:");
                        String retCuenta = input.readUTF();
                        output.writeUTF("Ingrese monto:");
                        double montoRet = Double.parseDouble(input.readUTF());

                        TransactionDTO dtoRet = new TransactionDTO();
                        dtoRet.userId = user.getId();
                        dtoRet.accountNumber = retCuenta;
                        dtoRet.amount = montoRet;

                        output.writeUTF(transactionsService.withdraw(dtoRet));
                        break;

                    case "5":
                        output.writeUTF("Ingrese número de cuenta:");
                        String invCuenta = input.readUTF();
                        output.writeUTF("Ingrese monto:");
                        double montoInv = Double.parseDouble(input.readUTF());

                        TransactionDTO dtoInv = new TransactionDTO();
                        dtoInv.userId = user.getId();
                        dtoInv.accountNumber = invCuenta;
                        dtoInv.amount = montoInv;

                        output.writeUTF(transactionsService.invertir(dtoInv));
                        break;

                    case "6":
                        output.writeUTF("Ingrese número de cuenta:");
                        String estadoCuenta = input.readUTF();
                        output.writeUTF(transactionsService.accountStatus(user.getId(), estadoCuenta));
                        break;

                    case "7":
                        authService.logout(user.getId());
                        output.writeUTF("Sesión cerrada correctamente.");
                        running = false;
                        break;

                    default:
                        output.writeUTF("Opción no válida.");
                        break;
                }
            } catch (Exception e) {
                output.writeUTF("Error: " + e.getMessage());
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

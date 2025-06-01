/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.models.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Usuario
 */
public class ClientePruebaByChatgpt {

    private DataInputStream input;
    private DataOutputStream output;
    private Socket client;
    private final String HOST = "127.0.0.1";
    private final int PORT = 12345;

    public void runClient() {
        try {
            connectToServer();
            getStreams();
            processConnection();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void connectToServer() throws IOException {
        System.out.println("Attempting connection\n");
        client = new Socket(HOST, PORT);
        System.out.println("Connected to: " + client.getInetAddress().getHostName());
    }

    private void getStreams() throws IOException {
        output = new DataOutputStream(client.getOutputStream());
        output.flush();
        input = new DataInputStream(client.getInputStream());
    }
    
    private void processConnection() throws IOException {
    Scanner scanner = new Scanner(System.in);

    // Paso 1: ingresar cédula
    String mensajeInicio = input.readUTF(); // "Ingrese su ID de usuario:"
    System.out.println("Servidor: " + mensajeInicio);

    System.out.print("Tu cedula: ");
    String cedula = scanner.nextLine();
    output.writeUTF(cedula);

    // Paso 2: recibe mensaje de bienvenida o error
    String respuesta = input.readUTF();
    System.out.println("Servidor: " + respuesta);

    if (respuesta.startsWith("ERROR")) {
        return; // Usuario inválido
    }

    // Paso 3: ya autenticado, entra al ciclo de operaciones
    boolean continuar = true;
    while (continuar) {
        String menu = input.readUTF(); // Menú de opciones
        System.out.println("Servidor: \n" + menu);

        System.out.print("Elige una opcion: ");
        String opcion = scanner.nextLine();
        output.writeUTF(opcion);

        switch (opcion) {
            case "1": // Ver cuentas
                String infoCuentas = input.readUTF();
                System.out.println("Servidor:\n" + infoCuentas);
                break;

            case "2": // Depósito
                String promptDepo = input.readUTF();
                System.out.println("Servidor: " + promptDepo);
                System.out.print("Monto a depositar: ");
                String montoDeposito = scanner.nextLine();
                output.writeUTF(montoDeposito);

                String confirmacionDepo = input.readUTF();
                System.out.println("Servidor: " + confirmacionDepo);
                break;

            case "3": // Retiro
                String promptRetiro = input.readUTF();
                System.out.println("Servidor: " + promptRetiro);
                System.out.print("Monto a retirar: ");
                String montoRetiro = scanner.nextLine();
                output.writeUTF(montoRetiro);

                String confirmacionRetiro = input.readUTF();
                System.out.println("Servidor: " + confirmacionRetiro);
                break;

            case "4": // Salida
                String despedida = input.readUTF();
                System.out.println("Servidor: " + despedida);
                continuar = false;
                break;

            default:
                String invalido = input.readUTF();
                System.out.println("Servidor: " + invalido);
                break;
        }
    }
}
    
    

    private void closeConnection() {
        System.out.println("\nClosing connection");
        try {
            output.close();
            input.close();
            client.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ClientePruebaByChatgpt().runClient();
    }
}

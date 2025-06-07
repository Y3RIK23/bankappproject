/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class Cliente {

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

    public void processConnection() {
        try {
            boolean activo = true;

            while (activo) {
                // Recibe mensaje inicial del servidor
                String mensaje = input.readUTF();

                // Mensajes que indican salida o cierre de sesión
                if (mensaje.equalsIgnoreCase("Saliendo...")
                        || mensaje.contains("Sesión cerrada correctamente.")) {
                    JOptionPane.showMessageDialog(null, mensaje);
                    activo = false;
                    break;
                }

                // Determinar si el mensaje requiere entrada del usuario
                boolean requiereEntrada = mensaje.contains("Seleccione una opción")
                        || mensaje.contains("Ingrese")
                        || mensaje.contains("Menú:");

                // Si requiere entrada, pedirla y enviarla
                if (requiereEntrada) {
                    String entrada = JOptionPane.showInputDialog(null, mensaje);

                    if (entrada == null) {
                        // Usuario presionó Cancelar
                        // En login o registro: volver al inicio
                        // En menú: cerrar sesión o salir
                        // Se envía "3", que en servidor es Salir / Cerrar sesión
                        output.writeUTF("3");
                    } else {
                        output.writeUTF(entrada);
                    }
                } // Si es solo un mensaje informativo, mostrarlo
                else {
                    JOptionPane.showMessageDialog(null, mensaje);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error de conexión con el servidor: " + e.getMessage());
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
        new Cliente().runClient();
    }
}

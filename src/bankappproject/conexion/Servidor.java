/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.conexion;

import bankappproject.funciones.baseDatos.Datos;
import bankappproject.funciones.transaccion.TransaccionDTO;
import bankappproject.funciones.transaccion.Transacciones;
import bankappproject.modelos.CuentaBancaria;
import bankappproject.modelos.usuario.Usuario;
import bankappproject.modelos.usuario.UsuarioConcreteB;
import bankappproject.modelos.usuario.UsuarioDirector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class Servidor {

    private ServerSocket server;
    private final int PORT = 12345;

    public void runServer() {
        try {
            server = new ServerSocket(PORT);
            while (true) {
                HiloServidor thread = new HiloServidor(waitForConnection());
                thread.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            closeServer();
        }
    }

    

    private Socket waitForConnection() throws IOException {
        System.out.println("Waiting for connection...\n");
        Socket connection = server.accept();
        System.out.println("Connection received from: " + connection.getInetAddress().getHostName());
        return connection;
    }

    private void closeServer() {
        System.out.println("\nTerminating server");
        try {
            server.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Servidor().runServer();
    }
}

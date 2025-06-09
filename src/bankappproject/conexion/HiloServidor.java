/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.conexion;

import bankappproject.modelos.CuentaBancaria;
import bankappproject.modelos.usuario.Usuario;
import bankappproject.modelos.usuario.UsuarioDirector;
import bankappproject.modelos.usuario.UsuarioConcreteB;
import bankappproject.modelos.usuario.ExcepcionUsuario;
import bankappproject.funciones.transaccion.TransaccionDTO;
import bankappproject.funciones.autenticacion.InicioSesionDTO;
import bankappproject.funciones.autenticacion.Autenticacion;
import bankappproject.funciones.transaccion.Transacciones;
import bankappproject.funciones.baseDatos.Datos;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class HiloServidor extends Thread {

    private DataOutputStream output;
    private DataInputStream input;
    private Socket connection;

    private final Autenticacion authService = new Autenticacion();
    private final Transacciones transactionsService = new Transacciones();

    public HiloServidor(Socket connection) {
        this.connection = connection;
    }

    private void getStreams() throws IOException {
        output = new DataOutputStream(connection.getOutputStream());
        input = new DataInputStream(connection.getInputStream());
    }

    private void procesarCliente() throws IOException {
        // Usuario autenticado (nulo inicialmente)
        Usuario usuario = null;
        boolean autenticado = false;

        // Bucle de autenticación: se repite hasta que el usuario inicie sesión correctamente
        while (!autenticado) {
            output.writeUTF("Seleccione una opción:\n1 - Iniciar sesión\n2 - Registrar\n3 - Salir");
            String opcion = input.readUTF();

            switch (opcion) {
                case "1":
                    // Proceso de inicio de sesión
                    output.writeUTF("Ingrese cédula:");
                    String cedula = input.readUTF();
                    output.writeUTF("Ingrese contraseña:");
                    String clave = input.readUTF();

                    try {
                        // Crear DTO con credenciales ingresadas
                        InicioSesionDTO datosInicioSesion = new InicioSesionDTO();
                        datosInicioSesion.cedula = cedula;
                        datosInicioSesion.contraseña = clave;

                        // Intentar autenticación
                        output.writeUTF(authService.inicioSesion(datosInicioSesion));

                        // Si se autentica correctamente, obtener el usuario
                        usuario = Datos.getInstance().buscarUsuarioPorId(cedula);
                        autenticado = true;
                    } catch (IllegalArgumentException e) {
                        output.writeUTF("Error: " + e.getMessage());
                    }
                    break;

                case "2":
                    // Proceso de registro de nuevo usuario
                    output.writeUTF("Ingrese cédula:");
                    String nuevaCedula = input.readUTF();
                    output.writeUTF("Ingrese contraseña:");
                    String nuevaClave = input.readUTF();

                    try {
                        // Usar patrón Builder para construir un nuevo usuario
                        UsuarioConcreteB constructorUsuario = new UsuarioConcreteB();
                        UsuarioDirector directorUsuario = new UsuarioDirector();
                        Usuario nuevoUsuario = directorUsuario.construirAlerta(constructorUsuario, nuevaCedula, nuevaClave);

                        // Indicar que aún no ha iniciado sesión
                        nuevoUsuario.setAlreadyActive(false);

                        // Registrar el nuevo usuario
                        authService.register(nuevoUsuario);
                        output.writeUTF("Registro exitoso. Ahora puede iniciar sesión.");
                    } catch (ExcepcionUsuario | IllegalArgumentException e) {
                        output.writeUTF("Error: " + e.getMessage());
                    }
                    break;

                case "3":
                    // Salir del sistema
                    output.writeUTF("Saliendo...");
                    return;

                default:
                    output.writeUTF("Opción inválida.");
            }
        }

        // Usuario autenticado: inicia menú de operaciones bancarias
        boolean activo = true;

        while (activo) {
            output.writeUTF("Menú:\n1 - Ver cuentas\n2 - Crear cuenta\n3 - Depositar\n4 - Retirar\n5 - Inversión\n6 - Estado de cuenta\n7 - Cerrar sesión");
            String opcion = input.readUTF();

            try {
                switch (opcion) {
                    case "1":
                        // Mostrar todas las cuentas del usuario
                        StringBuilder cuentas = new StringBuilder("Cuentas:\n");
                        for (CuentaBancaria cuenta : usuario.getBankAccounts()) {
                            cuentas.append("Cuenta: ").append(cuenta.getNumeroCuenta())
                                    .append(" - Saldo: ").append(cuenta.getSaldoDisponible()).append("\n");
                        }
                        output.writeUTF(cuentas.toString());
                        break;

                    case "2":
                        // Crear una nueva cuenta bancaria
                        CuentaBancaria nuevaCuenta = new CuentaBancaria();

                        // Inicializar lista de cuentas si es null
                        if (usuario.getBankAccounts() == null) {
                            usuario.setBankAccounts(new ArrayList<>());
                        }

                        // Agregar nueva cuenta al usuario y guardar cambios
                        usuario.getBankAccounts().add(nuevaCuenta);
                        Datos.getInstance().guardarUsuario(usuario);
                        output.writeUTF("Cuenta creada con éxito: " + nuevaCuenta.getNumeroCuenta());
                        break;

                    case "3":
                        // Proceso de depósito
                        output.writeUTF("Ingrese número de cuenta:");
                        String cuentaDeposito = input.readUTF();
                        output.writeUTF("Ingrese monto:");
                        double monto = Double.parseDouble(input.readUTF());

                        // Crear DTO para realizar el depósito
                        TransaccionDTO dtoDeposito = new TransaccionDTO();
                        dtoDeposito.userId = usuario.getId();
                        dtoDeposito.accountNumber = cuentaDeposito;
                        dtoDeposito.amount = monto;

                        output.writeUTF(transactionsService.deposit(dtoDeposito));
                        break;

                    case "4":
                        // Proceso de retiro
                        output.writeUTF("Ingrese número de cuenta:");
                        String cuentaRetiro = input.readUTF();
                        output.writeUTF("Ingrese monto:");
                        double montoRetiro = Double.parseDouble(input.readUTF());

                        TransaccionDTO dtoRetiro = new TransaccionDTO();
                        dtoRetiro.userId = usuario.getId();
                        dtoRetiro.accountNumber = cuentaRetiro;
                        dtoRetiro.amount = montoRetiro;

                        output.writeUTF(transactionsService.withdraw(dtoRetiro));
                        break;

                    case "5":
                        // Proceso de inversión
                        output.writeUTF("Ingrese número de cuenta:");
                        String cuentaInversion = input.readUTF();
                        output.writeUTF("Ingrese monto:");
                        double montoInversion = Double.parseDouble(input.readUTF());

                        TransaccionDTO dtoInversion = new TransaccionDTO();
                        dtoInversion.userId = usuario.getId();
                        dtoInversion.accountNumber = cuentaInversion;
                        dtoInversion.amount = montoInversion;

                        output.writeUTF(transactionsService.invertir(dtoInversion));
                        break;

                    case "6":
                        // Consultar estado de cuenta
                        output.writeUTF("Ingrese número de cuenta:");
                        String numeroCuentaEstado = input.readUTF();
                        output.writeUTF(transactionsService.accountStatus(usuario.getId(), numeroCuentaEstado));
                        break;

                    case "7":
                        // Cerrar sesión del usuario
                        authService.logout(usuario.getId());
                        output.writeUTF("Sesión cerrada correctamente.");
                        activo = false;
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
            procesarCliente();
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

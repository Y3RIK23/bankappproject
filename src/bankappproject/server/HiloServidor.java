/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankappproject.server;

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
import bankappproject.funciones.transaccion.Transaccion;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            // Mostrar todas las cuentas del usuario
            StringBuilder cuentas = new StringBuilder("Cuentas:\n");
            for (CuentaBancaria cuenta : usuario.getBankAccounts()) {
                cuentas.append(cuenta.getNumeroCuenta())
                        .append(" - Saldo: ").append(cuenta.getSaldoDisponible())
                        .append("\n");
            }
            output.writeUTF(cuentas.toString() + "\nMenú:\n1 - Crear cuenta\n2 - Depositar\n3 - Retirar\n4 - Inversión\n5 - Estado de cuenta\n6 - Cerrar sesión");
            String opcion = input.readUTF();

            try {
                switch (opcion) {
                    case "1":
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

                    case "2":
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

                        output.writeUTF(transactionsService.depositar(dtoDeposito));
                        break;

                    case "3":
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

                    case "4":
                        // Proceso de inversión
                        output.writeUTF("Ingrese número de cuenta:");
                        String cuentaInversion = input.readUTF();
                        output.writeUTF("Ingrese monto:");
                        double montoInversion = Double.parseDouble(input.readUTF());

                        TransaccionDTO dtoInversion = new TransaccionDTO();
                        dtoInversion.userId = usuario.getId();
                        dtoInversion.accountNumber = cuentaInversion;
                        dtoInversion.amount = montoInversion;
                        
                        System.out.println("Realizando inversion, espere...");
                        output.writeUTF(transactionsService.invertir(dtoInversion));
                        break;

                    case "5":
                        // Consultar estado de cuenta
                        output.writeUTF("Ingrese número de cuenta:");
                        String numeroCuentaEstado = input.readUTF();
                        output.writeUTF(transactionsService.accountStatus(usuario.getId(), numeroCuentaEstado));
                        break;

                    case "6":
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

    
    private void cargarDatosIniciales() {
    Datos datos = Datos.getInstance();

    // Prevenir doble carga si ya hay usuarios
    if (!datos.listarUsuarios().isEmpty()) return;

    // Preparar builder y director
    UsuarioDirector director = new UsuarioDirector();

    try {
        // Usuario 1
        UsuarioConcreteB builder1 = new UsuarioConcreteB();
        Usuario u1 = director.construirAlerta(builder1, "123456789", "pass123");
        u1.setAlreadyActive(false);

        CuentaBancaria c1 = new CuentaBancaria();
        CuentaBancaria c2 = new CuentaBancaria();
        agregarTransacciones(c1);
        agregarTransacciones(c2);
        u1.setBankAccounts(new ArrayList<>(List.of(c1, c2)));

        datos.guardarUsuario(u1);

        // Usuario 2
        UsuarioConcreteB builder2 = new UsuarioConcreteB();
        Usuario u2 = director.construirAlerta(builder2, "987654321", "clave456");
        u2.setAlreadyActive(false);

        CuentaBancaria c3 = new CuentaBancaria();
        agregarTransacciones(c3);
        u2.setBankAccounts(new ArrayList<>(List.of(c3)));

        datos.guardarUsuario(u2);

        // Usuario 3
        UsuarioConcreteB builder3 = new UsuarioConcreteB();
        Usuario u3 = director.construirAlerta(builder3, "456123789", "abc789");
        u3.setAlreadyActive(false);

        CuentaBancaria c4 = new CuentaBancaria();
        agregarTransacciones(c4);
        u3.setBankAccounts(new ArrayList<>(List.of(c4)));

        datos.guardarUsuario(u3);

        System.out.println("Usuarios de prueba cargados correctamente.");

    } catch (ExcepcionUsuario e) {
        System.err.println("Error cargando usuarios de prueba: " + e.getMessage());
    }
}
    
    private void agregarTransacciones(CuentaBancaria cuenta) {
    cuenta.addTransaccion(new Transaccion(new Date(), Transaccion.TransaccionType.DEPOSITO, 1000));
    cuenta.addTransaccion(new Transaccion(new Date(), Transaccion.TransaccionType.RETIRO, 250));
    cuenta.addTransaccion(new Transaccion(new Date(), Transaccion.TransaccionType.INVERSION, 150));
    cuenta.addSaldoDisponible(600); 
}

    public void run() {
        try {
            cargarDatosIniciales();
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

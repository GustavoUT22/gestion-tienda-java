package com.lideratec.vista;

import com.lideratec.dao.ClienteDAO;
import com.lideratec.dao.EmpleadoDAO;
import com.lideratec.dao.ProductoDAO;
import com.lideratec.dao.UsuarioDAO;
import com.lideratec.dao.VentaDAO;
import com.lideratec.model.Cliente;
import com.lideratec.model.DetalleVenta;
import com.lideratec.model.Empleado;
import com.lideratec.model.Producto;
import com.lideratec.model.Usuario;
import com.lideratec.model.Venta;
import com.lideratec.service.VentaService;

import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de consola del sistema.
 *
 * Se encarga solo de mostrar informacion y pedir datos al usuario:
 * los calculos los hace VentaService y el acceso a datos los DAO.
 * El menu cambia segun el rol del usuario que inicio sesion
 * (ADMINISTRADOR ve todas las opciones, VENDEDOR solo las de venta).
 */
public class MenuPrincipal {

    private final Scanner sc = new Scanner(System.in);

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final VentaDAO ventaDAO = new VentaDAO();
    private final VentaService ventaService = new VentaService();

    /** Usuario que inicio sesion; define que opciones se muestran. */
    private Usuario usuarioActual;

    /**
     * Punto de entrada de la interfaz: primero pide login y,
     * si es correcto, entra al bucle del menu.
     */
    public void iniciar() {
        System.out.println("=========================================");
        System.out.println("   SISTEMA DE GESTION DE TIENDA");
        System.out.println("=========================================");

        if (!iniciarSesion()) {
            System.out.println("Demasiados intentos fallidos. El sistema se cerrara.");
            return;
        }

        mostrarMenu();
    }

    // LOGIN
    private boolean iniciarSesion() {
        int intentos = 0;

        while (intentos < 3) {
            System.out.print("\nUsuario: ");
            String usuario = sc.nextLine().trim();
            System.out.print("Contrasena: ");
            String contrasena = sc.nextLine().trim();

            usuarioActual = usuarioDAO.validarCredenciales(usuario, contrasena);

            if (usuarioActual != null) {
                System.out.println("\nBienvenido " + usuarioActual.getUsuario()
                        + " (" + usuarioActual.getTipo() + ")");
                return true;
            }

            intentos++;
            System.out.println("Usuario o contrasena incorrectos. Intentos restantes: " + (3 - intentos));
        }
        return false;
    }

    // Verificamos si es admin
    private boolean esAdministrador() {
        return usuarioActual.getTipo().equalsIgnoreCase("ADMINISTRADOR");
    }




    //Bucle principal del menu. Se repite hasta que el usuario elige salir.

    private void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n--------- MENU PRINCIPAL ---------");
            System.out.println("1. Listar productos");
            System.out.println("2. Buscar producto por codigo");
            System.out.println("3. Listar clientes");
            System.out.println("4. Registrar cliente");
            System.out.println("5. Registrar venta");
            System.out.println("6. Historial de ventas");

            if (esAdministrador()) {
                System.out.println("7. Registrar producto      (admin)");
                System.out.println("8. Actualizar stock        (admin)");
                System.out.println("9. Listar empleados        (admin)");
            }

            System.out.println("0. Salir");
            opcion = leerEntero("Elija una opcion: ");

            switch (opcion) {
                case 1 -> listarProductos();
                case 2 -> buscarProducto();
                case 3 -> listarClientes();
                case 4 -> registrarCliente();
                case 5 -> registrarVenta();
                case 6 -> historialVentas();
                case 7 -> soloAdmin(this::registrarProducto);
                case 8 -> soloAdmin(this::actualizarStock);
                case 9 -> soloAdmin(this::listarEmpleados);
                case 0 -> System.out.println("\nCerrando el sistema. Hasta luego.");
                default -> System.out.println("Opcion invalida, intente de nuevo.");
            }

        } while (opcion != 0);
    }

    /** Ejecuta una accion solo si el usuario es administrador. */
    private void soloAdmin(Runnable accion) {
        if (esAdministrador()) {
            accion.run();
        } else {
            System.out.println("Opcion invalida, intente de nuevo.");
        }
    }

    // Mostramos productos

    private void listarProductos() {
        List<Producto> productos = productoDAO.listarTodos();

        System.out.println("\n--- PRODUCTOS REGISTRADOS ---");
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        for (Producto p : productos) {
            System.out.println(p);
        }
        System.out.println("Total de productos: " + productos.size());
    }

    // Buscamos un producto por codigo
    private void buscarProducto() {
        System.out.print("\nCodigo del producto (ej: P001): ");
        String codigo = sc.nextLine().trim().toUpperCase();

        Producto producto = productoDAO.buscarPorCodigo(codigo);

        if (producto == null) {
            System.out.println("No se encontro ningun producto con el codigo " + codigo);
        } else {
            System.out.println("Producto encontrado: " + producto);
        }
    }

    // Registramos un nuevo producto
    private void registrarProducto() {
        System.out.println("\n--- NUEVO PRODUCTO ---");

        System.out.print("Codigo: ");
        String codigo = sc.nextLine().trim().toUpperCase();

        if (!ventaService.validarNombre(codigo)) {
            System.out.println("El codigo no puede estar vacio.");
            return;
        }

        if (productoDAO.buscarPorCodigo(codigo) != null) {
            System.out.println("Ya existe un producto con ese codigo.");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        if (!ventaService.validarNombre(nombre)) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }

        System.out.print("Categoria: ");
        String categoria = sc.nextLine().trim();

        double precio = leerDecimal("Precio: S/ ");
        if (precio <= 0) {
            System.out.println("El precio debe ser mayor a cero.");
            return;
        }

        int stock = leerEntero("Stock inicial: ");
        if (stock < 0) {
            System.out.println("El stock no puede ser negativo.");
            return;
        }

        Producto producto = new Producto(codigo, nombre, categoria, precio, stock);

        if (productoDAO.insertar(producto)) {
            System.out.println("Producto registrado correctamente (id " + producto.getId() + ").");
        } else {
            System.out.println("No se pudo registrar el producto.");
        }
    }

    // Actualizamos el stock de un producto
    private void actualizarStock() {
        System.out.print("\nCodigo del producto a actualizar: ");
        String codigo = sc.nextLine().trim().toUpperCase();

        Producto producto = productoDAO.buscarPorCodigo(codigo);

        if (producto == null) {
            System.out.println("No se encontro ningun producto con el codigo " + codigo);
            return;
        }

        System.out.println("Stock actual de " + producto.getNombre() + ": " + producto.getStock());
        int nuevoStock = leerEntero("Nuevo stock: ");

        if (nuevoStock < 0) {
            System.out.println("El stock no puede ser negativo.");
            return;
        }

        if (productoDAO.actualizarStock(producto.getId(), nuevoStock)) {
            System.out.println("Stock actualizado correctamente.");
        } else {
            System.out.println("No se pudo actualizar el stock.");
        }
    }

    // Listamos clientes
    private void listarClientes() {
        List<Cliente> clientes = clienteDAO.listarTodos();

        System.out.println("\n--- CLIENTES REGISTRADOS ---");
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        // mostrarInformacion() es el metodo sobrescrito (polimorfismo)
        for (Cliente c : clientes) {
            c.mostrarInformacion();
            System.out.println();
        }
    }

    //Listamos EMPLEADOS
    private void listarEmpleados() {
        List<Empleado> empleados = empleadoDAO.listarTodos();

        System.out.println("\n--- EMPLEADOS REGISTRADOS ---");
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados.");
            return;
        }

        for (Empleado e : empleados) {
            e.mostrarInformacion();
            System.out.println();
        }
    }

    private void registrarCliente() {
        System.out.println("\n--- NUEVO CLIENTE ---");

        System.out.print("DNI (8 digitos): ");
        String dni = sc.nextLine().trim();

        if (!validarDni(dni)) {
            System.out.println("El DNI debe tener exactamente 8 digitos numericos.");
            return;
        }

        if (clienteDAO.buscarPorDni(dni) != null) {
            System.out.println("Ya existe un cliente registrado con ese DNI.");
            return;
        }

        System.out.print("Nombre completo: ");
        String nombre = sc.nextLine().trim();

        if (!ventaService.validarNombre(nombre)) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }

        System.out.print("Celular: ");
        String celular = sc.nextLine().trim();

        System.out.print("Correo: ");
        String correo = sc.nextLine().trim();

        Cliente cliente = new Cliente(dni, nombre, celular, correo);

        if (clienteDAO.insertar(cliente)) {
            System.out.println("Cliente registrado correctamente (id " + cliente.getId() + ").");
        } else {
            System.out.println("No se pudo registrar el cliente.");
        }
    }

    /**
     * Valida un DNI usando metodos de la clase String:
     * debe tener 8 caracteres y todos deben ser digitos.
     */
    private boolean validarDni(String dni) {
        if (dni == null || dni.length() != 8) {
            return false;
        }

        for (int i = 0; i < dni.length(); i++) {
            if (!Character.isDigit(dni.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private void registrarVenta() {
        System.out.println("\n--- REGISTRAR VENTA ---");

        System.out.print("DNI del cliente: ");
        Cliente cliente = clienteDAO.buscarPorDni(sc.nextLine().trim());

        if (cliente == null) {
            System.out.println("Cliente no encontrado. Registrelo primero con la opcion 4.");
            return;
        }

        System.out.print("DNI del empleado que atiende: ");
        Empleado empleado = empleadoDAO.buscarPorDni(sc.nextLine().trim());

        if (empleado == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }

        ventaService.limpiar();
        boolean seguir = true;

        while (seguir) {
            System.out.print("\nCodigo del producto (o ENTER para terminar): ");
            String codigo = sc.nextLine().trim().toUpperCase();

            if (codigo.isEmpty()) {
                seguir = false;
                continue;
            }

            Producto producto = productoDAO.buscarPorCodigo(codigo);

            if (producto == null) {
                System.out.println("Producto no encontrado.");
                continue;
            }

            System.out.println(producto);
            int cantidad = leerEntero("Cantidad: ");

            // VentaService valida cantidad y stock disponible
            if (ventaService.agregarProducto(producto, cantidad)) {
                System.out.println("Agregado. Subtotal parcial: S/ " + ventaService.calcularSubtotal());
            }
        }

        if (ventaService.getDetalles().isEmpty()) {
            System.out.println("Venta cancelada: no se agrego ningun producto.");
            return;
        }

        ventaService.mostrarVenta();

        System.out.print("\nConfirmar venta? (s/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.println("Venta cancelada.");
            ventaService.limpiar();
            return;
        }

        Venta venta = new Venta(
                cliente,
                empleado,
                ventaService.calcularSubtotal(),
                ventaService.calcularIGV(),
                ventaService.calcularTotal()
        );

        // registrarVenta() inserta la cabecera, el detalle y descuenta el stock
        // dentro de una transaccion: si algo falla no se guarda nada.
        if (ventaDAO.registrarVenta(venta, ventaService.getDetalles())) {
            System.out.println("Venta registrada correctamente. Numero de venta: " + venta.getId());
        } else {
            System.out.println("No se pudo registrar la venta.");
        }

        ventaService.limpiar();
    }

    private void historialVentas() {
        List<Venta> ventas = ventaDAO.listarTodas();

        System.out.println("\n--- HISTORIAL DE VENTAS ---");
        if (ventas.isEmpty()) {
            System.out.println("Todavia no hay ventas registradas.");
            return;
        }

        double acumulado = 0;

        for (Venta v : ventas) {
            System.out.println("Venta #" + v.getId()
                    + " | " + v.getFecha()
                    + " | Cliente: " + v.getCliente().getNombre()
                    + " | Total: S/ " + v.getTotal());

            for (DetalleVenta d : ventaDAO.listarDetallesPorVenta(v.getId())) {
                System.out.println("      " + d);
            }

            acumulado += v.getTotal();
        }

        System.out.println("---------------------------------");
        System.out.println("Ventas registradas: " + ventas.size());
        System.out.println("Monto acumulado   : S/ " + (Math.round(acumulado * 100.0) / 100.0));
    }

    // =====================================================================
    // LECTURA DE DATOS
    // =====================================================================

    /**
     * Lee un numero entero desde teclado. Si el usuario escribe algo que no
     * es un numero, vuelve a preguntar en vez de romper el programa.
     */
    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero valido.");
            }
        }
    }

    /**
     * Lee un numero decimal desde teclado, aceptando coma o punto.
     */
    private double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero valido (ejemplo: 18.50).");
            }
        }
    }
}

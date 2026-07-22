package com.lideratec;

import com.lideratec.dao.ClienteDAO;
import com.lideratec.dao.EmpleadoDAO;
import com.lideratec.dao.ProductoDAO;
import com.lideratec.dao.UsuarioDAO;
import com.lideratec.dao.VentaDAO;
import model.Cliente;
import model.DetalleVenta;
import model.Empleado;
import model.Producto;
import model.Usuario;
import model.Venta;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ProductoDAO productoDAO = new ProductoDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        EmpleadoDAO empleadoDAO = new EmpleadoDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        VentaDAO ventaDAO = new VentaDAO();

        // ----- Prueba ProductoDAO -----
        Producto nuevoProducto = new Producto("P004", "Fideos 1kg", "Abarrotes", 6.90, 40);
        boolean exitoProducto = productoDAO.insertar(nuevoProducto);
        System.out.println("Producto insertado: " + exitoProducto + " | id: " + nuevoProducto.getId());

        // ----- Prueba ClienteDAO -----
        Cliente nuevoCliente = new Cliente("87654321", "Ana Torres", "999888777", "ana.torres@correo.com");
        boolean exitoCliente = clienteDAO.insertar(nuevoCliente);
        System.out.println("Cliente insertado: " + exitoCliente + " | id: " + nuevoCliente.getId());

        // ----- Prueba EmpleadoDAO -----
        Empleado nuevoEmpleado = new Empleado("11223344", "Carlos Ruiz", "955444333", "carlos.ruiz@correo.com", "Cajero", 1300.00);
        boolean exitoEmpleado = empleadoDAO.insertar(nuevoEmpleado);
        System.out.println("Empleado insertado: " + exitoEmpleado + " | id: " + nuevoEmpleado.getId());

        // ----- Prueba UsuarioDAO (login) -----
        Usuario logueado = usuarioDAO.validarCredenciales("admin", "admin123");
        if (logueado != null) {
            System.out.println("Login exitoso. Rol: " + logueado.getTipo());
        } else {
            System.out.println("Usuario o contraseña incorrectos.");
        }

        // ----- Prueba VentaDAO -----
        Cliente cliente = clienteDAO.buscarPorDni("12345678"); // el que trae el script SQL
        Empleado empleado = empleadoDAO.buscarPorDni("87654321");
        Producto producto = productoDAO.buscarPorCodigo("P001");

        DetalleVenta detalle = new DetalleVenta(producto, 3); // vende 3 unidades
        List<DetalleVenta> detalles = new ArrayList<>();
        detalles.add(detalle);

        double subtotal = detalle.getSubtotal();
        double igv = subtotal * 0.18;
        double total = subtotal + igv;

        Venta venta = new Venta(cliente, empleado, subtotal, igv, total);
        boolean exitoVenta = ventaDAO.registrarVenta(venta, detalles);
        System.out.println("Venta registrada: " + exitoVenta + " | id venta: " + venta.getId());

        // Verifica que el stock bajo
        Producto actualizado = productoDAO.buscarPorCodigo("P001");
        System.out.println("Stock actualizado: " + actualizado.getStock());



        // Listar todos
        System.out.println("\n--- Todos los productos ---");
        for (Producto p : productoDAO.listarTodos()) {
            System.out.println(p.getCodigo() + " - " + p.getNombre() + " - Stock: " + p.getStock());
        }

// Actualizar
        Producto p = productoDAO.buscarPorCodigo("P001");
        p.setStock(p.getStock() + 10); // si no tiene setStock, dime y lo agregamos
        productoDAO.actualizar(p);
        System.out.println("Stock actualizado: " + productoDAO.buscarPorCodigo("P001").getStock());

// Eliminar (prueba con algo que no te importe borrar)
        boolean eliminado = productoDAO.eliminar(999); // un id que no existe, para ver que retorna false
        System.out.println("Eliminado (id inexistente): " + eliminado);
    }
}
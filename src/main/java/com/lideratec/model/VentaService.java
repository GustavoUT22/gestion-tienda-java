package model;

import java.util.ArrayList;

public class VentaService {

    private ArrayList<DetalleVenta> detalles = new ArrayList<>();

    private final double IGV = 0.18;


    public boolean validarNombre(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }

    public boolean validarCantidad(int cantidad) {
        return cantidad > 0;
    }

    public boolean validarStock(Producto producto, int cantidad) {

        if (producto == null) {
            return false;
        }

        return producto.getStock() >= cantidad;
    }


    public boolean agregarProducto(Producto producto, int cantidad) {

        if (!validarCantidad(cantidad)) {
            System.out.println("Cantidad inválida.");
            return false;
        }

        if (!validarStock(producto, cantidad)) {
            System.out.println("Stock insuficiente.");
            return false;
        }

        DetalleVenta detalle = new DetalleVenta(producto, cantidad);

        detalles.add(detalle);

        return true;
    }


    public double calcularSubtotal() {

        double subtotal = 0;

        for (DetalleVenta detalle : detalles) {
            subtotal += detalle.getSubtotal();
        }

        return Math.round(subtotal * 100.0) / 100.0;
    }


    public double calcularIGV() {

        return Math.round(calcularSubtotal() * IGV * 100.0) / 100.0;

    }



    public double calcularTotal() {

        return calcularSubtotal() + calcularIGV();

    }


    public void actualizarStock() {

        for (DetalleVenta detalle : detalles) {

            Producto producto = detalle.getProducto();

            producto.setStock(
                    producto.getStock() - detalle.getCantidad()
            );

        }

    }


    public void mostrarVenta() {

        System.out.println("========== DETALLE ==========");

        for (DetalleVenta detalle : detalles) {

            System.out.println(detalle);

        }

        System.out.println("-----------------------------");
        System.out.println("Subtotal : S/ " + calcularSubtotal());
        System.out.println("IGV      : S/ " + calcularIGV());
        System.out.println("Total    : S/ " + calcularTotal());

    }

    public ArrayList<DetalleVenta> getDetalles() {
        return detalles;
    }

}

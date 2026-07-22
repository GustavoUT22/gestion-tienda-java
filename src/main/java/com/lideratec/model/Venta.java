package model;

import java.time.LocalDate;

public class Venta {
    private int id;
    private LocalDate fecha;
    private Cliente cliente;
    private Empleado empleado;
    private double subtotal;
    private double igv;
    private double total;

    public Venta(Cliente cliente, Empleado empleado, double subtotal, double igv, double total) {
        this.fecha = LocalDate.now();
        this.cliente = cliente;
        this.empleado = empleado;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getIgv() { return igv; }
    public double getTotal() { return total; }

    @Override
    public String toString() {
        return "Venta #" + id + "Fecha: " + fecha + "Cliente: " + cliente.getNombre() + "Total: S/ " + total;
    }
}

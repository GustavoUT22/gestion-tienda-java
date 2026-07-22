package model;

public class DetalleVenta {
    private int id;
    private Producto producto;
    private int cantidad;
    private double precio;
    private double subtotal;

    public DetalleVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = producto.getPrecio();
        this.subtotal = precio * cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public double getSubtotal() {
        return subtotal;
    }

    @Override
    public String toString() {
        return producto.getNombre() + "x" + cantidad + "= S/ " + subtotal;
    }
}

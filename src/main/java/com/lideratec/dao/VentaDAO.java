package com.lideratec.dao;

import com.lideratec.bd.ConexionBD;
import com.lideratec.model.Cliente;
import com.lideratec.model.DetalleVenta;
import com.lideratec.model.Empleado;
import com.lideratec.model.Producto;
import com.lideratec.model.Venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad Venta.
 */
public class VentaDAO {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();

    /**
     * Registra una venta completa junto con su lista de detalles.
     * Tambien descuenta el stock de cada producto vendido.
     *
     * @param venta    objeto Venta con cliente, empleado, subtotal, igv y total ya calculados
     * @param detalles lista de DetalleVenta (producto + cantidad) que componen la venta
     * @return true si la venta se registro correctamente, false si algo fallo (no se guarda nada)
     */
    public boolean registrarVenta(Venta venta, List<DetalleVenta> detalles) {
        String sqlVenta = "INSERT INTO ventas (fecha, id_cliente, id_empleado, subtotal, igv, total) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id = ?";

        Connection con = ConexionBD.getConexion();

        try {
            con.setAutoCommit(false); // inicia la transaccion

            // 1) Insertar la venta (cabecera)
            int idVentaGenerado;
            try (PreparedStatement psVenta = con.prepareStatement(sqlVenta, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psVenta.setDate(1, java.sql.Date.valueOf(venta.getFecha()));
                psVenta.setInt(2, venta.getCliente().getId());
                psVenta.setInt(3, venta.getEmpleado().getId());
                psVenta.setDouble(4, venta.getSubtotal());
                psVenta.setDouble(5, venta.getIgv());
                psVenta.setDouble(6, venta.getTotal());

                psVenta.executeUpdate();

                try (ResultSet rs = psVenta.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("No se pudo obtener el id de la venta generada.");
                    }
                    idVentaGenerado = rs.getInt(1);
                    venta.setId(idVentaGenerado);
                }
            }

            // 2) Insertar cada detalle y descontar stock
            try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
                 PreparedStatement psStock = con.prepareStatement(sqlStock)) {

                for (DetalleVenta detalle : detalles) {
                    psDetalle.setInt(1, idVentaGenerado);
                    psDetalle.setInt(2, detalle.getProducto().getId());
                    psDetalle.setInt(3, detalle.getCantidad());
                    psDetalle.setDouble(4, detalle.getPrecio());
                    psDetalle.setDouble(5, detalle.getSubtotal());
                    psDetalle.addBatch();

                    psStock.setInt(1, detalle.getCantidad());
                    psStock.setInt(2, detalle.getProducto().getId());
                    psStock.addBatch();
                }

                psDetalle.executeBatch();
                psStock.executeBatch();
            }

            con.commit(); // todo salio bien: se confirman los cambios
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar venta, se revierten los cambios: " + e.getMessage());
            try {
                con.rollback(); // algo fallo: se deshace todo
            } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
            return false;

        } finally {
            try {
                con.setAutoCommit(true); // vuelve al modo normal para el resto de la app
            } catch (SQLException e) {
                System.out.println("Error al restaurar autoCommit: " + e.getMessage());
            }
        }
    }

    /**
     * Busca una venta por id (sin sus detalles). Retorna null si no existe.
     */
    public Venta buscarPorId(int id) {
        String sql = "SELECT * FROM ventas WHERE id = ?";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearVenta(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar venta: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retorna el historial completo de ventas (RF11), ordenado por fecha descendente.
     */
    public List<Venta> listarTodas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas ORDER BY fecha DESC, id DESC";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ventas.add(mapearVenta(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar ventas: " + e.getMessage());
        }
        return ventas;
    }

    /**
     * Retorna el detalle (productos y cantidades) de una venta especifica.
     */
    public List<DetalleVenta> listarDetallesPorVenta(int idVenta) {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_venta WHERE id_venta = ?";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto producto = productoDAO.buscarPorId(rs.getInt("id_producto"));
                    DetalleVenta detalle = new DetalleVenta(producto, rs.getInt("cantidad"));
                    detalle.setId(rs.getInt("id"));
                    detalles.add(detalle);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar detalles de venta: " + e.getMessage());
        }
        return detalles;
    }

    /**
     * Convierte una fila del ResultSet en un objeto Venta.
     * Reconstruye Cliente y Empleado consultando sus respectivos DAO.
     */
    private Venta mapearVenta(ResultSet rs) throws SQLException {
        Cliente cliente = clienteDAO.buscarPorId(rs.getInt("id_cliente"));
        Empleado empleado = empleadoDAO.buscarPorId(rs.getInt("id_empleado"));

        Venta venta = new Venta(
                cliente,
                empleado,
                rs.getDouble("subtotal"),
                rs.getDouble("igv"),
                rs.getDouble("total")
        );
        venta.setId(rs.getInt("id"));
        return venta;
    }
}
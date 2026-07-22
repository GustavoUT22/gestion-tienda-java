package com.lideratec.dao;

import com.lideratec.bd.ConexionBD;
import model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad Producto.
 * Se encarga de todas las operaciones CRUD contra la tabla "productos".
 */
public class ProductoDAO {

    /**
     * Inserta un nuevo producto en la base de datos.
     * Al insertar, el id generado por MySQL se asigna de vuelta al objeto.
     */
    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO productos (codigo, nombre, categoria, precio, stock) VALUES (?, ?, ?, ?, ?)";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCategoria());
            ps.setDouble(4, producto.getPrecio());
            ps.setInt(5, producto.getStock());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        producto.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los datos de un producto existente (busca por id).
     */
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET codigo = ?, nombre = ?, categoria = ?, precio = ?, stock = ? WHERE id = ?";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCategoria());
            ps.setDouble(4, producto.getPrecio());
            ps.setInt(5, producto.getStock());
            ps.setInt(6, producto.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza solamente el stock de un producto (util despues de una venta).
     */
    public boolean actualizarStock(int idProducto, int nuevoStock) {
        String sql = "UPDATE productos SET stock = ? WHERE id = ?";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevoStock);
            ps.setInt(2, idProducto);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un producto por su id.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un producto por su codigo (ej: "P001").
     * Retorna null si no lo encuentra.
     */
    public Producto buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto por codigo: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca un producto por su id. Retorna null si no lo encuentra.
     */
    public Producto buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id = ?";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto por id: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retorna todos los productos registrados.
     */
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY nombre";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
        return productos;
    }

    /**
     * Convierte una fila del ResultSet en un objeto Producto.
     * Centralizar esto evita repetir el mismo mapeo en cada metodo.
     */
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto(
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getString("categoria"),
                rs.getDouble("precio"),
                rs.getInt("stock")
        );
        producto.setId(rs.getInt("id"));
        return producto;
    }
}
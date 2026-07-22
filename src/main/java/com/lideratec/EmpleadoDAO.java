package com.lideratec.dao;

import com.lideratec.bd.ConexionBD;
import model.Empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad Empleado.
 * Mismo patron que ClienteDAO: CRUD contra la tabla "empleados".
 */
public class EmpleadoDAO {

    /**
     * Inserta un nuevo empleado. Al insertar, el id generado por MySQL
     * se asigna de vuelta al objeto con setId().
     */
    public boolean insertar(Empleado empleado) {
        String sql = "INSERT INTO empleados (dni, nombre, celular, correo, cargo, sueldo) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, empleado.getDni());
            ps.setString(2, empleado.getNombre());
            ps.setString(3, empleado.getCelular());
            ps.setString(4, empleado.getCorreo());
            ps.setString(5, empleado.getCargo());
            ps.setDouble(6, empleado.getSueldo());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        empleado.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.out.println("Error al insertar empleado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los datos de un empleado existente.
     * El objeto "empleado" debe traer el id ya asignado (via setId).
     */
    public boolean actualizar(Empleado empleado) {
        String sql = "UPDATE empleados SET dni = ?, nombre = ?, celular = ?, correo = ?, cargo = ?, sueldo = ? WHERE id = ?";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, empleado.getDni());
            ps.setString(2, empleado.getNombre());
            ps.setString(3, empleado.getCelular());
            ps.setString(4, empleado.getCorreo());
            ps.setString(5, empleado.getCargo());
            ps.setDouble(6, empleado.getSueldo());
            ps.setInt(7, empleado.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar empleado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un empleado por su id.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM empleados WHERE id = ?";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar empleado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un empleado por su DNI. Retorna null si no lo encuentra.
     */
    public Empleado buscarPorDni(String dni) {
        String sql = "SELECT * FROM empleados WHERE dni = ?";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearEmpleado(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar empleado por DNI: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca un empleado por su id. Retorna null si no lo encuentra.
     */
    public Empleado buscarPorId(int id) {
        String sql = "SELECT * FROM empleados WHERE id = ?";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearEmpleado(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar empleado por id: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retorna todos los empleados registrados.
     */
    public List<Empleado> listarTodos() {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM empleados ORDER BY nombre";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar empleados: " + e.getMessage());
        }
        return empleados;
    }

    /**
     * Convierte una fila del ResultSet en un objeto Empleado.
     */
    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        Empleado empleado = new Empleado(
                rs.getString("dni"),
                rs.getString("nombre"),
                rs.getString("celular"),
                rs.getString("correo"),
                rs.getString("cargo"),
                rs.getDouble("sueldo")
        );
        empleado.setId(rs.getInt("id"));
        return empleado;
    }
}
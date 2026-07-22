package com.lideratec.dao;

import com.lideratec.bd.ConexionBD;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad Usuario.
 * Ademas del CRUD basico, incluye un metodo para validar login (RF01).
 */
public class UsuarioDAO {

    /**
     * Inserta un nuevo usuario. Al insertar, el id generado por MySQL
     * se asigna de vuelta al objeto con setId().
     */
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (usuario, contrasena, tipo_acceso) VALUES (?, ?, ?)";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getContrasena());
            ps.setString(3, usuario.getTipo());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los datos de un usuario existente.
     * El objeto "usuario" debe traer el id ya asignado (via setId).
     */
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET usuario = ?, contrasena = ?, tipo_acceso = ? WHERE id = ?";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getContrasena());
            ps.setString(3, usuario.getTipo());
            ps.setInt(4, usuario.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un usuario por su id.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un usuario por su nombre de usuario. Retorna null si no lo encuentra.
     */
    public Usuario buscarPorUsuario(String nombreUsuario) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ?";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca un usuario por su id. Retorna null si no lo encuentra.
     */
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar usuario por id: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retorna todos los usuarios registrados.
     */
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY usuario";
        Connection con = ConexionBD.getConexion();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    /**
     * Valida el login (RF01): busca el usuario y compara la contrasena.
     * Retorna el Usuario si las credenciales son correctas, o null si no.
     *
     * Nota: para el alcance de este proyecto academico se compara la
     * contrasena en texto plano. En un sistema real se guardaria un hash
     * (por ejemplo con BCrypt) en vez de la contrasena original.
     */
    public Usuario validarCredenciales(String nombreUsuario, String contrasena) {
        Usuario usuario = buscarPorUsuario(nombreUsuario);

        if (usuario != null && usuario.getContrasena().equals(contrasena)) {
            return usuario;
        }
        return null;
    }

    /**
     * Convierte una fila del ResultSet en un objeto Usuario.
     */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario(
                rs.getString("usuario"),
                rs.getString("contrasena"),
                rs.getString("tipo_acceso")
        );
        usuario.setId(rs.getInt("id"));
        return usuario;
    }
}
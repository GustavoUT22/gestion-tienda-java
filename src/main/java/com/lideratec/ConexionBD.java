package com.lideratec.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de administrar la conexion JDBC hacia MySQL.
 * Se usa un unico punto de conexion (patron simple, no pool) suficiente
 * para el alcance de este proyecto academico.
 */
public class ConexionBD {

    // --- Ajustar estos datos segun la configuracion local de MySQL --
    private static final String HOST = "localhost";
    private static final String PUERTO = "3306";
    private static final String BASE_DATOS = "gestion_tienda";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "3620924a"; // TODO: reemplazar por tu password de MySQL

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DATOS +
                    "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static Connection conexion;

    private ConexionBD() {
        // Constructor privado: no se debe instanciar, se usa de forma estatica.
    }

    /**
     * Retorna una conexion activa a la base de datos
     * si no existe o esta cerrada, crea una nueva
     */
    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("Conexion a la base de datos establecida correctamente.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontro el driver de MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
    }

    /**
     * Cierra la conexion activa si existe
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexion cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexion: " + e.getMessage());
        }
    }

    /**
     * Metoodo rapido para probar que la conexion funciona
     * Puedes llamarlo desde Main.java para verificar la configuracion.
     */
    public static void probarConexion() {
        Connection c = getConexion();
        if (c != null) {
            System.out.println("Prueba de conexion exitosa.");
        } else {
            System.out.println("Prueba de conexion fallida.");
        }
    }
}

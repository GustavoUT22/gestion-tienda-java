package com.lideratec;

import com.lideratec.bd.ConexionBD;
import com.lideratec.vista.MenuPrincipal;

/**
 * Punto de entrada del sistema de gestion de tienda.
 *
 * Solo se encarga de verificar la conexion a la base de datos,
 * lanzar la interfaz de consola y cerrar la conexion al terminar.
 */
public class Main {

    public static void main(String[] args) {

        if (ConexionBD.getConexion() == null) {
            System.out.println("No se pudo conectar a la base de datos.");
            System.out.println("Revise las credenciales en ConexionBD.java y que MySQL este encendido.");
            return;
        }

        try {
            // Llamamos al menú princial
            MenuPrincipal menu = new MenuPrincipal();
            menu.iniciar();
        } finally {
            ConexionBD.cerrarConexion();
        }
    }
}

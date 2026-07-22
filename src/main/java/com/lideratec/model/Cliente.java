package model;

public class Cliente extends Persona {

    public Cliente(String dni, String nombre, String celular, String correo) {
        super(dni, nombre, celular, correo);
    }

    @Override
    public void mostrarInformacion() {
        System.out.print("Cliente: " + nombre + "DNI: " + dni + "Celular: " + celular);
    }

}

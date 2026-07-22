package model;

public abstract class Persona {
    protected int id;
    protected String dni;
    protected String nombre;
    protected String celular;
    protected String correo;

    public Persona(String dni, String nombre, String celular, String correo) {
        this.dni = dni;
        this.nombre = nombre;
        this.celular = celular;
        this.correo = correo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCelular() {
        return celular;
    }

    public String getCorreo() {
        return correo;
    }

    public abstract void mostrarInformacion();

}

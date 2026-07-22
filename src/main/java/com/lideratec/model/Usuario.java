package model;

public class Usuario {
    private int id;
    private String usuario;
    private String contrasena;
    private String tipoAcceso;

    public Usuario(String usuario, String contrasena, String tipoAcceso) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.tipoAcceso = tipoAcceso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getTipo() {
        return tipoAcceso;
    }

    @Override
    public String toString() {
        return "Usuario: " + usuario + "Rol: " + tipoAcceso;
    }
}
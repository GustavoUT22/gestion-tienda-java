package com.lideratec.model;

public class Empleado extends Persona {
    private String cargo;
    private double sueldo;

    public Empleado(String dni, String nombre, String celular, String correo, String cargo, double sueldo) {
        super(dni, nombre, celular, correo);
        this.cargo = cargo;
        this.sueldo = sueldo;
    }

    public String getCargo() {
        return cargo;
    }

    public double getSueldo() {
        return sueldo;
    }

    @Override
    public void mostrarInformacion() {
        System.out.print("Empleado: " + nombre + "Cargo: " + cargo + "Sueldo: S/" + sueldo);
    }
}

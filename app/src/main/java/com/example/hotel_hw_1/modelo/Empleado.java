/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: Empleado.java
 *
 */

package com.example.hotel_hw_1.modelo;


/**
 *
 */
public class Empleado {
    private String id;

    private String nombre;
    private String apellidos;
    private String rol;       // Aca defino el tipo de empleado que sera!!!!
    private String email;
    private String telefono;

    public Empleado() {
    }

    public Empleado(String nombre, String apellidos, String rol, String email, String telefono) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.rol = rol;
        this.email = email;
        this.telefono = telefono;
    }

    public String getId() {  return id;}

    public void setId(String id) { this.id = id;}

    // Getters
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getRol() { return rol; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setRol(String rol) { this.rol = rol; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toString() {
        return nombre + " " + apellidos + " - " + rol;
    }
}


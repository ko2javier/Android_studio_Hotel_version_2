/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: Huesped.java

 */

package com.example.hotel_hw_1.modelo;

import java.io.Serializable;

public class Huesped implements Serializable {
    private String id; // ID único de Firebase
    private String nombre;
    private String apellidos;
    private String telefono;
    private String habitacion;
    private String fechaEntrada; // Fecha de llegada
    private boolean checkInActivo; // true = hospedado, false = check-out

    public Huesped(String nombre, String apellidos, String telefono, String habitacion, String fechaEntrada) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.habitacion = habitacion;
        this.fechaEntrada = fechaEntrada;
        this.checkInActivo = true; // Por defecto entra como hospedado
    }

    public Huesped() {
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getFechaEntrada() { return fechaEntrada; }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getTelefono() { return telefono; }
    public String getHabitacion() { return habitacion; }
    public boolean isCheckInActivo() { return checkInActivo; }

    public void setCheckInActivo(boolean activo) { this.checkInActivo = activo; }

    public String getEstado() {
        return checkInActivo ? "Hospedado" : "Check-Out";
    }
}


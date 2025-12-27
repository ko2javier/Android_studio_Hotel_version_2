/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: Tarea.java
 *
 */

package com.example.hotel_hw_1.modelo;

public class Tarea {
    private String tipoTarea;
    private String planta;
    private String habitacion;

    private String pasillo; // norte , sur, este y oeste
    private String estado;
    private String asignadaA;

    public Tarea(String tipoTarea, String planta, String habitacion,  String pasillo) {
        this.tipoTarea = tipoTarea;
        this.planta = planta;
        this.habitacion = habitacion;

        this.pasillo = pasillo;
        this.estado = "Pendiente";
        this.asignadaA = "Sin asignar";
    }

    public Tarea() {
    }

    // Getters y Setters
    public String getTipoTarea() { return tipoTarea; }
    public String getPlanta() { return planta; }
    public String getHabitacion() { return habitacion; }

    public String getPasillo() { return pasillo; }
    public String getEstado() { return estado; }
    public String getAsignadaA() { return asignadaA; }

    public void setEstado(String estado) { this.estado = estado; }
    public void setAsignadaA(String asignadaA) { this.asignadaA = asignadaA; }
}

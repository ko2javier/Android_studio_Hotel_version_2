/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: Tarea.java
 *
 */

package com.example.hotel_hw_1.modelo;

public class Tarea {
    private String id;              // ID único de Firebase (ej: -Nj8s9d...)

    // Datos del Servicio
    private String tipoServicio;    // "Limpieza" o "Mantenimiento"
    private String tipoUbicacion;   // "Habitacion" o "Pasillo"

    // Ubicación exacta
    private String planta;          // "1", "2", "3", "4", "5" (OBLIGATORIO)
    private String numeroHabitacion;// "101", "502" (o vacío "" si es Pasillo)
    private String zona;            // Lo que sale del Spinner ("Norte", "Sur", "Baño", "Salón", etc.)

    // Estado y Asignación
    private String estado;          // "Pendiente", "Asignada", "Completada"
    private String asignadaA;

    private long timestamp;         // Fecha en milisegundos (Solo para ordenar la lista)


    public Tarea() {
    }


    public Tarea(String tipoServicio, String tipoUbicacion, String planta, String numeroHabitacion, String zona) {
        this.tipoServicio = tipoServicio;
        this.tipoUbicacion = tipoUbicacion;
        this.planta = planta;
        this.numeroHabitacion = numeroHabitacion; // Si es pasillo, vendrá vacío
        this.zona = zona;

        // Valores automáticos por defecto
        this.estado = "Pendiente";
        this.asignadaA = "Sin asignar";
        this.timestamp = System.currentTimeMillis(); // Guarda la hora actual automáticamente
    }

    // 3. GETTERS Y SETTERS

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public String getTipoUbicacion() { return tipoUbicacion; }
    public void setTipoUbicacion(String tipoUbicacion) { this.tipoUbicacion = tipoUbicacion; }

    public String getPlanta() { return planta; }
    public void setPlanta(String planta) { this.planta = planta; }

    public String getNumeroHabitacion() { return numeroHabitacion; }
    public void setNumeroHabitacion(String numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getAsignadaA() { return asignadaA; }
    public void setAsignadaA(String asignadaA) { this.asignadaA = asignadaA; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    // Método extra para mostrar un resumen bonito en los listados (RecyclerView)
    public String getResumenUbicacion() {
        if (tipoUbicacion.equalsIgnoreCase("Pasillo")) {
            return "Planta " + planta + " - Pasillo " + zona;
        } else {
            return "Habitación " + numeroHabitacion + " (" + zona + ")";
        }
    }
}

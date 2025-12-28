package com.example.hotel_hw_1.modelo;

/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 *
 */

public class Habitacion {
    private int planta;
    private String numero;
    private String tipo;
    private String estado; // "Libre", "Ocupada", "Reservada"
    private String limpieza; // "", "Pendiente", "Asignado"
    private String mantenimiento; // "", "Pendiente", "Asignado"
    private String fechaReserva;
    private String serviciosExtra;
    private String nombreHuesped;

    // Constructor vacío requerido por Firebase
    public Habitacion() {}

    public Habitacion(String numero, String estado, String fechaReserva, String nombreHuesped) {
        this.numero = numero;
        this.estado = estado;
        this.fechaReserva = fechaReserva;
        this.nombreHuesped = nombreHuesped;
    }

    // Getters
    public int getPlanta() { return planta; }
    public String getNombreHuesped() { return nombreHuesped;}

    public void setNombreHuesped(String nombreHuesped) { this.nombreHuesped = nombreHuesped; }
    public String getNumero() { return numero; }
    public String getTipo() { return tipo; }
    public String getEstado() { return estado; }
    public String getLimpieza() { return limpieza; }
    public String getMantenimiento() { return mantenimiento; }
    public String getFechaReserva() { return fechaReserva; }
    public String getServiciosExtra() { return serviciosExtra; }

    // Setters )

    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public void setPlanta(int planta) {
        this.planta = planta;
    }

    public void setServiciosExtra(String serviciosExtra) {
        this.serviciosExtra = serviciosExtra;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setEstado(String estado) { this.estado = estado; }
    public void setLimpieza(String limpieza) { this.limpieza = limpieza; }
    public void setMantenimiento(String mantenimiento) { this.mantenimiento = mantenimiento; }
}

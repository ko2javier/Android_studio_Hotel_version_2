package com.example.hotel_hw_1.modelo;
/**
 * Autor: K. Jabier O'Reilly
 *
 */


public class Reserva {
    private String idReserva;
    private String nombreCliente;
    private String habitacion;
    private String fechaEntrada;
    private String fechaSalida;
    private String tipoHabitacion; // "Doble/simple/triple"
    private String servicios;      // Spa/Parking"
    private String regimen;        // "Pensión Completa/Media Pension"
    private String estado;         // "PENDIENTE", "ACTIVA", "COMPLETADA", "CANCELADA"

    public Reserva() {}

    // Constructor completo
    public Reserva(String idReserva, String nombreCliente, String habitacion, String fechaEntrada, String fechaSalida, String tipoHabitacion, String servicios, String regimen, String estado) {
        this.idReserva = idReserva;
        this.nombreCliente = nombreCliente;
        this.habitacion = habitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.tipoHabitacion = tipoHabitacion;
        this.servicios = servicios;
        this.regimen = regimen;
        this.estado = estado;
    }


    public String getIdReserva() { return idReserva; }
    public void setIdReserva(String idReserva) { this.idReserva = idReserva; }

    public String getNombreCliente() {return nombreCliente;  }

    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }


    public String getHabitacion() { return habitacion; }
    public void setHabitacion(String habitacion) { this.habitacion = habitacion; }
    public String getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(String fechaEntrada) { this.fechaEntrada = fechaEntrada; }
    public String getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(String fechaSalida) { this.fechaSalida = fechaSalida; }
    public String getTipoHabitacion() { return tipoHabitacion; }
    public void setTipoHabitacion(String tipoHabitacion) { this.tipoHabitacion = tipoHabitacion; }
    public String getServicios() { return servicios; }
    public void setServicios(String servicios) { this.servicios = servicios; }
    public String getRegimen() { return regimen; }
    public void setRegimen(String regimen) { this.regimen = regimen; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
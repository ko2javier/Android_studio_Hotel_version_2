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
    public Huesped(String nombre, String apellidos, String telefono, String habitacion, boolean checkInActivo) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.habitacion = habitacion;
        this.checkInActivo = true; //
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
    // Metodo para registrar salida
    public void registrarSalida() {
        // 1. Obtengo  fecha de hoy como String
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        String fechaHoy = sdf.format(new java.util.Date());

        // 2. Concateno "FechaEntrada|FechaSalida"
        //  Asumo que fechaEntrada ya tiene la fecha de llegada
        if (this.fechaEntrada != null && !this.fechaEntrada.contains("|")) {
            this.fechaEntrada = this.fechaEntrada + "|" + fechaHoy;
        }

        // 3. Desactivamos el check-in
        this.checkInActivo = false;
    }
    /*Metodo para obtener el rango de fechas en millisegundos
    * tomando los valores de FechaEntrada*/
    public long[] obtenerRangoFechasEnMilisegundos() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        long inicio = 0;
        long fin = System.currentTimeMillis(); // Por defecto "ahora"

        try {
            if (fechaEntrada != null) {
                if (fechaEntrada.contains("|")) {
                    // CASO 1: Ya hizo Check-out ("12-12-2025|01-01-2026")
                    String[] partes = fechaEntrada.split("\\|");
                    inicio = sdf.parse(partes[0]).getTime();
                    fin = sdf.parse(partes[1]).getTime();
                } else {
                    // CASO 2: Aún está dentro ("12-12-2025") no ha salido !!
                    inicio = sdf.parse(fechaEntrada).getTime();
                    // El fin sigue siendo "ahora"
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new long[]{inicio, fin};
    }

    public boolean esMismoPersona(String nombreHuesped, String apellidosHuesped) {

        /*Elimino espacios si es que los hay en lo que necesito comparar y devuelvo true
         si coincide nombre y apellidos
         esto es para comparar un usuario con un huesped
         */
        boolean nombreCoincide = this.nombre.trim().equalsIgnoreCase(nombreHuesped.trim());
        boolean apellidoCoincide = this.apellidos.trim().equalsIgnoreCase(apellidosHuesped.trim());

        return nombreCoincide && apellidoCoincide;
    }
}



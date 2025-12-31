/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: Usuario.java
 *
 */

package com.example.hotel_hw_1.modelo;

import android.content.Context;
import android.content.Intent;

import com.example.hotel_hw_1.actividad.GerenteActivity;
import com.example.hotel_hw_1.actividad.HuespedActivity;
import com.example.hotel_hw_1.actividad.LimpiadorActivity;
import com.example.hotel_hw_1.actividad.MantenimientoActivity;
import com.example.hotel_hw_1.actividad.RecepcionistaActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Serializable {
    private static Usuario instance;

    private String email;
    private String pass;
    private String nombre;
    private String apellidos;
    private String telefono;

    // Defino campo reservas para los huspedes!!
    private List<String> reservas = new ArrayList<>();
    private String tipo_usuario; // Ejemplo: gerente, recepcionista, limpieza, mantenimiento, huesped

    // Singleton
    public static Usuario getInstance() {
        if (instance == null) instance = new Usuario();
        return instance;
    }

    public static void setInstance(Usuario u) {
        instance = u;
    }

    // Constructores
    public Usuario() {}

    public Usuario(String email, String pass, String tipo_usuario, String nombre,
                   String apellidos, String telefono) {
        this.email = email;
        this.pass = pass;
        this.tipo_usuario = tipo_usuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
    }

    // Getters y setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTipo_usuario() { return tipo_usuario; }
    public void setTipo_usuario(String tipo_usuario) { this.tipo_usuario = tipo_usuario; }

    // Método para obtener pantalla según el tipo
    public Intent obtenerPantalla(Context context) {
        if (tipo_usuario == null) return null;

        switch (tipo_usuario.toLowerCase()) {
            case "gerente":
                return new Intent(context, GerenteActivity.class);
            case "recepcionista":
                return new Intent(context, RecepcionistaActivity.class);
            case "limpieza":
                return new Intent(context, LimpiadorActivity.class);
            case "mantenimiento":
                return new Intent(context, MantenimientoActivity.class);
            case "huesped":
                return new Intent(context, HuespedActivity.class);
            default:
                return null;
        }
    }

// adiciono reserva!!
    public void agregarReserva(String reserva) {
        reservas.add(reserva);
    }
    public List<String> getReservas(){
        return this.reservas;
    }


}

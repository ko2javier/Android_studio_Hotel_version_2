package com.example.hotel_hw_1.modelo;
/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 *
 */

/*
* ItemOcupacion es una clase envoltorio
* que permite unificar cabeceras de texto y objetos de
* habitación en una sola lista para que el RecyclerView
* pueda pintarlos de forma secuencial.*/
public class ItemOcupacion {
    public static final int TIPO_CABECERA = 0;
    public static final int TIPO_HABITACION = 1;

    private int tipoVista;
    private String titulo;
    private Habitacion habitacion;

    // Nuevos campos para la estadística
    private int disponibles;
    private int ocupadas;

    // Constructor para Cabeceras con estadísticas
    public ItemOcupacion(String titulo, int disponibles, int ocupadas) {
        this.tipoVista = TIPO_CABECERA;
        this.titulo = titulo;
        this.disponibles = disponibles;
        this.ocupadas = ocupadas;
    }

    public ItemOcupacion(Habitacion habitacion) {
        this.tipoVista = TIPO_HABITACION;
        this.habitacion = habitacion;
    }

    // Getters
    public int getTipoVista() { return tipoVista; }
    public String getTitulo() { return titulo; }
    public Habitacion getHabitacion() { return habitacion; }
    public int getDisponibles() { return disponibles; }
    public int getOcupadas() { return ocupadas; }
}
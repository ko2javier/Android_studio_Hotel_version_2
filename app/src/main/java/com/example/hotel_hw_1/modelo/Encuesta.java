/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: Encuesta.java

 */

package com.example.hotel_hw_1.modelo;

public class Encuesta {
    private String id;
    private String categoria;
    private float promedio;
    private int cantidad;
    private int cantidadOpiniones;
    private boolean visible; // este parametro me permite completar la idea de mostrar encuestas segun rol!!

    public Encuesta() {
    }
    public Encuesta(String id, String categoria, float promedio, int cantidad, boolean visible) {
        this.id = id;
        this.categoria = categoria;
        this.promedio = promedio;
        this.cantidad = cantidad;
        this.visible = visible;
    }
    public Encuesta(String categoria, float promedio, int cantidad, boolean visible) {
        this.categoria = categoria;
        this.promedio = promedio;
        this.cantidad = cantidad;
        this.visible = visible;
    }
    public Encuesta(String id, String categoria, float promedio, int cantidad, int cantidadOpiniones, boolean visible) {
        this.id = id;
        this.categoria = categoria;
        this.promedio = promedio;
        this.cantidad = cantidad;
        this.cantidadOpiniones = cantidadOpiniones;
        this.visible = visible;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCategoria() { return categoria; }
    public float getPromedio() { return promedio; }
    public void setPromedio(float promedio){
        this.promedio= promedio;
    }
    public int getCantidad() { return cantidad; }
    public boolean isVisible() { return visible; }

    public void setVisible(boolean visible) { this.visible = visible; }

    public int getCantidadOpiniones() {
        return cantidadOpiniones;
    }

    public void setCantidadOpiniones(int cantidadOpiniones) {
        this.cantidadOpiniones = cantidadOpiniones;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}

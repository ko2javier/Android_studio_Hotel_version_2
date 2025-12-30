package com.example.hotel_hw_1.modelo;
/**
 * Autor: K. Jabier O'Reilly
  *
 */
public class DetalleVoto {
    private float nota;
    private String comentario;
    private long fecha;


    public DetalleVoto() {
    }

    // Constructor completo
    public DetalleVoto(float nota, String comentario, long fecha) {
        this.nota = nota;
        this.comentario = comentario;
        this.fecha = fecha;
    }


    public float getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }

    public long getFecha() {
        return fecha;
    }

    public void setNota(float nota) { this.nota = nota; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public void setFecha(long fecha) { this.fecha = fecha; }
}
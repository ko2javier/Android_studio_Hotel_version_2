package com.example.hotel_hw_1.repositorio;
/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: Usuario.java
 *
 */
import com.example.hotel_hw_1.modelo.Usuario;
public interface LoginCallback {
    /**
     *Defino la interface porque Firebase es asincrono
     * asi que espero la respuesta de mi peticion
     * y con la interface recupero el valor
     */
    void onSuccess(Usuario usuario);
    void onError(String mensaje);
}

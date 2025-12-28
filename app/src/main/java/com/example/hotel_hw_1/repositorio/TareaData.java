/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: TareaData.java

 */

package com.example.hotel_hw_1.repositorio;

/*
import com.example.hotel_hw_1.modelo.Tarea;

import java.util.ArrayList;
import java.util.List;

public class TareaData {

    private static final List<Tarea> tareas = new ArrayList<>();



    // Esto es fundamental --> Añadir nueva tarea  desde huesped
    public static void agregarTarea(Tarea t) {
        tareas.add(t);
    }

    // Obtener todas las tareas
    public static List<Tarea> getTareas() {
        return tareas;
    }

    // Filtrar por tipo (Limpieza / Mantenimiento) para separar lo que muestro segun rol !!
    public static List<Tarea> getTareasPorTipo(String tipo) {
        List<Tarea> lista = new ArrayList<>();
        for (Tarea t : tareas) {
            if (t.getTipoServicio() .equalsIgnoreCase(tipo)) {
                lista.add(t);
            }
        }
        return lista;
    }

    // Obtener tareas sin asignar
    public static List<Tarea> getTareasSinAsignar(String tipo) {
        List<Tarea> disponibles = new ArrayList<>();
        for (Tarea t : tareas) {
            if (t.getTipoServicio().equalsIgnoreCase(tipo)
                    && t.getAsignadaA().equalsIgnoreCase("Sin asignar")) {
                disponibles.add(t);
            }
        }
        return disponibles;
    }


    // Asignar tarea a empleado ( Valido para Gerente o para autoasignarse)
    public static boolean asignarTarea(Tarea tarea, String empleado) {
        if (tarea.getAsignadaA().equalsIgnoreCase("Sin asignar")) {
            tarea.setAsignadaA(empleado);
            tarea.setEstado("Asignada");
            return true;
        }
        return false;
    }

    // Comprobar si un empleado puede autoasignarse (limite diario)
    public static boolean puedeAutoAsignarse(String empleado, String tipo, int limite) {
        int contador = 0;
        for (Tarea t : tareas) {
            if ( t.getAsignadaA().equalsIgnoreCase(empleado)) {
                contador++;
            }
        }
        return contador < limite; // si el limite para este empleado fue alcanzado mando false!!!
    }
}
*/

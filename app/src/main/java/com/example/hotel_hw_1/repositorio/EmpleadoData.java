/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: EmpleadoData.java

 */

package com.example.hotel_hw_1.repositorio;

import com.example.hotel_hw_1.modelo.Empleado;

import java.util.ArrayList;
import java.util.List;

public class EmpleadoData {
/*
    private static final List<Empleado> listaEmpleados = new ArrayList<>();



    public static List<Empleado> getEmpleados() {
        return listaEmpleados;
    }

    public static void agregarEmpleado(Empleado e) {
        listaEmpleados.add(e);
    }

    public static void eliminarEmpleado(Empleado e) {
        listaEmpleados.remove(e);
    }

    public static void actualizarEmpleado(int index, Empleado nuevo) {
        listaEmpleados.set(index, nuevo);
    }

    public static Empleado getEmpleadoPorNombre(String nombre, String apellidos) {
        for (Empleado e : listaEmpleados) {
            if (e.getNombre().equalsIgnoreCase(nombre) && e.getApellidos().equalsIgnoreCase(apellidos)) {
                return e;
            }
        }
        return null;
    }

    /*
    * Con este metodo garatizo entregar una lista de nombres
    * segun el rol que ocupan
    * asi puedo asignar a quien sea segun su especialidad
    * */
/*
    public static String[] getNombresPorRol(String rol) {
        String nombres_total = "";

        for (Empleado e : listaEmpleados) {
            if (e.getRol().equalsIgnoreCase(rol)) {
                nombres_total += e.getNombre() + " " + e.getApellidos() + ";";
            }
        }
        // Dividimos los nombres en un array
        String[] nombres = nombres_total.split(";");
        return nombres;
    }
*/

}

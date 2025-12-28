package com.example.hotel_hw_1.repositorio;
/**
 * Autor: K. Jabier O'Reilly
 *
 */

import com.example.hotel_hw_1.modelo.Huesped;

import java.util.ArrayList;
import java.util.List;

public class HuespedData {
    private static final List<Huesped> listaHuespedes = new ArrayList<>();
// agrego una lista de varios usuarios

    public static void agregarHuesped(Huesped h) {
        listaHuespedes.add(h);
    }

    /*
    *  2 respuestas de este método
    *  null si no hay gest , el húesped si es que está*/
    public static Huesped buscarHuesped(String nombre, String apellidos) {
        for (Huesped h : listaHuespedes) {
            if (h.getNombre().equalsIgnoreCase(nombre) &&
                    h.getApellidos().equalsIgnoreCase(apellidos)) {
                return h;
            }
        }
        return null;
    }

// Ponemos el check out en el usuario para que se vea en la lista!!
    public static boolean marcarCheckOut(String nombre, String apellidos) {
        Huesped h = buscarHuesped(nombre, apellidos);
        if (h != null && h.isCheckInActivo()) {
            h.setCheckInActivo(false);
            return true;
        }
        return false;
    }

    public static List<Huesped> listarHuespedes() {
        return listaHuespedes;
    }
}


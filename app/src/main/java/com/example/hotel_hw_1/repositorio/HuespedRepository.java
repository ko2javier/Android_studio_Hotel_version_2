package com.example.hotel_hw_1.repositorio;

/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: AdapterHuesped.java
 *
 */


import android.util.Log;
import androidx.annotation.NonNull;

import com.example.hotel_hw_1.modelo.Huesped;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HuespedRepository {

    // 1. Listado para la carga sincrona de datos
    private static final List<Huesped> listaHuespedesCache = new ArrayList<>();

    public interface HuespedCallback {
        void onSuccess(String mensaje);
        void onError(String error);
    }

    // 2. Referencia al nodo "huespedes" en Firebase
    private static final DatabaseReference dbHuespedes =
            FirebaseDatabase.getInstance().getReference("huespedes");

    /**
     * con este metodo cargamos la lista y la mantenemos actualizada de los cambios
        */
    public static void inicializarListener() {
        dbHuespedes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaHuespedesCache.clear();
                for (DataSnapshot doc : snapshot.getChildren()) {
                    Huesped h = doc.getValue(Huesped.class);
                    if (h != null) {
                        h.setId(doc.getKey()); // Seteamos el ID de Firebase en el objeto
                        listaHuespedesCache.add(h);
                    }
                }
                Log.d("REPO_HUESPED", "Huéspedes sincronizados: " + listaHuespedesCache.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("REPO_HUESPED", "Error en Firebase: " + error.getMessage());
            }
        });
    }


    // MÉTODOS DE CONSULTA (Lectura)

    /**
     * Retorna todos los huéspedes de la base de datos.
     */
    public static List<Huesped> getTodosLosHuespedes() {
        return new ArrayList<>(listaHuespedesCache);
    }

    /**
     * Retorna solo los huéspedes que tienen el Check-In activo (Hospedados).
     */
    public static List<Huesped> getHuespedesActivos() {
        List<Huesped> resultado = new ArrayList<>();
        for (Huesped h : listaHuespedesCache) {
            if (h.isCheckInActivo()) {
                resultado.add(h);
            }
        }
        return resultado;
    }

    /**
     * Busca un huésped activo por nombre y apellidos
     */
    public static Huesped buscarHuespedActivo(String nombre, String apellidos) {
        for (Huesped h : listaHuespedesCache) {
            // Usamos equalsIgnoreCase para evitar problemas con mayúsculas/minúsculas
            if (h.getNombre().equalsIgnoreCase(nombre) &&
                    h.getApellidos().equalsIgnoreCase(apellidos) &&
                    h.isCheckInActivo()) {
                return h;
            }
        }
        return null;
    }
    /**
     * Busqueda de huesped por habitacion!!
     *      */
    public static Huesped getHuespedPorHabitacion(String numHabitacion) {
        for (Huesped h : listaHuespedesCache) {
            if (h.getHabitacion().equals(numHabitacion) && h.isCheckInActivo()) {
                return h;
            }
        }
        return null;
    }

  // Escritura de Datos

    /**
     *  Check-Out de un huesped cambiando cambiando su estado!!!
     */
    public static void realizarCheckOut(String idHuesped, HuespedCallback callback) {
        dbHuespedes.child(idHuesped).child("checkInActivo").setValue(false)
                .addOnSuccessListener(v -> callback.onSuccess("Check-Out realizado con éxito"))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    // Crear huesped ok!!
    public static void crearHuesped(Huesped huesped, HuespedCallback callback) {
        dbHuespedes.push().setValue(huesped)
                .addOnSuccessListener(v -> callback.onSuccess("Huésped registrado correctamente"))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }
}

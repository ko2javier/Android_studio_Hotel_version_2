package com.example.hotel_hw_1.repositorio;
/**
 * Autor: K. Jabier O'Reilly
 *
 */

import androidx.annotation.NonNull;

import com.example.hotel_hw_1.modelo.Reserva;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ReservaRepository {

    // Referencias
    private static final DatabaseReference dbReservas = FirebaseDatabase.getInstance().getReference("reservas");
    private static final ArrayList<Reserva> listaReservasLocal = new ArrayList<>();

    // Interfaz necesaria para los metodos sin sincronismo como escritura de datos en Firebase!!
    public interface ReservaCallback {
        void onSuccess(String mensaje);
        void onError(String error);
    }

    // --- 1. Iniciarlizacion
    /**
   Carga inicial de datos y automatizacion para si surgen cambios
     se guarden en la lista local
     */
    public static void inicializar() {
        dbReservas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaReservasLocal.clear();
                for (DataSnapshot shot : snapshot.getChildren()) {
                    Reserva r = shot.getValue(Reserva.class);
                    if (r != null) {
                        r.setIdReserva(shot.getKey()); // Aseguramos el ID
                        listaReservasLocal.add(r);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error silencioso de conexión
            }
        });
    }

    // 2. Metodos de escritura

    public static void crearReserva(Reserva reserva, ReservaCallback callback) {
        String key = dbReservas.push().getKey();
        reserva.setIdReserva(key);

        dbReservas.child(key).setValue(reserva)
                .addOnSuccessListener(v -> callback.onSuccess("Reserva creada correctamente"))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public static void actualizarEstado(String idReserva, String nuevoEstado, ReservaCallback callback) {
        dbReservas.child(idReserva).child("estado").setValue(nuevoEstado)
                .addOnSuccessListener(v -> callback.onSuccess("Estado actualizado"))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    // 3- Metodos de lectura con sincronismo

    /**
     * Devuelve las reservas de un cliente específico.
     */
    public static ArrayList<Reserva> getReservasPorCliente(String nombreCliente) {
        ArrayList<Reserva> resultado = new ArrayList<>();

        for (Reserva r : listaReservasLocal) {
            if (r .getNombreCliente() != null && r.getNombreCliente().equalsIgnoreCase(nombreCliente)) {
                resultado.add(r);
            }
        }

        // Ordenamos por fecha las mas actuales primero
         Collections.reverse(resultado);
        return resultado;
    }
}
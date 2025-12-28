package com.example.hotel_hw_1.repositorio;

/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 *
 */

import androidx.annotation.NonNull;
import com.example.hotel_hw_1.modelo.Habitacion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class HabitacionRepository {

    /*
    * 1. Diccionario para almacenar los datos
    * de todas las habitaciones
    * la clave indicara el valor de la habitacion
    * siendo mas facil y rapidad la busqueda*/
    private static final Map<String, Habitacion> habitacionesCache = new HashMap<>();

    // 2. Referencia al nodo "habitaciones" en Firebase
    private static final DatabaseReference dbHabitaciones =
            FirebaseDatabase.getInstance().getReference("habitaciones");
/*
* interface que garantiza que sean
* exitosos los metodos de escritura*/
    public interface HabitacionCallback {
        void onSuccess(String mensaje);
        void onError(String error);
    }

    /**
     * Metodo que garatiza la sincronia para la peticion de
     * datos futuro asigno los valores a la lista y rompo el asincronismo de Firebase!!
     */
    public static void inicializarListener() {
        dbHabitaciones.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                habitacionesCache.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Habitacion hab = ds.getValue(Habitacion.class);
                    if (hab != null) {
                        // La clave es el número de habitación (100, 101, etc.)
                        habitacionesCache.put(ds.getKey(), hab);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar error si es necesario
            }
        });
    }

    // Metodos de consulta y validacion


    /**
     * Valida si una habitación está disponible para Check-In.
     * @return null si todo OK, o un String con el mensaje de error.
     */
    public static String validarDisponibilidad(String idHabitacion) {
        Habitacion hab = habitacionesCache.get(idHabitacion);

        if (hab == null) {
            return "Error: La habitación " + idHabitacion + " no existe.";
        }

        if (!hab.getEstado().equalsIgnoreCase("Libre")) {
            return "La habitación " + idHabitacion + " está actualmente " + hab.getEstado() + ".";
        }

        return null; // Disponible
    }

    /**
     * Obtiene una habitación específica de la caché.
     */
    public static Habitacion getHabitacion(String idHabitacion) {
        return habitacionesCache.get(idHabitacion);
    }

    /**
     * Retorna el mapa completo para la pantalla de Ocupación.
     */
    public static Map<String, Habitacion> getTodasLasHabitaciones() {
        return new HashMap<>(habitacionesCache);
    }

    //
    /**
     * Cambia el estado de ocupación (Libre/Ocupada/Reservada).
     */
    /**
     * Actualiza localmente el objeto de la caché y lo lanza a Firebase.
     * No importa qué cambie (estado, fecha, etc.), se envía el objeto completo.
     */
    public static void actualizarHabitacion(String idHabitacion, String nuevoEstado, String fecha, HabitacionCallback callback) {
        // 1. Tomamos el objeto que ya existe en nuestro mapa (diccionario)
        Habitacion hab = habitacionesCache.get(idHabitacion);

        if (hab != null) {
            // 2. Actulizo en mi lista local
            hab.setEstado(nuevoEstado);
            hab.setFechaReserva(fecha);

            // 3. Paso el objeto al Firebase
            dbHabitaciones.child(idHabitacion).setValue(hab)
                    .addOnSuccessListener(aVoid -> callback.onSuccess("Habitación " + idHabitacion + " sincronizada"))
                    .addOnFailureListener(e -> callback.onError("Fallo en Firebase: " + e.getMessage()));
        } else {
            callback.onError("El numero de habitacion no es correcto");
        }
    }

    /**
     * Se libera la habitacion tras un Check-Out
     */
    public static void liberarHabitacion(String idHabitacion, HabitacionCallback callback) {
       // 1. Tomamos el objeto que ya existe en nuestro mapa (diccionario)
        Habitacion hab = habitacionesCache.get(idHabitacion);

        if (hab != null) {
            // 2. Actulizo en mi lista local
            hab.setEstado("Libre");

            // 3. Paso el objeto al Firebase
            dbHabitaciones.child(idHabitacion).setValue(hab)
                    .addOnSuccessListener(aVoid -> callback.onSuccess("Habitación " + idHabitacion + " sincronizada"))
                    .addOnFailureListener(e -> callback.onError("Fallo en Firebase: " + e.getMessage()));
        } else {
            callback.onError("El numero de habitacion no es correcto");
        }
    }

}

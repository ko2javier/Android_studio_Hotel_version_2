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
     * Retorna todas las habitaciones!!
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
    public static void actualizarEstadoHabitacion(String idHabitacion, String nuevoEstado,
                                                  String fecha, String nombreCliente, HabitacionCallback callback) {

        // 1. Buscamos el objeto en la lista local
        Habitacion hab = habitacionesCache.get(idHabitacion);

        if (hab != null) {
            // 2. Actualizamos los datos en memoria
            hab.setEstado(nuevoEstado);
            hab.setFechaReserva(fecha);
            hab.setNombreHuesped(nombreCliente);

            // 3. Enviamos el objeto a Firebase y se actualiza en la nube!!
            dbHabitaciones.child(idHabitacion).setValue(hab)
                    .addOnSuccessListener(aVoid -> callback.onSuccess("Habitación " + idHabitacion + " actualizada correctamente"))
                    .addOnFailureListener(e -> callback.onError("Error al guardar: " + e.getMessage()));
        } else {
            callback.onError("Error: La habitación " + idHabitacion + " no existe.");
        }
    }

    /**
     * Se libera la habitacion tras un Check-Out o fin de reserva!!
     */
    public static void liberarHabitacion(String idHabitacion, HabitacionCallback callback) {
       // 1. Tomamos el objeto que ya existe en nuestro mapa (diccionario)
        Habitacion hab = habitacionesCache.get(idHabitacion);

        if (hab != null) {
            // 2. Actulizo en mi lista local
            hab.setEstado("Libre");
            hab.setFechaReserva("");
            hab.setNombreHuesped("");

            // 3. Paso el objeto al Firebase
            dbHabitaciones.child(idHabitacion).setValue(hab)
                    .addOnSuccessListener(aVoid -> callback.onSuccess("Habitación " + idHabitacion + " sincronizada"))
                    .addOnFailureListener(e -> callback.onError("Fallo en Firebase: " + e.getMessage()));
        } else {
            callback.onError("El numero de habitacion no es correcto");
        }
    }


    /**
     * Metodos para el Reservar habitacion
     */
    // --- NUEVOS MÉTODOS DE LÓGICA DE NEGOCIO ---

    /**
     * Recorre la lista de habitaciones  y devuelve un texto formateado con la disponibilidad de esa planta.
     *  ej: "- Simples: 5  - Dobles: 10  - Triples: 0"
     */
    public static String obtenerEstadisticasTexto(int planta) {
        int simples = 0, dobles = 0, triples = 0;

        // Iteramos los índices posibles de una planta (00 a 99)
        for (int i = 0; i < 100; i++) {
            String sufijo = (i < 10) ? "0" + i : String.valueOf(i);
            String id = planta + sufijo; // Ej: 100, 105...

            Habitacion h = habitacionesCache.get(id);

            // Solo contamos si existe y está libre
            if (h != null && "Libre".equals(h.getEstado())) {
                if (i < 40) simples++;       // 00-39
                else if (i < 90) dobles++;   // 40-89
                else triples++;              // 90-99
            }
        }

        return "- Simples: " + simples + "  - Dobles: " + dobles + "  - Triples: " + triples;
    }

    /**
     * Busca la primera habitación libre según planta y tipo.

     */
    public static String buscarPrimeraLibre(int planta, String tipo) {
        int inicio = 0;
        int fin = 0;

        switch (tipo) {
            case "Simple": inicio = 0; fin = 39; break;
            case "Doble":  inicio = 40; fin = 89; break;
            case "Triple": inicio = 90; fin = 99; break;
            default: return null;
        }

        for (int i = inicio; i <= fin; i++) {
            // Construimos el ID: Planta (3) + Sufijo (40) -> "340"
            String sufijo = (i < 10) ? "0" + i : String.valueOf(i);
            String idCalculado = planta + sufijo; // ESTA ES LA CLAVE

            Habitacion h = habitacionesCache.get(idCalculado);

            // Si existe y está libre, devolvemos el ID CALCULADO ("340"), no h.getNumero() ("40")
            if (h != null && "Libre".equals(h.getEstado())) {
                return idCalculado;
            }
        }

        return null;
    }
}

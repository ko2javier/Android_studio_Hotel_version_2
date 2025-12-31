package com.example.hotel_hw_1.repositorio;

/**
 * Autor: K. Jabier O'Reilly
 */


import android.util.Log;
import androidx.annotation.NonNull;

import com.example.hotel_hw_1.modelo.Tarea;
import com.example.hotel_hw_1.modelo.Usuario;
import com.example.hotel_hw_1.modelo.Huesped;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TareaRepository {

    // 1. Lista para lograr la sincronia. Chache!!
    private static final List<Tarea> listaTareasCache = new ArrayList<>();

    // Referencia a la base de datos "tareas"
    private static final DatabaseReference dbTareas =
            FirebaseDatabase.getInstance().getReference("tareas");

    // Interfaz para saber si guardó bien (Éxito o Error)
    public interface TareaCallback {
        void onSuccess(String mensaje);
        void onError(String error);
    }


    // 2. Importante metodo para incializar datos y mantener la lista updated

    public static void inicializarListener() {
        dbTareas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaTareasCache.clear();
                for (DataSnapshot doc : snapshot.getChildren()) {
                    Tarea t = doc.getValue(Tarea.class);
                    if (t != null) {
                        t.setId(doc.getKey());
                        listaTareasCache.add(t);
                    }
                }
                Log.d("REPO_TAREA", "Tareas cargadas: " + listaTareasCache.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("REPO_TAREA", "Error: " + error.getMessage());
            }
        });
    }


    // 3. MÉTODOS DE LECTURA . Usan sincronismo, es decir la lista del Repositorio!!



    public static List<Tarea> getTareasPorTipo(String tipoServicio) {
        List<Tarea> resultado = new ArrayList<>();
        for (Tarea t : listaTareasCache) {
            if (t.getTipoServicio().equalsIgnoreCase(tipoServicio)) {
                resultado.add(t);
            }
        }
        return resultado;
    }
    // Ver todas las tareas
    public static List<Tarea> getTodasLasTareas() {
        return new ArrayList<>(listaTareasCache);
    }

    public static boolean puedeAutoAsignarse(String nombreEmpleado, String tipoTarea) {

        // 1. Defino el limite de las tareas aca
        int limite,contador = 0;

        // si es limpieza 4, else es mantennimiento por tanto = 3
        limite= (tipoTarea.equalsIgnoreCase("Limpieza"))? 4:3;


        // 2. CONTAR TAREAS ACTUALES

        for (Tarea t : listaTareasCache) {
            // Contamos si es el empleado Y si el estado ES "Asignada"
            if (t.getAsignadaA() != null
                    && t.getAsignadaA().equalsIgnoreCase(nombreEmpleado)
                    && "Asignada".equalsIgnoreCase(t.getEstado())) { // Asignada a Pedro/Juan/Carlos y distinta de null!!
                contador++;
            }
        }
        // 3. VALIDAR
        return contador < limite;
    }
    // A. CREAR NUEVA TAREA (Desde Huesped)
    public static void crearTarea(Tarea tarea, TareaCallback callback) {
        dbTareas.push().setValue(tarea)
                .addOnSuccessListener(v -> callback.onSuccess("Solicitud enviada"))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }



    public static void asignarTarea(String idTarea, String nombreEmpleado, TareaCallback callback) {
        Map<String, Object> update = new HashMap<>();
        update.put("estado", "Asignada");
        update.put("asignadaA", nombreEmpleado);

        dbTareas.child(idTarea).updateChildren(update)
                .addOnSuccessListener(v -> callback.onSuccess("Tarea asignada a " + nombreEmpleado))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    /**
     * Verifica si hay tareas de mantenimiento que afecten a este HUESPED.
     * Recibe el objeto Huesped completo con sus fechas y habitación reales.
     */
    public static boolean verificarMantenimientoHuesped(Huesped huesped) {
        // Validación de seguridad
        if (huesped == null) return false;

        // 1. Obtener el rango de fechas de la estancia del huésped
        long[] rangoFechas = huesped.obtenerRangoFechasEnMilisegundos();
        long fechaInicio = rangoFechas[0];
        long fechaFin = rangoFechas[1];

        // 2. Datos de ubicación del huésped

        String miPlanta = "0";
        String miHab = huesped.getHabitacion();

        // obtengo la planta
        if (miHab != null && !miHab.isEmpty()) {
            miPlanta = String.valueOf(miHab.charAt(0));
        }


        // 3. Recorrer la lista de tareas en CACHÉ
        // (Asumimos que getListaTareas() devuelve la lista completa de tareas cargadas)
        for (Tarea t : getTodasLasTareas()) {

            // A. FILTRO TIPO: Solo nos interesa "Mantenimiento"
            if ("Mantenimiento".equalsIgnoreCase(t.getTipoServicio())) {

                // B. FILTRO FECHA: ¿Ocurrió durante su estancia?
                if (t.getTimestamp() >= fechaInicio && t.getTimestamp() <= fechaFin) {

                    // C. FILTRO UBICACIÓN (Jerárquico)

                    // Paso 1: La PLANTA tiene que coincidir obligatoriamente.
                    if (t.getPlanta() != null && t.getPlanta().equals(miPlanta)) {

                        // Paso 2: Analizamos la HABITACIÓN de la tarea
                        String taskHab = t.getNumeroHabitacion();

                        // ¿Es una tarea específica de una habitación?
                        if (taskHab != null && !taskHab.trim().isEmpty() && taskHab.equals(miHab)) {

                                return true; // ¡Te afecta!

                        } else {
                            // NO: El campo habitación está vacío.
                            // Significa que es una tarea general de planta (ej: pintar pasillo).
                            // Como estoy en esa planta, me afecta.
                            return true; // ¡Te afecta!
                        }
                    }
                }
            }
        }

        // Si terminamos el bucle y nada coincidió
        return false;
    }

}
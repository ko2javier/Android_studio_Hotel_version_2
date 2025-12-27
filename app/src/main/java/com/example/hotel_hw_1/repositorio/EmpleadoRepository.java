package com.example.hotel_hw_1.repositorio;

/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 */

import androidx.annotation.NonNull;
import android.util.Log;

import com.example.hotel_hw_1.modelo.Empleado;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class EmpleadoRepository {

   // 1- corazon de mi clase, en esta lista viven los datos de Firebase y se actualizan!!
    private static final List<Empleado> listaCache = new ArrayList<>();

    // Referencia a la base de datos
    private static final DatabaseReference dbEmpleados =
            FirebaseDatabase
                    .getInstance("https://sanviatorprimerproyecto-default-rtdb.firebaseio.com")
                    .getReference("empleados");

    // Interface como respaldo a las operacioes de escritura!!
    public interface CallbackEscritura {
        void onResultado(boolean exito, String mensaje);
    }


    /**
     *Este metodo es el corazon y cerebro, si cambio algo en
     * firebase se actualiza mi lista. De este modo logro la sincronia automatica.
     */
    public static void inicializarListener() {
        dbEmpleados.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaCache.clear(); // Limpiamos la caché vieja

                for (DataSnapshot empSnapshot : snapshot.getChildren()) {
                    Empleado e = empSnapshot.getValue(Empleado.class);
                    if (e != null) {
                        e.setId(empSnapshot.getKey());
                        listaCache.add(e);
                    }
                }
                Log.d("REPO", "Datos actualizados. Total empleados: " + listaCache.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("REPO", "Error escuchando Firebase: " + error.getMessage());
            }
        });
    }

    // 3- Lectura asincrona!!!
    public static List<Empleado> getListaLocal() {
        // Devolvemos una copia para proteger la lista original
        return new ArrayList<>(listaCache);
    }

    public static Empleado getEmpleadoPorId(String id) {
        for (Empleado e : listaCache) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }


    // 4. ESCRITURA (CREATE / UPDATE / DELETE) Firebase dispara el listener de arriba

      public static void guardarEmpleado(Empleado e, CallbackEscritura callback) {
        DatabaseReference ref;

        // Si no tiene ID, es NUEVO (Create). Si tiene, es UPDATE.
        if (e.getId() == null || e.getId().isEmpty()) {
            ref = dbEmpleados.push(); // Crea ID nuevo
        } else {
            ref = dbEmpleados.child(e.getId()); // Usa ID existente
        }

        ref.setValue(e)
                .addOnSuccessListener(unused -> {
                    if(callback != null) callback.onResultado(true, "Guardado correctamente");
                })
                .addOnFailureListener(error -> {
                    if(callback != null) callback.onResultado(false, error.getMessage());
                });
    }

    public static void eliminarEmpleado(String idEmpleado, CallbackEscritura callback) {
        dbEmpleados.child(idEmpleado).removeValue()
                .addOnSuccessListener(unused -> {
                    if(callback != null) callback.onResultado(true, "Eliminado");
                })
                .addOnFailureListener(e -> {
                    if(callback != null) callback.onResultado(false, e.getMessage());
                });
    }


    // Metodo de filtrado ahora funcionan de manera sincrona!!!

    /*
     * Con este metodo garatizo entregar una lista de nombres
     * segun el rol que ocupan
     * asi puedo asignar a quien sea segun su especialidad
     * */
    public static String[] getNombresPorRol(String rol) {
        String nombres_total = "";

        for (Empleado e : listaCache) {
            if (e.getRol().equalsIgnoreCase(rol)) {
                nombres_total += e.getNombre() + " " + e.getApellidos() + ";";
            }
        }
        // Dividimos los nombres en un array
        String[] nombres = nombres_total.split(";");
        return nombres;
    }
}

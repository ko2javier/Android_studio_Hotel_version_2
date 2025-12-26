package com.example.hotel_hw_1.repositorio;

/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 */


import androidx.annotation.NonNull;

import com.example.hotel_hw_1.modelo.Empleado;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class EmpleadoRepository {

    private static final List<Empleado> listaEmpleados = new ArrayList<>();

    // REFERENCIA FIREBASE
    private static final DatabaseReference dbEmpleados =
            FirebaseDatabase
                    .getInstance("https://sanviatorprimerproyecto-default-rtdb.firebaseio.com")
                    .getReference("empleados");



    // OBTENER EMPLEADOS

    /**
     * Este interface es importante porque Firebase es asincrono
     * por tanto hay que esperar hasta que de la respuesta! o el error !!
     * Para el metodo de Leer la lista completa !!!
     */

    public interface EmpleadosCallback {
        void onSuccess(List<Empleado> empleados);
        void onError(DatabaseError error);
    }

    /**
     * Esta interface es para la escritura y actualizacion
     * de datos, no devuelve datos a diferencia de la anterior
     * que es para devolver la lista o Error
     * Metodos (update, Create, Delete)
     */
    public interface ResultadoCallback {
        void onSuccess();
        void onError(DatabaseError error);
    }


    public static void obtenerEmpleados(EmpleadosCallback callback) {

        dbEmpleados.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Empleado> lista = new ArrayList<>();

                for (DataSnapshot empSnapshot : snapshot.getChildren()) {
                    Empleado e = empSnapshot.getValue(Empleado.class);
                    if (e != null) {
                        e.setId(empSnapshot.getKey()); // IMPORTANTE
                        lista.add(e);
                    }
                }

                callback.onSuccess(lista);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error);
            }
        });
    }

    // AGREGAR
    public static void crearEmpleado(Empleado e, ResultadoCallback callback) {
        dbEmpleados.push()
                .setValue(e)
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(error ->
                        callback.onError(DatabaseError.fromException(error))
                );
    }


    // ELIMINAR
    public static void eliminarEmpleado(String idEmpleado) {
        dbEmpleados.child(idEmpleado).removeValue();
    }

    // Update
    public static void actualizarEmpleado(Empleado e, ResultadoCallback callback) {
        if (e.getId() == null) {
            callback.onError(DatabaseError.fromException(
                    new IllegalArgumentException("ID nulo")
            ));
            return;
        }

        dbEmpleados.child(e.getId())
                .setValue(e)
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(error ->
                        callback.onError(DatabaseError.fromException(error))
                );
    }



    // ==================================================
    // MÉTODO CLAVE QUE YA USA LA APP
    // ==================================================
    public static String[] getNombresPorRol(String rol) {

        String nombres_total = "";

        for (Empleado e : listaEmpleados) {
            if (e.getRol().equalsIgnoreCase(rol)) {
                nombres_total += e.getNombre() + " " + e.getApellidos() + ";";
            }
        }

        return nombres_total.isEmpty() ? new String[0] : nombres_total.split(";");
    }


}

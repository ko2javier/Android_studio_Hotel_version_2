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
     */

    public interface EmpleadosCallback {
        void onSuccess(List<Empleado> empleados);
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
    public static void agregarEmpleado(Empleado e) {
        dbEmpleados.push().setValue(e);
    }

    // ELIMINAR
    public static void eliminarEmpleado(String idEmpleado) {
        dbEmpleados.child(idEmpleado).removeValue();
    }

    // Update

    public static void actualizarEmpleado(String id, Empleado nuevo) {
        dbEmpleados.child(id).setValue(nuevo);
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

        return nombres_total.isEmpty()
                ? new String[0]
                : nombres_total.split(";");
    }


}

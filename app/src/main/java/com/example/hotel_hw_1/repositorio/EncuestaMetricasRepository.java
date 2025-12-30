package com.example.hotel_hw_1.repositorio;

/**
 * Autor: K. Jabier O'Reilly
  */

import androidx.annotation.NonNull;
import com.example.hotel_hw_1.modelo.Encuesta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncuestaMetricasRepository {

    private static final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("encuestas_metricas");

    /*
    * usamos lista para acceder rapido a los datos de las encuestas!!*/
    private static final Map<String, Encuesta> metricasCache = new HashMap<>();



    /*
        Correspodiente metodo para actualizar la lista
        con cada evento!!!
* */
    public static void inicializar() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                metricasCache.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    // Extraemos los datos del JSON
                    String categoriaId = ds.getKey(); // Ejemplo: cat_Limpieza_Habitacion
                    String nombre = ds.child("nombre_visible").getValue(String.class);
                    Float promedio = ds.child("promedio").getValue(Float.class);
                    Integer cantidad = ds.child("cantidad_votos").getValue(Integer.class);
                    Integer opiniones = ds.child("cantidad_opiniones").getValue(Integer.class);
                    if (opiniones == null) opiniones = 0;

                    if (nombre != null && promedio != null && cantidad != null) {
                        Encuesta encuesta = new Encuesta(categoriaId, nombre, promedio, cantidad, opiniones, true);
                        metricasCache.put(categoriaId, encuesta);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error de lectura
            }
        });
    }

    // Devuelve la lista (diccionario) con todos los datos!!!
    public static List<Encuesta> getListaMetricas() {
        return new ArrayList<>(metricasCache.values());
    }


}

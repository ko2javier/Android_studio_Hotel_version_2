package com.example.hotel_hw_1.actividad;

/**
 * Autor: K. Jabier O'Reilly

 */
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.adaptador.AdaptadorDetalleVoto;
import com.example.hotel_hw_1.modelo.DetalleVoto;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DetalleEncuestaActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private TextView txtTitulo;
    private MaterialButton btnVolver;

    // Variables para saber qué categoría estamos consultando
    private String idCategoria;      // Ej: "cat_Limpieza_Habitacion"
    private String nombreCategoria;  // Ej: "Limpieza Habitación"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_encuesta);

        // 1. Recibir los datos del Intent (de la pantalla anterior)
        idCategoria = getIntent().getStringExtra("ID_CAT");
        nombreCategoria = getIntent().getStringExtra("NOMBRE_CAT");

        // 2. Vincular vistas
        recycler = findViewById(R.id.lista_comentarios);
        txtTitulo = findViewById(R.id.titulo_detalle);
        btnVolver = findViewById(R.id.btn_volver_detalle);

        // 3. Configurar UI básica
        recycler.setLayoutManager(new LinearLayoutManager(this));
        txtTitulo.setText("Detalles: " + nombreCategoria);

        // 4. Cargar datos
        cargarDatosDeFirebase();

        btnVolver.setOnClickListener(v -> finish());
    }

    private void cargarDatosDeFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("encuestas_detalle");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DetalleVoto> listaVotos = new ArrayList<>();

                // Recorremos CADA encuesta registrada en el sistema
                for (DataSnapshot registro : snapshot.getChildren()) {

                    // Construimos las claves específicas para ESTA categoría
                    // Ej: buscamos "cat_Limpieza_Habitacion_nota" dentro del registro
                    String keyNota = idCategoria + "_nota";
                    String keyComentario = idCategoria + "_comentario";

                    // Leemos los valores (usando Float para evitar nulos primitivos)
                    Float nota = registro.child(keyNota).getValue(Float.class);
                    String comentario = registro.child(keyComentario).getValue(String.class);
                    Long fecha = registro.child("fecha").getValue(Long.class);

                    // VALIDACIÓN:
                    // 1. Que exista nota y fecha.
                    // 2. Que la nota sea > 0 (Si es 0, es que el cliente NO usó ese servicio, como Mantenimiento)
                    if (nota != null && fecha != null && nota > 0) {

                        // Si el comentario es null, ponemos cadena vacía para evitar errores
                        if (comentario == null) comentario = "";

                        // Añadimos a la lista (tenga texto o no)
                        listaVotos.add(new DetalleVoto(nota, comentario, fecha));
                    }
                }

                // ORDENAR: Los más recientes primero
                Collections.sort(listaVotos, new Comparator<DetalleVoto>() {
                    @Override
                    public int compare(DetalleVoto o1, DetalleVoto o2) {
                        // Comparamos long de fecha (orden descendente)
                        return Long.compare(o2.getFecha(), o1.getFecha());
                    }
                });

                // Si no hay datos, avisamos
                if (listaVotos.isEmpty()) {
                    Toast.makeText(DetalleEncuestaActivity.this, "No hay votos registrados para esta categoría", Toast.LENGTH_SHORT).show();
                }

                // Cargar Adaptador
                AdaptadorDetalleVoto adapter = new AdaptadorDetalleVoto(listaVotos);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetalleEncuestaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
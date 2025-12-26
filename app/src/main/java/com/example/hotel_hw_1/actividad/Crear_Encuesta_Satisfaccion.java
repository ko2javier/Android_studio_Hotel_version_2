/**
 * Autor: K. Jabier O'Reilly

 */

package com.example.hotel_hw_1.actividad;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.adaptador.AdaptadorEncuestaEditable;
import com.example.hotel_hw_1.modelo.Encuesta;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class Crear_Encuesta_Satisfaccion extends AppCompatActivity {
    // definimos variables
    private ListView lvEncuestas;
    private AdaptadorEncuestaEditable adaptador;
    private MaterialButton btnEnviar, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_encuesta_satisfaccion);

        // Referencio las variables con ID
        lvEncuestas = findViewById(R.id.lv_encuestas);
        btnEnviar = findViewById(R.id.btn_enviar_encuesta);
        btnVolver = findViewById(R.id.btn_volver_encuesta);

        // Creamos la lista y añadimos registros
        List<Encuesta> listaPreguntas = new ArrayList<>();
        listaPreguntas.add(new Encuesta("Limpieza de la habitación", 0, 0, true));
        listaPreguntas.add(new Encuesta("Limpieza de la planta", 0, 0, true));
        listaPreguntas.add(new Encuesta("Limpieza del vestíbulo", 0, 0, true));
        listaPreguntas.add(new Encuesta("Atención del personal de limpieza", 0, 0, true));
        listaPreguntas.add(new Encuesta("Atención del personal de mantenimiento", 0, 0, true));
        listaPreguntas.add(new Encuesta("Atención en recepción", 0, 0, true));
        listaPreguntas.add(new Encuesta("Servicio de mantenimiento durante su estancia", 0, 0, true));

        // Configuramos adaptador con la lista creada
        adaptador = new AdaptadorEncuestaEditable(this, listaPreguntas);
        lvEncuestas.setAdapter(adaptador);

        // Botón Enviar
        btnEnviar.setOnClickListener(v -> {
            mostrar_resultados_encuesta(v);
        });

        // Botón Volver
        btnVolver.setOnClickListener(v -> finish());
    }

private void mostrar_resultados_encuesta(View v) {
    List<Encuesta> resultados = adaptador.getResultados();

    List<Integer> incompletas = new ArrayList<>();
    float suma = 0;

    // Validar que no haya ratings vacíos (0)
    for (int i = 0; i < resultados.size(); i++) {
        Encuesta e = resultados.get(i);
        if (e.getPromedio() == 0f) {
            incompletas.add(i + 1);
        } else {
            suma += e.getPromedio();
        }
    }

    // Si hay encuestas sin valorar
    if (!incompletas.isEmpty()) {
        // Paso 2  Mostrar alerta con detalle
        StringBuilder msg = new StringBuilder("Faltan por valorar:\n");
        for (int n : incompletas) msg.append("• Pregunta ").append(n).append("\n");

        new AlertDialog.Builder(this)
                .setTitle("Encuesta incompleta")
                .setMessage(msg.toString())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Aceptar", null)
                .show();


        return;
    }

    // Paso 3 . Si todas están completas, calcular promedio general
    float promedioGeneral = suma / resultados.size();

    new AlertDialog.Builder(this)
            .setTitle("Encuesta enviada")
            .setMessage("Gracias por su valoración.\n\nPromedio general: "
                    + String.format("%.1f", promedioGeneral) + " ★")
            .setPositiveButton("Aceptar", (dialog, which) ->
                    Snackbar.make(v, "Encuesta registrada correctamente", Snackbar.LENGTH_LONG).show()
            )
            .show();

    v.postDelayed(this::finish, 2800);
}

}

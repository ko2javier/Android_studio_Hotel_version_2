/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: Consultar_Encuestas_Satisfaccion.java
 *
 */

package com.example.hotel_hw_1.actividad;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.adaptador.AdaptadorEncuesta;
import com.example.hotel_hw_1.modelo.Encuesta;
import com.example.hotel_hw_1.modelo.Usuario;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class Consultar_Encuesta_Satisfaccion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consultar_encuestas_satisfaccion);

        ListView listaEncuestas = findViewById(R.id.lista_encuestas);
        MaterialButton btnVolver = findViewById(R.id.btn_volver);


        Usuario usuario = Usuario.getInstance();
        String tipoUser = usuario.getTipo_usuario().toLowerCase();

        // simulo los datos de las encuestas recibidas mdte el array
        List<Encuesta> encuestas = new ArrayList<>();
        encuestas.add(new Encuesta("Limpieza habitación", 4.3f, 10, true));
        encuestas.add(new Encuesta("Limpieza planta", 4.1f, 9, true));
        encuestas.add(new Encuesta("Limpieza vestíbulo", 4.4f, 8, true));
        encuestas.add(new Encuesta("Atención personal limpieza", 4.6f, 12, true));
        encuestas.add(new Encuesta("Atención personal mantenimiento", 4.2f, 7, true));
        encuestas.add(new Encuesta("Atención en recepción", 4.8f, 15, true));
        encuestas.add(new Encuesta("Servicio mantenimiento durante estancia", 4.0f, 5, true));

        // Filtro las encuestas segun el rol. para ello declaro las encuestas que seran o no visibles
        for (Encuesta e : encuestas) {
            switch (tipoUser) {
                case "limpieza":
                    if (!e.getCategoria().toLowerCase().contains("limpieza"))
                        e.setVisible(false);
                    break;

                case "mantenimiento":
                    if (!e.getCategoria().toLowerCase().contains("mantenimiento"))
                        e.setVisible(false);
                    break;

                case "recepcionista":
                    if (!e.getCategoria().toLowerCase().contains("recepción"))
                        e.setVisible(false);
                    break;

                case "gerente":
                    e.setVisible(true); // lo ve todo
                    break;
            }
        }

        // Finalmente hago la lista con lo que esta visible !!
        List<Encuesta> encuestasFiltradas = new ArrayList<>();
        for (Encuesta e : encuestas) {
            if (e.isVisible()) encuestasFiltradas.add(e);
        }

        // Cargo el adaptador con la lista encuestasFiltradas !!!
        AdaptadorEncuesta adaptador = new AdaptadorEncuesta(this, encuestasFiltradas);
        listaEncuestas.setAdapter(adaptador);

        btnVolver.setOnClickListener(v -> finish());
    }
}

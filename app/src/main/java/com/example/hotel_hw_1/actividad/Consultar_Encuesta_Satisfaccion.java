/**
 * Autor: K. Jabier O'Reilly

 */

package com.example.hotel_hw_1.actividad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.adaptador.AdaptadorEncuestaResumen;
import com.example.hotel_hw_1.modelo.Encuesta;
import com.example.hotel_hw_1.modelo.Usuario;
import com.example.hotel_hw_1.repositorio.EncuestaMetricasRepository;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class Consultar_Encuesta_Satisfaccion extends AppCompatActivity {
    private RecyclerView recycler;
    MaterialButton btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consultar_encuestas_satisfaccion);

        recycler = findViewById(R.id.lista_encuestas);
        btnVolver = findViewById(R.id.btn_volver);

        recycler.setLayoutManager(new LinearLayoutManager(this));

        // 1. Tomo el rol segun el usuario
        Usuario usuario = Usuario.getInstance();
        String tipoUser = (usuario.getTipo_usuario() != null) ? usuario.getTipo_usuario().toLowerCase() : "gerente";

        // 2. Inicializo mi Repositorio con los datos de Firebase
        EncuestaMetricasRepository.inicializar();

        // 3. Suscribirse para recibir los datos cuando lleguen
            List<Encuesta> encuestasReales = EncuestaMetricasRepository.getListaMetricas();

                // 4. Aplicamos logica del filtrado segun rol
                List<Encuesta> encuestasFiltradas = new ArrayList<>();

                for (Encuesta e : encuestasReales) {
                    //Por defecto visible, y despues comenzamos a cambiar
                    boolean esVisible = true;
                    String nombreCategoria = e.getCategoria().toLowerCase();

                    switch (tipoUser) {
                        case "limpieza":
                            // Solo ve cosas que contengan "limpieza"
                            if (!nombreCategoria.contains("limpieza")) esVisible = false;
                            break;

                        case "mantenimiento":
                            // Solo ve cosas que contengan "mantenimiento"
                            if (!nombreCategoria.contains("mantenimiento")) esVisible = false;
                            break;

                        case "recepcionista":

                            if (!nombreCategoria.contains("recepción") && !nombreCategoria.contains("recepcion")) esVisible = false;
                            break;

                        case "gerente":
                            esVisible = true; // Gerente ve todo
                            break;

                        default:
                            esVisible = true; // Por seguridad
                            break;
                    }

                    // Uana vez filtrada el usuario añadimos la encuesta a la lista de filtrados
                    if (esVisible) {
                        encuestasFiltradas.add(e);
                    }
                }

                // 5. Configuro ya el adaptador con a lista filtrada
                AdaptadorEncuestaResumen adapter = new AdaptadorEncuestaResumen(encuestasFiltradas, new AdaptadorEncuestaResumen.OnEncuestaClickListener() {
                    @Override
                    public void onEncuestaClick(Encuesta encuesta) {
                        Intent intent = new Intent(Consultar_Encuesta_Satisfaccion.this, DetalleEncuestaActivity.class);

                        // Pasamos los datos de que escuesta queremos mostrar!!
                        intent.putExtra("ID_CAT", encuesta.getId());
                        intent.putExtra("NOMBRE_CAT", encuesta.getCategoria());

                        startActivity(intent);

                    }
                });

                recycler.setAdapter(adapter);

                btnVolver.setOnClickListener(v -> finish());
            }


    }


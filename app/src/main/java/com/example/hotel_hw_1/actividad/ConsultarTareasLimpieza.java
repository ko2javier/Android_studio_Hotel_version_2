/**
 * Autor: K. Jabier O'Reilly

 */

package com.example.hotel_hw_1.actividad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Tarea;
import com.example.hotel_hw_1.adaptador.TareaAdapter;
import com.example.hotel_hw_1.repositorio.TareaData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;

import java.util.ArrayList;
import java.util.List;

public class ConsultarTareasLimpieza extends AppCompatActivity {
// defino variables
    private ListView listViewTareas;
    private RadioGroup radioGroup;
    private MaterialRadioButton rbPendientes, rbAsignadas;
    private MaterialButton btnVolver;

    private TareaAdapter adapter;
    private List<Tarea> listaFiltrada = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_tareas_limpieza);

        //  identifico  variables con los id
        listViewTareas = findViewById(R.id.list_tareas_limpieza);
        radioGroup = findViewById(R.id.rg_tipo_tareas);
        rbPendientes = findViewById(R.id.rb_pendientes);
        rbAsignadas = findViewById(R.id.rb_asignadas);
        btnVolver = findViewById(R.id.btn_volver_tareas);

        // Por defecto muestro tareas pdtes
        actualizarLista("Pendiente");

        // Si cambio el estado muestro pdte/ asignadas
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_pendientes) {
                actualizarLista("Pendiente");
            } else if (checkedId == R.id.rb_asignadas) {
                actualizarLista("Asignada");
            }
        });

        // Retornar al menu del limpiador
        btnVolver.setOnClickListener(v -> {
            Intent intent = new Intent(this, LimpiadorActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // aca creo la lista filtrada por pdte o por asignada!!

    private void actualizarLista(String estado) {
        listaFiltrada.clear();
        for (Tarea t : TareaData.getTareas()) {
            if (t.getTipoTarea().equalsIgnoreCase("Limpieza") &&
                    t.getEstado().equalsIgnoreCase(estado)) {
                listaFiltrada.add(t);
            }
        }

        adapter = new TareaAdapter(this, listaFiltrada, "Limpieza");// paso al adapter la lista filtrada!!
        listViewTareas.setAdapter(adapter);
    }
}

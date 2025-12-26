/**
 * Autor: K. Jabier O'Reilly

 */

package com.example.hotel_hw_1.actividad;

import android.os.Bundle;

import android.content.Intent;
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

public class consultar_tareas_mantenimiento extends AppCompatActivity {
// declaro mis variables
    private ListView listaTareas;
    private RadioGroup rgFiltro;
    private MaterialRadioButton rbPendientes, rbAsignadas;
    private MaterialButton btnVolver;
    private TareaAdapter adapter;
    private List<Tarea> tareasFiltradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_tareas_mantenimiento);

        // les asigno Id a mis variables !!!1

        listaTareas = findViewById(R.id.list_tareas);
        rgFiltro = findViewById(R.id.rg_tipo_tareas);
        rbPendientes = findViewById(R.id.rb_pendientes);
        rbAsignadas = findViewById(R.id.rb_asignadas);
        btnVolver = findViewById(R.id.btn_volver_tareas);

        // Obtener todas las tareas de mantenimiento
        tareasFiltradas = new ArrayList<>(TareaData.getTareasPorTipo("Mantenimiento"));

        // Configurar adapter con el rol del usuario
        adapter = new TareaAdapter(this, tareasFiltradas, "Mantenimiento");
        listaTareas.setAdapter(adapter);

        // Filtro por radio buttons
        rgFiltro.setOnCheckedChangeListener((group, checkedId) -> actualizarLista());

        btnVolver.setOnClickListener(v -> {
            Intent i = new Intent(consultar_tareas_mantenimiento.this, MantenimientoActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void actualizarLista() {
        tareasFiltradas.clear();
        List<Tarea> todas = TareaData.getTareasPorTipo("Mantenimiento");

        if (rbPendientes.isChecked()) {
            for (Tarea t : todas) {
                if (t.getEstado().equalsIgnoreCase("Pendiente")) {
                    tareasFiltradas.add(t);
                }
            }
        } else {
            for (Tarea t : todas) {
                if (t.getEstado().equalsIgnoreCase("Asignada")) {
                    tareasFiltradas.add(t);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}



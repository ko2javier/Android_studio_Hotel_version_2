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
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Tarea;
import com.example.hotel_hw_1.adaptador.TareaAdapter;

import com.example.hotel_hw_1.repositorio.TareaRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;

import java.util.ArrayList;
import java.util.List;

public class consultar_tareas_mantenimiento extends AppCompatActivity {

    // declaro mis variables
    private RecyclerView recyclerTareas;
    private RadioGroup rgFiltro;
    private MaterialRadioButton rbPendientes, rbAsignadas;
    private MaterialButton btnVolver;
    private TareaAdapter adapter;
    private List<Tarea> tareasFiltradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_tareas_mantenimiento);


        // les asigno Id a mis variables
        recyclerTareas = findViewById(R.id.recycler_tareas);
        recyclerTareas.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        rgFiltro = findViewById(R.id.rg_tipo_tareas);
        rbPendientes = findViewById(R.id.rb_pendientes);
        rbAsignadas = findViewById(R.id.rb_asignadas);
        btnVolver = findViewById(R.id.btn_volver_tareas);

        // 1. Inicializamos la lista y el adaptador (vacíos al principio)
        tareasFiltradas = new ArrayList<>();
        adapter = new TareaAdapter(this, tareasFiltradas, "Mantenimiento");

        // CORREGIDO: Usamos la variable recyclerTareas, no listaTareas
        recyclerTareas.setAdapter(adapter);

        // 3. Cargamos los datos iniciales
        actualizarLista();

        // 3. Filtro por radio buttons
        rgFiltro.setOnCheckedChangeListener((group, checkedId) -> actualizarLista());

        // 4. Botón volver
        btnVolver.setOnClickListener(v -> {
            Intent i = new Intent(consultar_tareas_mantenimiento.this, MantenimientoActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void actualizarLista() {
        tareasFiltradas.clear();

        // 1. Obtenemos TODAS las de Mantenimiento desde el Repositorio (Caché Firebase)
        List<Tarea> todas = TareaRepository.getTareasPorTipo("Mantenimiento");

        // 2. Definimos qué estado estamos buscando
        String estadoBuscado = rbPendientes.isChecked() ? "Pendiente" : "Asignada";

        // 3. Filtramos
        for (Tarea t : todas) {
            if (t.getEstado().equalsIgnoreCase(estadoBuscado)) {
                tareasFiltradas.add(t);
            }
        }

        // 4. Avisamos al adaptador que los datos cambiaron
        adapter.notifyDataSetChanged();
    }
}



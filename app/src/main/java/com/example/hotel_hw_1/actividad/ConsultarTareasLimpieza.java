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
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Tarea;
import com.example.hotel_hw_1.adaptador.TareaAdapter;
import com.example.hotel_hw_1.repositorio.TareaRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class ConsultarTareasLimpieza extends AppCompatActivity {

    // defino variables
    private RecyclerView recyclerTareas;
    private RadioGroup radioGroup;
    private MaterialRadioButton rbPendientes, rbAsignadas;
    private MaterialButton btnVolver;

    private TareaAdapter adapter;
    private List<Tarea> listaFiltrada = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_tareas_limpieza);

        // Defino mi recycler
        recyclerTareas = findViewById(R.id.recycler_tareas);
        recyclerTareas.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        radioGroup = findViewById(R.id.rg_tipo_tareas);
        rbPendientes = findViewById(R.id.rb_pendientes);
        rbAsignadas = findViewById(R.id.rb_asignadas);
        btnVolver = findViewById(R.id.btn_volver_tareas);

        // Por defecto muestro tareas pendientes
        actualizarLista("Pendiente");

        // Si cambio el estado muestro pdte / asignadas
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

    // Aca creo la lista filtrada por pdte o por asignada, LEYENDO DEL REPOSITORIO
    private void actualizarLista(String estado) {
        listaFiltrada.clear();

        // 1. Obtenemos SOLO las de Limpieza directamente del Repo (más eficiente)
        List<Tarea> todasLasDeLimpieza = TareaRepository.getTareasPorTipo("Limpieza");

        // 2. Filtramos localmente por estado (Pendiente / Asignada)
        for (Tarea t : todasLasDeLimpieza) {
            if (t.getEstado().equalsIgnoreCase(estado)) {
                listaFiltrada.add(t);
            }
        }

        // 3. Cargamos el adaptador
        adapter = new TareaAdapter(this, listaFiltrada, "Limpieza");
        recyclerTareas.setAdapter(adapter);
    }
}

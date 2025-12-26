package com.example.hotel_hw_1.actividad;
/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 *
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.adaptador.EmpleadoAdapter;
import com.example.hotel_hw_1.modelo.Empleado;
import com.example.hotel_hw_1.repositorio.EmpleadoRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class GestionEmpleadosActivity extends AppCompatActivity {

    private RecyclerView rv_empleados;
    private MaterialButton btnAgregar, btnVolver;
    private EmpleadoAdapter adapter;
    private List<Empleado> listaEmpleados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_empleados);

          rv_empleados = findViewById(R.id.rv_empleados);
        btnAgregar = findViewById(R.id.btn_agregar_empleado);
        btnVolver = findViewById(R.id.btn_volver_gestion_empleados);

        //  Logica del RECYCLER

        rv_empleados.setLayoutManager(new LinearLayoutManager(this));

        listaEmpleados = new ArrayList<>();
/**
 * Implemento  los metodos de la interfaz del Recicler
 * para hacerlo mas locol creo un objeto del tipo de la interfaz !!!!
 */

        adapter = new EmpleadoAdapter( listaEmpleados,
                new EmpleadoAdapter.OnEmpleadoClickListener() {

                    @Override
                    public void onEditarClick(Empleado empleado) {
                        Intent intent = new Intent(
                                GestionEmpleadosActivity.this,
                                EmpleadoFormActivity.class
                        );
                        intent.putExtra("ID_EMPLEADO", empleado.getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onBorrarClick(Empleado empleado) {
                        mostrarDialogoEliminar(empleado);
                    }
                }
        );

        rv_empleados.setAdapter(adapter);

        // =========================
        // CARGAR EMPLEADOS DESDE REPOSITORY (FIREBASE)
        // =========================
        EmpleadoRepository.obtenerEmpleados(new EmpleadoRepository.EmpleadosCallback() {
            @Override
            public void onSuccess(List<Empleado> empleados) {
                listaEmpleados.clear();
                listaEmpleados.addAll(empleados);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(DatabaseError error) {
                Snackbar.make(rv_empleados, "Error en lectura: " + error.getMessage(),
                        Snackbar.LENGTH_LONG).show();
            }
        });

        // =========================
        // BOTONES
        // =========================
        btnAgregar.setOnClickListener(v ->
                startActivity(new Intent(this, EmpleadoFormActivity.class))
        );

        btnVolver.setOnClickListener(v -> finish());
    }

    // =========================
    // DIÁLOGO ELIMINAR
    // =========================
    private void mostrarDialogoEliminar(Empleado empleado) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar empleado")
                .setMessage("¿Seguro que desea eliminar a " + empleado.getNombre() + "?")
                .setPositiveButton("Sí", (dialog, which) ->
                        EmpleadoRepository.eliminarEmpleado(empleado.getId())
                )
                .setNegativeButton("No", null)
                .show();
    }
}

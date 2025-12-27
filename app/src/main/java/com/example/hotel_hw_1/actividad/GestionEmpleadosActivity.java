package com.example.hotel_hw_1.actividad;
/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 *
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

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

      //condiguracion del Recicler !!
        rv_empleados.setLayoutManager(new LinearLayoutManager(this));

        listaEmpleados = new ArrayList<>();

        adapter = new EmpleadoAdapter(listaEmpleados,
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


        // 1. Iniciamos la escucha en Firebase (por si no se hizo en el Main)
        EmpleadoRepository.inicializarListener();

        // 2. Cargamos lo que haya en memoria actualmente
        actualizarVista();


        btnAgregar.setOnClickListener(v ->
                startActivity(new Intent(this, EmpleadoFormActivity.class))
        );

        btnVolver.setOnClickListener(v -> finish());
    }

    /**
     * IMPORTANTE: Al usar un sistema de caché, cuando volvemos de
     * crear/editar un empleado, onResume se ejecuta.
     * Aprovechamos para refrescar la lista visualmente.
     */
    @Override
    protected void onResume() {
        super.onResume();
        actualizarVista();
    }

    // Método auxiliar para traer los datos de la caché al adaptador
    private void actualizarVista() {
        List<Empleado> datosCache = EmpleadoRepository.getListaLocal();
        listaEmpleados.clear();
        listaEmpleados.addAll(datosCache);
        adapter.notifyDataSetChanged();
    }


    private void mostrarDialogoEliminar(Empleado empleado) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar empleado")
                .setMessage("¿Seguro que desea eliminar a " + empleado.getNombre() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {

                    // Llamada al método delete del repositorio nuevo con CallbackEscritura
                    EmpleadoRepository.eliminarEmpleado(empleado.getId(),
                            new EmpleadoRepository.CallbackEscritura() {
                                @Override
                                public void onResultado(boolean exito, String mensaje) {
                                    if (exito) {
                                        Snackbar.make(rv_empleados, "Empleado eliminado", Snackbar.LENGTH_SHORT).show();
                                        // Firebase actualiza la caché solo, nosotros solo refrescamos la vista
                                        actualizarVista();
                                    } else {
                                        Snackbar.make(rv_empleados, "Error: " + mensaje, Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                })
                .setNegativeButton("No", null)
                .show();
    }
}

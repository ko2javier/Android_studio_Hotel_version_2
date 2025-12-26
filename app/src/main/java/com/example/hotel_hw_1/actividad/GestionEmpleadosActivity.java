package com.example.hotel_hw_1.actividad;

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
import com.example.hotel_hw_1.repositorio.EmpleadoData;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class GestionEmpleadosActivity extends AppCompatActivity {

    private RecyclerView rv_empleados;
    private MaterialButton btnAgregar, btnVolver;
    private EmpleadoAdapter adapter;
    private List<Empleado> listaEmpleados;
    private DatabaseReference dbEmpleados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_empleados);

        // VIEWS
        rv_empleados = findViewById(R.id.rv_empleados);
        btnAgregar = findViewById(R.id.btn_agregar_empleado);
        btnVolver = findViewById(R.id.btn_volver_gestion_empleados);

        // RECYCLER
        rv_empleados.setLayoutManager(new LinearLayoutManager(this));

        // DATOS
        listaEmpleados = EmpleadoData.getEmpleados();

        // ADAPTER
        adapter = new EmpleadoAdapter(listaEmpleados,
                new EmpleadoAdapter.OnEmpleadoClickListener() {

                    @Override
                    public void onEditarClick(Empleado empleado) {
                        int position = listaEmpleados.indexOf(empleado);

                        Intent intent = new Intent(
                                GestionEmpleadosActivity.this,
                                EmpleadoFormActivity.class
                        );
                        intent.putExtra("posicion", position);
                        startActivity(intent);
                    }

                    @Override
                    public void onBorrarClick(Empleado empleado) {
                        mostrarDialogoEliminar(empleado);
                    }
                });

        rv_empleados.setAdapter(adapter);

        // BOTONES
        btnAgregar.setOnClickListener(v ->
                startActivity(new Intent(this, EmpleadoFormActivity.class))
        );

        btnVolver.setOnClickListener(v -> finish());
    }

    // REFRESCAR LISTA AL VOLVER DEL FORM
    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    //  DIÁLOGO ELIMINAR
    private void mostrarDialogoEliminar(Empleado empleado) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar empleado")
                .setMessage("¿Seguro que desea eliminar a " + empleado.getNombre() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    EmpleadoData.eliminarEmpleado(empleado);
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("No", null)
                .show();
    }
}

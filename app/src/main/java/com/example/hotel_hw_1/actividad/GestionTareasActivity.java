/**
 * Autor: K. Jabier O'Reilly

 */

package com.example.hotel_hw_1.actividad;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Tarea;
import com.example.hotel_hw_1.adaptador.TareaAdapter;

import com.example.hotel_hw_1.modelo.Usuario;
import com.example.hotel_hw_1.repositorio.TareaRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;
public class GestionTareasActivity extends AppCompatActivity {

    private RecyclerView recyclerTareas;
    private MaterialTextView txtTitulo;
    private MaterialButton btnVolver;
    private TareaAdapter adapter; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_tareas);
        //Defino mi recycler y le paso el LinearLayoutManager
        recyclerTareas = findViewById(R.id.recycler_tareas);
        recyclerTareas.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        txtTitulo = findViewById(R.id.txt_titulo_tareas);
        btnVolver = findViewById(R.id.btn_volver_tareas);

        // Obtenemos usuario logueado
        Usuario usuario = Usuario.getInstance();
        String rol = usuario.getTipo_usuario();

        // Dependiendo del rol, filtramos las tareas USANDO EL REPOSITORIO
        List<Tarea> listaTareas = new ArrayList<>();

        if (rol.equalsIgnoreCase("Gerente")) {
            txtTitulo.setText("Tareas de mantenimiento y limpieza");
            // El gerente ve TODO
            listaTareas = TareaRepository.getTodasLasTareas();

        } else if (rol.equalsIgnoreCase("Mantenimiento")) {
            txtTitulo.setText("Tareas de mantenimiento disponibles");
            // Mantenimiento ve solo las suyas
            listaTareas = TareaRepository.getTareasPorTipo("Mantenimiento");

        } else if (rol.equalsIgnoreCase("Limpieza")) {
            txtTitulo.setText("Tareas de limpieza disponibles");
            // Limpieza ve solo las suyas
            listaTareas = TareaRepository.getTareasPorTipo("Limpieza");

        } else {
            txtTitulo.setText("Sin tareas disponibles");
        }

        // 4. Configurar el Adaptador
        if (listaTareas != null && !listaTareas.isEmpty()) {
            adapter = new TareaAdapter(this, listaTareas, rol);
            recyclerTareas.setAdapter(adapter);
        } else {
            // Si está vacía, avisamos en el título
            txtTitulo.setText(txtTitulo.getText() + "\n(No hay tareas registradas)");
            // Opcional: poner adaptador vacío para que no falle nada
            recyclerTareas.setAdapter(new TareaAdapter(this, new ArrayList<>(), rol));
        }

        btnVolver.setOnClickListener(v -> finish());
    }
}
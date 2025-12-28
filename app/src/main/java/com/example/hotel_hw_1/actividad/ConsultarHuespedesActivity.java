/**
 * Autor: K. Jabier O'Reilly

 */

package com.example.hotel_hw_1.actividad;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.adaptador.HuespedAdapter;
import com.example.hotel_hw_1.adaptador.TareaAdapter;
import com.example.hotel_hw_1.modelo.Huesped;
import com.example.hotel_hw_1.repositorio.HuespedData;
import com.example.hotel_hw_1.repositorio.HuespedRepository;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ConsultarHuespedesActivity extends AppCompatActivity {
    private HuespedAdapter adapter;
    private RecyclerView rc_huespedes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_huespedes);

        rc_huespedes = findViewById(R.id.rc_huespedes);
        MaterialButton btnVolver = findViewById(R.id.btn_volver_listado);

        // 2. Configuraro el LayoutManager
        rc_huespedes.setLayoutManager(new LinearLayoutManager(this));

        // 3. Obtengo la lista del Repo despues de haber sigo cargada antes!!
        List<Huesped> listaHuespedes = HuespedRepository.getTodosLosHuespedes();

        // 4. Inicializo el adaptador con la lista. Asigno al recicler el adaptador
        adapter = new HuespedAdapter(this, listaHuespedes);
        rc_huespedes.setAdapter(adapter);


        btnVolver.setOnClickListener(v -> finish());
    }
}

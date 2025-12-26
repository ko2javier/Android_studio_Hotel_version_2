/**
 * Autor: K. Jabier O'Reilly

 */

package com.example.hotel_hw_1.actividad;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.adaptador.AdapterHuesped;
import com.example.hotel_hw_1.repositorio.HuespedData;
import com.google.android.material.button.MaterialButton;

public class ConsultarHuespedesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_huespedes);

        ListView listViewHuespedes = findViewById(R.id.list_view_huespedes);
       MaterialButton btnVolver = findViewById(R.id.btn_volver_listado);

        AdapterHuesped adapter = new AdapterHuesped(this, HuespedData.listarHuespedes());
        listViewHuespedes.setAdapter(adapter);

        btnVolver.setOnClickListener(v -> finish());
    }
}

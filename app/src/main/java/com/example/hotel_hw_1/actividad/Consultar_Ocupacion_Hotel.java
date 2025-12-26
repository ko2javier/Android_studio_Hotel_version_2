/**
 * Autor: K. Jabier O'Reilly
 *
 */

package com.example.hotel_hw_1.actividad;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.adaptador.AdaptadorPlanta;
import com.example.hotel_hw_1.modelo.Planta;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class Consultar_Ocupacion_Hotel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consultar_ocupacion_hotel);

        // defino
        MaterialButton btn_volver= findViewById(R.id.btn_volver);

        ListView listView = findViewById(R.id.listaPlantas);

        List<Planta> plantas = new ArrayList<>();
        plantas.add(new Planta("Planta 1", 9, "101, 102, 104", "115, 118, 120, 121", "198, 199"));
        plantas.add(new Planta("Planta 2", 8, "202, 204, 209", "220, 222, 228", "297, 299"));
        plantas.add(new Planta("Planta 3", 8, "303, 305, 310", "324, 325, 330", "397, 399"));
        plantas.add(new Planta("Planta 4", 10, "401, 403, 404, 408", "420, 425, 430", "496, 498, 499"));
        plantas.add(new Planta("Planta 5", 9, "501, 504, 507", "520, 525, 528, 530", "596, 598"));

        AdaptadorPlanta adaptador = new AdaptadorPlanta(this, plantas);
        listView.setAdapter(adaptador);

        btn_volver.setOnClickListener(v->{
            finish();
        });


    }
}
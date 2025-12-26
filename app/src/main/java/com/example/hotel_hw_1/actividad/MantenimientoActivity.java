/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: MantenimientoActivity.java
 *
 */

package com.example.hotel_hw_1.actividad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Usuario;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class MantenimientoActivity extends AppCompatActivity {

    // defino variables.
    private MaterialButton btn_consultar_editar_perfil_mmto,btn_consultar_tareas_pdtes_mmto_hotel,
            btn_consultar_Encuestas_mmto,boton_cerrar_sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimiento_menu);

        // les asigno los id a las variables.
         btn_consultar_editar_perfil_mmto= findViewById(R.id.btn_consultar_editar_perfil_mmto);
         btn_consultar_tareas_pdtes_mmto_hotel= findViewById(R.id.btn_consultar_tareas_pdtes_mmto_hotel);
         btn_consultar_Encuestas_mmto= findViewById(R.id.btn_consultar_Encuestas_mmto);
         boton_cerrar_sesion= findViewById(R.id.boton_cerrar_sesion);

        // Pongo a la escucha los listeners

        btn_consultar_editar_perfil_mmto.setOnClickListener(v->
        {
            Intent i = new Intent(MantenimientoActivity.this,
                    Consultar_Editar_Perfil.class);
            startActivity(i);

        });

        btn_consultar_Encuestas_mmto.setOnClickListener(v->{
            Intent i = new Intent(MantenimientoActivity.this, Consultar_Encuesta_Satisfaccion.class);
            startActivity(i);
        });

        boton_cerrar_sesion.setOnClickListener(v->{
            Intent i = new Intent(MantenimientoActivity.this, Pantalla_Inicio.class);
            Usuario.setInstance(null); // limpio lo que tenga el usuario

            Snackbar.make(v, "Sesión cerrada correctamente", Snackbar.LENGTH_SHORT).show();
            // limpio lo que exista en el historial de las activities, para q cdo comience este todo en cero!!
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });
        btn_consultar_tareas_pdtes_mmto_hotel.setOnClickListener(v->{
            Intent i = new Intent(this, consultar_tareas_mantenimiento.class);
            startActivity(i);
        });

    }
}
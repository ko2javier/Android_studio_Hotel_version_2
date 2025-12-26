/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: LimpiadorActivity.java
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

public class LimpiadorActivity extends AppCompatActivity {
    private MaterialButton btn_consulta_perfil, btn_consultar_tareas_asignadas_pdtes,
             btn_consultar_encuestas_limpiador,boton_cerrar_sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limpiador_menu);

        //defino botones
        btn_consulta_perfil= findViewById(R.id.btn_consultar_editar_perfil_limp);
         btn_consultar_tareas_asignadas_pdtes=
                findViewById(R.id.btn_consultar_tareas_pdtes_asigandas_limpiador);

         btn_consultar_encuestas_limpiador= findViewById(R.id.btn_consultar_Encuestas_limpiador);
         boton_cerrar_sesion= findViewById(R.id.boton_cerrar_sesion);


        //Pongo a la escucha
        btn_consulta_perfil.setOnClickListener(v -> {
            Intent i = new Intent(LimpiadorActivity.this, Consultar_Editar_Perfil.class);
            startActivity(i);
        });
        btn_consultar_encuestas_limpiador.setOnClickListener(v->{
            Intent i = new Intent(LimpiadorActivity.this, Consultar_Encuesta_Satisfaccion.class );
            startActivity(i);
        });

        boton_cerrar_sesion.setOnClickListener(v->{
            Intent i= new Intent(LimpiadorActivity.this, Pantalla_Inicio.class);
            Usuario.setInstance(null); // limpio lo que tenga el usuario

            Snackbar.make(v, "Sesión cerrada correctamente", Snackbar.LENGTH_SHORT).show();
            // limpio lo que exista en el historial de las activities, para q cdo comience este todo en cero!!
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        });

        btn_consultar_tareas_asignadas_pdtes.setOnClickListener(v->{
            Intent i = new Intent(this, ConsultarTareasLimpieza.class);
            startActivity(i);
        });

    }
}
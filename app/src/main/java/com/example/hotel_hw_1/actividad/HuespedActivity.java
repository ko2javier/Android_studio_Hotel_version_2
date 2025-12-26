
/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: HuespedActivity.java
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

public class HuespedActivity extends AppCompatActivity {
    private MaterialButton btn_consultar_perfil,btn_add_reservas,btn_check_reservas_done,
            btn_realizar_encuestas, boton_cerrar_sesion, btn_solictar_limpieza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_menu);


         btn_consultar_perfil= findViewById(R.id.btn_consultar_editar_perfil_huesped);

         btn_add_reservas = findViewById(R.id.btn_add_reservas_hotel_huesped);
         btn_check_reservas_done= findViewById(R.id.btn_check_status_realizadas);
         btn_realizar_encuestas= findViewById(R.id.btn_realizar_encuestas_al_hotel);
        boton_cerrar_sesion= findViewById(R.id.boton_cerrar_sesion);
         btn_solictar_limpieza= findViewById(R.id.btn_solictar_limpieza);

        // Pongo a la escucha botones
        btn_consultar_perfil.setOnClickListener(v -> {
            Intent i= new Intent(HuespedActivity.this, Consultar_Editar_Perfil.class);
            startActivity(i);

        });
        btn_realizar_encuestas.setOnClickListener(v->{
            Intent i = new Intent(HuespedActivity.this, Crear_Encuesta_Satisfaccion.class);
            startActivity(i);
        });

        btn_add_reservas.setOnClickListener(v->{
            Intent i= new Intent(HuespedActivity.this, Realizar_Reserva_Activity.class);
            startActivity(i);
        });

        btn_check_reservas_done.setOnClickListener(v->{
            Intent i = new Intent(HuespedActivity.this, Consultar_Estado_Reserva.class);
            startActivity(i);
        });

        boton_cerrar_sesion.setOnClickListener(v->{
            Intent i = new Intent(HuespedActivity.this, Pantalla_Inicio.class);
            Usuario.setInstance(null); // limpio lo que tenga el usuario

            Snackbar.make(v, "Sesión cerrada correctamente", Snackbar.LENGTH_SHORT).show();
            // limpio lo que exista en el historial de las activities, para q cdo comience este todo en cero!!
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        });
        btn_solictar_limpieza.setOnClickListener(v->{
            Intent i = new Intent(HuespedActivity.this,Solicitar_Tarea.class);
            startActivity(i);
        });


    }
}
/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: RecepcionistaActivity.java
 *
 */

package com.example.hotel_hw_1.actividad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Usuario;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class RecepcionistaActivity extends AppCompatActivity {
    private MaterialButton btn_consultar_editar_perfil_recp,btn_consultar_ocupacion_hotel_recp,
            btn_consultar_listado_huspedes_recp,btn_add_reservas_hotel,btn_gestionar_check_in_out,
            btn_consultar_encuestas,boton_cerrar_sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepcionista_menu);

        // defino botones
         btn_consultar_editar_perfil_recp=
                findViewById(R.id.btn_consultar_editar_perfil_recp);
         btn_consultar_ocupacion_hotel_recp=
                findViewById(R.id.btn_consultar_ocupacion_hotel_recp);
         btn_consultar_listado_huspedes_recp =
                findViewById(R.id.btn_consultar_listado_huspedes_recp);
         btn_add_reservas_hotel= findViewById(R.id.btn_add_reservas_hotel);
         btn_gestionar_check_in_out= findViewById(R.id.btn_gestionar_check_in_out);
         btn_consultar_encuestas= findViewById(R.id.btn_consultar_Encuestas);
         boton_cerrar_sesion = findViewById(R.id.boton_cerrar_sesion);

        btn_consultar_editar_perfil_recp.setOnClickListener(v->
        {
            Intent i = new Intent(RecepcionistaActivity.this,
                    Consultar_Editar_Perfil.class);
            startActivity(i);
        });
        btn_consultar_ocupacion_hotel_recp.setOnClickListener(v->{
            Intent i= new Intent(RecepcionistaActivity.this, Consultar_Ocupacion_Hotel.class);
            startActivity(i);
        });

        btn_consultar_encuestas.setOnClickListener(v->{
            Intent i = new Intent(RecepcionistaActivity.this, Consultar_Encuesta_Satisfaccion.class);
            startActivity(i);
        });

        btn_add_reservas_hotel.setOnClickListener(v->{
            Intent i = new Intent(RecepcionistaActivity.this, Realizar_Reserva_Activity.class);
            startActivity(i);
        });
        boton_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecepcionistaActivity.this, Pantalla_Inicio.class);
                Usuario.setInstance(null); // limpio lo que tenga el usuario

                Snackbar.make(v, "Sesión cerrada correctamente", Snackbar.LENGTH_SHORT).show();
                // limpio lo que exista en el historial de las activities, para q cdo comience este todo en cero!!
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });

        btn_gestionar_check_in_out.setOnClickListener(v->{
            Intent i= new Intent(RecepcionistaActivity.this,
                    GestionEntradasSalidasActivity.class);
            startActivity(i);
        });

        btn_consultar_listado_huspedes_recp.setOnClickListener(v->{
            Intent i = new Intent(RecepcionistaActivity.this, ConsultarHuespedesActivity.class);
            startActivity(i);
        });



    }
}
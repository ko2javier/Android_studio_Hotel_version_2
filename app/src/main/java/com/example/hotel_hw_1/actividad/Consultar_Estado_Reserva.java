/**
 * Autor: K. Jabier O'Reilly

 */

package com.example.hotel_hw_1.actividad;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.adaptador.AdaptadorReserva;
import com.example.hotel_hw_1.modelo.Usuario;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class Consultar_Estado_Reserva extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consultar_estado_reservas);

        // definimos variables

        MaterialButton btnVolver = findViewById(R.id.btn_volver_consultar_reservas);
        ListView listaReservas = findViewById(R.id.lista_reservas);

        //  Obtener reservas del usuario actual
        Usuario usuario = Usuario.getInstance();
        List<String> reservas = usuario.getReservas();

        //  Si el usuario aún no ha reservado, agregamos algunas para que no este en blanco!!
        if (reservas.isEmpty()) {
            reservas.add("Fecha: 12-10-2025 | Habitación: Doble | Servicios: Spa | Estado: Cancelada");
            reservas.add("Fecha: 09-10-2025 | Habitación: Simple | Servicios: Sin servicios adicionales | Estado: Cancelada");
            reservas.add("Fecha: 08-10-2025 | Habitación: Doble | Servicios: Parking 1/2 Pensión | Estado: Confirmada");
        }

        //  configuro mi adaptador
        AdaptadorReserva adaptador = new AdaptadorReserva(this, reservas);
        listaReservas.setAdapter(adaptador);

        // boton volver a la escucha
        btnVolver.setOnClickListener(v -> finish());
    }
}

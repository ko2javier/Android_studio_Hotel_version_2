/**
 * Autor: K. Jabier O'Reilly

 */

package com.example.hotel_hw_1.actividad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.adaptador.ReservaAdapter;
import com.example.hotel_hw_1.modelo.Reserva;
import com.example.hotel_hw_1.modelo.Usuario;
import com.example.hotel_hw_1.repositorio.HabitacionRepository;
import com.example.hotel_hw_1.repositorio.ReservaRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Consultar_Estado_Reserva extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReservaAdapter adaptador;
    private MaterialButton btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_estado_reservas);


        btnVolver = findViewById(R.id.btn_volver_consultar_reservas);
        recyclerView = findViewById(R.id.lista_reservas);

        // Configuro mi Recycler con el  LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 2. Vuelvo a incializar la escucha del Repositorio !!
        ReservaRepository.inicializar();

        // 3. Cargo datos
        cargarReservas();

        // 4. Botón Volver
        btnVolver.setOnClickListener(v -> finish());
    }

    private void cargarReservas() {
        Usuario usuario = Usuario.getInstance();

        // Filtramos por el nombre del usuario que esta logueado!!!
        String nombreBusqueda = usuario.getNombre() + " " + usuario.getApellidos();

        // Hago la consulta al Repositorio
        ArrayList<Reserva> listaReservas = ReservaRepository.getReservasPorCliente(nombreBusqueda);

        // en caso de que este vacio inicialmente pongo datos de prueba
        if (listaReservas.isEmpty()) {
            cargarDatosDePrueba(listaReservas);
        }

        // Implmento los metodos de la interfaz del Adaptador localmente para los click de los botones
        adaptador = new ReservaAdapter(listaReservas, new ReservaAdapter.OnItemClickListener() {
            @Override
            public void onCancelarClick(Reserva reserva) {
                procesarCancelacion(reserva);
            }

            @Override
            public void onValorarClick(Reserva reserva) {
                /*
                Snackbar.make(Consultar_Estado_Reserva.this.getCurrentFocus(),
                        "Abriendo encuesta para habitación " + reserva.getHabitacion(), Snackbar.LENGTH_LONG)
                                .show();*/
                    Intent i = new Intent(Consultar_Estado_Reserva.this, Crear_Encuesta_Satisfaccion.class);
                    startActivity(i);
                
            }
        });

        recyclerView.setAdapter(adaptador);
    }

    private void procesarCancelacion(Reserva reserva) {
        // 1. MArco como cancelada en Firebase
        ReservaRepository.actualizarEstado(reserva.getIdReserva(), "CANCELADA", new ReservaRepository.ReservaCallback() {
            @Override
            public void onSuccess(String msg) {

                // 2. Si fue exitoso Libero la habitacion !!
                HabitacionRepository.liberarHabitacion(reserva.getHabitacion(), new HabitacionRepository.HabitacionCallback() {
                    @Override
                    public void onSuccess(String m) {
                        Snackbar.make(recyclerView, "Reserva Cancelada y Habitación Liberada", Snackbar.LENGTH_LONG).show();
                        cargarReservas(); // Refrescamos la lista para que se ponga roja
                    }

                    @Override
                    public void onError(String e) {
                        // Si falla liberar habitación, avisamos, esto es un problema porque no se sincronizo el otro Repositorio
                        Snackbar.make(recyclerView, "Cancelada, pero error en habitación: " + e, Snackbar.LENGTH_LONG).show();
                        cargarReservas();
                    }
                });
            }
            @Override
            public void onError(String error) {
                Snackbar.make(recyclerView, "Error al cancelar: " + error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    // Metodo auxiliar para añadir datos al huesped en caso que no haya hecho ninguna reserva!!
    private void cargarDatosDePrueba(ArrayList<Reserva> lista) {
        // Cancelada (Roja)
        lista.add(new Reserva("dummy1", "Juan", "101", "12-10-2025", "15-10-2025",
                "Doble", "Spa", "Media Pensión", "CANCELADA"));

        /*
        lista.add(new Reserva("dummy2", "Juan", "102", "09-10-2025", "10-10-2025",
                "Simple", "Sin servicios", "Solo Alojamiento", "PENDIENTE"));*/

        // Objeto 3: Completada, la que me sirve para Realizar encuestas
        lista.add(new Reserva("dummy3", "Juan", "205", "01-01-2024", "05-01-2024",
                "Triple", "Parking", "Pensión Completa", "COMPLETADA"));
    }
}
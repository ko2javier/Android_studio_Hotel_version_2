package com.example.hotel_hw_1.actividad;

/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 */

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.adaptador.OcupacionAdapter;
import com.example.hotel_hw_1.modelo.Habitacion;
import com.example.hotel_hw_1.modelo.ItemOcupacion;
import com.example.hotel_hw_1.repositorio.HabitacionRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Consultar_Ocupacion_Hotel extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OcupacionAdapter adapter;
    private MaterialTextView txtOcupacionTotal;
    private int contadorOcupadas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_ocupacion_hotel);

        // 1. Inicializar vistas
        recyclerView = findViewById(R.id.recyclerOcupacion);
        txtOcupacionTotal = findViewById(R.id.txt_ocupacion_total);
        MaterialButton btnVolver = findViewById(R.id.btn_volver_ocupacion);

        // 2. Preparar los datos reales desde el repositorio
        List<ItemOcupacion> listaFinal = generarListaProcesada();

        // 3. Configurar el Adapter con el detalle de la interfaz
        adapter = new OcupacionAdapter(listaFinal, new OcupacionAdapter.OnHabitacionClickListener() {
            @Override
            public void onHabitacionClick(Habitacion hab) {
                // Aquí es donde el controlador (Activity) decide qué hacer
                if (!hab.getEstado().equals("Libre")) {
                    mostrarDetalleHabitacion(hab);
                }
            }
        });

        // 4. Configurar GridLayoutManager con SpanSizeLookup para cabeceras
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.esCabecera(position) ? 5 : 1;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // 5. Mostrar estadísticas totales
        txtOcupacionTotal.setText("Ocupación total: " + contadorOcupadas + " / 500 habitaciones");

        btnVolver.setOnClickListener(v -> finish());
    }

    /**
     * Transforma el mapa de 500 habitaciones en una lista plana con cabeceras por tipo.
     */
    private List<ItemOcupacion> generarListaProcesada() {
        List<ItemOcupacion> lista = new ArrayList<>();
        Map<String, Habitacion> mapa = HabitacionRepository.getTodasLasHabitaciones(); //
        contadorOcupadas = 0;

        for (int p = 1; p <= 5; p++) {
            // Añadimos secciones por planta y tipo
            agregarSeccion(lista, mapa, p, "SIMPLES", 0, 39);
            agregarSeccion(lista, mapa, p, "DOBLES", 40, 89);
            agregarSeccion(lista, mapa, p, "TRIPLES", 90, 99);
        }
        return lista;
    }
/*
* Metodo para mostrar detalle de la habitacion si hacen click y esta reservada
*  */
    private void mostrarDetalleHabitacion(Habitacion hab) {
        String info = "Estado: " + hab.getEstado();
        if (hab.getEstado().equals("Reservada")) {
            info += "\nReserva para la feche: " + hab.getFechaReserva();
        }

        new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                .setTitle("Habitación " + hab.getNumero())
                .setMessage(info)
                .setPositiveButton("Entendido", null)
                .show();
    }
    private void agregarSeccion(List<ItemOcupacion> lista, Map<String, Habitacion> mapa, int planta, String tipo, int inicio, int fin) {
        int dispSeccion = 0;
        int ocupSeccion = 0;
        List<ItemOcupacion> temporalHabitaciones = new ArrayList<>();

        for (int i = inicio; i <= fin; i++) {
            String id = String.valueOf((planta * 100) + i);
            Habitacion hab = mapa.get(id);

            if (hab != null) {
                temporalHabitaciones.add(new ItemOcupacion(hab));
                if (hab.getEstado().equals("Libre")) {
                    dispSeccion++;
                } else if (hab.getEstado().equals("Ocupada")) {
                    ocupSeccion++;
                    contadorOcupadas++; // Global
                }
            }
        }

        // Añadimos la cabecera con sus datos calculados
        lista.add(new ItemOcupacion("PLANTA " + planta + " - " + tipo, dispSeccion, ocupSeccion));

        // Añadimos las habitaciones que guardamos temporalmente
        lista.addAll(temporalHabitaciones);
    }
}
/**
 * Autor: K. Jabier O'Reilly
 *
 */

package com.example.hotel_hw_1.actividad;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Tarea;
import com.example.hotel_hw_1.repositorio.TareaRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class Solicitar_Tarea extends AppCompatActivity {

    private TextInputEditText etx_numero_room;
    private MaterialTextView txt_error_habitacion;
    private RadioGroup radioGroupTarea, radioGroupTipoTarea;

    private MaterialAutoCompleteTextView spinnerZona, spinnerPasillo, spinnerPlanta;
    private MaterialButton btnEnviar, btnVolver;

    // Arrays de datos
    private static final String[] pasillos = {"Norte", "Sur", "Este", "Oeste"};
    private static final String[] zonasLimpieza = {"Salón", "Dormitorio", "Baño", "General"};
    private static final String[] zonasMantenimiento = {"Salón", "Dormitorio", "Baño"};
    private static final String[] plantas = {"1", "2", "3", "4", "5"};

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_tarea);

        // 1. Vincular IDs
        spinnerPlanta = findViewById(R.id.spinnerPlanta); // El nuevo spinner
        radioGroupTarea = findViewById(R.id.radioGroupTarea);
        radioGroupTipoTarea = findViewById(R.id.radioGroupTipoTarea);
        etx_numero_room = findViewById(R.id.etx_numero_room);
        txt_error_habitacion = findViewById(R.id.txt_error_habitacion);
        spinnerZona = findViewById(R.id.spinnerZona);
        spinnerPasillo = findViewById(R.id.spinnerPasillo);
        btnEnviar = findViewById(R.id.btn_enviar_solicitud);
        btnVolver = findViewById(R.id.btn_volver);

        // 2. CONFIGURAR ADAPTADORES (SPINNERS)

        // Pasillos
        configurarSpinner(spinnerPasillo, pasillos);
        spinnerPasillo.setText(pasillos[0], false); // Valor por defecto

        // Plantas (Nuevo)
        configurarSpinner(spinnerPlanta, plantas);
        spinnerPlanta.setText(plantas[0], false); // Valor por defecto "1"

        // Zonas (Por defecto cargamos Limpieza)
        configurarSpinner(spinnerZona, zonasLimpieza);
        spinnerZona.setText(zonasLimpieza[0], false);

        // 3. LISTENERS

        // Cambio de tipo de servicio (Limpieza <-> Mantenimiento)
        radioGroupTarea.setOnCheckedChangeListener((group, checkedId) -> {
            spinner_zona(checkedId);
        });

        // Cambio de ubicación (Habitación <-> Pasillo)
        radioGroupTipoTarea.setOnCheckedChangeListener((group, checkedId) -> {
            visibilidad_hab_pasillo(checkedId);
        });

        // BOTÓN ENVIAR (Lógica Firebase)
        btnEnviar.setOnClickListener(v -> enviarSolicitud(v));

        // Botón Volver
        btnVolver.setOnClickListener(v -> finish());
    }

    // --- MÉTODOS AUXILIARES ---

    private void configurarSpinner(MaterialAutoCompleteTextView spinner, String[] datos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, datos);
        spinner.setAdapter(adapter);
    }

    private void visibilidad_hab_pasillo(int checkedId) {
        boolean esPasillo = (checkedId == R.id.rb_pasillo);

        if (esPasillo) {
            // MOSTRAR: Pasillo y Planta
            findViewById(R.id.linear_spinner_pasillo).setVisibility(View.VISIBLE);
            findViewById(R.id.linear_spinner_planta).setVisibility(View.VISIBLE); // ID del XML

            // OCULTAR: Habitación y Zona
            findViewById(R.id.txt_no_habitacion).setVisibility(View.GONE);
            etx_numero_room.setVisibility(View.GONE);
            txt_error_habitacion.setVisibility(View.GONE);
            findViewById(R.id.linear_spinner_zona).setVisibility(View.GONE);
        } else {
            // MOSTRAR: Habitación y Zona
            findViewById(R.id.txt_no_habitacion).setVisibility(View.VISIBLE);
            etx_numero_room.setVisibility(View.VISIBLE);
            findViewById(R.id.linear_spinner_zona).setVisibility(View.VISIBLE);

            // OCULTAR: Pasillo y Planta
            findViewById(R.id.linear_spinner_pasillo).setVisibility(View.GONE);
            findViewById(R.id.linear_spinner_planta).setVisibility(View.GONE);
        }
    }

    private void spinner_zona(int checkedId) {
        if (checkedId == R.id.rb_limpieza) {
            configurarSpinner(spinnerZona, zonasLimpieza);
            spinnerZona.setText(zonasLimpieza[0], false); // Reiniciar selección
        } else if (checkedId == R.id.rb_mmto) {
            configurarSpinner(spinnerZona, zonasMantenimiento);
            spinnerZona.setText(zonasMantenimiento[0], false); // Reiniciar selección
        }
    }

    // LÓGICA DE ENVÍO A FIREBASE
    private void enviarSolicitud(View v) {
        String tipoServicio = (radioGroupTarea.getCheckedRadioButtonId() == R.id.rb_limpieza) ? "Limpieza" : "Mantenimiento";
        String tipoUbicacion = "";
        String plantaFinal = "";
        String habitacionFinal = "";
        String zonaFinal = "";

        // RECOGIDA DE DATOS
        if (radioGroupTipoTarea.getCheckedRadioButtonId() == R.id.rb_pasillo) {
            // CASO PASILLO
            tipoUbicacion = "Pasillo";
            plantaFinal = spinnerPlanta.getText().toString(); // Del Spinner Planta
            zonaFinal = spinnerPasillo.getText().toString();  // Del Spinner Pasillo
            habitacionFinal = "";
        } else {
            // CASO HABITACIÓN
            tipoUbicacion = "Habitacion";
            habitacionFinal = etx_numero_room.getText().toString().trim();

            // Validación manual rápida
            if (habitacionFinal.isEmpty()) {
                txt_error_habitacion.setVisibility(View.VISIBLE);
                etx_numero_room.setError("Campo obligatorio");
                return;
            } else {
                txt_error_habitacion.setVisibility(View.GONE);
            }

            // Calculamos planta del primer número (Ej: 101 -> 1)
            plantaFinal = String.valueOf(habitacionFinal.charAt(0));
            zonaFinal = spinnerZona.getText().toString(); // Del Spinner Zona
        }

        // CREAR OBJETO TAREA
        Tarea nueva = new Tarea(
                tipoServicio,
                tipoUbicacion,
                plantaFinal,
                habitacionFinal,
                zonaFinal
        );

        // BLOQUEAR BOTÓN Y ENVIAR
        btnEnviar.setEnabled(false);

        TareaRepository.crearTarea(nueva, new TareaRepository.TareaCallback() {
            @Override
            public void onSuccess(String mensaje) {
                Snackbar.make(v, mensaje, Snackbar.LENGTH_SHORT).show();
                etx_numero_room.setText(""); // Limpiar
                btnEnviar.setEnabled(true);
            }

            @Override
            public void onError(String error) {
                Snackbar.make(v, "Error: " + error, Snackbar.LENGTH_LONG).show();
                btnEnviar.setEnabled(true);
            }
        });
    }
}
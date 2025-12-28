package com.example.hotel_hw_1.actividad;

/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 */


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Huesped;
import com.example.hotel_hw_1.modelo.Validacion;
import com.example.hotel_hw_1.repositorio.HabitacionRepository;
import com.example.hotel_hw_1.repositorio.HuespedRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckInActivity extends AppCompatActivity {

    private TextInputEditText etNombre, etApellidos, etTelefono;
    private String numeroHabitacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        // 1. Recogemos el Intent de la otra actividad
        numeroHabitacion = getIntent().getStringExtra("EXTRA_HABITACION");
        if (numeroHabitacion == null) {
            Snackbar.make(CheckInActivity.this.getCurrentFocus(), "Error: No se seleccionó habitación", Snackbar.LENGTH_LONG)
                            .show();
            finish();
            return;
        }


        MaterialTextView lblTitulo = findViewById(R.id.lbl_titulo_habitacion);
        lblTitulo.setText("Check-In Habitación: " + numeroHabitacion);

        etNombre = findViewById(R.id.et_nombre);
        etApellidos = findViewById(R.id.et_apellidos);
        etTelefono = findViewById(R.id.et_telefono);

        MaterialButton btnConfirmar = findViewById(R.id.btn_realizar_checkin);
        MaterialButton btnCancelar = findViewById(R.id.btn_cancelar);


        // Pongo los botones a la escucha
        btnCancelar.setOnClickListener(v -> finish());

        btnConfirmar.setOnClickListener(v -> {
            // Valido mis datos!!
            if (validarCampos()) {
                guardarDatos();
            }
        });
    }

    private boolean validarCampos() {

        int errores = 0;
        StringBuilder msg = new StringBuilder();
        boolean esValido = true;

        // Paso 1:  Nombre
        if (!Validacion.validarNombre(etNombre)) {
            errores++;
            msg.append("• Nombre inválido.\n");
        }

        // Paso 2: Apellidos
        if (!Validacion.validarApellidos(etApellidos)) {
            errores++;
            msg.append("• Apellidos inválidos.\n");
        }
        //Paso 3: Telefono
        if (!Validacion.validarTelefonoNuevo(etTelefono)) {
            errores++;
            msg.append("• Teléfono inválido (9 dígitos).\n");
        }
        if (errores > 0 && !isFinishing()) {
            Validacion.mostrarErrores(this, msg);
            return false;
        }
        return esValido;
    }

    private void guardarDatos() {
        String nombre = etNombre.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String nombreCompleto = nombre + " " + apellidos;
        SimpleDateFormat df= new SimpleDateFormat("dd-MM-yyyy");
        String fechaHoy = df.format(new Date());
        Huesped huesped = new Huesped(nombre, apellidos, telefono,numeroHabitacion, fechaHoy);

        // B. GUARDADO EN CADENA (Militar: Primero Huésped -> Luego Habitación)
        HuespedRepository.crearHuesped (huesped, new HuespedRepository.HuespedCallback() {
            @Override
            public void onSuccess(String msg) {

                // Si el huésped se guardó, bloqueamos la habitación
                HabitacionRepository.actualizarEstadoHabitacion(numeroHabitacion, "Ocupada", "", nombreCompleto, new HabitacionRepository.HabitacionCallback() {
                    @Override
                    public void onSuccess(String msgHab) {
                        Snackbar.make(CheckInActivity.this.getCurrentFocus(), "¡Check-In realizado con éxito!", Snackbar.LENGTH_LONG).show();

                        finish(); // Cerramos la actividad y volvemos al mapa
                    }

                    @Override
                    public void onError(String error) {
                        Snackbar.make(etNombre, "Error al actualizar habitación: " + error, Snackbar.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                Snackbar.make(etNombre, "Error al registrar huésped: " + error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
/**
 * Autor: K. Jabier O'Reilly
 *
 */

package com.example.hotel_hw_1.actividad;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Huesped;
import com.example.hotel_hw_1.modelo.Validacion;
import com.example.hotel_hw_1.repositorio.HuespedData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.example.hotel_hw_1.repositorio.HuespedRepository;

public class GestionEntradasSalidasActivity extends AppCompatActivity {
    private RadioGroup radioGroupOperacion;
    private MaterialRadioButton rbCheckIn, rbCheckOut;
    private MaterialButton btnVolver, btnCheckIn, btnBuscar,btnCheckOut;
    private String nombre,apellidos, telefono,habitacion;
    private TextInputEditText etNombre,etApellidos, etTelefono, etHabitacion, etNombreBuscar, etApellidosBuscar;
    private Huesped gest_check_out=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_entradas_salidas);

        //  Definimos varibles grales
         radioGroupOperacion = findViewById(R.id.radio_group_operacion);
        rbCheckIn = findViewById(R.id.rb_check_in);
        rbCheckOut = findViewById(R.id.rb_check_out);
        btnVolver = findViewById(R.id.btn_volver);

        // Ahora los linear
        LinearLayout layoutCheckIn = findViewById(R.id.linear_form_checkin);
        LinearLayout layoutCheckOut = findViewById(R.id.linear_buscar_checkout);

        // Los  Check-In
         etNombre = findViewById(R.id.et_nombre_huesped);
         etApellidos = findViewById(R.id.et_apellidos_huesped);
         etTelefono = findViewById(R.id.et_telefono_huesped);
         etHabitacion = findViewById(R.id.et_habitacion_huesped);
        btnCheckIn = findViewById(R.id.btn_realizar_check_in);

        // Los Check-Out
         etNombreBuscar = findViewById(R.id.et_nombre_buscar);
         etApellidosBuscar = findViewById(R.id.et_apellidos_buscar);
         btnBuscar = findViewById(R.id.btn_buscar_checkout);
         btnCheckOut = findViewById(R.id.btn_realizar_check_out);

        // hacemos los cambios entre  Check-In / Check-Out
        radioGroupOperacion.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_check_in) {
                layoutCheckIn.setVisibility(LinearLayout.VISIBLE);
                layoutCheckOut.setVisibility(LinearLayout.GONE);
            } else {
                layoutCheckIn.setVisibility(LinearLayout.GONE);
                layoutCheckOut.setVisibility(LinearLayout.VISIBLE);
            }
        });

        // Registrar huesped con el buton a la escucha!!
        btnCheckIn.setOnClickListener(v -> {

            registrar_huesped(v);
        });

        //  Buscamos gest  para Check-Out
        btnBuscar.setOnClickListener(v -> {
            buscar_huesped(v);
        });

        // Hacemos  Check-Out (cambiar estado)
        btnCheckOut.setOnClickListener(v -> {
            realizar_check_out(v);
        });

        //  Volver
        btnVolver.setOnClickListener(v -> finish());
    }
/*
Con este metodo hacemos la salida
ya habiamos encontrado el huesped con buscar
asi que es solo llamar al repo e implementar la interfaz si hay exito o error en la accion
* */
    private void realizar_check_out(View v) {
        // Obtenemos los datos de los campos de búsqueda
        nombre = etNombreBuscar.getText().toString().trim();
        apellidos = etApellidosBuscar.getText().toString().trim();

        if (gest_check_out != null) {
            // 2. Ejecutamos la salida en Firebase usando el ID
            HuespedRepository.realizarCheckOut(gest_check_out.getId(), new HuespedRepository.HuespedCallback() {
                @Override
                public void onSuccess(String mensaje) {
                    Snackbar.make(v, "Éxito: Check-Out realizado con exito", Snackbar.LENGTH_LONG).show();
                    btnCheckOut.setVisibility(View.GONE);
                    etNombreBuscar.setText("");
                    etApellidosBuscar.setText("");
                    gest_check_out= null;
                }

                @Override
                public void onError(String error) {
                    Snackbar.make(v, "Error: No se pudo procesar la salida", Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
            Snackbar.make(v, "Error: No se pudo procesar la salida", Snackbar.LENGTH_LONG).show();
        }

    }

    private void registrar_huesped(View v) {
        if (!validaciones_campos(v)) return;
        nombre = etNombre.getText().toString().trim();
        apellidos = etApellidos.getText().toString().trim();
        telefono = etTelefono.getText().toString().trim();
        habitacion = etHabitacion.getText().toString().trim();
        String fechaHoy = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(new java.util.Date());

        Huesped nuevo = new Huesped(nombre, apellidos, telefono, habitacion, fechaHoy);

        HuespedRepository.crearHuesped(nuevo, new HuespedRepository.HuespedCallback() {
            @Override
            public void onSuccess(String mensaje) {
                Snackbar.make(v, mensaje, Snackbar.LENGTH_LONG).show();
                // Limpiar campos
                etNombre.setText("");
                etApellidos.setText("");
                etTelefono.setText("");
                etHabitacion.setText("");

            }

            @Override
            public void onError(String error) {
                Snackbar.make(v, "Error: " + error, Snackbar.LENGTH_LONG).show();

            }
        });
    }

    /*
con este metodo buscamos el huesped, una vez encontrado mostramos el boton para hacer el
check out, ademas dejamos listo el valor de ese usuario en la variable " gest_check_out "  !!!!!
* */
    private void buscar_huesped(View v) {
        // Mantengo tus validaciones de campos existentes
        int errores = 0;
        StringBuilder msg = new StringBuilder();

        if (!Validacion.validarNombre(etNombreBuscar)) {
            errores++;
            msg.append("• Nombre inválido.\n");
        }
        if (!Validacion.validarApellidos(etApellidosBuscar)) {
            errores++;
            msg.append("• Apellidos inválidos.\n");
        }
        if (errores > 0 && !isFinishing()) {
            Validacion.mostrarErrores(this, msg);
            return;
        }
        nombre = etNombreBuscar.getText().toString().trim();
        apellidos = etApellidosBuscar.getText().toString().trim();

        // 3. BUSQUEDA REAL EN EL REPOSITORIO (Firebase Cache)
        gest_check_out = HuespedRepository.buscarHuespedActivo(nombre, apellidos);

        if (gest_check_out != null) {
            // Huésped encontrado y activo
            btnCheckOut.setVisibility(View.VISIBLE);
            Snackbar.make(v, "Huésped encontrado en Hab: " + gest_check_out.getHabitacion(), Snackbar.LENGTH_SHORT).show();

        }
    }

    private boolean validaciones_campos(View v) {
        int errores = 0;
        StringBuilder msg = new StringBuilder();

        if (!Validacion.validarNombre( etNombre)) {
            errores++;
            msg.append("• Nombre inválido.\n");
        }
        if (!Validacion.validarApellidos( etApellidos)) {
            errores++;
            msg.append("• Apellidos inválidos.\n");
        }
        if (!Validacion.validarTelefonoNuevo( etTelefono)) {
            errores++;
            msg.append("• Teléfono inválido (9 dígitos).\n");
        }
        if (!Validacion.validarHabitacionObligatoria(v, etHabitacion, new TextView(this))) {
            errores++;
            msg.append("• Habitación inválida (100–599).\n");
        }

        if (errores > 0 && !isFinishing()) {
            Validacion.mostrarErrores(this, msg);
            return false;
        }
        return true;
    }

    private void mostrarDialogoNoEncontrado() {
        new AlertDialog.Builder(this)
                .setTitle("Huésped no encontrado")
                .setMessage("No se encontró ningún huésped con esos datos.")
                .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                .show();
    }
}

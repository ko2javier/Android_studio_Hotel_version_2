/**
 * Autor: K. Jabier O'Reilly
 *
 */

package com.example.hotel_hw_1.actividad;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Validacion;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class Solicitar_Tarea extends AppCompatActivity {

    private TextInputEditText etx_numero_room;
    private MaterialTextView txt_error_habitacion;
    private RadioGroup radioGroupTarea, radioGroupTipoTarea;

   private  MaterialAutoCompleteTextView spinnerZona,spinnerPasillo;
    private MaterialButton btnEnviar, btnVolver;

    private static String[] pasillos = null, zonasLimpieza = null, zonasMantenimiento = null;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_tarea);

        //Id a las variables
        radioGroupTarea = findViewById(R.id.radioGroupTarea);
        radioGroupTipoTarea = findViewById(R.id.radioGroupTipoTarea);
        etx_numero_room = findViewById(R.id.etx_numero_room);
        txt_error_habitacion = findViewById(R.id.txt_error_habitacion);
        spinnerZona = findViewById(R.id.spinnerZona);
        spinnerPasillo = findViewById(R.id.spinnerPasillo);
        btnEnviar = findViewById(R.id.btn_enviar_solicitud);
        btnVolver = findViewById(R.id.btn_volver);

        // Definos los arrays para los spinners
        pasillos = new String[]{"Norte", "Sur", "Este", "Oeste"};
        zonasLimpieza = new String[]{"Salón", "Dormitorio", "Baño", "General"};
        zonasMantenimiento = new String[]{"Salón", "Dormitorio", "Baño"};

        // configuro  spinner pasillos
        ArrayAdapter<String> adapterPasillo = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, pasillos);
        adapterPasillo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPasillo.setAdapter(adapterPasillo);

        // configuro por defecto para zona Limpieza
        ArrayAdapter<String> adapterZona = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, zonasLimpieza);
        adapterZona.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerZona.setAdapter(adapterZona);

        /**
         * Cambio de opciones el spinnerZona según
         * tipo principal Limpieza / Mantenimiento
         */

        radioGroupTarea.setOnCheckedChangeListener((group, checkedId) -> {
            spinner_zona(checkedId);
        });

        /**
         * Cambio de visibilidad según tipo de tarea (Habitación / Pasillo)
         */

        radioGroupTipoTarea.setOnCheckedChangeListener((group, checkedId) -> {

            visibilidad_hab_pasillo(checkedId);
        });

        // Pongo a la escucha  los listeners
        btnEnviar.setOnClickListener(v -> {
            // Reviso campo habitación solo si está visible
            if (etx_numero_room.getVisibility() == View.VISIBLE) {
                boolean ok = Validacion.validarHabitacionObligatoria(v, etx_numero_room, txt_error_habitacion);
                if (!ok) return;
            }
            Snackbar.make(v, "Solicitud enviada correctamente", Snackbar.LENGTH_SHORT).show();
            etx_numero_room.setText("");
        });

        btnVolver.setOnClickListener(v -> finish());
    }

    private void visibilidad_hab_pasillo(int checkedId) {
        boolean esPasillo = (checkedId == R.id.rb_pasillo);

        // Si es pasillo → mostrar solo el pasillo
        findViewById(R.id.linear_spinner_pasillo).setVisibility(esPasillo ? View.VISIBLE : View.GONE);

        // Si es habitación → mostrar número y zona
        findViewById(R.id.txt_no_habitacion).setVisibility(esPasillo ? View.GONE : View.VISIBLE);
        etx_numero_room.setVisibility(esPasillo ? View.GONE : View.VISIBLE);
        txt_error_habitacion.setVisibility(esPasillo ? View.GONE : View.VISIBLE);
        findViewById(R.id.linear_spinner_zona).setVisibility(esPasillo ? View.GONE : View.VISIBLE);
    }

    private void spinner_zona(int checkedId) {
        if (checkedId == R.id.rb_limpieza) {
            ArrayAdapter<String> adapterLimpieza = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, zonasLimpieza);
            adapterLimpieza.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerZona.setAdapter(adapterLimpieza);
        } else if (checkedId == R.id.rb_mmto) {
            ArrayAdapter<String> adapterMmto = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, zonasMantenimiento);
            adapterMmto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerZona.setAdapter(adapterMmto);
        }
    }

    private void validaciones_de_limpieza(View v) {
        Snackbar.make(v, "Solicitud enviada correctamente", Snackbar.LENGTH_SHORT).show();
        // Limpiar campos
        etx_numero_room.setText("");
        spinnerZona.setSelection(0);
        spinnerPasillo.setSelection(0);
        radioGroupTarea.check(R.id.rb_limpieza);
        radioGroupTipoTarea.check(R.id.rb_habitacion);
    }
}

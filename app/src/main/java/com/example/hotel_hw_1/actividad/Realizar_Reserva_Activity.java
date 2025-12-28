/**
 * Autor: K. Jabier O'Reilly
 *
 */

package com.example.hotel_hw_1.actividad;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Habitacion;
import com.example.hotel_hw_1.modelo.ValidadorReserva;
import com.example.hotel_hw_1.modelo.Usuario;
import com.example.hotel_hw_1.repositorio.HabitacionRepository; // Usamos el Repo Real
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.Calendar;
import java.util.TimeZone;

public class Realizar_Reserva_Activity extends AppCompatActivity {


    private MaterialRadioButton rbSeleccionado, rbSimple, rbDoble, rbTriple,
            rb_media_pension, rb_pension_full;
    private MaterialCheckBox checkbox_spa, checkbox_parking;
    private RadioGroup radio_group, radiog_type_pension;
    private TextInputEditText  etNombreHuesped, et_apellidos, edit_fecha;
    private MaterialTextView txt_disponibilidad_actual;
    private MaterialButton btn_confirmar_reserva, btn_volver_reserva_flat;

    private MaterialTextView txt_habitacion_asignada;
    private MaterialCardView card_habitacion_asignada;
    private MaterialAutoCompleteTextView spinnerPlanta;
    private String idHabitacionSeleccionada = null;
    private int plantaActual = 1; // Por defecto Planta 1 para el spinner !!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_realizar_reserva);

        etNombreHuesped = findViewById(R.id.et_nombre_huesped);
        et_apellidos = findViewById(R.id.et_apellidos_huesped);
        edit_fecha = findViewById(R.id.edit_fecha);
        radio_group = findViewById(R.id.radioGroupHabitacion);
        rbSimple = findViewById(R.id.rbSimple);
        rbDoble = findViewById(R.id.rbDoble);
        rbTriple = findViewById(R.id.rbTriple);
        checkbox_spa = findViewById(R.id.checkbox_spa);
        checkbox_parking = findViewById(R.id.checkbox_parking);
        radiog_type_pension = findViewById(R.id.radiog_type_pension);
        rb_media_pension = findViewById(R.id.rb_media_pension);
        rb_pension_full = findViewById(R.id.rb_pension_full);
        btn_confirmar_reserva = findViewById(R.id.btn_confirmar_reserva_flat);
        btn_volver_reserva_flat = findViewById(R.id.btn_volver_reserva_flat);
        txt_disponibilidad_actual = findViewById(R.id.txt_disponibilidad_actual);


        txt_habitacion_asignada = findViewById(R.id.txt_habitacion_asignada);
        card_habitacion_asignada = findViewById(R.id.card_habitacion_asignada);
        spinnerPlanta = findViewById(R.id.spinner_planta);
        LinearLayout linearPlanta = findViewById(R.id.linear_planta_reserva);

       // Obtengo el usuario en cuestion y muestro si es recepcionista
        Usuario usuario = Usuario.getInstance();
        if (usuario.getTipo_usuario().equalsIgnoreCase("recepcionista")) {
            linearPlanta.setVisibility(View.VISIBLE);
            etNombreHuesped.setVisibility(View.VISIBLE);
            et_apellidos.setVisibility(View.VISIBLE);

            String[] plantas = {"Planta 1", "Planta 2", "Planta 3", "Planta 4", "Planta 5"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, plantas);
            spinnerPlanta.setAdapter(adapter);
            spinnerPlanta.setText("Planta 1", false); // Valor inicial
        } else {
            linearPlanta.setVisibility(View.GONE);
            plantaActual = 1; // Huesped siempre ve planta 1 por defecto (o lógica interna)
        }

        // Carga inicial de estadísticas reales del Repositorio Habitacion
        actualizarEstadisticasPlanta(plantaActual);


        btn_volver_reserva_flat.setOnClickListener(v -> finish());

        // Selector de fecha (con restricción de 2 días)
        edit_fecha.setOnClickListener(v -> reservar_fecha());


        btn_confirmar_reserva.setOnClickListener(v -> confirmar_reserva(v));

        // Listener para CAMBIO DE PLANTA (Requerido para actualizar stats)

        spinnerPlanta.setOnItemClickListener((parent, view, position, id) -> {
            String seleccion = parent.getItemAtPosition(position).toString();
            // "Planta 2" -> extraemos el 2
            plantaActual = Integer.parseInt(seleccion.replace("Planta ", ""));

            actualizarEstadisticasPlanta(plantaActual);
            buscarHabitacionAutomatica(); // Recalcular asignación
        });

        // Listener pare el cambio de habitacion
        radio_group.setOnCheckedChangeListener((group, checkedId) -> {
            buscarHabitacionAutomatica();
        });
    }


    private void buscarHabitacionAutomatica() {
        int idSeleccionado = radio_group.getCheckedRadioButtonId();
        if (idSeleccionado == -1) return;

        // Traducir selección a texto para el Repo
        String tipoTexto = "";
        if (idSeleccionado == R.id.rbSimple) tipoTexto = "Simple";
        else if (idSeleccionado == R.id.rbDoble) tipoTexto = "Doble";
        else if (idSeleccionado == R.id.rbTriple) tipoTexto = "Triple";

        // Pido al repositorio la primera habitacion libre en la planta !!
        String idEncontrado = HabitacionRepository.buscarPrimeraLibre(plantaActual, tipoTexto);

        // ACTUALIZAR UI
        if (idEncontrado != null) {
            idHabitacionSeleccionada = idEncontrado; // Guardamos "340"
            txt_habitacion_asignada.setText("Habitación Asignada: " + idHabitacionSeleccionada);
            txt_habitacion_asignada.setTextColor(Color.parseColor("#2E7D32"));
            card_habitacion_asignada.setStrokeColor(Color.parseColor("#4CAF50"));
            btn_confirmar_reserva.setEnabled(true);
        } else {
            idHabitacionSeleccionada = null;
            txt_habitacion_asignada.setText("¡Sin disponibilidad en " + tipoTexto + "!");
            txt_habitacion_asignada.setTextColor(Color.RED);
            card_habitacion_asignada.setStrokeColor(Color.RED);
            btn_confirmar_reserva.setEnabled(false);
        }
    }

    private void actualizarEstadisticasPlanta(int planta) {
        // Usamos el método estático que añadiste al Repo
        String textoEstadisticas = HabitacionRepository.obtenerEstadisticasTexto(planta);
        txt_disponibilidad_actual.setText("Disponibilidad Planta " + planta + ":\n" + textoEstadisticas);
    }


    /* Reservo la fecha adecuada, con rstriccion de 2 dias en adelante!! */
    private void reservar_fecha() {
        // Restricción: Mínimo 2 días después de hoy
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        long minDate = calendar.getTimeInMillis();

        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.from(minDate))
                .build();

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Fecha (Mín. 48h)")
                .setSelection(minDate)
                .setCalendarConstraints(constraints) // Aplicamos restricción
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(selection);
            String fecha = String.format("%02d-%02d-%04d",
                    c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
            edit_fecha.setText(fecha);
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    // Metodo para verificar y GUARDAR EN FIREBASE
    private void confirmar_reserva(View v) {
        // Paso 1: Tu validador original (Se mantiene, es útil)
        boolean esValido = ValidadorReserva.validarFormulario(this, v, radio_group,
                rbSimple, rbDoble, rbTriple, edit_fecha, etNombreHuesped, et_apellidos);

        if (!esValido) return;

        // Validación Extra: ¿Tenemos habitación asignada?
        if (idHabitacionSeleccionada == null) {
            Snackbar.make(v, "Error: El sistema no ha asignado habitación.", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Paso 2: Obtener datos
        String fecha = edit_fecha.getText().toString().trim();
        String nombreHuespedCompleto;
        Usuario usuario = Usuario.getInstance();

        if (usuario.getTipo_usuario().equalsIgnoreCase("recepcionista")) {
            nombreHuespedCompleto = etNombreHuesped.getText().toString().trim() + " " +
                    et_apellidos.getText().toString().trim();
        } else {
            // Si es Huesped cogemos el nombre del usuario logueado
            nombreHuespedCompleto = "App: " + usuario.getNombre()+ " "+ usuario.getApellidos();
        }

        // Paso 3: GUARDAR EN FIREBASE (Sustituye a ReservaData)
        btn_confirmar_reserva.setEnabled(false); // Evitar doble clic
        btn_confirmar_reserva.setText("Procesando...");

        HabitacionRepository.actualizarEstadoHabitacion(
                idHabitacionSeleccionada,
                "Reservada",
                fecha,
                nombreHuespedCompleto,
                new HabitacionRepository.HabitacionCallback() {
                    @Override
                    public void onSuccess(String mensaje) {
                        new AlertDialog.Builder(Realizar_Reserva_Activity.this)
                                .setTitle("¡Reserva Confirmada!")
                                .setMessage("Habitación: " + idHabitacionSeleccionada + "\n" +
                                        "Fecha: " + fecha + "\n" +
                                        "Cliente: " + nombreHuespedCompleto)
                                .setPositiveButton("Aceptar", (d, w) -> finish())
                                .setCancelable(false)
                                .show();
                    }

                    @Override
                    public void onError(String error) {
                        btn_confirmar_reserva.setEnabled(true);
                        btn_confirmar_reserva.setText("CONFIRMAR RESERVA");
                        Snackbar.make(v, "Error Firebase: " + error, Snackbar.LENGTH_LONG).show();
                    }
                });
    }
}
/**
 * Autor: K. Jabier O'Reilly
 *
 */

package com.example.hotel_hw_1.actividad;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Huesped;
import com.example.hotel_hw_1.modelo.Validacion;
import com.example.hotel_hw_1.repositorio.HabitacionRepository;
import com.example.hotel_hw_1.repositorio.HuespedRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class GestionEntradasSalidasActivity extends AppCompatActivity {
    private MaterialButton btnVolver, btnBuscar, btnCheckOut;
    private String nombre, apellidos;
    private TextInputEditText etNombreBuscar, etApellidosBuscar;
    private MaterialCardView cardResultado;
    private MaterialTextView txtInfoHuesped;

    // Logic Variables
    private Huesped gest_check_out = null;
    private String numHabitacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_entradas_salidas); // Asegúrate de que el XML se llame así


        btnVolver = findViewById(R.id.btn_volver);


        etNombreBuscar = findViewById(R.id.et_nombre_buscar);
        etApellidosBuscar = findViewById(R.id.et_apellidos_buscar);
        btnBuscar = findViewById(R.id.btn_buscar_checkout);


        cardResultado = findViewById(R.id.card_resultado_checkout);
        txtInfoHuesped = findViewById(R.id.txt_info_huesped);
        btnCheckOut = findViewById(R.id.btn_realizar_check_out); // Ahora está dentro de la card

        //  Buscamos gest para Check-Out
        btnBuscar.setOnClickListener(v -> {
            buscar_huesped(v);
        });

        // Hacemos Check-Out (cambiar el estado)
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
        // Obtenemos los datos (aunque ya los tenemos en el objeto gest_check_out)
        if (gest_check_out != null) {

            // Guardamos el número antes de borrar nada, para liberar la habitación después
            numHabitacion = gest_check_out.getHabitacion();

            // 2. Ejecutamos la salida en Firebase usando el ID
            HuespedRepository.realizarCheckOut(gest_check_out.getId(), new HuespedRepository.HuespedCallback() {
                @Override
                public void onSuccess(String mensaje) {
                    // ÉXITO PARCIAL: Huésped actualizado
                    // Ahora liberamos la habitación
                    complemento_check_out(v);
                }

                @Override
                public void onError(String error) {
                    Snackbar.make(v, "Error: No se pudo procesar la salida", Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
            Snackbar.make(v, "Error: No hay huésped seleccionado", Snackbar.LENGTH_LONG).show();
        }
    }

    /*
     * con este metodo enlazamos el repositorio huesped
     * con la gestion de ocupacion del hotel
     * de este modo se libera la habitacion tambien!!*/
    private void complemento_check_out(View v) {
        HabitacionRepository.liberarHabitacion(numHabitacion, new HabitacionRepository.HabitacionCallback() {
            @Override
            public void onSuccess(String msgHab) {
                // ÉXITO TOTAL: Ambos procesos terminaron bien
                Snackbar.make(v, "Check-Out completo. Habitación " + numHabitacion + " liberada.", Snackbar.LENGTH_LONG).show();

                // Limpieza de UI
                cardResultado.setVisibility(View.GONE); // Ocultamos la tarjeta
                etNombreBuscar.setText("");
                etApellidosBuscar.setText("");
                gest_check_out = null;
            }

            @Override
            public void onError(String error) {
                // El huesped salio, pero la habitacion no se libero!!
                Snackbar.make(v, "Huésped salió pero falló liberar habitación: " + error, Snackbar.LENGTH_LONG).show();
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
            // Huésped encontrado y activo -> MOSTRAMOS LA TARJETA
            cardResultado.setVisibility(View.VISIBLE);
            txtInfoHuesped.setText("Huésped: " + gest_check_out.getNombre() + " " + gest_check_out.getApellidos() +
                    "\nHabitación: " + gest_check_out.getHabitacion());

            Snackbar.make(v, "Huésped encontrado", Snackbar.LENGTH_SHORT).show();

        } else {
            // No encontrado
            cardResultado.setVisibility(View.GONE);
            mostrarDialogoNoEncontrado();
        }
    }

    private void mostrarDialogoNoEncontrado() {
        new AlertDialog.Builder(this)
                .setTitle("Huésped no encontrado")
                .setMessage("No hay ningún huésped ACTIVO con esos datos.")
                .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
/**
 * Autor: K. Jabier O'Reilly

 */

package com.example.hotel_hw_1.actividad;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Huesped;
import com.example.hotel_hw_1.modelo.Usuario;
import com.example.hotel_hw_1.repositorio.HuespedRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.hotel_hw_1.repositorio.TareaRepository;

import java.util.HashMap;
import java.util.Map;

public class Crear_Encuesta_Satisfaccion extends AppCompatActivity {

    // Variables de UI
    private RatingBar rbHab, rbPlanta, rbVest, rbPerLimp, rbPerMant, rbRecep, rbServMant;
    private EditText etHab, etPlanta, etVest, etPerLimp, etPerMant, etRecep, etServMant;
    private MaterialCardView cardServicioMantenimiento;

    // Base de datos
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_encuesta_satisfaccion);

        mDatabase = FirebaseDatabase.getInstance().getReference("encuestas_detalle");

        inicializarVistas();
        configurarVisibilidadMantenimiento();

        MaterialButton btnEnviar = findViewById(R.id.btn_enviar_encuesta);
        MaterialButton btnVolver = findViewById(R.id.btn_volver_encuesta);

        btnEnviar.setOnClickListener(v -> enviarEncuesta());
        btnVolver.setOnClickListener(v -> finish());
    }

    private void inicializarVistas() {

        rbHab = findViewById(R.id.rating_habitacion);
        rbPlanta = findViewById(R.id.rating_planta);
        rbVest = findViewById(R.id.rating_vestibulo);
        rbPerLimp = findViewById(R.id.rating_personal_limpieza);
        rbRecep = findViewById(R.id.rating_recepcion);

        rbPerMant = findViewById(R.id.rating_personal_mantenimiento);

        // Esta tarjeta es especial porque esta oculta
        rbServMant = findViewById(R.id.rating_servicio_mantenimiento);
        cardServicioMantenimiento = findViewById(R.id.card_servicio_mantenimiento);

        // Enlazo EditTexts (Comentarios)
        etHab = findViewById(R.id.edit_comentario_habitacion);
        etPlanta = findViewById(R.id.edit_comentario_planta);
        etVest = findViewById(R.id.edit_comentario_vestibulo);
        etPerLimp = findViewById(R.id.edit_comentario_personal_limpieza);
        etPerMant = findViewById(R.id.edit_comentario_personal_mantenimiento);
        etRecep = findViewById(R.id.edit_comentario_recepcion);
        etServMant = findViewById(R.id.edit_comentario_servicio_mantenimiento);
    }

    private void configurarVisibilidadMantenimiento() {
        // 1. Obtener datos del usuario logueado
        Usuario usuario = Usuario.getInstance();
        // 2- Obtengo los datos del huesped loguedo
        Huesped miHuesped = HuespedRepository.buscarHuespedPorNombre(
                usuario.getNombre(),
                usuario.getApellidos() );
        /* Verifico si solicito mantenimiento */
        boolean mostrarTarjeta = false;
        mostrarTarjeta = TareaRepository. verificarMantenimientoHuesped(miHuesped);

        if (mostrarTarjeta) {
            cardServicioMantenimiento.setVisibility(View.VISIBLE);
        } else {
            cardServicioMantenimiento.setVisibility(View.GONE);
        }
    }

    private void enviarEncuesta() {
        // --- VALIDACIÓN ---

        // 1. Validar las categorías obligatorias (Rating > 0)
        if (rbHab.getRating() == 0 || rbPlanta.getRating() == 0 || rbVest.getRating() == 0 ||
                rbPerLimp.getRating() == 0 || rbPerMant.getRating() == 0 || rbRecep.getRating() == 0) {
            Snackbar.make(this.getCurrentFocus(),"Por favor, valore todas las categorías visibles ",
                    Snackbar.LENGTH_LONG ).show();

            return;
        }

        // 2. Validar la categoría OPCIONAL de mantenimiento
        // Solo validamos si la tarjeta está VISIBLE
        if (cardServicioMantenimiento.getVisibility() == View.VISIBLE) {
            if (rbServMant.getRating() == 0) {
                Snackbar.make(this.getCurrentFocus(),"Por favor, valore todas las categorías visibles ",
                        Snackbar.LENGTH_LONG ).show();
                return;
            }
        }

        // --- PREPARAR DATOS (JSON) ---
        Map<String, Object> datos = new HashMap<>();

        // Datos generales
        datos.put("idReserva", "reserva_" + System.currentTimeMillis()); // O usa Usuario.getInstance().getIdReserva()
        datos.put("fecha", System.currentTimeMillis());

        // Categorías fijas
        datos.put("cat_Limpieza_Habitacion_nota", rbHab.getRating());
        datos.put("cat_Limpieza_Habitacion_comentario", etHab.getText().toString());

        datos.put("cat_Limpieza_Planta_nota", rbPlanta.getRating());
        datos.put("cat_Limpieza_Planta_comentario", etPlanta.getText().toString());

        datos.put("cat_Limpieza_Vestibulo_nota", rbVest.getRating());
        datos.put("cat_Limpieza_Vestibulo_comentario", etVest.getText().toString());

        datos.put("cat_Atencion_Personal_Limpieza_nota", rbPerLimp.getRating());
        datos.put("cat_Atencion_Personal_Limpieza_comentario", etPerLimp.getText().toString());

        datos.put("cat_Atencion_Personal_Mantenimiento_nota", rbPerMant.getRating());
        datos.put("cat_Atencion_Personal_Mantenimiento_comentario", etPerMant.getText().toString());

        datos.put("cat_Atencion_en_Recepcion_nota", rbRecep.getRating());
        datos.put("cat_Atencion_en_Recepcion_comentario", etRecep.getText().toString());

        // Categoría Dinámica
        if (cardServicioMantenimiento.getVisibility() == View.VISIBLE) {
            datos.put("cat_Servicio_de_Mantenimiento_nota", rbServMant.getRating());
            datos.put("cat_Servicio_de_Mantenimiento_comentario", etServMant.getText().toString());
        } else {
            // Si no se mostró, enviamos 0 y vacío para mantener coherencia en la BD
            datos.put("cat_Servicio_de_Mantenimiento_nota", 0f);
            datos.put("cat_Servicio_de_Mantenimiento_comentario", "");
        }

        // --- ENVIAR A FIREBASE ---
        mDatabase.push().setValue(datos)
                .addOnSuccessListener(aVoid -> {
                    Snackbar.make(this.getCurrentFocus(),"¡Encuesta enviada correctamente!", Snackbar.LENGTH_LONG ).show();

                    finish(); // Cerrar actividad y volver atrás
                })
                .addOnFailureListener(e -> {
                    Snackbar.make(this.getCurrentFocus(),"Error al enviar: ",
                            Snackbar.LENGTH_LONG ).show();

                });
    }
}
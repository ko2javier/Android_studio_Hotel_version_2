package com.example.hotel_hw_1.actividad;


/**
 * Autor: K. Jabier O'Reilly

 */
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Empleado;
import com.example.hotel_hw_1.modelo.Validacion;
import com.example.hotel_hw_1.repositorio.EmpleadoRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class EmpleadoFormActivity extends AppCompatActivity {

    private TextInputEditText etNombre, etApellidos, etEmail, etTelefono, etRol;
    private MaterialButton btnGuardar, btnCancelar;

    private boolean modoEditar = false;
    private String idEmpleadoEditar = null;
    private Empleado empleadoActual = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_empleado_form);

        // VIEWS
        etNombre = findViewById(R.id.et_nombre_empleado);
        etApellidos = findViewById(R.id.et_apellidos_empleado);
        etRol = findViewById(R.id.et_rol_empleado);
        etEmail = findViewById(R.id.et_email_empleado);
        etTelefono = findViewById(R.id.et_telefono_empleado);

        btnGuardar = findViewById(R.id.btn_guardar);
        btnCancelar = findViewById(R.id.btn_cancelar);

        // ====== MODO EDITAR (si llega ID_EMPLEADO) ======
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("ID_EMPLEADO")) {
            idEmpleadoEditar = extras.getString("ID_EMPLEADO");
            if (idEmpleadoEditar != null && !idEmpleadoEditar.isEmpty()) {
                modoEditar = true;
                cargarEmpleadoDesdeLista(idEmpleadoEditar);
            }
        }

        btnGuardar.setOnClickListener(this::guardarEmpleado);
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void cargarEmpleadoDesdeLista(String idEmpleado) {

        EmpleadoRepository.obtenerEmpleados(
                new EmpleadoRepository.EmpleadosCallback() {
                    @Override
                    public void onSuccess(List<Empleado> empleados) {
                        for (Empleado e : empleados) {
                            if (e.getId().equals(idEmpleado)) {
                                empleadoActual = e;
                                rellenarCampos();
                                return;
                            }
                        }

                        Snackbar.make(etNombre, "Empleado no encontrado", Snackbar.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Snackbar.make(etNombre, "Error al cargar empleado", Snackbar.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );
    }



    private void rellenarCampos() {
        etNombre.setText(empleadoActual.getNombre());
        etApellidos.setText(empleadoActual.getApellidos());
        etRol.setText(empleadoActual.getRol());
        etEmail.setText(empleadoActual.getEmail());
        etTelefono.setText(empleadoActual.getTelefono());
    }

    private void guardarEmpleado(View v) {

        String nombre = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
        String apellidos = etApellidos.getText() != null ? etApellidos.getText().toString().trim() : "";
        String rol = etRol.getText() != null ? etRol.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String telefono = etTelefono.getText() != null ? etTelefono.getText().toString().trim() : "";

        int errores = 0;
        StringBuilder msg = new StringBuilder();

        // 1) Validar nombre y apellidos
        if (!Validacion.validarNombre(etNombre)) {
            errores++;
            msg.append("• Nombre inválido (mínimo 3 letras).\n");
        }
        if (!Validacion.validarApellidos(etApellidos)) {
            errores++;
            msg.append("• Apellidos inválidos (mínimo 3 letras).\n");
        }

        // 2) Validar rol
        if (!Validacion.validarRol(etRol)) {
            errores++;
            msg.append("• Rol Incorrecto. Escoja Recepcionista\\Mantenimiento\\Limpieza).\n");
        }

        // 3) Validar teléfono
        if (!Validacion.validarTelefonoNuevo(etTelefono)) {
            errores++;
            msg.append("• Teléfono inválido (9 dígitos).\n");
        }

        // 4) Validar email
        if (!Validacion.validarEmail(v, etEmail)) {
            errores++;
            msg.append("• Correo electrónico inválido.\n");
        }

        // 5) Si hay errores, mostramos y salimos
        if (errores > 0 && !isFinishing()) {
            Validacion.mostrarErrores(this, msg);
            return;
        }

        // ====== GUARDAR / ACTUALIZAR EN FIREBASE ======
        if (modoEditar) {

            // Si por lo que sea no cargó empleadoActual, lo creamos desde los campos
            Empleado actualizado = new Empleado(nombre, apellidos, rol, email, telefono);
            actualizado.setId(idEmpleadoEditar);

            EmpleadoRepository.actualizarEmpleado(actualizado, new EmpleadoRepository.ResultadoCallback() {
                @Override
                public void onSuccess() {
                    Snackbar.make(v, "Empleado actualizado correctamente", Snackbar.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(DatabaseError error) {
                    Snackbar.make(v, "Error al actualizar empleado", Snackbar.LENGTH_SHORT).show();
                }
            });

        } else {

            Empleado nuevo = new Empleado(nombre, apellidos, rol, email, telefono);

            EmpleadoRepository.crearEmpleado(nuevo, new EmpleadoRepository.ResultadoCallback() {
                @Override
                public void onSuccess() {
                    Snackbar.make(v, "Empleado agregado correctamente", Snackbar.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(DatabaseError error) {
                    Snackbar.make(v, "Error al guardar empleado", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }
}

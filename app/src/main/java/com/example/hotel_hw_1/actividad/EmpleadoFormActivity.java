/**
 * Autor: K. Jabier O'Reilly

 */
package com.example.hotel_hw_1.actividad;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Empleado;
import com.example.hotel_hw_1.modelo.Validacion;
import com.example.hotel_hw_1.repositorio.EmpleadoData;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class EmpleadoFormActivity extends AppCompatActivity {
    // defino variables
    private TextInputEditText etNombre, etApellidos, etEmail, etTelefono, etRol;

    private Button btnGuardar, btnCancelar;

    private boolean modoEditar = false;
    private int posicionEditar = -1;
    private Empleado empleadoActual = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_empleado_form);

        // Les asigno id a las variables
        etNombre = findViewById(R.id.et_nombre_empleado);
        etApellidos = findViewById(R.id.et_apellidos_empleado);
        etRol = findViewById(R.id.et_rol_empleado);

        etEmail = findViewById(R.id.et_email_empleado);
        etTelefono = findViewById(R.id.et_telefono_empleado);
        btnGuardar = findViewById(R.id.btn_guardar);
        btnCancelar = findViewById(R.id.btn_cancelar);

        /**
         * Configuro mi spinner
         */


        /*Verifico que el bundle no sea nulo y me
        traiga la posicion del array para mostrar datos del empleado!!!
        * */
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("posicion")) {
            modoEditar = true;
            posicionEditar = extras.getInt("posicion");
            empleadoActual = EmpleadoData.getEmpleados().get(posicionEditar);
            rellenarCampos();
        }

        btnGuardar.setOnClickListener(v -> guardarEmpleado(v));
        btnCancelar.setOnClickListener(v -> finish());
    }
    // Metodo para mostrar los datos del empleado!!
    private void rellenarCampos() {
        etNombre.setText(empleadoActual.getNombre());
        etApellidos.setText(empleadoActual.getApellidos());

        etRol.setText(empleadoActual.getRol());
        etEmail.setText(empleadoActual.getEmail());
        etTelefono.setText(empleadoActual.getTelefono());
    }
/*
* En este metodo defino todo lo necesario
* para guardar los datos del empleado con validaciones incluidaas.
* Esto tambien incluye añadir nuevos usuarios con
* sus validaciones de campos incluidas.
* divido el metodo en añadir y modificar siendo
* el elemento clave para ver que hace --> modoEditar = true; !!!
*
* */
private void guardarEmpleado(View v) {
    String nombre = etNombre.getText().toString().trim();
    String apellidos = etApellidos.getText().toString().trim();
    String rol = etRol.getText().toString().trim();
    String email = etEmail.getText().toString().trim();
    String telefono = etTelefono.getText().toString().trim();

    int errores = 0;
    StringBuilder msg = new StringBuilder();

    //  Paso 1- Validar nombre y apellidos
    if (!Validacion.validarNombre(etNombre)) {
        errores++;
        msg.append("• Nombre inválido (mínimo 3 letras).\n");
    }
    if (!Validacion.validarApellidos(etApellidos)) {
        errores++;
        msg.append("• Apellidos inválidos (mínimo 3 letras).\n");
    }

    //  Paso 2- Validar rol
    if (!Validacion.validarRol(etRol)) {
        errores++;
        msg.append("• Rol Incorrecto. Escoja Recepcionista\\Mantenimiento\\Limpieza).\n");
    }

       // Paso 3- Validar teléfono
    if (!Validacion.validarTelefonoNuevo(etTelefono)) {
        errores++;
        msg.append("• Teléfono inválido (9 dígitos).\n");
    }

    // Paso 4-  Validar email
    if (!Validacion.validarEmail(v, etEmail)) {
        errores++;
        msg.append("• Correo electrónico inválido.\n");
    }

    // PAso 5-  Si hay errores, mostramos un diálogo y salimos
    if (errores > 0 && !isFinishing()) {
        Validacion.mostrarErrores(this, msg);
        return;
    }


    // PAso 6- Si no hay errores, guardamos o actualizamos
    if (modoEditar) {
        empleadoActual.setNombre(nombre);
        empleadoActual.setApellidos(apellidos);
        empleadoActual.setRol(rol);
        empleadoActual.setEmail(email);
        empleadoActual.setTelefono(telefono);
        Snackbar.make(v, "Empleado actualizado correctamente", Snackbar.LENGTH_SHORT).show();
    } else {
        Empleado nuevo = new Empleado(nombre, apellidos, rol, email, telefono);
        EmpleadoData.agregarEmpleado(nuevo);
        Snackbar.make(v, "Empleado agregado correctamente", Snackbar.LENGTH_SHORT).show();
    }

    finish(); // Volver a la pantalla anterior
}



}
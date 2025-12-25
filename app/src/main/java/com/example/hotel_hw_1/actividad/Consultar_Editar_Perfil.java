/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: Consultar_Editar_Perfil.java
 *
 * Módulo: Programación Multimedia y Dispositivos Móviles
 */

package com.example.hotel_hw_1.actividad;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Usuario;
import com.example.hotel_hw_1.modelo.Validacion;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class Consultar_Editar_Perfil extends AppCompatActivity {
    private String nuevoNombre,nuevosApellidos, nuevoTelefono, nuevaPass;
    private TextInputEditText field_email, field_password, field_type_user,
            field_nombre, field_apellidos, field_phone;
    private MaterialButton btn_confirmar_cambios, btn_volver;


    /* Con este metodo
    * obtengo los datos del usuario y los cargo
    * en el formulario
    * habilitando solo los que quiero
    * que pueda cambiar */
    private  void pasar_ventana_editar_perfil(Usuario usuario) {

            // obtengo los datos y habilito los campos que podrá cambiar !!
            field_email.setText(usuario.getEmail());
            field_password.setText(usuario.getPass());
            field_password.setEnabled(true);

            field_type_user.setText(usuario.getTipo_usuario());
            field_nombre.setText(usuario.getNombre());
            field_nombre.setEnabled(true);
            field_apellidos.setText(usuario.getApellidos());
            field_apellidos.setEnabled(true);
            field_phone.setText(usuario.getTelefono());
            field_phone.setEnabled(true);

    }

    /**
     *
     * Con este metodo
     *     valido todo lo que tiene que ver
     *     con los campos nombre, apellidos , etc. Si estan correcto
     *     los guardo!!
     * @param v
     * @param usuario
     * @return
     */

    private boolean validando_compos_usuario(View v, Usuario usuario) {
        int errores = 0;
        StringBuilder msg = new StringBuilder();

        // Paso 1- Validar nombre
        if (!Validacion.validarNombre(field_nombre)) {
            errores++;
            msg.append("• Nombre inválido.\n");
        }

        // Paso 2- Validar apellidos
        if (!Validacion.validarApellidos(field_apellidos)) {
            errores++;
            msg.append("• Apellidos inválidos.\n");
        }

        // Paso 3-  Validar teléfono
        if (!Validacion.validarTelefonoNuevo(field_phone)) {
            errores++;
            msg.append("• Teléfono inválido (9 dígitos).\n");
        }

        // Paso 4- Validar contraseña
        if (!Validacion.validarPassword(v, field_password)) {
            errores++;
            msg.append("• La contraseña debe tener al menos 4 caracteres.\n");
        }

        // Paso 5. Si hay errores mostramos y salimos
        if (errores > 0 && !isFinishing()) {
            Validacion.mostrarErrores(this, msg);
            return false;
        }

        //  Paso 6 - Si todo está correcto actualizamos Usuario
        nuevoNombre = field_nombre.getText().toString().trim();
        nuevosApellidos = field_apellidos.getText().toString().trim();
        nuevoTelefono = field_phone.getText().toString().trim();
        nuevaPass = field_password.getText().toString().trim();

        usuario.setNombre(nuevoNombre);
        usuario.setApellidos(nuevosApellidos);
        usuario.setTelefono(nuevoTelefono);
        usuario.setPass(nuevaPass);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consultar_editar_perfil);

        // Asigno los id a mis variables para que esten referenciadas
         field_nombre= findViewById(R.id.campo_nombre_editar_perfil);
         field_apellidos= findViewById(R.id.campo_apellidos_editar_perfil);
         field_email= findViewById(R.id.campo_email_editar_perfil);
         field_phone= findViewById(R.id.campo_telefono_editar_perfil);
         field_password= findViewById(R.id.campo_pass_editar_perfil);
         field_type_user= findViewById(R.id.campo_tipo_user_editar_perfil);
         btn_confirmar_cambios= findViewById(R.id.btn_confirmar_editar_perfil);
         btn_volver= findViewById(R.id.btn_volver_editar_perfil);

        // Obtengo Instancia de usuario y paso datos para cargarlos con el metodo

        Usuario usuario = Usuario.getInstance();

        pasar_ventana_editar_perfil(usuario );

        btn_volver.setOnClickListener(v->{
            finish();
        });

        btn_confirmar_cambios.setOnClickListener(v->{
            if (!validando_compos_usuario(v, usuario))
                return;

            Snackbar.make(v, "Cambios guardados correctamente ", Snackbar.LENGTH_SHORT).show();
        });

    }




}
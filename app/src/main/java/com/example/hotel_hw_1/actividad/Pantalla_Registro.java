/**
 * Autor: K. Jabier O'Reilly
 *
 */

package com.example.hotel_hw_1.actividad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Usuario;
import com.example.hotel_hw_1.modelo.Validacion;
import com.example.hotel_hw_1.repositorio.UsuarioRepository;
import com.google.android.material.button.MaterialButton;
//import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class Pantalla_Registro extends AppCompatActivity {
    private TextInputEditText et_nombre,et_apellidos,et_email, et_phone, et_password,et_confirmPassword;
    private MaterialButton btn_registrar, btn_cancelar;
    private Switch sw_terminos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_registro);


        // Identificamos varibles con id !!
         et_nombre = findViewById(R.id.Campo_DatosNombreUser_PantallaRegistro);
         et_apellidos = findViewById(R.id.Campo_DatosApellidosUser_PantallaRegistro);
         et_email = findViewById(R.id.Campo_email_PantallaRegistro);
         et_phone = findViewById(R.id.Campo_telefono_PantallaRegistro);
         et_password = findViewById(R.id.Campo_Contrasena_PantallaRegistro);
         et_confirmPassword = findViewById(R.id.Campo_Confirma_Contrasena_PantallaRegistro);
         sw_terminos = findViewById(R.id.switch_pantalla_registro);
         btn_registrar = findViewById(R.id.boton_registrate_pantalla_registro);
         btn_cancelar = findViewById(R.id.boton_cancelar_pantalla_registro);

         // Defino listeners
        // Botón cancelar
        btn_cancelar.setOnClickListener(v -> {
            Intent intent = new Intent(Pantalla_Registro.this, Pantalla_Inicio.class);
            startActivity(intent);
        });

        // Botón registrar
        btn_registrar.setOnClickListener(v -> {
            if (!validar_campos(v)) return;

            registrar_user(v);
        });
    }

    private void registrar_user(View v) {
        // Si todo está correcto registramos al usuario
        Usuario nuevo = new Usuario(
                et_email.getText().toString().trim(),
                et_password.getText().toString().trim(),
                "huesped",
                et_apellidos.getText().toString().trim(),
                et_nombre.getText().toString().trim(),
                et_phone.getText().toString().trim()
        );
        // 1. Llamo al metodo del repositorio y instancio la interface asi evito tener que implementarla arriba
        UsuarioRepository.registrarUsuario(this, nuevo, new UsuarioRepository.LoginCallback() {
            @Override
            public void onSuccess(Usuario usuario) {
                // ¡BINGO! Aquí es donde confirmamos que todo salió bien
                Snackbar.make(v, "Usuario registrado con éxito", Snackbar.LENGTH_LONG).show();

                // Esperar un segundo y cerrar la pantalla para mostrar mensaje!!
                v.postDelayed(() -> {
                    Intent intent = new Intent(Pantalla_Registro.this, Pantalla_Inicio.class);
                    startActivity(intent);
                    finish();
                        }, 1500);
            }
            @Override
            public void onError(String mensaje) {
                // Si algo falló (correo repetido, sin internet, etc.)
                Snackbar.make(v, "Error: " + mensaje, Snackbar.LENGTH_LONG).show();
            }
        });

        //UsuarioData.addUsuario(nuevo);
        /*
        Snackbar.make(v, " Usuario registrado con éxito", Snackbar.LENGTH_LONG).show();

        // Vuelve al inicio tras breve pausa
        v.postDelayed(() -> {
            Intent intent = new Intent(Pantalla_Registro.this, Pantalla_Inicio.class);
            startActivity(intent);
            finish();
        }, 1500);*/
    }

    /* metodo para validar todos los campos del registro
    * en este caaso el usuario validado podra entrar al sistema como huesped despues!!
    * */
    private boolean validar_campos(View v) {
        int errores = 0;
        StringBuilder msg = new StringBuilder();

        // Paso 1:  Nombre
        if (!Validacion.validarNombre(et_nombre)) {
            errores++;
            msg.append("• Nombre inválido.\n");
        }

        // Paso 2: Apellidos
        if (!Validacion.validarApellidos(et_apellidos)) {
            errores++;
            msg.append("• Apellidos inválidos.\n");
        }

        // Paso 3: Email
        if (!Validacion.validarEmail(v, et_email)) {
            errores++;
            msg.append("• Email inválido.\n");
        }

        // Paso 4: Teléfono
        if (!Validacion.validarTelefonoNuevo(et_phone)) {
            errores++;
            msg.append("• Teléfono inválido (9 dígitos).\n");
        }

        // Paso 5: Contraseña
        if (!Validacion.validarPassword(v, et_password)) {
            errores++;
            msg.append("• La contraseña debe tener al menos 4 caracteres.\n");
        }

        // Paso 6: Confirmación de contraseña
        if (!Validacion.validarConfirmacionPassword(v, et_password, et_confirmPassword)) {
            errores++;
            msg.append("• Las contraseñas no coinciden.\n");
        }

        // Paso 7: Términos y condiciones
        if (!Validacion.validarTerminos(sw_terminos, this)) {
            errores++;
            msg.append("• Debe aceptar los términos y condiciones.\n");
        }

        // Paso 8: Si hay errores, mostrar todos juntos
        if (errores > 0 && !isFinishing()) {
            Validacion.mostrarErrores(this, msg);
            return false;
        }

        return true;
    }

}


/**
 * Autor: K. Jabier O'Reilly
 *
 */

package com.example.hotel_hw_1.actividad;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Usuario;
import com.example.hotel_hw_1.modelo.Validacion;
import com.example.hotel_hw_1.repositorio.UsuarioData;
import com.google.android.material.snackbar.Snackbar;

public class Pantalla_Inicio extends AppCompatActivity {
    private Button btn_iniciar_sesion,btn_registrarse;
    private EditText et_campo_user, et_campo_password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_inicio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Pantalla_Inicio), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*
        * defino los datos xml,
        * */
         btn_iniciar_sesion= findViewById(R.id.boton_inicio_sesion);
         btn_registrarse= findViewById(R.id.boton_inicio_registrase);
         et_campo_user= findViewById(R.id.campo_user_name);
         et_campo_password= findViewById(R.id.campo_pasword);

        // creo la instancia de el model usuario.
        Usuario usuario = Usuario.getInstance();


        /*
        * pongo a la escucha el boton iniciar- sesion */

        btn_iniciar_sesion.setOnClickListener(v -> {
            validar_login(v);
        });



        btn_registrarse.setOnClickListener(v->{
            Intent intent = new Intent(Pantalla_Inicio.this, Pantalla_Registro.class);
            startActivity(intent);

        });

    }
/* Método para validar campos user y pass, si están
*     bien, cumplen con los parámetros mínimos
* se comprueba si existe en UsuarioData.*/

    private void validar_login(View v) {
        int errores = 0;
        StringBuilder msg = new StringBuilder();

        // Paso 1-  Validar usuario/email
        if (!Validacion.validarEmail(v, et_campo_user)) {
            errores++;
            msg.append("• Email inválido o vacío.\n");
        }

        // Paso 2- Validar contraseña
        if (!Validacion.validarPassword(v, et_campo_password)) {
            errores++;
            msg.append("• La contraseña debe tener al menos 4 caracteres.\n");
        }

        // Paso 3-  Mostrar errores si los hay
        if (errores > 0 && !isFinishing()) {
            Validacion.mostrarErrores(this, msg);
            return;
        }

        // Paso 4- Intentar login solo si pasa las validaciones
        String campo_user = et_campo_user.getText().toString().trim();
        String campo_pass = et_campo_password.getText().toString().trim();

        Usuario u = UsuarioData.checkLogin(this, campo_user, campo_pass);

        if (u != null) {
            Usuario.setInstance(u);
            Log.d("LOGIN_DEBUG", "Usuario logueado: " + u.getEmail() + " | Tipo: " + u.getTipo_usuario());
            Intent intent = u.obtenerPantalla(this);
            startActivity(intent);
        } else {

            Snackbar.make(v, "Usuario o contraseña incorrectos", Snackbar.LENGTH_SHORT).show();
        }
    }
}
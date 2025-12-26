/**
 * Autor: K. Jabier O'Reilly
 *
 */

package com.example.hotel_hw_1.modelo;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.*;

import com.google.android.material.materialswitch.MaterialSwitch;


public class Validacion {

    //1-  Método para validar numero de habitaciones
    public static boolean validarHabitacionObligatoria(View v,
                                                       EditText etxNumero,
                                                       TextView txtError) {
        String valor = etxNumero.getText().toString().trim();

        // paso 1 ver si esta vacio el campo. Si lo esta error !!
        if (valor.isEmpty()) {
            txtError.setText("El número de habitación es obligatorio");
            txtError.setVisibility(View.VISIBLE);
            etxNumero.setError("Obligatorio");
            return false;
        }
        // Paso 2 valido el numero escrito con parse En caso negativo error!!
        int n;
        try {
            n = Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            txtError.setText("Debe ser un número válido");
            txtError.setVisibility(View.VISIBLE);
            etxNumero.setError("Número inválido");
            return false;
        }
// Paso 3 valido el rango del la habitacion si es incorrecto error !!
        if (n < 100 || n > 599) {
            txtError.setText("El número debe estar entre 100 y 599");
            txtError.setVisibility(View.VISIBLE);
            etxNumero.setError("Fuera de rango");
            return false;
        }

        // OK
        txtError.setVisibility(View.GONE);
        etxNumero.setError(null);
        return true;
    }

    // 2- Valida nombre: solo letras, mínimo 3 caracteres
    public static boolean validarNombre(EditText campo) {
        String texto = campo.getText().toString().trim();
        if (!texto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{3,}$")) {
            campo.setError("Nombre inválido. Solo letras, mínimo 3 caracteres");
            return false;
        } else {
            campo.setError(null);
            return true;
        }
    }

    // 3- Valida apellidos: solo letras, mínimo 3 caracteres
    public static boolean validarApellidos(EditText campo) {
        String texto = campo.getText().toString().trim();
        if (!texto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{3,}$")) {
            campo.setError("Apellidos inválidos. Solo letras, mínimo 3 caracteres");
            return false;
        } else {
            campo.setError(null);
            return true;
        }
    }

    // 4- Valida teléfono: exactamente 9 dígitos numéricos
    public static boolean validarTelefonoNuevo(EditText campo) {
        String texto = campo.getText().toString().trim();
        if (!texto.matches("^\\d{9}$")) {
            campo.setError("Teléfono inválido (9 dígitos)");
            return false;
        } else {
            campo.setError(null);
            return true;
        }
    }
// 5- Valida contraseña , tiene que tener mas de 4 caracteres
    public static boolean validarPassword(View v, EditText campo) {
        String pass = campo.getText().toString().trim();
        if (pass.length() < 4) {
           campo.setError("La contraseña debe tener al menos 4 caracteres");
           return false;
        }
        campo.setBackgroundColor(Color.TRANSPARENT);

        return true;
    }

/* 6- Para Validar el rol del Empleado*/

public static boolean validarRol(EditText campo) {
    String texto = campo.getText().toString().trim();

    // Lista de roles válidos
    String[] rolesValidos = {"Recepción", "Mantenimiento", "Gerente", "Limpieza"};
    boolean valido = false;

    for (String rol : rolesValidos) {
        if (rol.equalsIgnoreCase(texto)) {
            valido = true;
            break;
        }
    }
    if (!valido) {
        campo.setError("Rol inválido. Use: Recepción, Mantenimiento, Gerente o Limpieza");
        return false;
    }

    campo.setError(null);
    return true;
    }

    //  8-  Metodo para validar Email !!
    public static boolean validarEmail(View v, EditText campo) {
        String email = campo.getText().toString().trim();
        // Expresión regular para validar formato de email
        String patronEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if (!email.matches(patronEmail) || email.isEmpty()) {
            campo.setError("Email vacio o formato inválido");

            return false;
        }
        campo.setError(null);
        return true;
    }

    // 9- Método para validar terminos y condiciones
    public static boolean validarTerminos(Switch switchTerminos, Context context) {
        if (!switchTerminos.isChecked()) {
            switchTerminos.setError("Debe aceptar los términos y condiciones");
            return false;
        }
        switchTerminos.setError(null);
        return true;
    }
    // 10- Método para validar terminos y condiciones


    /**
     *
     * @param v
     * @param pass1
     * @param pass2
     * @return 1 si coinciden 2 si son distintas
     */
    public static boolean validarConfirmacionPassword(View v, EditText pass1, EditText pass2) {
        if (!pass1.getText().toString().equals(pass2.getText().toString())) {
            pass2.setError("La contraseña no coincide");

            return false;
        }
         pass2.setError(null);

        return true;
    }
    // 11- Método para mostrar mensajes de error !!
    public static void mostrarErrores(Context context, StringBuilder mensajes) {
        if (mensajes.length() == 0) return;

        //if (context instanceof Activity && ((Activity) context).isFinishing()) return;

        new AlertDialog.Builder(context)
                .setTitle("Errores en el formulario")
                .setMessage(mensajes.toString())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Aceptar", null)
                .show();
    }

}

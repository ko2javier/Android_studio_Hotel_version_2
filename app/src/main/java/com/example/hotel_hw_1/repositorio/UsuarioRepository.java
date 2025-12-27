package com.example.hotel_hw_1.repositorio;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: Usuario.java
 *
 */

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

import com.example.hotel_hw_1.dto.AppDatabase;
import com.example.hotel_hw_1.dto.UsuarioDao;
import com.example.hotel_hw_1.dto.UsuarioEntity;
import com.example.hotel_hw_1.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.os.Handler;
public class UsuarioRepository {
    private static final List<Usuario> listaUsuariosCache = new ArrayList<>();

    private static final DatabaseReference dbUsuarios =
            FirebaseDatabase.getInstance().getReference("usuarios");
/*Inteerfaz interna para el manejo de los datos con
* Firebase que es asincrono Defino 2 metodos
* exito y error!*/
        public interface LoginCallback {
        void onSuccess(Usuario usuario);
        void onError(String mensaje);
    }


    public static void inicializarListener() {
        dbUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaUsuariosCache.clear();
                for (DataSnapshot userSnap : snapshot.getChildren()) {

                    String email     = userSnap.child("email").getValue(String.class);
                    String pass      = userSnap.child("pass").getValue(String.class);
                    String nombre    = userSnap.child("nombre").getValue(String.class);
                    String apellidos = userSnap.child("apellidos").getValue(String.class);
                    String tipo      = userSnap.child("tipo_usuario").getValue(String.class);
                    String tlf       = userSnap.child("telefono").getValue(String.class);


                    Usuario u = new Usuario(email, pass, tipo, nombre, apellidos, tlf);
                    listaUsuariosCache.add(u);
                }
                Log.d("REPO_USER", "Usuarios cacheados: " + listaUsuariosCache.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("REPO_USER", "Error firebase: " + error.getMessage());
            }
        });
    }

  /*
  * login hibrido y se cargan los datos de la lista en el splash screen ,
  * asi no hay demora en las consultas todo esta guardadd
  * */
    public static void login(Context ctx, String email, String pass, LoginCallback callback) {

       // Ya la lista esta guardada desde la presentacion!!
        Usuario usuarioFirebase = null;

        for (Usuario u : listaUsuariosCache) {
            if (u.getEmail().equals(email) && u.getPass().equals(pass)) {
                usuarioFirebase = u;
                break;
            }
        }

        if (usuarioFirebase == null) {
            callback.onError("Usuario o contraseña incorrectos (Firebase)");
            return;
        }

        // PASO 2: Verificar en SQLite

        Usuario usuarioSQLite = checkLoginSQLite(ctx, email, pass);

        if (usuarioSQLite == null) {

            callback.onError("El usuario existe en la nube pero no en la base local (SQLite)");
            return;
        }

        callback.onSuccess(usuarioFirebase);
    }

    // ==========================================================
    // 5. MÉTODO PRIVADO PARA CHEQUEAR SQLITE
    // ==========================================================
    private static Usuario checkLoginSQLite(Context ctx, String email, String pass) {

        UsuarioDao dao = AppDatabase.getInstance(ctx).usuarioDao();
        UsuarioEntity entity = dao.login(email, pass);

        if (entity == null) return null;

        return new Usuario(entity.getEmail(), entity.getPass(), entity.getTipo_usuario(),
                entity.getNombre(), entity.getApellidos(), entity.getTelefono());

          }


    // Conversor Auxiliar: Usuario -> UsuarioEntity
    private static UsuarioEntity convertirAEntity(Usuario u) {
        return new UsuarioEntity(
                u.getEmail(),
                u.getPass(),
                u.getNombre(),
                u.getApellidos(),
                u.getTelefono(),
                u.getTipo_usuario()
        );
    }
    /**
     * el "mambo" del registro!!!! * --->
     * Primero lo mando para la nube (Firebase) y si eso camina bien,
     * entonces lo guardo en la local (SQLite) usando un hilo aparte
     * para que la app no se trabe. Así nos aseguramos de que el dato
     * esté en los dos lados y no haya invento.
     */
    public static void registrarUsuario(Context ctx, Usuario usuario, LoginCallback callback) {

        // A. Validar duplicados en caché local (por email)
        // Esto evita que registren dos veces el mismo correo, aunque tengan IDs diferentes
        for (Usuario u : listaUsuariosCache) {
            if (u.getEmail().equalsIgnoreCase(usuario.getEmail())) {
                callback.onError("El email ya está registrado");
                return;
            }
        }

        // B. Guardar en Firebase
        // Usamos push() para que Firebase genere un ID único válido (sin caracteres prohibidos)
        // La estructura quedará: usuarios -> -Nkd8s7d8s... -> { nombre: "...", email: "..." }
        dbUsuarios.push().setValue(usuario)
                .addOnSuccessListener(unused -> {

                    // C. Si Firebase OK -> Guardar en SQLite
                    // Aquí SÍ usamos el email como Primary Key porque SQLite lo permite y tu Entity así lo pide
                    new Thread(() -> {
                        try {
                            UsuarioEntity entity = convertirAEntity(usuario);
                            AppDatabase.getInstance(ctx).usuarioDao().insertar(entity);

                            // Éxito total: Avisamos en el hilo principal
                            new Handler(Looper.getMainLooper()).post(() ->
                                    callback.onSuccess(usuario)
                            );

                        } catch (Exception e) {
                            // Error al guardar en local
                            new Handler(Looper.getMainLooper()).post(() ->
                                    callback.onError("Guardado en nube, pero error local: " + e.getMessage())
                            );
                        }
                    }).start();

                })
                .addOnFailureListener(e ->
                        callback.onError("Error al conectar con la nube: " + e.getMessage())
                );
    }
}

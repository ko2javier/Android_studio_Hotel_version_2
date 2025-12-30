/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: Presentacion.java
 *
 */

package com.example.hotel_hw_1.actividad;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Handler;
import android.util.Log;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.dto.AppDatabase;
import com.example.hotel_hw_1.dto.UsuarioDao;
import com.example.hotel_hw_1.dto.UsuarioEntity;
import com.example.hotel_hw_1.repositorio.EmpleadoRepository;
import com.example.hotel_hw_1.repositorio.EncuestaMetricasRepository;
import com.example.hotel_hw_1.repositorio.HabitacionRepository;
import com.example.hotel_hw_1.repositorio.HuespedRepository;
import com.example.hotel_hw_1.repositorio.ReservaRepository;
import com.example.hotel_hw_1.repositorio.TareaRepository;
import com.example.hotel_hw_1.repositorio.UsuarioRepository;

public class Presentacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_presentacion);
        // inserto los usuarios a pelo!!
        insertarUsuariosIniciales();

        iniciar_repositorios();
        // Espera 3 segundos y luego abre la segunda actividad
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Presentacion.this, Pantalla_Inicio.class);
                startActivity(intent);
                finish(); // para que no vuelva al splash si el usuario pulsa atrás
            }
        }, 3000); // 3000 ms = 3 segundos
    }
    /*Carga inicial de todos los repositorios  que tengo en Firebase */
    private void iniciar_repositorios(){
        EmpleadoRepository.inicializarListener();
        UsuarioRepository.inicializarListener();
        TareaRepository.inicializarListener();
        HuespedRepository.inicializarListener();
        HabitacionRepository.inicializarListener();
        ReservaRepository.inicializar();
        EncuestaMetricasRepository.inicializar();
    }
    /*Cargo los datos inciales en la tabla sqlite si es la primera vez*/
    private void insertarUsuariosIniciales() {
        UsuarioDao dao = AppDatabase.getInstance(this).usuarioDao();

        if (dao.getAll().size() > 0) {
            Log.d("ROOM", "Usuarios ya existen: " + dao.getAll().size());
            return;  // Ya no los inserta
        }

        Log.d("ROOM", "Insertando usuarios iniciales...");

        dao.insertar(new UsuarioEntity(
                "gerente@hotel.com", "1234", "Pedrito", "Calvo", "666777888", "gerente"
        ));

        dao.insertar(new UsuarioEntity(
                "recepcion@hotel.com", "1234", "Ana", "Martínez", "666777888", "recepcionista"
        ));

        dao.insertar(new UsuarioEntity(
                "limpieza@hotel.com", "1234", "Luis", "Pérez", "666111222", "limpieza"
        ));

        dao.insertar(new UsuarioEntity(
                "mantenimiento@hotel.com", "1234", "Marcos", "Gómez", "666333444", "mantenimiento"
        ));

        dao.insertar(new UsuarioEntity(
                "huesped_2@hotel.com", "1234", "Juan", "Lorenzo", "699999999", "huesped"
        ));

        dao.insertar(new UsuarioEntity(
                "huesped@hotel.com", "1234", "Diana", "Río", "699999999", "huesped"
        ));
    }
}
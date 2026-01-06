/**
 * Autor: K. Jabier O'Reilly

 * Clase: HuespedActivity.java
 */

package com.example.hotel_hw_1.actividad;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Usuario;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class HuespedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cargamos el nuevo diseño con Drawer
        setContentView(R.layout.activity_huesped_menu);

        // 1. Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 2. Inicializar Drawer y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Poner a la escucha el menú
        navigationView.setNavigationItemSelectedListener(this);

        // 3. Configurar Hamburguesa
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /*
    * Logica anterior de los botones e intends*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            // Antiguo boton del perfil
            Intent i = new Intent(this, Consultar_Editar_Perfil.class);
            startActivity(i);

        } else if (id == R.id.nav_add_reservas) {
            // Antiguo boton de realizar reservas
            Intent i = new Intent(this, Realizar_Reserva_Activity.class);
            startActivity(i);

        } else if (id == R.id.nav_check_status_realizadas) {
            // Antiguo boton de  Estado Reservas
            Intent i = new Intent(this, Consultar_Estado_Reserva.class);
            startActivity(i);

        } else if (id == R.id.nav_tareas_adicionales) {
            // Solicitar Tareas Adicionales
            Intent i = new Intent(this, Solicitar_Tarea.class);
            startActivity(i);

        } else if (id == R.id.nav_realizar_encuestas_al_hotel) {
            // Realizar Encuestas

            Intent i = new Intent(this, Crear_Encuesta_Satisfaccion.class);

            startActivity(i);

        } else if (id == R.id.nav_logout) {
            // Cerrar Sesión
            Usuario.setInstance(null);
            Snackbar.make(drawerLayout, "Sesión cerrada correctamente", Snackbar.LENGTH_SHORT).show();

            Intent i = new Intent(this, Pantalla_Inicio.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
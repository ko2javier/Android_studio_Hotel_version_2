/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: RecepcionistaActivity.java
 *
 */

package com.example.hotel_hw_1.actividad;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem; // Import necesario para el menú
import androidx.annotation.NonNull; // Import necesario
import androidx.appcompat.app.ActionBarDrawerToggle; // Import para la hamburguesa
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Import para la barra superior
import androidx.core.view.GravityCompat; // Import para cerrar el cajón
import androidx.drawerlayout.widget.DrawerLayout; // Import del layout principal

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Usuario;
import com.google.android.material.navigation.NavigationView; // Import del menú lateral
import com.google.android.material.snackbar.Snackbar;

// Implementamos el listener para escuchar los clics del menú lateral
public class RecepcionistaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cargamos el layout del menú lateral del recepcionista
        setContentView(R.layout.activity_recepcionista_menu);

        // 1. Configuro el Toolbar (Barra superior)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 2. Inicializo el menú lateral (Drawer)
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Pongo a la escucha el menu
        navigationView.setNavigationItemSelectedListener(this);

        // 4. Configuro el menu hambuergesa
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Botonos anteriores trasladados al menu lateral
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            // Antiguo  btn_consultar_perfil
            Intent i = new Intent(RecepcionistaActivity.this, Consultar_Editar_Perfil.class);
            startActivity(i);

        } else if (id == R.id.nav_ocupacion) {
            // Antiguo  btn_consultar_ocupacion
            Intent i = new Intent(RecepcionistaActivity.this, Consultar_Ocupacion_Hotel.class);
            startActivity(i);

        } else if (id == R.id.nav_huespedes) {
            //Antiguo  btn_consultar_listado_huspedes
            Intent i = new Intent(RecepcionistaActivity.this, ConsultarHuespedesActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_reservas) {
            // Antiguo  btn_añadir_reservas

            Intent i = new Intent(RecepcionistaActivity.this, Realizar_Reserva_Activity.class);
            startActivity(i);

        } else if (id == R.id.nav_encuestas) {
            // Antiguo  btn_consultar_encuestas
            Intent i = new Intent(RecepcionistaActivity.this, Consultar_Encuesta_Satisfaccion.class);
            startActivity(i);

        } else if (id == R.id.nav_check_out) {
            // Antiguo  btn_realizar_salidas

            Intent i = new Intent(RecepcionistaActivity.this, GestionEntradasSalidasActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_check_in) {
            // Antiguo  btn_realizar_entradas
            Intent i = new Intent(RecepcionistaActivity.this, Consultar_Ocupacion_Hotel.class);
            startActivity(i);

        } else if (id == R.id.nav_logout) {
            // Antiguo  boton_cerrar_sesion !!
            Usuario.setInstance(null); // limpio lo que tenga el usuario

            Snackbar.make(drawerLayout, "Sesión cerrada correctamente", Snackbar.LENGTH_SHORT).show();

            Intent i = new Intent(RecepcionistaActivity.this, Pantalla_Inicio.class);
            // limpio lo que exista en el historial de las activities, para q cdo comience este todo en cero!!
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        // Ciera el menu deslizante despues de haberlo pulsado
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // Gestion del boton cerrar sesion (Atrás) !!
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
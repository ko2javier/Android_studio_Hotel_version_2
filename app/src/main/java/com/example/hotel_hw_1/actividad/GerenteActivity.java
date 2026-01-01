/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: GerenteActivity.java
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
public class GerenteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gerente_menu);

        // 1. Configuro el  Toolbar (Barra superior)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 2. Inicializo el menú lateral (Drawer)
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);



        //  Pongo a la escucha el menu
        navigationView.setNavigationItemSelectedListener(this);

        // 4. Configuro el menu hambuergesa
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Todos los botones anteriores trasladados al lateral
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            // Logica anterior del btn_consultar_perfil
            Intent i = new Intent(GerenteActivity.this, Consultar_Editar_Perfil.class);
            startActivity(i);

        } else if (id == R.id.nav_ocupacion) {
            // Logica anterior del btn_consultar_ocupacion
            Intent i = new Intent(GerenteActivity.this, Consultar_Ocupacion_Hotel.class);
            startActivity(i);

        } else if (id == R.id.nav_huespedes) {
            // Logica anterior del btn_consultar_listado_huspedes
            Intent i = new Intent(GerenteActivity.this, ConsultarHuespedesActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_tareas) {
            // Logica anterior del btn_consultar_tareas_pdtes
            Intent i = new Intent(GerenteActivity.this, GestionTareasActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_encuestas) {
            // Logica anterior del btn_consultar_encuestas_gerente
            Intent i = new Intent(GerenteActivity.this, Consultar_Encuesta_Satisfaccion.class);
            startActivity(i);

        } else if (id == R.id.nav_empleados) {
            // Logica anterior del btn_consultar_listado_empleados
            Intent i = new Intent(GerenteActivity.this, GestionEmpleadosActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_logout) {
            // Logica anterior del  boton_cerrar_sesion !!
            Usuario.setInstance(null); // limpio lo que tenga el usuario


            Snackbar.make(drawerLayout, "Sesión cerrada correctamente", Snackbar.LENGTH_SHORT).show();

            Intent i = new Intent(GerenteActivity.this, Pantalla_Inicio.class);
            // limpio lo que exista en el historial de las activities, para q cdo comience este todo en cero!!
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        //  Ciera el menu deslizante despues de haberlo pulsado sar
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // Gestion del boton cerrar sesion !!
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: MantenimientoActivity.java
 *
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

// Implementamos el Listener del menú lateral
public class MantenimientoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mantenimiento_menu);

        // 1. configuro el toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 2. Inicializo el Drawer y el Navigation View
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        // 3. Pongo a la escucha el menu
        navigationView.setNavigationItemSelectedListener(this);

        // 4. Configuro el menu hambuergesa
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     Logica anterior adaptada a mi menu!!!
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            // Antiguo  btn_consultar_editar_perfil_mmto
            Intent i = new Intent(MantenimientoActivity.this, Consultar_Editar_Perfil.class);
            startActivity(i);

        } else if (id == R.id.nav_tareas) {
            // Antiguo  btn_consultar_tareas_mmto
            Intent i = new Intent(this, consultar_tareas_mantenimiento.class);
            startActivity(i);

        } else if (id == R.id.nav_encuestas) {
            // Antiguo btn_consultar_Encuestas_mmto
            Intent i = new Intent(MantenimientoActivity.this, Consultar_Encuesta_Satisfaccion.class);
            startActivity(i);

        } else if (id == R.id.nav_logout) {
            // Antiguo boton_cerrar_sesion
            Usuario.setInstance(null); // Limpio usuario

            // drawerLayout para mostrar el mensaje ya que el botón no existe
            Snackbar.make(drawerLayout, "Sesión cerrada correctamente", Snackbar.LENGTH_SHORT).show();

            Intent i = new Intent(MantenimientoActivity.this, Pantalla_Inicio.class);
            // Limpio historial de activities
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        // Cierro el menú al pulsar
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // GEstiono el boton atras del cerrar sesion!!
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
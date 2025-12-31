/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: LimpiadorActivity.java
 *
 */

package com.example.hotel_hw_1.actividad;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem; // Nuevo import
import androidx.annotation.NonNull; // Nuevo import
import androidx.appcompat.app.ActionBarDrawerToggle; // Nuevo import
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Nuevo import
import androidx.core.view.GravityCompat; // Nuevo import
import androidx.drawerlayout.widget.DrawerLayout; // Nuevo import

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Usuario;
import com.google.android.material.navigation.NavigationView; // Nuevo import
import com.google.android.material.snackbar.Snackbar;

// Añadimos 'implements NavigationView.OnNavigationItemSelectedListener'
public class LimpiadorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. configuro el toolbar
        setContentView(R.layout.activity_limpiador_menu);

        // 2. Inicializo el Drawer y el Navigation View
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 3. INICIALIZAR EL MENÚ LATERAL
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
     * Logica anterior de los botones
     * que estaban antes!!!
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {

            Intent i = new Intent(LimpiadorActivity.this, Consultar_Editar_Perfil.class);
            startActivity(i);

        } else if (id == R.id.nav_tareas) {

            Intent i = new Intent(LimpiadorActivity.this, ConsultarTareasLimpieza.class);
            startActivity(i);

        } else if (id == R.id.nav_encuestas) {

            Intent i = new Intent(LimpiadorActivity.this, Consultar_Encuesta_Satisfaccion.class);
            startActivity(i);

        } else if (id == R.id.nav_logout) {
            Usuario.setInstance(null); // Limpio usuario

            Snackbar.make(drawerLayout, "Sesión cerrada correctamente", Snackbar.LENGTH_SHORT).show();

            Intent i = new Intent(LimpiadorActivity.this, Pantalla_Inicio.class);
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
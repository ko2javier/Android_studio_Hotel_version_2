/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: TareaAdapter.java
 *
 */

package com.example.hotel_hw_1.adaptador;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.repositorio.EmpleadoData;
import com.example.hotel_hw_1.modelo.Tarea;
import com.example.hotel_hw_1.repositorio.EmpleadoRepository;
import com.example.hotel_hw_1.repositorio.TareaData;
import com.example.hotel_hw_1.modelo.Usuario;

import java.util.List;

public class TareaAdapter extends ArrayAdapter<Tarea> {

    private Context context;
    private List<Tarea> listaTareas;
    private String rolUsuario;

    public TareaAdapter(@NonNull Context context, List<Tarea> listaTareas, String rolUsuario) {
        super(context, 0, listaTareas);
        this.context = context;
        this.listaTareas = listaTareas;
        this.rolUsuario = rolUsuario;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tarea, parent, false);
        }

        Tarea tarea = listaTareas.get(position);

        TextView txtTipo = convertView.findViewById(R.id.txt_tipo_tarea);
        TextView txtEstado = convertView.findViewById(R.id.txt_estado_tarea);
        TextView txtUbicacion = convertView.findViewById(R.id.txt_ubicacion_tarea);
        TextView txtDetalle = convertView.findViewById(R.id.txt_detalle_tarea);
        TextView txtAsignada = convertView.findViewById(R.id.txt_asignada_a);
        Button btnAccion = convertView.findViewById(R.id.btn_accion_tarea);
        ImageView imgIcono = convertView.findViewById(R.id.img_icono_tarea);

        txtTipo.setText("Tipo: " + tarea.getTipoTarea());
        txtEstado.setText(tarea.getEstado());
        txtUbicacion.setText(tarea.getPlanta() + " - Habitación " + tarea.getHabitacion());
        txtDetalle.setText("Pasillo: "+tarea.getPasillo());
        txtAsignada.setText("Asignada a: " + tarea.getAsignadaA());


        // en dependencia del tipo tarea asignamos un icono o otro!!
        if (tarea.getTipoTarea().equalsIgnoreCase("Limpieza")) {
            imgIcono.setImageResource(R.drawable.ic_limpieza);
        } else {
            imgIcono.setImageResource(R.drawable.ic_mantenimiento);
        }

        if (tarea.getEstado().equalsIgnoreCase("Pendiente")) {
            txtEstado.setTextColor(context.getColor(android.R.color.holo_red_dark));
        } else {
            txtEstado.setTextColor(context.getColor(android.R.color.holo_green_dark));
        }

        if (rolUsuario.equalsIgnoreCase("Gerente")) {
            btnAccion.setText("ASIGNAR");
            btnAccion.setOnClickListener(view -> asignarTarea(tarea));
        } else {
            btnAccion.setText("AUTO_ASIGNAR");
            btnAccion.setOnClickListener(view -> autoasignarTarea(tarea));
        }

        return convertView;
    }

    private void asignarTarea(Tarea tarea) {

        // compruebo que la tarea no ha sido asignada ya!! para que no permita hacer cambios

        if (!tarea.getAsignadaA().equalsIgnoreCase("Sin asignar")) {
            mostrarDialogo("Aviso", "Esta tarea ya está asignada y no se puede modificar.");
            return;
        }

        // obtengo el tipo de tarea que voy a asignar para luego ver si son 4 o 3.
        String tipo = tarea.getTipoTarea();

        // Obtenemos los nombres de empleados con ese rol (limpieza o mantenimiento) de TareaData

        String[] empleadosPorRol= EmpleadoRepository.getNombresPorRol(tipo);

        // Mostramos el diálogo con la lista de empleados disponibles
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Asignar tarea a:");
            builder.setItems(empleadosPorRol, (dialog, which) -> {
            String empleadoSeleccionado = empleadosPorRol[which]; // defino el nombre del empleado seleccionado
            int limite = tipo.equalsIgnoreCase("Limpieza") ? 4 : 3; // defino lim 4 limpieza , 3 mmto!!

            // Verificamos si el empleado ha llegado o no al limite!!

         /* esta puesto a proposito el # de empleados en EmpleadoData
         * para que compruebe que cdo se llegue a 3 en mmto por un empleado no puede
         * asignarsele mas tareas !!
         * */
            if (!TareaData.puedeAutoAsignarse(empleadoSeleccionado, tipo, limite)) {
                mostrarDialogo("Límite alcanzado", "El empleado ya tiene el número máximo de tareas asignadas.");
                return;
            }

            // Confirmo la tarea y ademas de esto la asigno al empleado correspondiente!!
            new AlertDialog.Builder(context)
                    .setTitle("Confirmar asignación")
                    .setMessage("¿Asignar tarea a " + empleadoSeleccionado + "?")
                    .setPositiveButton("Sí", (d, w) -> {
                        tarea.setAsignadaA(empleadoSeleccionado); // aca asigno
                        tarea.setEstado("Asignada"); // confirmo en la tarea
                        notifyDataSetChanged(); // para que se recargue la lista y se muestre actualizada
                        mostrarDialogo("Éxito", "Tarea asignada correctamente a " + empleadoSeleccionado + ".");
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }


    private void autoasignarTarea(Tarea tarea) {
        if (!tarea.getAsignadaA().equalsIgnoreCase("Sin asignar")) {
            mostrarDialogo("Error", "Esta tarea ya está asignada.");
            return;
        }
        // obtengo name+ apellido de la instancia de user. El de mantenimiento y limpieza
        // si son usuarios propios de la aplicacion.
        String empleado = Usuario.getInstance().getNombre() + " " + Usuario.getInstance().getApellidos();
        int limite = rolUsuario.equalsIgnoreCase("Limpieza") ? 4 : 3;

        if (!TareaData.puedeAutoAsignarse(empleado, rolUsuario, limite)) {
            mostrarDialogo("Límite alcanzado", "Ya tienes el número máximo de tareas permitidas.");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmar autoasignación");
        builder.setMessage("¿Deseas autoasignarte esta tarea?");
        builder.setPositiveButton("Sí", (dialog, which) -> {
            tarea.setAsignadaA(empleado);
            tarea.setEstado("Asignada");
            notifyDataSetChanged();
            mostrarDialogo("Éxito", "Tarea autoasignada correctamente.");
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void mostrarDialogo(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", null);
        builder.show();
    }
}


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


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Tarea;
import com.example.hotel_hw_1.modelo.Usuario;
import com.example.hotel_hw_1.repositorio.EmpleadoRepository;
import com.example.hotel_hw_1.repositorio.TareaRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private Context context;
    private List<Tarea> listaTareas;
    private String rolUsuario;

    public TareaAdapter(Context context, List<Tarea> listaTareas, String rolUsuario) {
        this.context = context;
        this.listaTareas = listaTareas;
        this.rolUsuario = rolUsuario;
    }

    // 1. INFLAR EL DISEÑO (item_tarea.xml)
    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(view);
    }

    // 2. VINCULAR DATOS A LA VISTA
    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea tarea = listaTareas.get(position);

        // --- ASIGNACIÓN DE TEXTOS ---
        holder.txtTipo.setText("Tipo: " + tarea.getTipoServicio());
        holder.txtEstado.setText(tarea.getEstado());

        String habitacion = (tarea.getNumeroHabitacion() == null) ? "" : tarea.getNumeroHabitacion();
        holder.txtUbicacion.setText(tarea.getPlanta() + " - Habitación " + habitacion);

        holder.txtDetalle.setText("Zona: " + tarea.getZona());
        holder.txtAsignada.setText("Asignada a: " + tarea.getAsignadaA());

        // --- LÓGICA VISUAL (ICONOS Y COLORES) ---
        if (tarea.getTipoServicio().equalsIgnoreCase("Limpieza")) {
            holder.imgIcono.setImageResource(R.drawable.ic_limpieza);
        } else {
            holder.imgIcono.setImageResource(R.drawable.ic_mantenimiento);
        }

        if (tarea.getEstado().equalsIgnoreCase("Pendiente")) {
            holder.txtEstado.setTextColor(context.getColor(android.R.color.holo_red_dark));
            holder.txtEstado.setBackgroundColor(context.getColor(android.R.color.transparent)); // O un color suave si quieres
        } else {
            holder.txtEstado.setTextColor(context.getColor(android.R.color.holo_green_dark));
        }

        // --- LÓGICA DEL BOTÓN ---
        if (tarea.getEstado().equalsIgnoreCase("Asignada")) {
            holder.btnAccion.setVisibility(View.GONE);
        } else {
            holder.btnAccion.setVisibility(View.VISIBLE);

            if (rolUsuario.equalsIgnoreCase("Gerente")) {
                holder.btnAccion.setText("ASIGNAR");
                holder.btnAccion.setOnClickListener(v -> asignarTarea(tarea));
            } else {
                holder.btnAccion.setText("AUTO_ASIGNAR");
                holder.btnAccion.setOnClickListener(v -> autoasignarTarea(tarea));
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    // ==========================================
    // VIEWHOLDER (Referencias a los IDs del XML)
    // ==========================================
    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView txtTipo, txtEstado, txtUbicacion, txtDetalle, txtAsignada;
        MaterialButton btnAccion;
        ShapeableImageView imgIcono;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTipo = itemView.findViewById(R.id.txt_tipo_tarea);
            txtEstado = itemView.findViewById(R.id.txt_estado_tarea);
            txtUbicacion = itemView.findViewById(R.id.txt_ubicacion_tarea);
            txtDetalle = itemView.findViewById(R.id.txt_detalle_tarea);
            txtAsignada = itemView.findViewById(R.id.txt_asignada_a);
            btnAccion = itemView.findViewById(R.id.btn_accion_tarea);
            imgIcono = itemView.findViewById(R.id.img_icono_tarea);
        }
    }

    // ==========================================
    // LÓGICA DE NEGOCIO (COPIADA DE TU CÓDIGO ANTERIOR)
    // ==========================================

    private void asignarTarea(Tarea tarea) {
        if (!tarea.getAsignadaA().equalsIgnoreCase("Sin asignar")) {
            mostrarDialogo("Aviso", "Esta tarea ya está asignada.");
            return;
        }

        String tipo = tarea.getTipoServicio();
        String[] empleadosPorRol = EmpleadoRepository.getNombresPorRol(tipo);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Asignar tarea a:");
        builder.setItems(empleadosPorRol, (dialog, which) -> {
            String empleadoSeleccionado = empleadosPorRol[which];

            if (!TareaRepository.puedeAutoAsignarse(empleadoSeleccionado, tipo)) {
                mostrarDialogo("Límite alcanzado", "El empleado ya tiene el máximo de tareas hoy.");
                return;
            }

            new AlertDialog.Builder(context)
                    .setTitle("Confirmar asignación")
                    .setMessage("¿Asignar tarea a " + empleadoSeleccionado + "?")
                    .setPositiveButton("Sí", (d, w) -> {
                        TareaRepository.asignarTarea(tarea.getId(), empleadoSeleccionado, new TareaRepository.TareaCallback() {
                            @Override
                            public void onSuccess(String mensaje) {
                                tarea.setAsignadaA(empleadoSeleccionado);
                                tarea.setEstado("Asignada");
                                notifyDataSetChanged(); // Refresca la lista
                                mostrarDialogo("Éxito", "Tarea asignada a " + empleadoSeleccionado);
                            }
                            @Override
                            public void onError(String error) {
                                mostrarDialogo("Error", error);
                            }
                        });
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
        builder.show();
    }

    private void autoasignarTarea(Tarea tarea) {
        String empleado = Usuario.getInstance().getNombre() + " " + Usuario.getInstance().getApellidos();
        String tipo = rolUsuario;

        if (!TareaRepository.puedeAutoAsignarse(empleado, tipo)) {
            mostrarDialogo("Límite alcanzado", "Ya tienes el número máximo de tareas permitidas.");
            return;
        }

        new AlertDialog.Builder(context)
                .setTitle("Confirmar")
                .setMessage("¿Deseas autoasignarte esta tarea?")
                .setPositiveButton("Sí", (d, w) -> {
                    TareaRepository.asignarTarea(tarea.getId(), empleado, new TareaRepository.TareaCallback() {
                        @Override
                        public void onSuccess(String mensaje) {
                            tarea.setAsignadaA(empleado);
                            tarea.setEstado("Asignada");
                            notifyDataSetChanged();
                            mostrarDialogo("Éxito", "Tarea autoasignada correctamente.");
                        }
                        @Override
                        public void onError(String error) {
                            mostrarDialogo("Error", error);
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void mostrarDialogo(String titulo, String mensaje) {
        new AlertDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("OK", null)
                .show();
    }
}
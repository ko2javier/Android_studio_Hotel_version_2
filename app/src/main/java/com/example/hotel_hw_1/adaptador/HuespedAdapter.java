/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: AdapterHuesped.java
 *
 */

package com.example.hotel_hw_1.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.textview.MaterialTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Huesped;
import java.util.List;
import androidx.annotation.NonNull;

public class HuespedAdapter extends RecyclerView.Adapter<HuespedAdapter.HuespedViewHolder> {

    private final Context context;
    private final List<Huesped> listaHuespedes;

    public HuespedAdapter(Context context, List<Huesped> listaHuespedes) {
        this.context = context;
        this.listaHuespedes = listaHuespedes;
    }

    @NonNull
    @Override
    public HuespedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el layout item_huesped que diseñamos con MaterialCardView
        View view = LayoutInflater.from(context).inflate(R.layout.item_huesped, parent, false);
        return new HuespedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HuespedViewHolder holder, int position) {
        Huesped h = listaHuespedes.get(position);

        // Seteamos los datos manteniendo tu lógica original
        holder.txtNombre.setText(h.getNombre() + " " + h.getApellidos());
        holder.txtTelefono.setText("Teléfono: " + h.getTelefono());
        holder.txtHabitacion.setText("Habitación: " + h.getHabitacion());

        // Lógica de colores según el estado del Check-In
        if (h.isCheckInActivo()) {
            holder.txtEstado.setText("Estado: Check-In");
            holder.txtEstado.setTextColor(context.getColor(android.R.color.holo_green_dark));
            // Opcional: Si usas el fondo suave del diseño anterior
            holder.txtEstado.setBackgroundColor(context.getColor(android.R.color.transparent));
        } else {
            holder.txtEstado.setText("Estado: Check-Out");
            holder.txtEstado.setTextColor(context.getColor(android.R.color.holo_red_dark));
        }
    }

    @Override
    public int getItemCount() {
        return listaHuespedes.size();
    }

    // El ViewHolder para optimizar las referencias a las vistas
    public static class HuespedViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView txtNombre, txtTelefono, txtHabitacion, txtEstado;

        public HuespedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txt_nombre_huesped);
            txtTelefono = itemView.findViewById(R.id.txt_telefono_huesped);
            txtHabitacion = itemView.findViewById(R.id.txt_habitacion_huesped);
            txtEstado = itemView.findViewById(R.id.txt_estado_huesped);
        }
    }
}

/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: ReservaAdapter.java
 * */

package com.example.hotel_hw_1.adaptador;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Reserva;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder> {

    private List<Reserva> listaReservas;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onCancelarClick(Reserva reserva);
        void onValorarClick(Reserva reserva);
    }

    public ReservaAdapter(List<Reserva> listaReservas, OnItemClickListener listener) {
        this.listaReservas = listaReservas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserva, parent, false);
        return new ReservaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        Reserva reserva = listaReservas.get(position);

        // 1. Asignar Textos segun ids
        holder.txtFechas.setText("Fecha: " + reserva.getFechaEntrada() + " | " + reserva.getFechaSalida());
        holder.txtHabitacion.setText("Habitación: " + reserva.getHabitacion() + " (" + reserva.getTipoHabitacion() + ")");
        holder.txtServicios.setText("Servicios: " + reserva.getServicios() + " | " + reserva.getRegimen());
        holder.txtEstado.setText("Estado: " + reserva.getEstado());

        // 2. Defino mi logica de colores en dependiencia del estado!!
        String estado = reserva.getEstado();

        // Reiniciar visibilidad
        holder.btnCancelar.setVisibility(View.GONE);
        holder.btnValorar.setVisibility(View.GONE);

        if (estado.equalsIgnoreCase("CANCELADA")) {
            // ROJO Cancelada
            int colorRojo = Color.parseColor("#D32F2F");
            holder.txtEstado.setTextColor(colorRojo);
            holder.iconoCama.setColorFilter(colorRojo); // Icono rojo
            holder.card.setStrokeColor(colorRojo);

        } else if (estado.equalsIgnoreCase("COMPLETADA")) {
            // AZUL (Opinar)
            int colorAzul = Color.parseColor("#1976D2");
            holder.txtEstado.setTextColor(colorAzul);
            holder.iconoCama.setColorFilter(colorAzul);
            holder.card.setStrokeColor(colorAzul);

            holder.btnValorar.setVisibility(View.VISIBLE);

        } else {
            // VERDE (Pendiente )
            int colorVerde = Color.parseColor("#388E3C");
            holder.txtEstado.setTextColor(colorVerde);
            holder.iconoCama.setColorFilter(colorVerde);
            holder.card.setStrokeColor(colorVerde);

            holder.btnCancelar.setVisibility(View.VISIBLE);
        }

        // 3. LISTENERS
        holder.btnCancelar.setOnClickListener(v -> listener.onCancelarClick(reserva));
        holder.btnValorar.setOnClickListener(v -> listener.onValorarClick(reserva));
    }

    @Override
    public int getItemCount() {
        return listaReservas.size();
    }

    // Clase Interna Holder con los IDs
    public static class ReservaViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        ShapeableImageView iconoCama;

        TextView txtFechas, txtHabitacion, txtServicios, txtEstado; // TUS NOMBRES
        MaterialButton btnCancelar, btnValorar;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card_reserva);
            iconoCama = itemView.findViewById(R.id.img_icono_hotel);

            txtFechas = itemView.findViewById(R.id.txt_fechas);
            txtHabitacion = itemView.findViewById(R.id.txt_habitacion);
            txtServicios = itemView.findViewById(R.id.txt_servicios);
            txtEstado = itemView.findViewById(R.id.txt_estado);

            btnCancelar = itemView.findViewById(R.id.btn_cancelar_reserva);
            btnValorar = itemView.findViewById(R.id.btn_valorar_estancia);
        }
    }
}
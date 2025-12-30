package com.example.hotel_hw_1.adaptador;

/**
 * Autor: K. Jabier O'Reilly
 *
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Encuesta;

import java.util.List;

public class AdaptadorEncuestaResumen extends RecyclerView.Adapter<AdaptadorEncuestaResumen.ViewHolder> {

    private List<Encuesta> listaEncuestas;
    private OnEncuestaClickListener listener;



    public interface OnEncuestaClickListener {
        void onEncuestaClick(Encuesta encuesta);
    }

    public AdaptadorEncuestaResumen(List<Encuesta> listaEncuestas, OnEncuestaClickListener listener) {
        this.listaEncuestas = listaEncuestas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_encuesta_dash, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Encuesta encuesta = listaEncuestas.get(position);

        // Si usas el campo "visible" para filtrar roles, puedes ocultar la vista aquí
        // if (!encuesta.isVisible()) { ... }

        holder.txtCategoria.setText(encuesta.getCategoria());
        holder.ratingBar.setRating(encuesta.getPromedio());
        holder.txtPromedio.setText("Promedio: " + encuesta.getPromedio());
        holder.txtCantidad.setText(encuesta.getCantidad() + " encuestas registradas");

        holder.itemView.setOnClickListener(v -> listener.onEncuestaClick(encuesta));
    }

    @Override
    public int getItemCount() {
        return listaEncuestas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategoria, txtPromedio, txtCantidad;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoria = itemView.findViewById(R.id.txt_categoria);
            txtPromedio = itemView.findViewById(R.id.txt_promedio);
            txtCantidad = itemView.findViewById(R.id.txt_cantidad);
            ratingBar = itemView.findViewById(R.id.rating_bar_resumen);
        }
    }
}
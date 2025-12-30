package com.example.hotel_hw_1.adaptador;

/**
 * Autor: K. Jabier O'Reilly
 *
 */

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.DetalleVoto;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdaptadorDetalleVoto extends RecyclerView.Adapter<AdaptadorDetalleVoto.ViewHolder> {

    private List<DetalleVoto> lista;

    public AdaptadorDetalleVoto(List<DetalleVoto> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle_voto, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DetalleVoto voto = lista.get(position);

        // 1. Asignamos valor de la puntuacion dada en la encuesta
        holder.rating.setRating(voto.getNota());
        holder.txtNota.setText(String.format(Locale.getDefault(), "%.1f", voto.getNota()));

        // 2. Formateamos la fecha a un valor legible y bonito
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(voto.getFecha());
        String fechaLegible = DateFormat.format("dd/MM/yyyy", cal).toString();
        holder.txtFecha.setText(fechaLegible);

        // 3. Detalle visual si no tiene comentario no mostramos el TextView asi no aparece vacio
        if (voto.getComentario() != null && !voto.getComentario().trim().isEmpty()) {
            // Tiene texto entonces va!!
            holder.txtComentario.setText(voto.getComentario());
            holder.txtComentario.setVisibility(View.VISIBLE);
        } else {
            // No tiene entonces fuera y mostramos solo la fecha y rating

            holder.txtComentario.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RatingBar rating;
        TextView txtFecha, txtComentario, txtNota;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rating = itemView.findViewById(R.id.rating_detalle);
            txtFecha = itemView.findViewById(R.id.txt_fecha_detalle);
            txtComentario = itemView.findViewById(R.id.txt_comentario_detalle);
            txtNota = itemView.findViewById(R.id.txt_nota_numerica);
        }
    }
}
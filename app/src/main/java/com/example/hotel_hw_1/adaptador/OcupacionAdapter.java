package com.example.hotel_hw_1.adaptador;

/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 */

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.ItemOcupacion;
import com.example.hotel_hw_1.modelo.Habitacion;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import java.util.List;

public class OcupacionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
/*
* interface para desarrollar el
* metodo clic para ver la info de una habitacion ocupada!!
* */
    public interface OnHabitacionClickListener {
        void onHabitacionClick(Habitacion hab);
    }

    private List<ItemOcupacion> items;
    private OnHabitacionClickListener listener; // La interfaz

    public OcupacionAdapter(List<ItemOcupacion> items, OnHabitacionClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getTipoVista();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ItemOcupacion.TIPO_CABECERA) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cabecera_ocupacion, parent, false);
            return new CabeceraViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habitacion_cuadro, parent, false);
            return new HabitacionViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemOcupacion item = items.get(position);

        if (holder instanceof CabeceraViewHolder) {
            CabeceraViewHolder cHolder = (CabeceraViewHolder) holder;

            // Formateo el texto con las estadisticas de cada seccion
            String textoBase = item.getTitulo() + "  ";
            String estadistica = "(" + item.getDisponibles() + " / " + item.getOcupadas() + ")";
            cHolder.txtTitulo.setText(textoBase + estadistica);

        } else if (holder instanceof HabitacionViewHolder) {
            Habitacion hab = item.getHabitacion();
            HabitacionViewHolder hHolder = (HabitacionViewHolder) holder;

            // Muestro el numero de la habitacion ejemplo 101
            hHolder.txtNum.setText(hab.getNumero());

            // Logica para mostrar los colores en dependencia del estado
            if (hab.getEstado() != null) {
                switch (hab.getEstado()) {
                    case "Libre":
                        hHolder.card.setCardBackgroundColor(Color.parseColor("#388E3C")); // Verde
                        break;
                    case "Ocupada":
                        hHolder.card.setCardBackgroundColor(Color.parseColor("#D32F2F")); // Rojo
                        break;
                    case "Reservada":
                        hHolder.card.setCardBackgroundColor(Color.parseColor("#FBC02D")); // Amarillo
                        break;
                }
            }

            // Si hacen click en una habitacion ocupada se muestran los datos de la misma
            hHolder.card.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onHabitacionClick(hab);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

      public static class CabeceraViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView txtTitulo;

        public CabeceraViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txt_titulo_seccion);
        }
    }

    public static class HabitacionViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        MaterialTextView txtNum;

        public HabitacionViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card_habitacion);
            txtNum = itemView.findViewById(R.id.txt_numero_hab);
        }
    }

    public boolean esCabecera(int position) {
        return items.get(position).getTipoVista() == ItemOcupacion.TIPO_CABECERA;
    }
}
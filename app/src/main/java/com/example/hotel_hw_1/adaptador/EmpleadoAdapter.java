package com.example.hotel_hw_1.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_hw_1.R;
import com.example.hotel_hw_1.modelo.Empleado;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class EmpleadoAdapter
        extends RecyclerView.Adapter<EmpleadoAdapter.EmpleadoViewHolder> {

    // ===== INTERFACE (IGUAL QUE EN MODULO) =====
    public interface OnEmpleadoClickListener {
        void onEditarClick(Empleado empleado);
        void onBorrarClick(Empleado empleado);
    }

    private List<Empleado> listaEmpleados;
    private OnEmpleadoClickListener listener;

    public EmpleadoAdapter(List<Empleado> listaEmpleados,
                           OnEmpleadoClickListener listener) {
        this.listaEmpleados = listaEmpleados;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmpleadoViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_empleado, parent, false);

        return new EmpleadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull EmpleadoViewHolder holder, int position) {

        Empleado empleado = listaEmpleados.get(position);

        holder.txtNombreEmpleado.setText(
                empleado.getNombre() + " " + empleado.getApellidos()
        );
        holder.txtRolEmpleado.setText(empleado.getRol());

        // CLICK EDITAR
        holder.btnEditarEmpleado.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditarClick(empleado);
            }
        });

        // CLICK BORRAR
        holder.btnEliminarEmpleado.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBorrarClick(empleado);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaEmpleados.size();
    }

    // ===== VIEW HOLDER =====
    static class EmpleadoViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombreEmpleado, txtRolEmpleado;
        MaterialButton btnEditarEmpleado, btnEliminarEmpleado;

        public EmpleadoViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombreEmpleado = itemView.findViewById(R.id.txt_nombre_empleado);
            txtRolEmpleado = itemView.findViewById(R.id.txt_rol_empleado);
            btnEditarEmpleado = itemView.findViewById(R.id.btn_editar_empleado);
            btnEliminarEmpleado = itemView.findViewById(R.id.btn_eliminar_empleado);
        }
    }
}

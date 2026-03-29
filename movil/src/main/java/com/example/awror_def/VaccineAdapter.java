package com.example.awror_def;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.ViewHolder> {

    ArrayList<Vaccine> vacunas;

    public interface OnVaccineEditListener {
        void onEdit(Vaccine v, int position);
    }

    public interface OnVaccineDeleteListener {
        void onDelete(Vaccine v, int position);
    }

    private OnVaccineEditListener editListener;
    private OnVaccineDeleteListener deleteListener;

    public void setOnVaccineEditListener(OnVaccineEditListener listener) {
        this.editListener = listener;
    }

    public void setOnVaccineDeleteListener(OnVaccineDeleteListener listener) {
        this.deleteListener = listener;
    }

    public VaccineAdapter(ArrayList<Vaccine> vacunas) {
        this.vacunas = vacunas;
    }

    public void setVaccines(ArrayList<Vaccine> nuevasVacunas) {
        this.vacunas.clear();
        this.vacunas.addAll(nuevasVacunas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vacuna, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vaccine v = vacunas.get(position);

        holder.tvNombreVacuna.setText(v.getName());
        holder.tvFechaVacuna.setText("Administrada: " + v.getDateGiven());
        holder.tvProximaVacuna.setText("Próxima: " + v.getNextDate());

        holder.btnEditarVacuna.setOnClickListener(view -> {
            if (editListener != null){
                editListener.onEdit(v, position);
            }
        });

        holder.btnEliminarVacuna.setOnClickListener(view -> {
            if (deleteListener != null){
                deleteListener.onDelete(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vacunas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombreVacuna;
        TextView tvFechaVacuna;
        TextView tvProximaVacuna;
        ImageView btnEditarVacuna;
        ImageView btnEliminarVacuna;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreVacuna = itemView.findViewById(R.id.tvNombreVacuna);
            tvFechaVacuna = itemView.findViewById(R.id.tvFechaVacuna);
            tvProximaVacuna = itemView.findViewById(R.id.tvProximaVacuna);
            btnEditarVacuna = itemView.findViewById(R.id.btnEditarVacuna);
            btnEliminarVacuna = itemView.findViewById(R.id.btnEliminarVacuna);
        }
    }
}

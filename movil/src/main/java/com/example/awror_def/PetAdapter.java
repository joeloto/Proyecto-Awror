package com.example.awror_def;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {

    ArrayList<Pet> mascotas;

    public interface OnPetDeleteListener {
        void onDelete(Pet pet, int position);
    }

    public interface OnPetUpdateListener {
        void onUpdate(Pet pet, int position);
    }

    OnPetDeleteListener deleteListener;
    OnPetUpdateListener updateListener;

    public void setOnPetDeleteListener(OnPetDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void setOnPetUpdateListener(OnPetUpdateListener listener) {
        this.updateListener = listener;
    }

    public PetAdapter(ArrayList<Pet> mascotas) {
        this.mascotas = mascotas;
    }

    public void setPets(ArrayList<Pet> newMascotas) {
        this.mascotas.clear();
        this.mascotas.addAll(newMascotas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mascota, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Pet mascota = mascotas.get(position);

        holder.tvNombreMascota.setText(mascota.getNombre());
        holder.tvDescripcion.setText(mascota.getTipo());

        if (mascota.getImagen() != null && !mascota.getImagen().isEmpty()) {
            byte[] decoded = Base64.decode(mascota.getImagen(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
            holder.ivImagen.setImageBitmap(bitmap);
        } else {
            holder.ivImagen.setImageDrawable(null);
        }

        holder.btnEliminarMascota.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(mascota, position);
            }
        });

        holder.btnActualizarMascota.setOnClickListener(v -> {
            if (updateListener != null) {
                updateListener.onUpdate(mascota, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mascotas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombreMascota;
        TextView tvDescripcion;
        ImageView ivImagen;
        ImageView btnEliminarMascota;
        ImageView btnActualizarMascota;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreMascota = itemView.findViewById(R.id.tvNombreMascota);
            tvDescripcion = itemView.findViewById(R.id.tvTipoAnimal);
            ivImagen = itemView.findViewById(R.id.ivMascotaFoto);
            btnEliminarMascota = itemView.findViewById(R.id.btnEliminarMascota);
            btnActualizarMascota = itemView.findViewById(R.id.btnActualizarMascota);
        }
    }
}

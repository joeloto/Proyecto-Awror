package com.example.awror;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awror.R;
import com.example.awror.Publicacion;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PublicacionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Publicacion> listaPublicaciones;

    public PublicacionAdapter(ArrayList<Publicacion> listaPublicaciones) {
        this.listaPublicaciones = listaPublicaciones;
    }

    @Override
    public int getItemViewType(int position) {
        return listaPublicaciones.get(position).getTipo();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == com.example.awror.Publicacion.TIPO_FOTO) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.publicacion, parent, false);
            return new FotoViewHolder(view);

        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.publicacion2, parent, false);
            return new TextoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Publicacion p = listaPublicaciones.get(position);

        if (holder.getClass() == FotoViewHolder.class) {
            FotoViewHolder vh = (FotoViewHolder) holder;
            vh.tvUsuario.setText(p.getUsuario());
            vh.tvLikes.setText(p.getLikes());
            vh.tvComentarios.setText(p.getComentarios());
            vh.imgPublicacion.setImageResource(p.getImagen());

        } else if (holder.getClass() == TextoViewHolder.class) {
            TextoViewHolder vh = (TextoViewHolder) holder;
            vh.tvUsuario.setText(p.getUsuario());
            vh.tvTexto.setText(p.getTexto());
            vh.tvLikes.setText(p.getLikes());
            vh.tvComentarios.setText(p.getComentarios());
        }
    }

    @Override
    public int getItemCount() {
        return listaPublicaciones.size();
    }

    static class FotoViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsuario;
        TextView tvLikes;
        TextView tvComentarios;
        ImageView imgPublicacion;

        public FotoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsuario = itemView.findViewById(R.id.tvUsuario);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvComentarios = itemView.findViewById(R.id.tvComentarios);
            imgPublicacion = itemView.findViewById(R.id.imgPublicacion);
        }
    }

    static class TextoViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsuario;
        TextView tvTexto;
        TextView tvLikes;
        TextView tvComentarios;

        public TextoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsuario = itemView.findViewById(R.id.tvUsuario);
            tvTexto = itemView.findViewById(R.id.tvTextoPublicacion);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvComentarios = itemView.findViewById(R.id.tvComentarios);
        }
    }
}